package model;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;

    // Constructor
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter and setter methods
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Add stock to the product
    public void addStock(int qty) { this.quantity += qty; }

    // Reduce stock if sufficient quantity is available
    public boolean reduceStock(int qty) {
        if (this.quantity >= qty) {
            this.quantity -= qty;
            return true;
        }
        return false; // Not enough stock to reduce
    }

    // Convert product details to a readable string
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Price: " + price + ", Quantity: " + quantity;
    }
}
