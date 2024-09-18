package view;

import service.ConsomeApi;

import java.util.Scanner;

public class Shell {
    public static void run() {
        System.out.println("**Chat GEMINI**");
        System.out.println("Digite 'sair' para sair");
        while (true) {
            System.out.print("->> ");
            pegarInput();
        }
    }

    static void pegarInput() {
        var sc = new Scanner(System.in);
        var input = sc.nextLine().trim();
        switch (input) {
            case "":
                return;
            case "sair":
                System.exit(1);
            default:
                var res = ConsomeApi.fazerPergunta(input);
                System.out.print("AI -* ");
                System.out.println(res);
        }
    }
}
