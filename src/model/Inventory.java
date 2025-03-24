package model;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Integer, Product> products = new HashMap<>();

    // Add a new product to the inventory
    public void addProduct(int id, String name, double price, int quantity) {
        if (products.containsKey(id)) {
            System.out.println("Product with this ID already exists.");
        } else {
            products.put(id, new Product(id, name, price, quantity));
        }
    }

    // Update product price and quantity
    public void updateProduct(int id, double price, int quantity) {
        Product p = products.get(id);
        if (p != null) {
            p.setPrice(price);
            p.setQuantity(quantity);
        } else {
            System.out.println("Product not found.");
        }
    }

    // Remove a product from the inventory
    public void removeProduct(int id) {
        if (products.containsKey(id)) {
            products.remove(id);
        } else {
            System.out.println("Product not found.");
        }
    }

    // List all products in the inventory
    public void listProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
        } else {
            for (Product p : products.values()) {
                System.out.println(p);
            }
        }
    }

    // Get a product by its ID
    public Product getProduct(int id) {
        return products.get(id);
    }
}
