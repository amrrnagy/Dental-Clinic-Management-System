package Models;

public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final String username;
    private final String password;
    private final UserRole role;


    public Person(String firstName, String lastName, Gender gender,
                  String username, String password, UserRole role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public Gender getGender() { return gender; }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public UserRole getRole() { return role; }
}