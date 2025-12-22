package Models;

// The admin but here we're renaming as nurse
public class Nurse extends Person{
    public Nurse(String firstName, String lastName, Gender gender,
                 String username, String password) {
        super(firstName, lastName, gender, username, password);
    }
}
