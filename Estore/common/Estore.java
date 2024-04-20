package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface Estore extends Remote {
    // User Management
    User login(String username, String password) throws RemoteException;
    boolean registerUser(User user) throws RemoteException;

    // Administrator Management
    Admin loginAdmin(String username, String password) throws RemoteException;
    boolean registerAdmin(Admin admin) throws RemoteException;
    boolean addUser(User user) throws RemoteException;
    boolean removeUser(String username) throws RemoteException;
    boolean addAdmin(Admin admin) throws RemoteException;


    // Product Management for Customers
    List<Product> browseProducts() throws RemoteException;
    boolean addToCart(String userId, String productId, int quantity) throws RemoteException;
    double purchase(String userId) throws RemoteException; 
    boolean removeItemFromCart(String userId, String productId) throws RemoteException;
    Map<Product, Integer> getShoppingCartContents(String userId) throws RemoteException;


    // Product Management for Administrators
    boolean addProduct(Product product,Admin admin) throws RemoteException;
    boolean updateProduct(Product product,Admin admin) throws RemoteException; // Update description, price, quantity
    boolean removeProduct(String productId, Admin admin) throws RemoteException; // Remove product from catalog

    // Additional methods for robust handling
    String checkProductAvailability(String productId, int quantity) throws RemoteException; // Check availability before adding to cart
}
