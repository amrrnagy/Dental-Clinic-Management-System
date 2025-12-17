package Models;

import java.util.List;

/**
 * Patient entity. Implements Comparable so patients can be sorted by lastName, then firstName.
 */
public class Patient extends Person implements Comparable<Patient> {
    private final int id;
    private final String phone;
    private String medicalHistory;
    private double balance;
    private static int nextID = 1;
    private List<Prescription> prescriptions;

    public Patient(String firstName, String lastName, Gender gender, String phone,
                   String username, String password) {
        super(firstName, lastName, gender, username, password, UserRole.PATIENT);
        this.id = nextID++;
        this.phone = phone;
        this.medicalHistory = "";
        this.balance = 0.0;
    }

    public Integer getId() {
        return id;
    }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getPhone() { return phone; }

    public void applyPayment(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be >= 0");
        balance = Math.max(0, balance - amount);
    }

    @Override
    public int compareTo(Patient other) {
        int cmp = this.getLastName().compareToIgnoreCase(other.getLastName());
        if (cmp != 0) return cmp;
        cmp = this.getFirstName().compareToIgnoreCase(other.getFirstName());
        if (cmp != 0) return cmp;
        return this.getId().compareTo(other.getId());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
