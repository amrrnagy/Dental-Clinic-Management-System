package Models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment implements Payable{
    private final String paymentID;
    private final String patientId;
    private final String appointmentId;
    private final double amount;
    private final PaymentMethod method;
    private final LocalDateTime dateTime;

    public static int nextId = 1;

    public Payment(String patientId, String appointmentId, double amount, PaymentMethod method) {
        if (patientId == null) throw new IllegalArgumentException("patientId required");

        LocalDateTime now = LocalDateTime.now();

        this.paymentID = String.format("PAY-%d-%02d-%03d",
                now.getYear(),
                now.getMonthValue(),
                nextId++);

        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.method = method == null ? PaymentMethod.CASH : method;
        this.dateTime = now;
    }

    public String getId() { return paymentID; }
    public String getPatientId() { return patientId; }
    public String getAppointmentId() { return appointmentId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public LocalDateTime getDateTime() { return dateTime; }

    @Override
    public String toString() {
        return String.format("Payment{id=%s, patientId=%s, amount=%.2f, method=%s, at=%s}",
                paymentID, patientId, amount, method, dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment payment)) return false;
        return paymentID.equals(payment.paymentID);
    }

    public Patient getPayer() {
        return ClinicManager.getInstance().findPatientById(patientId);
    }

}
