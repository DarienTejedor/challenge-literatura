package com.darien.challenge_literatura.principal;

import com.darien.challenge_literatura.model.Autores;
import com.darien.challenge_literatura.model.Datos;
import com.darien.challenge_literatura.model.DatosLibros;
import com.darien.challenge_literatura.model.Libros;
import com.darien.challenge_literatura.repository.IAutoresRepository;
import com.darien.challenge_literatura.repository.ILibrosRepository;
import com.darien.challenge_literatura.service.ConsumoAPI;
import com.darien.challenge_literatura.service.ConvierteDatos;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private ILibrosRepository librosRepository;
    private IAutoresRepository autoresRepository;
    private final String URL = "https://gutendex.com/books/";
    private List<Libros> libros;
    private List<Autores> autores;


    public void mostrarMenu() throws UnsupportedEncodingException {
        int opc = -1;
        while (opc != 0) {
            String menu = """
                         
                         Elige una opcion
                    
                     1. Buscar libro por titulo
                     2. Ver libros registrados
                     3. Ver autores registrados 
                     4. Ver autores en un determinado año
                     5. Ver libros por idioma
                    
                    0. Salir
                    """;
            System.out.println(menu);
            opc = scanner.nextInt();
            scanner.nextLine();
            switch (opc) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listaLibros();
                    break;
                case 3:
                    listaAutores();
                    break;
                case 4:
                    autorsForYear();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 0:
                    System.out.println("Gracias por usar el programa");
                default:
                    System.out.println("Opcion invalida");

            }
        }
    }

    public Principal(IAutoresRepository autoresRepository, ILibrosRepository librosRepository){
        this.autoresRepository = autoresRepository;
        this.librosRepository = librosRepository;
    }

    private boolean libroYaExiste(String titulo, String nombreAutor) {
        Optional<Autores> autorExistente = autoresRepository.findByNombre(nombreAutor);
        if (autorExistente.isPresent()) {
            Optional<Libros> libroExistente = librosRepository.findByTituloAndAutor(titulo, autorExistente.get());
            return libroExistente.isPresent();
        }
        return false;
    }

    private void guardarLibro(DatosLibros datosLibros){
        //Guardar autor
            Autores autor = new Autores(datosLibros.autor().get(0));
            Optional<Autores> autorExistente = autoresRepository.findByNombre(autor.getNombre());
            if (autorExistente.isPresent()){
                autor = autorExistente.get();
            } else {
                autoresRepository.save(autor);
            }

            //Guardar libro
            Libros libro = new Libros(datosLibros);
            libro.setAutores(autor);
            autor.addLibro(libro);
            librosRepository.save(libro);
        System.out.println(libro);
    }


    private void buscarLibro() throws UnsupportedEncodingException {
        System.out.println("Ingresa el nombre del libro a buscar: ");
        var libroABuscar = URLEncoder.encode(scanner.nextLine(), "UTF-8");
        var json = consumoAPI.obtenerDatos(URL+"?search="+libroABuscar);
        var datosBuscados = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> librosBuscados = datosBuscados.resultados().stream()
                .filter(l->l.titulo().toUpperCase().contains(libroABuscar.toUpperCase()))
                .findFirst();
        if (librosBuscados.isPresent()){
            System.out.println("Libro encontrado ");
            System.out.println(librosBuscados.get());
            DatosLibros datosLibros = librosBuscados.get();

            if (libroYaExiste(datosLibros.titulo(),datosLibros.autor().get(0).nombre()))
            {
                System.out.println("El libro ya fue agregado al sistema");
            } else {

                guardarLibro(datosLibros);
            }

        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listaLibros() {
        System.out.println("-------- LIBROS --------");

        libros = librosRepository.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(System.out::println);
    }

    private void listaAutores() {
        System.out.println("-------- Autores --------");
        autores = autoresRepository.findAll();

        autores.stream()
                .sorted(Comparator.comparing(Autores::getNombre))
                .forEach(System.out::println);
    }

    private void autorsForYear() {
        System.out.println("Ingresa el año a buscar: ");
        String year = scanner.nextLine();

        autores = autoresRepository.findByYear(year);

        if (autores.isEmpty()){
            System.out.println("No hubieron coincidencias en los años");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void buscarLibroPorIdioma(String idioma){
        List<Libros> librosPorLenguaje = librosRepository.findByLenguaje(idioma);
        if (librosPorLenguaje.isEmpty()){
            System.out.println("No hay libros en este idioma ");
        } else {
            librosPorLenguaje.stream().sorted(Comparator.comparing(Libros::getTitulo)) .forEach(System.out::println);
        }
    }

    private void librosPorIdioma() {
        String opciones = """
                Escoje el idioma a filtrar:
                1. Español
                2. Ingles
                3. Frances
                4. Portugues
                """;
        System.out.println(opciones);
        var opc = -1;
        opc = scanner.nextInt();
        scanner.nextLine();
        switch (opc){
            case 1:
                System.out.println("----- LIBROS EN ESPAÑOL -----");
                String idiomaEs = "es";
                buscarLibroPorIdioma(idiomaEs);
                break;
            case 2:
                System.out.println("----- LIBROS EN INGLES -----");
                String idiomaEn = "en";
                buscarLibroPorIdioma(idiomaEn);
                break;
            case 3:
                System.out.println("----- LIBROS EN FRANCES -----");
                String idiomaFr = "fr";
                buscarLibroPorIdioma(idiomaFr);
                break;
            case 4:
                System.out.println("----- LIBROS EN PORTUGUES -----");
                String idiomaPt = "pt";
                buscarLibroPorIdioma(idiomaPt);
                break;
            case 0:
                System.out.println("Volviendo...");
            default:
                System.out.println("Opcion invalida");
        }


    }




}
