package model;

public record Cep(
        String cep,
        String complemento,
        String unidade,
        String bairro,
        String ibge,
        String gia,
        String siafi,
        String logradouro,
        String localidade,
        String uf,
        String ddd) { }
