package common;

public class User extends Person {
    private static final long serialVersionUID = 1L;
    private String accessType;

    public User(String firstName, String lastName, String username, String password,String accessType) {
        super(firstName, lastName, username, password, accessType);
    }

}
