package Models;

import java.time.LocalDateTime;

public class Payment implements Payable{
    private final String paymentID;
    private final String patientId;
    private final double amount;
    private final PaymentMethod method;
    private final LocalDateTime dateTime;

    public static int nextId = 1;

    public Payment(String patientId, double amount, PaymentMethod method) {
        if (patientId == null) throw new IllegalArgumentException("patientId required");

        LocalDateTime now = LocalDateTime.now();

        this.paymentID = String.format("PAY-%d-%02d-%03d",
                now.getYear(),
                now.getMonthValue(),
                nextId++);

        this.patientId = patientId;
        this.amount = amount;
        this.method = method == null ? PaymentMethod.CASH : method;
        this.dateTime = now;
    }

    // Getters
    public String getId() { return paymentID; }
    public String getPatientId() { return patientId; }
    public Patient getPayer() {
        return ClinicManager.getInstance().findPatientById(patientId);
    }

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
}
