package Models;

/*
    Abstract Class for other people to inherit from.
    Has the default attributes
 */

public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final String username;
    private final String password;


    public Person(String firstName, String lastName, Gender gender,
                  String username, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.username = username;
        this.password = password;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public Gender getGender() { return gender; }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}