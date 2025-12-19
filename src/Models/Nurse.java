package Models;

public class Nurse extends Person{
    public Nurse(String firstName, String lastName, Gender gender,
                 String username, String password) {
        super(firstName, lastName, gender, username, password, UserRole.NURSE);
    }
}
