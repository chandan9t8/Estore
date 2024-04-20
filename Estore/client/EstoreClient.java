package client;

import common.*;
import java.rmi.Naming;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;  
import java.util.Map.Entry;


public class EstoreClient {

    private static final String SERVER_ADDRESS = "rmi://in-csci-rrpc04.cs.iupui.edu:2102/FrontController";

    public static void main(String[] args) {
        try {
            FrontController frontController = (FrontController) Naming.lookup(SERVER_ADDRESS);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            initialMenu(frontController, reader);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void initialMenu(FrontController frontController, BufferedReader reader) throws IOException {
        while (true) {
            System.out.println("Welcome to EStore! Are you a 'User' or 'Admin'? Type 'exit' to quit.");
            String role = reader.readLine();

            if ("exit".equalsIgnoreCase(role)) {
                System.out.println("Exiting the application.");
                break;
            }


            System.out.println("Do you want to 'Login' or 'Signup'?");
            String action = reader.readLine();

            if ("user".equalsIgnoreCase(role)) {
                if ("login".equalsIgnoreCase(action)) {
                    handleLogin(frontController, reader, "loginUser");
                } else if ("signup".equalsIgnoreCase(action)) {
                    handleSignup(frontController, reader, "registerUser");
                }
            } else if ("admin".equalsIgnoreCase(role)) {
                if ("login".equalsIgnoreCase(action)) {
                    handleLogin(frontController, reader, "loginAdmin");
                } else if ("signup".equalsIgnoreCase(action)) {
                    handleSignup(frontController, reader, "registerAdmin");
                }
            } else {
                System.out.println("Invalid role or action. Please try again.");
            }
        }
    }

    private static void handleLogin(FrontController frontController, BufferedReader reader, String method) throws IOException {
        System.out.println("Enter your username:");
        String username = reader.readLine();
        System.out.println("Enter your password:");
        String password = reader.readLine();

        Person person = (Person) frontController.processRequest(method, username, password);
    
        if (person != null) {
            System.out.println("\nLogin successful!");
            if (person instanceof Admin) {
                handleAdminOperations(frontController, reader, (Admin) person);

            } else if (person instanceof User) {
                handleUserOperations(frontController, reader, (User) person);
            }
        } else {
            System.out.println("Invalid credentials! Please try again.");
        }
    }

    private static void handleSignup(FrontController frontController, BufferedReader reader, String method) throws IOException {

        System.out.println("Enter your first name:");
        String firstName = reader.readLine();

        System.out.println("Enter your last name:");
        String lastName = reader.readLine();

        System.out.println("Choose a username:");
        String username = reader.readLine();

        System.out.println("Set a password:");
        String password = reader.readLine();
        
        System.out.println("Confirm access type (User/Admin):");
        String accessType = reader.readLine();

        Person person = PersonFactory.createPerson(firstName, lastName, username, password, accessType);
        Boolean result = (Boolean) frontController.processRequest(method, firstName, lastName, username, password, accessType);
        if (result != null && result) {
            System.out.println("Registration successful.");

            if (person instanceof Admin) {
                handleAdminOperations(frontController, reader, (Admin) person);
            } else if (person instanceof User) {
                handleUserOperations(frontController, reader, (User) person);
            }
        } else {
            System.out.println("Username already exists. Please try a different username or log in.");
        }
    }

    private static void handleUserOperations(FrontController frontController, BufferedReader reader, User user) throws IOException {

        while(true){
            System.out.println("\nUser Operations:");
            System.out.println("1. Browse Store");
            System.out.println("2. Add item to cart");
            System.out.println("3. Remove item from cart");
            System.out.println("4. View cart");
            System.out.println("5. Purchase Items");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            String choice = reader.readLine();
            switch (choice) {
                case "1":
                    browseStore(frontController,user);
                    break;
                case "2":
                    addItemToCart(frontController, reader, user);
                    break;
                case "3":
                    removeItemFromCart(frontController, reader, user);
                    break;
                case "4":
                    ViewCart(frontController, user);
                    break;
                case "5":
                    PurchaseItems(frontController, user);;
                case "6":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    handleUserOperations(frontController, reader, user);
            }

        }
        
    }

    private static void addItemToCart(FrontController frontController, BufferedReader reader, User user) throws IOException {
        System.out.println("Enter the product ID:");
        String productId = reader.readLine();
        System.out.println("Enter quantity:");
        int quantity = Integer.parseInt(reader.readLine());
        frontController.processRequest("addToCart", user, productId, quantity);
        System.out.println("Item added to cart successfully!");
    }


    private static void removeItemFromCart(FrontController frontController, BufferedReader reader, User user) throws IOException {
        System.out.println("Enter the product ID:");
        String productId = reader.readLine();
        Boolean result = (Boolean) frontController.processRequest("removeItemFromCart", user.getUsername(), productId);
        if (Boolean.TRUE.equals(result)) {
            System.out.println("Item removed from cart successfully!");
        } else {
            System.out.println("Failed to remove item or item does not exist.");
        }
    }

    private static void ViewCart(FrontController frontController, User user) throws IOException {
        Map<Product, Integer> cartContents = (Map<Product, Integer>) frontController.processRequest("viewCart", user.getUsername());
        if (cartContents.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\nItems in Your Cart:");
            for (Map.Entry<Product, Integer> entry : cartContents.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                System.out.printf("%s - %s - $%.2f - Qty: %d\n", product.getId(), product.getName(), product.getPrice(), quantity);
            }
        }
    }

    private static void PurchaseItems(FrontController frontController, User user) throws IOException {
        double totalCost = (double) frontController.processRequest("purchaseItems", user);
        System.out.println("Purchase completed. Total cost: " + totalCost);
    }


    private static void handleAdminOperations(FrontController frontController, BufferedReader reader, Admin person) throws IOException {
        Admin admin = (Admin) frontController.processRequest("loginAdmin", person.getUsername(), person.getPassword());
        if (admin == null) {
            System.out.println("Not authorized as admin");
            return;
        }
        while (true){
            System.out.println("\nAdmin Operations:");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Remove Product");
            System.out.println("4. Browse Store");
            System.out.println("5. Add User");
            System.out.println("6. Remove User");
            System.out.println("7. Add Admin");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            String choice = reader.readLine();
            switch (choice) {
                case "1":
                    addProduct(frontController, reader, admin);
                    break;
                case "2":
                    updateProduct(frontController, reader, admin);
                    break;
                case "3":
                    removeProduct(frontController, reader, admin);
                    break;
                case "4":
                    browseStore(frontController, admin);
                    break;
                case "5":
                    addUser(frontController, reader, admin);
                    break;
                case "6":
                    removeUser(frontController, reader,admin);
                    break;
                case "7":
                    addAdmin(frontController,reader, admin);
                    break;
                case "8":
                    System.out.println("Exiting admin operations.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    handleAdminOperations(frontController, reader, admin);
            }

        }

        
    }

    private static void browseStore(FrontController frontController, Person person) throws IOException {
        List<Product> products = (List<Product>) frontController.processRequest("browseStore",  person);
        if (!products.isEmpty()) {
            System.out.println("\nItems available in store:");
            for (Product product : products) {
                System.out.println(product.getId() + "\t" + product.getName() + "\t" + product.getPrice() + "\t" + product.getQuantity());
            }
        } else {
            System.out.println("The store is empty. Come back later!");
        }
    }

    private static void addProduct(FrontController frontController, BufferedReader reader, Admin admin) throws IOException {
        System.out.println("\nEnter product ID:");
        String id = reader.readLine();
        System.out.println("Enter product name:");
        String name = reader.readLine();
        System.out.println("Enter product price:");
        double price = Double.parseDouble(reader.readLine());
        System.out.println("Enter product quantity:");
        int quantity = Integer.parseInt(reader.readLine());

        Boolean result = (Boolean) frontController.processRequest("addProduct",name,id, price, quantity, admin);

        if (result) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Failed to add product: Product may already exist.");
        }
    }

    private static void updateProduct(FrontController frontController, BufferedReader reader, Admin admin) throws IOException {
        System.out.println("\nEnter product ID you want to update:");
        String id = reader.readLine();
        System.out.println("Enter new product name:");
        String name = reader.readLine();
        System.out.println("Enter new product price:");
        double price = Double.parseDouble(reader.readLine());
        System.out.println("Enter new product quantity:");
        int quantity = Integer.parseInt(reader.readLine());

        Boolean result = (Boolean) frontController.processRequest("updateProduct", name, id, price, quantity, admin);

        if (Boolean.TRUE.equals(result)){
            System.out.println("Product updated successfully!");
        }
        else{
            System.out.println("Product doesn't exist");
        }
        
    }
    private static void removeProduct(FrontController frontController, BufferedReader reader, Admin admin) throws IOException {
        System.out.println("\nEnter product ID to remove:");
        String id = reader.readLine();

        Boolean result = (Boolean) frontController.processRequest("removeProduct", id, admin);

        if (Boolean.TRUE.equals(result)){
            System.out.println("Product removed successfully!");
        }
        else{
            System.out.println("Product doesn't exist");
        }
    }

    private static void addUser(FrontController frontController, BufferedReader reader, Admin admin) throws IOException {
        System.out.println("Enter first name:");
        String firstName = reader.readLine();
        System.out.println("Enter last name:");
        String lastName = reader.readLine();
        System.out.println("Choose a username:");
        String username = reader.readLine();
        System.out.println("Set a password:");
        String password = reader.readLine();

        User newUser = new User(firstName, lastName, username, password, "user");
        Boolean result = (Boolean) frontController.processRequest("addUser", newUser);
        if (Boolean.TRUE.equals(result)) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Failed to add user. Username may already exist.");
        }
    }

    private static void removeUser(FrontController frontController, BufferedReader reader, Admin admin) throws IOException {
        System.out.println("Enter the username of the user to remove:");
        String username = reader.readLine();

        Boolean result = (Boolean) frontController.processRequest("removeUser", username);
        if (Boolean.TRUE.equals(result)) {
            System.out.println("User removed successfully.");
        } else {
            System.out.println("Failed to remove user. User may not exist.");
        }
    }

    private static void addAdmin(FrontController frontController, BufferedReader reader, Admin admin) throws IOException {
        System.out.println("Enter first name:");
        String firstName = reader.readLine();
        System.out.println("Enter last name:");
        String lastName = reader.readLine();
        System.out.println("Choose a username:");
        String username = reader.readLine();
        System.out.println("Set a password:");
        String password = reader.readLine();

        Admin newAdmin = new Admin(firstName, lastName, username, password, "admin");
        Boolean result = (Boolean) frontController.processRequest("addAdmin", newAdmin);
        if (Boolean.TRUE.equals(result)) {
            System.out.println("Admin added successfully.");
        } else {
            System.out.println("Failed to add admin. Username may already exist.");
        }
    }

}
