package server;

import common.*;
import server.EstoreImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EstoreServer {
    public static void main(String[] args){
        try {
            System.setProperty("java.rmi.server.hostname", "in-csci-rrpc04.cs.iupui.edu");
            Estore estore = new EstoreImpl();
            FrontController frontController = new FrontControllerImpl(estore);

            Registry registry = LocateRegistry.createRegistry(2102); 
            registry.bind("FrontController", frontController);
            System.out.println("Front Controller bound successfully");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
