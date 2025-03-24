package transactions;
import model.Inventory;
import model.Product;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private Inventory inventory;
    private List<String> transactionLog = new ArrayList<>();

    public Transaction(Inventory inventory) {
        this.inventory = inventory;
    }

    public void purchaseProduct(int id, int quantity) {
        Product product = inventory.getProduct(id);
        if (product != null) {
            product.addStock(quantity);
            String log = "Purchased " + quantity + " units of " + product.getName();
            transactionLog.add(log);
            System.out.println(log);
        } else {
            System.out.println("Product not found.");
        }
    }

    public void sellProduct(int id, int quantity) {
        Product product = inventory.getProduct(id);
        if (product != null) {
            if (product.reduceStock(quantity)) {
                String log = "Sold " + quantity + " units of " + product.getName();
                transactionLog.add(log);
                System.out.println(log);
            } else {
                System.out.println("Insufficient stock.");
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    public void showTransactionLog() {
        System.out.println("Transaction History:");
        for (String log : transactionLog) {
            System.out.println(log);
        }
    }
}
