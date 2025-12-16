package Models;

public abstract class Person {
    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private String phone;
    private String email;


    public Person(String firstName, String lastName, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public Gender getGender() { return gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return String.format("%s{id=%s, name=%s %s, phone=%s, email=%s}",
                this.getClass().getSimpleName(), firstName, lastName, gender, phone, email);
    }
}