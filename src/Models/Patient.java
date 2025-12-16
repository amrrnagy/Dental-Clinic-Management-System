package Models;

import java.util.List;
import java.util.Objects;

/**
 * Patient entity. Implements Comparable so patients can be sorted by lastName, then firstName.
 */
public class Patient extends Person implements Comparable<Patient> {
    private final int id;
    private String medicalHistory;
    private double balance;
    private static int nextID = 1;
    private List<Prescription> prescriptions;

    public Patient(String firstName, String lastName, Gender gender) {
        super(firstName, lastName, gender);
        this.id = nextID++;
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
    public String toString() {
        return String.format("Patient{id=%s, name=%s, balance=%.2f}",
                getId(), getFullName(), balance);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
