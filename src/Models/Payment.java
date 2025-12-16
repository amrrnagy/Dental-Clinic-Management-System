package Models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment implements Payable{
    private final String paymentID;
    private final String patientId;
    private final String appointmentId; // can be null for general payments
    private double amount;
    private PaymentMethod method;
    private LocalDateTime dateTime;
    private String notes;

    public Payment(String patientId, String appointmentId, double amount, PaymentMethod method) {
        if (patientId == null) throw new IllegalArgumentException("patientId required");
        this.paymentID = UUID.randomUUID().toString();
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.method = method == null ? PaymentMethod.CASH : method;
        this.dateTime = LocalDateTime.now();
        this.notes = "";
    }

    public String getId() { return paymentID; }
    public String getPatientId() { return patientId; }
    public String getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("Payment{id=%s, patientId=%s, amount=%.2f, method=%s, at=%s}",
                paymentID, patientId, amount, method, dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return paymentID.equals(payment.paymentID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentID);
    }
}
