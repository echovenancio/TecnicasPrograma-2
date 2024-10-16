import model.Product;
import service.FilterService;
import service.RequestService;

import java.util.logging.Filter;

public class App {

    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        var reqService = new RequestService();
        var productsInSale = FilterService.getProductsInSale(reqService.getProducts());
        System.out.print("\u001B[40m");
        System.out.println(YELLOW + "***********PRODUTOS IMPERDÍVEIS E EM PROMOÇÃO!***********");
        for (Product product : productsInSale) {
            System.out.println(RED + String.format("%s EM *PROMOÇÂO*", product.title()).toUpperCase());
            System.out.print("\u001B[34m");
            System.out.println(product.description());
            System.out.println(YELLOW + String.format("POR APENAS %s!", product.price()));
            System.out.print("\u001B[37m");
            System.out.println("--------------------------------------------------");
        }
    }
}