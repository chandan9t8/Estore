package server;

import common.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class EstoreImpl extends UnicastRemoteObject implements Estore {
    
    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Admin> admins = new ConcurrentHashMap<>();
    private List<Product> products = new ArrayList<>();
    private ConcurrentHashMap<String, ShoppingCart> shoppingCarts = new ConcurrentHashMap<>();

    public EstoreImpl() throws RemoteException {
        super();
        initializeStore();
    }

    private void initializeStore() {
        // Initialize sample data
        products.add(new Product("Laptop","001", 999.99, 10));
        products.add(new Product("Smartphone","002", 499.99, 20));
        products.add(new Product("Tablet","003",  299.99, 15));

        Admin admin1 = (Admin) PersonFactory.createPerson("admin1","doe", "admin1doe", "12345","admin");
        User user1 = (User) PersonFactory.createPerson("user1", "doe", "user1doe", "12345","user");

        admins.put(admin1.getUsername(), admin1);
        users.put(user1.getUsername(), user1);
        shoppingCarts.put(user1.getUsername(), new ShoppingCart());
    }

    @Override
    public User login(String username, String password) throws RemoteException {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean registerUser(User user) throws RemoteException {
        //debug
        System.out.println("Inside registerUser()");
        if (users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        shoppingCarts.put(user.getUsername(), new ShoppingCart());
        return true;
    }

    @Override
    public Admin loginAdmin(String username, String password) throws RemoteException {
        Admin admin = admins.get(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    @Override
    public boolean registerAdmin(Admin admin) throws RemoteException {
        if (admins.containsKey(admin.getUsername())) {
            return false;
        }
        admins.put(admin.getUsername(), admin);
        return true;
    }


    @Override
    public List<Product> browseProducts() throws RemoteException {
        return new ArrayList<>(products);
    }

    @Override
    public boolean addToCart(String userId, String productId, int quantity) throws RemoteException {
        ShoppingCart cart = shoppingCarts.get(userId);
        Product product = products.stream()
                                  .filter(p -> p.getId().equals(productId))
                                  .findFirst()
                                  .orElse(null);
        if (cart == null || product == null || quantity <= 0) {
            return false;
        }
        boolean added = cart.addItem(product, quantity);
        if (added) {
        // Decrease the stock quantity of the product
            product.setQuantity(product.getQuantity() - quantity);
        }
        return added;
    }


    @Override
    public double purchase(String username) throws RemoteException {
        ShoppingCart cart = shoppingCarts.get(username);
        if (cart != null && !cart.getCart().isEmpty()) {
            double totalCost = cart.getTotalCost();
            cart.clearCart();
            return totalCost; 
        }
        return 0.0;
    }

    @Override
    public boolean addProduct(Product product, Admin admin) throws RemoteException {
        if (admin == null || !admins.containsKey(admin.getUsername())) {
            throw new RemoteException("Unauthorized access");
        }
        
        for (Product existingProduct : products) {
            if (existingProduct.getId().equals(product.getId())) {
                return false;

            }
        }
        if (!products.contains(product)) {
            products.add(product);
            return true;
        } 
        return false;
    }

    @Override
    public boolean updateProduct(Product product, Admin admin) throws RemoteException {
        if (admin == null || !admins.containsKey(admin.getUsername())) {
            throw new RemoteException("Unauthorized access");
        }
        for (int i = 0; i < products.size(); i++) {
            Product existingProduct = products.get(i);
            if (existingProduct.getId().equals(product.getId())) {
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            return true;
            }
        }
    return false;
    }


    @Override
    public boolean removeProduct(String productId, Admin admin) throws RemoteException {
        if (admin == null || !admins.containsKey(admin.getUsername())) {
            throw new RemoteException("Unauthorized access");
        }
        return products.removeIf(p -> p.getId().equals(productId));
    }

    @Override
    public String checkProductAvailability(String productId, int quantity) throws RemoteException {
        for (Product product : products) {
            if (product.getId().equals(productId) && product.getQuantity() >= quantity) {
                return "Available";
            }
        }
        return "Not available or insufficient quantity";
    }

    @Override
    public Map<Product, Integer> getShoppingCartContents(String userId) throws RemoteException {
        ShoppingCart cart = shoppingCarts.get(userId);
        if (cart != null) {
            return new HashMap<>(cart.getCart());
        }
        return Collections.emptyMap(); 
    }

    @Override
    public boolean removeItemFromCart(String username, String productId) throws RemoteException {
        ShoppingCart cart = shoppingCarts.get(username);
        if (cart != null) {
            return cart.deleteItem(productId);
        }
        return false;
    }


    @Override
    public boolean addUser(User user) throws RemoteException {
        if (users.containsKey(user.getUsername())) {
            return false; // User already exists
        }
        users.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean removeUser(String username) throws RemoteException {
        if (!users.containsKey(username)) {
            return false; // User does not exist
        }
        users.remove(username);
        return true;
    }

    @Override
    public boolean addAdmin(Admin admin) throws RemoteException {
        if (admins.containsKey(admin.getUsername())) {
            return false; // Admin already exists
        }
        admins.put(admin.getUsername(), admin);
        return true;
    }
}
