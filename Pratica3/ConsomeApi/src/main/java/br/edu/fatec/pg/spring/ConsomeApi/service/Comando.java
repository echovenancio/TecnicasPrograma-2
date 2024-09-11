package br.edu.fatec.pg.spring.ConsomeApi.service;

import br.edu.fatec.pg.spring.ConsomeApi.model.Filme;
import br.edu.fatec.pg.spring.ConsomeApi.model.Serie;

import java.util.Scanner;

public class Comando {
    public static void run() {
        System.out.println("Sistema de busca de filmes e series");
        System.out.println("Exemplos: ");
        System.out.println("\tfilme john wick -- busca o filme john wick");
        System.out.println("\tserie the flash -- busca a serie the flash");
        while (true){
            System.out.print("->> ");
            Comando.getInput();
        }
    }

    static void buscarFilme(String filmeq) {
        var conversor = new ConverteDado();
        var json = ConsumirApi.obterDado(filmeq);
        if (json == null) {
            System.out.println("Filme não existe, verifique que você digitou corretamente.");
            return;
        }
        var filme = conversor.obterDado(json, Filme.class);
        var fmtString = String.format("-Titulo: %s\n-Duração: %s\n-Ano lançamento: %s\n-Genero: %s", filme.titulo(), filme.duracao(), filme.ano(), filme.genero());
        System.out.println(fmtString);
    }

    static void buscarSerie(String serieq) {
        var conversor = new ConverteDado();
        var json = ConsumirApi.obterDado(serieq);
        if (json == null) {
            System.out.println("Serie não existe, verifique que você digitou corretamente.");
            return;
        }
        var serie = conversor.obterDado(json, Serie.class);
        var fmtString = String.format("-Titulo: %s\n-Duração: %s\n-Pais: %s", serie.title(), serie.duracao(), serie.pais());
        System.out.println(fmtString);
    }

    static void getInput() {
        var sc = new Scanner(System.in);
        var input = sc.nextLine().trim().split(" ");
        switch (input[0]) {
            case "":
                return;
            case "filme":
                Comando.buscarFilme(input[1]);
                break;
            case "serie":
                Comando.buscarSerie(input[1]);
                break;
            case "sair":
                System.exit(1);
            default:
                System.err.println("Comando: '" + input[0] + "' desconhecido.");
        }
    }
}
