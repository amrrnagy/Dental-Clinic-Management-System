package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a formal medical prescription issued by a doctor to a patient.
 */
public class Prescription {

    // Fields inferred from Prescription.class
    private final String id; // Generated using UUID.randomUUID().toString()
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final LocalDate issuedDate; // Generated using LocalDate.now()
    private final List<PrescriptionItem> items; // Initialized using ArrayList
    private String notes;

    /**
     * Full constructor for creating a new Prescription.
     * Generates ID and sets the issue date automatically.
     */
    public Prescription(String appointmentId, String patientId, String doctorId) {
        if (patientId == null || patientId.trim().isEmpty() || doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID and Doctor ID are required for a Prescription.");
        }

        this.id = UUID.randomUUID().toString();
        this.appointmentId = appointmentId; // Can be null/optional, based on your system logic
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.issuedDate = LocalDate.now();
        this.items = new ArrayList<>();
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public List<PrescriptionItem> getItems() {
        // Return a defensive copy to prevent external modification of the list
        return new ArrayList<>(items);
    }

    public String getNotes() {
        return notes;
    }

    // --- Setters ---

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // --- List Management ---

    /**
     * Adds an item to the prescription list. Inferred from Prescription.class.
     */
    public void addItem(PrescriptionItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    /**
     * Removes an item from the prescription list.
     */
    public void removeItem(PrescriptionItem item) {
        this.items.remove(item);
    }

    // --- Utility Methods ---

    @Override
    public String toString() {
        // Format inferred from Prescription.class: "APrescription{id=%s, patientId=%s, doctorId=%s, date=%s, items=%d}"
        return String.format("Prescription{id=%s, patientId=%s, doctorId=%s, date=%s, items=%d, notes=%s}",
                id, patientId, doctorId, issuedDate, items.size(),
                notes != null ? notes : "N/A");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        // Equality based on ID (which is final/unique)
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}