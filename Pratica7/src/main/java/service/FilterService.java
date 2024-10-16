package service;

import model.Product;

import java.util.List;
import java.util.Stack;

public class FilterService {
    public static List<String> getProductsInSale(List<Product> products, int price) {
        return products.stream()
                .filter(p -> p.price() < price)
                .map(Product::title).toList();
    }
}