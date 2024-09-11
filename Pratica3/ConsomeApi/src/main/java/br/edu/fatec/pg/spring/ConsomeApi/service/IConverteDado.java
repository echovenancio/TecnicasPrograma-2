package br.edu.fatec.pg.spring.ConsomeApi.service;

public interface IConverteDado {
    <T> T obterDado(String json, Class<T> classe);
}
