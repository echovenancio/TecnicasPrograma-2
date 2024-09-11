package tabelafipe;

import io.github.cdimascio.dotenv.Dotenv;
import tabelafipe.model.ResGenerico;
import tabelafipe.model.Veiculo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class Comando {
    public static void run() {
        System.out.println("Sistema de consulta tabela fipe");
        System.out.println("Exemplos:");
        System.out.println("\tmarcas --lista as marcas de veiculos");
        System.out.println("\tmodelos marcaId --lista os modelos de veiculos em uma marca, onde 'marcaId' é o id da marca");
        System.out.println("\tanos marcaId modeloId --lista os anos do modelo, onde 'marcaId' é o id da marca e 'modeloId' é o id do modelo");
        System.out.println("\tpreco marcaId modeloId anoId --mostra as informações do veiculo, onde 'marcaId' é o id da marca, 'modeloId' é o id do modelo e 'anoId' é o id do ano");
        while (true) {
            System.out.print("->> ");
            Comando.getInput();
        }
    }

    static void getInput() {
        var sc = new Scanner(System.in);
        var input = sc.nextLine().trim().split(" ");
        var marcaId = 0;
        var modeloId = 0;
        String anoId = "";
        switch (input[0]) {
            case "":
                return;
            case "marcas":
                Comando.getMarcas();
                break;
            case "modelos":
                if (input.length != 2) {
                   System.err.println("Esperava um argumento");
                   break;
                }
                try {
                    marcaId = Integer.parseInt(input[1]);
                } catch (Exception e) {
                   System.err.println("Id inválido");
                   break;
                }
                Comando.getModelos(marcaId);
                break;
            case "anos":
                if (input.length != 3) {
                   System.err.println("Esperava dois argumentos");
                   break;
                }
                marcaId = 0;
                modeloId = 0;
                try {
                    marcaId = Integer.parseInt(input[1]);
                    modeloId = Integer.parseInt(input[2]);
                } catch (Exception e) {
                    System.err.println("Id inválido");
                    break;
                }
                Comando.getAnos(marcaId, modeloId);
                break;
            case "preco":
                if (input.length != 4) {
                    System.err.println("Esperava três argumentos");
                    break;
                }
                try {
                    marcaId = Integer.parseInt(input[1]);
                    modeloId = Integer.parseInt(input[2]);
                    anoId = input[3];
                } catch (Exception e) {
                    System.err.println("Id inválido");
                    break;
                }
                Comando.getPreco(marcaId, modeloId, anoId);
                break;
            case "sair":
                System.exit(1);
            default:
                System.out.println("Comando desconhecido");
        }
    }

    static void getMarcas() {
        String json = FipeAPI.doreq("cars/brands");
        var conversor = new ConverteDado();
        var marcasList = conversor.obterDado(json, ResGenerico[].class);
        for (ResGenerico item : marcasList) {
            var fmtString = String.format("-Id:%s\n-Name:%s\n", item.id(), item.name());
            System.out.println(fmtString);
        }
    }

    static void getModelos(int marcaId) {
        var query = String.format("cars/brands/%s/models", marcaId);
        String json = FipeAPI.doreq(query);
        var conversor = new ConverteDado();
        var modelosList = conversor.obterDado(json, ResGenerico[].class);
        for (ResGenerico item : modelosList) {
            var fmtString = String.format("-Id:%s\n-Name:%s\n", item.id(), item.name());
            System.out.println(fmtString);
        }
    }

    static void getAnos(int marcaId, int modeloId) {
        var query = String.format("cars/brands/%s/models/%s/years", marcaId, modeloId);
        String json = FipeAPI.doreq(query);
        var conversor = new ConverteDado();
        var anosList = conversor.obterDado(json, ResGenerico[].class);
        for (ResGenerico item : anosList) {
            var fmtString = String.format("-Id:%s\n-Name:%s\n", item.id(), item.name());
            System.out.println(fmtString);
        }
    }

    static void getPreco(int marcaId, int modeloId, String anoId) {
        var query = String.format("cars/brands/%s/models/%s/years/%s", marcaId, modeloId, anoId);
        var conversor = new ConverteDado();
        String json = FipeAPI.doreq(query);
        var veiculo = conversor.obterDado(json, Veiculo.class);
        System.out.println("-Preco: " + veiculo.preco());
        System.out.println("-Ano: " + veiculo.ano());
        System.out.println("-Modelo: " + veiculo.model());
        System.out.println("-Combustivel: " + veiculo.combustivel());
    }
}
