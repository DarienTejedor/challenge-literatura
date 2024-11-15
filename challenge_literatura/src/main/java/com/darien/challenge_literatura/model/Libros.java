package com.darien.challenge_literatura.model;


import com.darien.challenge_literatura.repository.ILibrosRepository;
import jakarta.persistence.*;
import org.springframework.data.repository.cdi.Eager;

import java.util.List;

@Entity
@Table(name="libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String titulo;
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autores autor;
    private List<String> lenguajes;
    private Double numeroDeDescargas;

    public Libros(){}

    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        if(!datosLibros.autor().isEmpty()) {
            this.autor = new Autores(datosLibros.autor().get(0)); // Asumimos el primer autor de la lista para simplicidad
        }
        this.lenguajes = datosLibros.limpiandoLenguajes();
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutores() {
        return autor;
    }

    public void setAutores(Autores autor) {
        this.autor = autor;
    }

    public List<String> getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(List<String> lenguajes) {
        this.lenguajes = lenguajes;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
            return " " + "\n" +
                "Titulo: " + titulo + "\n" +
                "Autor: " + autor.getNombre() + "\n" +
                "Lenguajes: " + lenguajes + "\n" +
                "Numero de descargas: " + numeroDeDescargas ;
    }


}
