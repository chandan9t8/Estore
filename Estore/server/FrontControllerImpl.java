package server;

import common.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FrontControllerImpl extends UnicastRemoteObject implements FrontController {
    private Estore estore;

    public FrontControllerImpl(Estore estore) throws RemoteException {
        super();
        this.estore = estore;
    }

    @Override
    public Object processRequest(String requestType, Object... parameters) throws RemoteException {
        switch (requestType) {
            case "loginUser":
                return estore.login((String) parameters[0], (String) parameters[1]);
                
            case "loginAdmin":
                return estore.loginAdmin((String) parameters[0], (String) parameters[1]);
                
            case "registerUser":
                if (parameters.length == 5) {
                    return estore.registerUser(new User((String) parameters[0], (String) parameters[1], 
                        (String) parameters[2], (String) parameters[3], (String) parameters[4]));
                }
                break;

            case "registerAdmin":
                if (parameters.length == 5) {
                    return estore.registerAdmin(new Admin((String) parameters[0], (String) parameters[1], 
                        (String) parameters[2], (String) parameters[3], (String) parameters[4]));
                }
                break;

            case "addProduct":
                if (parameters.length == 5 && parameters[4] instanceof Admin) {
                    return estore.addProduct(new Product((String) parameters[0], (String) parameters[1], 
                        (Double) parameters[2], (Integer) parameters[3]), (Admin) parameters[4]);
                }
                throw new RemoteException("Unauthorized access - Only admins can add products.");

            case "updateProduct":
                if (parameters.length == 5 && parameters[4] instanceof Admin) {
                    return estore.updateProduct(new Product((String) parameters[0], (String) parameters[1], 
                        (Double) parameters[2], (Integer) parameters[3]), (Admin) parameters[4]);
                }
                throw new RemoteException("Unauthorized access - Only admins can update products.");

            case "removeProduct":
                if (parameters.length == 2 && parameters[1] instanceof Admin) {
                    return estore.removeProduct((String) parameters[0], (Admin) parameters[1]);
                }
                throw new RemoteException("Unauthorized access - Only admins can remove products.");

            case "browseStore":
                return estore.browseProducts();

            case "addToCart":
                if (parameters.length == 3 && parameters[0] instanceof User) {
                    User user = (User) parameters[0];
                    String productId = (String) parameters[1];
                    Integer quantity = (Integer) parameters[2];
                    return estore.addToCart(user.getUsername(), productId, quantity);
                }
                throw new RemoteException("Unauthorized access - Only users can add items to cart.");

            case "removeItemFromCart":
                if (parameters.length == 2 && parameters[0] instanceof String && parameters[1] instanceof String) {
                    return estore.removeItemFromCart((String) parameters[0], (String) parameters[1]);
                }
                throw new RemoteException("Invalid arguments for removing item from cart.");

            case "viewCart":
            if (parameters.length == 1 && parameters[0] instanceof String) {
                String username = (String) parameters[0];
                return estore.getShoppingCartContents(username);
            }
            throw new RemoteException("Unauthorized access - Only users can view the cart.");
            
            case "purchaseItems":
                if (parameters.length == 1 && parameters[0] instanceof User) {
                    User user = (User) parameters[0];
                    return estore.purchase(user.getUsername());
                }
                throw new RemoteException("Unauthorized access - Only users can purchase items.");

            case "addUser":
                return estore.addUser((User) parameters[0]);
            case "removeUser":
                return estore.removeUser((String) parameters[0]);
            case "addAdmin":
                return estore.addAdmin((Admin) parameters[0]);

            default:
                throw new IllegalArgumentException("Unknown request type: " + requestType);
        }
        return null; 
    }
}
