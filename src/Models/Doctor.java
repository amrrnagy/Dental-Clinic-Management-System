package Models;

public class Doctor extends Person implements Comparable<Doctor> {
    private final String id;
    private final Specialization specialization;
    private int consultationFee;

    private static int nextId = 1;

    public Doctor(String firstName, String lastName, Gender gender,
                  String username, String password, Specialization specialization) {

        super(firstName, lastName, gender, username, password, UserRole.DOCTOR);
        this.id = "DOC" + nextId++;
        this.consultationFee = 150;
        this.specialization = specialization == null ? Specialization.OTHER : specialization;
    }

    public String getId() { return id; }
    public static void minusNextID() {
        if(nextId > 1)
            nextId--;
    }


    public Specialization getSpecialization() { return specialization; }

    public int getConsultationFee() { return consultationFee; }
    public void setConsultationFee(int consultationFee) { this.consultationFee = consultationFee; }

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
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return this.getId() + ": " + this.getFullName();
    }
}
