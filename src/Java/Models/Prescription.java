package Models;

import java.time.LocalDateTime;
import java.util.Objects;

// Prescriptions to be written by the Doctor for the Patient
public class Prescription {
    private final String id;
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime issuedDate;
    private final PrescriptionItem item;

    public Prescription(String appointmentId, String patientId, String doctorId, PrescriptionItem item) {

        if (patientId == null || patientId.trim().isEmpty() || doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID and Doctor ID are required for a Prescription.");
        }

        this.id = "PRE" + appointmentId.substring(3);
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.issuedDate = LocalDateTime.now();
        this.item = item;
    }

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
    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }
    public PrescriptionItem getItem() {
        return item;
    }

    @Override
    public String toString() {
        // Format inferred from Prescription.class: "APrescription{id=%s, patientId=%s, doctorId=%s, date=%s, items=%d}"
        return id + ": " + patientId + ": " + issuedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return Objects.equals(id, that.id);
    }
}