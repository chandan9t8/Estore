package common;

public class PersonFactory {

    public static Person createPerson(String firstName, String lastName, String username, String password, String accessType) {
        if ("Admin".equalsIgnoreCase(accessType)) {
            return new Admin(firstName, lastName, username, password, "admin");
        } else if ("User".equalsIgnoreCase(accessType)){
            return new User(firstName, lastName, username, password, "user");
        } else {
        throw new IllegalArgumentException("Unsupported person type: " + accessType);
        }
    } 

}  
