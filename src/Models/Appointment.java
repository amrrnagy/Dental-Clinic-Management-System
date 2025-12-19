package Models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment implements Comparable<Appointment> {

    private final String id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime dateTime;
    private final AppointmentSlot slot;
    private AppointmentStatus status;

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
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }

    public AppointmentSlot getSlot() { return slot; }

    public LocalDateTime getDateTime() { return dateTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status == null ? AppointmentStatus.SCHEDULED : status; }


    @Override
    public int compareTo(Appointment other) {
        int dateTimeCmp = this.dateTime.compareTo(other.dateTime);
        if (dateTimeCmp != 0) {
            return dateTimeCmp;
        }
        return this.doctorId.compareTo(other.doctorId);
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