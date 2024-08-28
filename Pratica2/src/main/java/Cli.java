import api.CepApi;
import db.Connect;
import model.LogEntry;

import java.util.ArrayList;
import java.util.Scanner;

public class Cli {
    public void exec() {
        this.help();
        while (true) {
            this.input();
        }
    }

    private void help() {
        System.out.println("* Sistema de busca de ceps.");
        System.out.println("busca 01001000 -- comando para buscar cep.");
        System.out.println("historico      -- mostra o historico de ceps consultados.");
        System.out.println("help           -- mostra essa mensagem de ajuda.");
        System.out.println("sair           -- encerra o programa");
    }

    private void input() {
        var sc = new Scanner(System.in);
        System.out.println();
        System.out.print("->> ");
        var input = sc.nextLine().trim().split(" ");
        switch (input[0]) {
            case "":
                break;
            case "busca":
                if (input.length != 2) {
                    System.out.println("Número incorreto de argumentos, esperava um (o cep)");
                    return;
                }
                this.busca(input[1]);
                break;
            case "historico":
                if (input.length > 1) {
                    System.out.println("Não esperava nenhum argumento");
                    return;
                }
                this.historico();
                break;
            case "help":
                if (input.length > 1) {
                    System.out.println("Não esperava nenhum argumento");
                    return;
                }
                this.help();
                break;
            case "sair":
                if (input.length > 1) {
                    System.out.println("Não esperava nenhum argumento");
                    return;
                }
                this.sair();
                break;
        }
    }

    private void historico() {
        var conn = Connect.connect();
        try {
            var sql = "SELECT rowid, cep, logradouro, localidade, datetime, client_ip FROM LOG_ENTRY";
            var pstmt = conn.prepareStatement(sql);
            var rs = pstmt.executeQuery();
            var historico = new ArrayList<LogEntry>();
            while (rs.next()) {
                var logEntry = new LogEntry(
                        rs.getInt("rowid"),
                        rs.getString("cep"),
                        rs.getString("logradouro"),
                        rs.getString("localidade"),
                        rs.getString("datetime"),
                        rs.getString("client_ip")
                );
                historico.add(logEntry);
            }
            if (historico.size() == 0) {
                System.out.println("histórico vazio");
                return;
            }
            for (LogEntry logEntry: historico) {
                System.out.println("├─────────────────────────────────");
                System.out.println("│Cep: " + logEntry.cep());
                System.out.println("│Logradouro: " + logEntry.logradouro());
                System.out.println("│Localidade: " + logEntry.localidade());
                System.out.println("│Datetime: " + logEntry.datetime());
                System.out.println("│Ip: " + logEntry.client_ip());
            }
        } catch (Exception e) {
            System.err.println("Erro buscando historico no banco: " + e);
            this.sair();
        }
    }

    private void busca(String cep) {
        if (cep.length() != 8) {
            System.out.println("formato de cep incorreto");
            return;
        }
        for (int c: cep.chars().toArray()) {
            if (c < 48 || c > 57) {
                System.out.println("formato de cep incorreto");
                return;
            }
        }
        var resCep = CepApi.buscarCep(cep);
        var res = String.format(
                "Cep: %s\nLocalidade: %s\nLogradouro: %s\nDDD: %s\nBairro: %s\nUF: %s",
                resCep.cep(), resCep.localidade(), resCep.logradouro(), resCep.ddd(),
                resCep.bairro(), resCep.uf()
                );
        System.out.println(res);
    }

    private void sair() {
        System.out.println("Encerrando aplicação");
        System.exit(0);
    }
}
