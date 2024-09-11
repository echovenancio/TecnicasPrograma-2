package br.edu.fatec.pg.spring.ConsomeApi.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Serie(
        @JsonAlias("Title") String title,
        @JsonAlias("Runtime") String duracao,
        @JsonAlias("Country") String pais,
        @JsonAlias("Poster") String poster) {
}
