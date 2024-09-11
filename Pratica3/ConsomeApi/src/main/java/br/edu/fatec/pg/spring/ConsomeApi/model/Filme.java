package br.edu.fatec.pg.spring.ConsomeApi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Filme(
        @JsonAlias("Title") String titulo,
        @JsonAlias("Year") int ano,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Genre") String genero
) {
}
