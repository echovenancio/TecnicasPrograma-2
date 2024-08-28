package model;

public record LogEntry(
        int rowid,
        String cep,
        String logradouro,
        String localidade,
        String datetime,
        String client_ip) {
}
