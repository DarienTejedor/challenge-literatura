package com.darien.challenge_literatura.repository;

import com.darien.challenge_literatura.model.Autores;
import com.darien.challenge_literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAutoresRepository extends JpaRepository<Autores,Long> {
    Optional<Autores> findByNombre(String nombreAutor);

    @Query("SELECT a FROM Autores a WHERE a.fechaNacimiento <= :year AND (:year <= a.fechaMuerte OR a.fechaMuerte IS NULL)")
    List<Autores> findByYear(String year);
}
