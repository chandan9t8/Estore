package common;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String username;
    protected String password; 
    protected String firstName;
    protected String lastName;
    protected String accessType;

    public Person(String firstName, String lastName, String username, String password,String accessType) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessType = accessType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAccessType() {
        return accessType;
    }

}
