package view;

import service.ConsomeApi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Shell {

    private List<String> hist = new ArrayList<>();
    private FileWriter saveFile = new FileWriter("respostas.txt", true);

    public Shell() throws IOException {
        Path path = Paths.get("respostas.txt");
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public void run() throws IOException {
        System.out.println("**Chat MODO BATCH GEMINI**");
        System.out.println("Escreva todas as suas peguntas, quando você quiser o resumo de suas respostas");
        System.out.println("pressione <enter> em uma linha vazia. Minimo de três perguntas");
        System.out.println("*******************************************************************************");
        System.out.println("Digite 'sair' para sair");
        while (true) {
            System.out.print("->> ");
            pegarInput();
        }
    }

    void pegarInput() throws IOException {
        var sc = new Scanner(System.in);
        var input = sc.nextLine().trim();
        switch (input) {
            case "":
                if (this.hist.size() < 3) {
                    System.out.println("Você precisa fazer no mínimo três perguntas");
                    return;
                }
                System.out.println("Processando as suas perguntas...");
                var res = ConsomeApi.fazerPergunta(this.hist);
                res = res.trim();
                this.saveFile.append("****Perguntas\n");
                for (int i = 0; i < this.hist.size(); i++) {
                    this.saveFile.append(String.format("%d. %s", i + 1, this.hist.get(i)));
                    this.saveFile.append("\n");
                }
                this.saveFile.append("****Respostas\n");
                this.saveFile.append(res);
                this.saveFile.append("\n**********************************************************\n\n");
                this.saveFile.flush();
                System.out.println("Resposta salva no arquivo: 'respostas.txt'");
                this.hist.clear();
                return;
            case "sair":
                System.exit(1);
            default:
                this.hist.add(input);
        }
    }
}
