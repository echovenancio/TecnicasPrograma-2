package service;

import model.Product;

import java.util.List;

public class FilterService {
    public static List<Product> getProductsInSale(List<Product> products, int price) {
        return products.stream().filter(p -> p.price() < price).toList();
    }
}