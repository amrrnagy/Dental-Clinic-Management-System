package Models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single prescribed item (medicine) with dosage details.
 * Implements Serializable as inferred from the .class file.
 */
public class PrescriptionItem implements Serializable {

    // Fields inferred from PrescriptionItem.class
    private String medicineName;
    private String dose;
    private String frequency;
    private int days;

    /**
     * Full constructor for creating a new prescription item.
     */
    public PrescriptionItem(String medicineName, String dose, String frequency, int days) {
        // Basic validation
        if (medicineName == null || medicineName.trim().isEmpty() || days <= 0) {
            throw new IllegalArgumentException("Medicine name and positive days are required.");
        }
        this.medicineName = medicineName;
        this.dose = dose;
        this.frequency = frequency;
        this.days = days;
    }

    // Default constructor (required for some frameworks/serialization)
    public PrescriptionItem() {
        // Initializes fields to default values
    }

    // --- Getters ---

    public String getMedicineName() {
        return medicineName;
    }

    public String getDose() {
        return dose;
    }

    public String getFrequency() {
        return frequency;
    }

    public int getDays() {
        return days;
    }

    // --- Setters ---

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setDays(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive.");
        }
        this.days = days;
    }

    // --- Utility Methods ---

    @Override
    public String toString() {
        // Format inferred from PrescriptionItem.class: "%s %s, %s for %d days"
        return String.format("%s %s, %s for %d days", medicineName, dose, frequency, days);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrescriptionItem that = (PrescriptionItem) o;
        return days == that.days &&
                Objects.equals(medicineName, that.medicineName) &&
                Objects.equals(dose, that.dose) &&
                Objects.equals(frequency, that.frequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicineName, dose, frequency, days);
    }
}