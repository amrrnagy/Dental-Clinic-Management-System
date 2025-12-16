package Models;

import java.util.Objects;


public class Doctor extends Person implements Comparable<Doctor> {
    private final Specialization specialization;
    private final int id ;
    private int consualtionFee ;

    private static int nextID = 1;

    public Doctor(String firstName, String lastName, Gender gender, Specialization specialization) {
        super(firstName, lastName, gender);
        this.id = nextID++;
        this.consualtionFee = 150;
        this.specialization = specialization == null ? Specialization.OTHER : specialization;

    }

    public Integer getId() { return id; }

    public Specialization getSpecialization() { return specialization; }

    public int getConsualtionFee() { return consualtionFee; }
    public void setConsualtionFee(int consualtionFee) { this.consualtionFee = consualtionFee; }

    @Override
    public int compareTo(Doctor other) {
        int cmp = this.specialization.name().compareToIgnoreCase(other.specialization.name());
        if (cmp != 0) return cmp;
        cmp = this.getLastName().compareToIgnoreCase(other.getLastName());
        if (cmp != 0) return cmp;
        cmp = this.getFirstName().compareToIgnoreCase(other.getFirstName());
        if (cmp != 0) return cmp;
        return this.getId().compareTo(other.getId());
    }

    @Override
    public String toString() {
        return String.format("Doctor{id=%s, name=%s, spec=%s}", getId(), getFullName(), specialization);
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
