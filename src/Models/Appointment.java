package Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Appointment implements Comparable<Appointment> {

    private final String id;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime dateTime;
    private final AppointmentSlot slot;
    private AppointmentStatus status;

    public static int nextId = 1;

    public Appointment(String patientId, String doctorId, LocalDateTime dateTime, AppointmentSlot slot) {
        if (patientId == null || doctorId == null || dateTime == null) {
            throw new IllegalArgumentException("patientId, doctorId and dateTime are required");
        }

        this.id = String.format("APT-%d-%02d-%03d",
                dateTime.getYear(),
                dateTime.getMonthValue(),
                nextId++);

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.slot = slot;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }

    public static void minusNextID() {
        if(nextId > 1)
            nextId--;
    }
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, hh:mm a");
        return this.dateTime.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id.equals(that.id);
    }
}