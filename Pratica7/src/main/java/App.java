import model.Product;
import service.FilterService;
import service.RequestService;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Filter;

public class App {

    public static void main(String[] args) {
        System.out.println("****SISTEMA DE BUSCA DE PRODUTOS EM PROMOÇÂO****");
        System.out.println("digite 'sair' para sair.");
        var sc = new Scanner(System.in);
        while (true) {
            System.out.print("Digite o preço máximo do produto ->> ");
            var input = sc.nextLine().split(" ");
            if (Objects.equals(input[0], "")) { continue; }
            else if (input.length > 1) { System.out.println("Não esperava mais de um argumento"); continue; }
            if (Objects.equals(input[0], "sair")) { System.exit(0); }
            try {
                int price = Integer.parseInt(input[0]);
                App.printProducts(price);
            } catch (NumberFormatException e) {
                System.err.println("\nFormato numérico inválido!");
            }
        }
    }

    private static void printProducts(int price) {
        var reqService = new RequestService();
        var productsInSale = FilterService.getProductsInSale(reqService.getProducts(), price);
        if (productsInSale.isEmpty()) {
            System.out.println("Nenhum produto encontrado nessa faixa de preço.");
            return;
        }
        System.out.println("***********PRODUTOS IMPERDÍVEIS E EM PROMOÇÃO!***********");
        for (String productTitle : productsInSale) {
            System.out.println(String.format("+ %s EM *PROMOÇÂO*", productTitle).toUpperCase());
        }
    }
}