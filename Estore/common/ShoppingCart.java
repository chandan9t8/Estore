package common;

import java.util.Map;
import java.util.HashMap;

public class ShoppingCart {
    private Map<Product, Integer> cart;

    public ShoppingCart() {
        cart = new HashMap<>();
    }

    public boolean addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false; 
        }
        cart.put(product, cart.getOrDefault(product, 0) + quantity);
        return true;
    }

    public boolean removeItem(Product product) {
        if (product == null || !cart.containsKey(product)) {
            return false; // Product not in cart
        }
        cart.remove(product);
        return true;
    }

    public boolean updateItemQuantity(Product product, int quantity) {
        if (product == null || !cart.containsKey(product) || quantity < 0) {
            return false; // Invalid product or quantity
        }
        cart.put(product, quantity);
        return true;
    }

    public Map<Product, Integer> getCart() {
        return new HashMap<>(cart); // Return a copy to prevent external modifications
    }

    public double getTotalCost() {
        return cart.entrySet().stream()
                   .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                   .sum();
    }

    public void clearCart() {
        cart.clear();
    }

    public boolean deleteItem(String productId) {
        return cart.keySet().removeIf(product -> product.getId().equals(productId));
}

}
