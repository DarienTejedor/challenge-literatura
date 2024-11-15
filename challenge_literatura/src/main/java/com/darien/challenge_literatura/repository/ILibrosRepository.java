package com.darien.challenge_literatura.repository;

import com.darien.challenge_literatura.model.Autores;
import com.darien.challenge_literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ILibrosRepository extends JpaRepository<Libros,Long> {
    Optional<Libros> findByTituloAndAutor(String titulo, Autores autores);

    @Query("SELECT a FROM Autores a WHERE a.fechaNacimiento <= :year AND (:year <= a.fechaMuerte OR a.fechaMuerte IS NULL)")
    List<Autores> findByYear(String year);

    @Query("SELECT l FROM Libros l WHERE :idioma IN (l.lenguajes)")
    List<Libros> findByLenguaje(@Param("idioma") String idioma);
}
