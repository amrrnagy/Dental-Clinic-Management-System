package Models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Appointment represents a scheduled appointment between a patient and a doctor.
 * Implements Comparable by dateTime to allow chronological sorting.
 */

public class Appointment implements Comparable<Appointment> {
    private final String id;
    private final String patientId;
    private final String doctorId;
    private LocalDateTime dateTime;
    private AppointmentSlot slot;
    private AppointmentStatus status;
    private String reason;

    public Appointment(String patientId, String doctorId, LocalDateTime dateTime, AppointmentSlot slot) {
        if (patientId == null || doctorId == null || dateTime == null) {
            throw new IllegalArgumentException("patientId, doctorId and dateTime are required");
        }
        this.id = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.slot = slot;
        this.status = AppointmentStatus.SCHEDULED;
        this.reason = "";
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public void setSlot(AppointmentSlot slot) { this.slot = slot; }
    public AppointmentSlot getSlot() { return slot; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null) throw new IllegalArgumentException("dateTime required");
        this.dateTime = dateTime;
    }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status == null ? AppointmentStatus.SCHEDULED : status; }


    @Override
    public int compareTo(Appointment other) {
        return this.dateTime.compareTo(other.dateTime);
    }

    @Override
    public String toString() {
        return String.format("Appointment{id=%s, patientId=%s, doctorId=%s, at=%s, duration=%d, status=%s}",
                id, patientId, doctorId, dateTime, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;
        return id.equals(that.id);
    }
}