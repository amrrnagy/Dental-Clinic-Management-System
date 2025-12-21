package Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Prescription {
    private final String id;
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final LocalDate issuedDate;
    private final List<PrescriptionItem> items;
    private final String notes;

    public Prescription(String appointmentId, String patientId, String doctorId, String notes) {

        if (patientId == null || patientId.trim().isEmpty() || doctorId == null || doctorId.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient ID and Doctor ID are required for a Prescription.");
        }

        this.id = "PRE" + appointmentId.substring(3);
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.notes = notes;
        this.issuedDate = LocalDate.now();
        this.items = new ArrayList<>();
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
    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public List<PrescriptionItem> getItems() {
        return items;
    }

    public void addItems(PrescriptionItem item) { items.add(item); }

    public String getNotes() {
        return notes;
    }

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
        return Objects.equals(id, that.id);
    }
}