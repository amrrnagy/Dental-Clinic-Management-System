package Models;

public record PrescriptionItem(String medicineName, String dose, String frequency, int days) {
    public PrescriptionItem {

        if (medicineName == null || medicineName.trim().isEmpty() || days <= 0) {
            throw new IllegalArgumentException("Medicine name and positive days are required.");
        }

    }

    @Override
    public String toString() {
        return String.format("%s %s, %s for %d days", medicineName, dose, frequency, days);
    }
}