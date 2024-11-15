package com.darien.challenge_literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutores> autor,
        @JsonAlias("languages") List<String> lenguajes,
        @JsonAlias("download_count") Double numeroDeDescargas
) {
    public List<String> limpiandoLenguajes() {
        return lenguajes.stream().map(l -> l.replaceAll("[{]", "").replaceAll("}", "")).collect(Collectors.toList());
    }
}
