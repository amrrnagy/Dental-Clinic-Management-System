package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClinicManager {

    // --- 1. Singleton Instance ---
    private static ClinicManager instance;

    // --- 2. Data Collections ---
    private final List<Patient> patients;
    private final List<Doctor> doctors;
    private final List<Appointment> appointments;
    private final List<Payment> payments;

    /**
     * Private constructor to enforce the Singleton pattern.
     * Initializes all data collections and loads dummy data.
     */

    private ClinicManager() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.payments = new ArrayList<>();
        loadInitialData(); // Populate with some test data
    }

    /**
     * Global access point for the Singleton instance.
     * @return The single instance of ClinicManager.
     */

    public static ClinicManager getInstance() {
        if (instance == null) {
            instance = new ClinicManager();
        }
        return instance;
    }

    // --- 3. Data Initialization (Dummy Data) ---

    private void loadInitialData() {
        // NOTE: Person ID is auto-generated in your Person class structure

        // Add Doctors
        Doctor doc1 = new Doctor("Dr. Alice", "Hany", Gender.MALE, Specialization.GENERAL_DENTISTRY);
        Doctor doc2 = new Doctor("Dr. Wesam", "Carter", Gender.FEMALE, Specialization.ORTHODONTICS);
        doctors.add(doc1);
        doctors.add(doc2);

        // Add Patients
        Patient pat1 = new Patient("John", "Ashraf", Gender.MALE);
        Patient pat2 = new Patient("Jane", "Nagy", Gender.FEMALE);
        // Set an initial balance for testing payment logic
        pat1.setBalance(150.00);
        patients.add(pat1);
        patients.add(pat2);

        // Schedule an initial appointment using the new method
        // Schedule for tomorrow at 10:00 AM for 60 minutes
        scheduleAppointment(
                pat1.getId().toString(),
                doc1.getId().toString(),
                LocalDateTime.now().plusDays(1).toLocalDate(), // Use LocalDate for the day
                AppointmentSlot.SLOT_10_00_AM// Use the new Slot
        );
    }

    // -------------------------------------------------------------------
    // --- 4. Core Entity Management (CRUD and Search) ---
    // -------------------------------------------------------------------

    /**
     * Finds a Patient by their ID. (Using String ID for consistency)
     * @param id The String ID of the patient (p.getId().toString()).
     * @return The Patient object, or null if not found.
     */
    public Patient findPatientById(String id) {
        return patients.stream()
                .filter(p -> p.getId().toString().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a Doctor by their ID. (Using String ID for consistency)
     * @param id The String ID of the doctor (d.getId().toString()).
     * @return The Doctor object, or null if not found.
     */
    public Doctor findDoctorById(String id) {
        return doctors.stream()
                .filter(d -> d.getId().toString().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ... addDoctor and addPatient methods remain unchanged ...
    public void addPatient(Patient p) {
        if (p != null) {
            this.patients.add(p);
        }
    }
    public void addDoctor(Doctor d) {
        if (d != null) {
            this.doctors.add(d);
        }
    }


    // -------------------------------------------------------------------
    // --- 5. Core Operational Logic ---
    // -------------------------------------------------------------------

    /**
     * Checks if a doctor is available during a specified time window.
     */
    public boolean isDoctorAvailable(String doctorId, LocalDateTime start, LocalDateTime end) {
        // Check for existing appointments that overlap
        return appointments.stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .noneMatch(existing -> {
                    // Overlap check: [start, end) vs [existingStart, existingEnd)
                    LocalDateTime existingStart = existing.getDateTime();
                    LocalDateTime existingEnd = existingStart.plusHours(1);

                    // If the proposed new time starts before existing ends AND
                    // the proposed new time ends after existing starts, they overlap.
                    return (start.isBefore(existingEnd) && end.isAfter(existingStart));
                });
    }

    /**
     * Schedules a new appointment using a preset time slot.
     * This is the core method now, replacing the one taking LocalDateTime directly.
     *
     * @param patientId ID of the patient.
     * @param doctorId ID of the doctor.
     * @param date The LocalDate of the appointment.
     * @param slot The preset AppointmentSlot (start time).
     * @return The newly created Appointment object, or null if scheduling failed.
     */
    public Appointment scheduleAppointment(String patientId, String doctorId,
                                           java.time.LocalDate date, AppointmentSlot slot) {

        LocalDateTime dateTime = LocalDateTime.of(date, slot.getStartTime());
        LocalDateTime endTime = dateTime.plusMinutes(60);

        // 1. Basic checks
        if (findPatientById(patientId) == null || findDoctorById(doctorId) == null) {
            System.out.println("Error: Patient or Doctor not found.");
            return null;
        }

        // 2. Check Doctor Availability
        if (!isDoctorAvailable(doctorId, dateTime, endTime)) {
            System.out.println("Error: Doctor is not available at the requested time slot.");
            return null;
        }

        // 3. Create and add Appointment
        try {
            // The Appointment constructor handles validation
            Appointment newAppointment = new Appointment(patientId, doctorId, dateTime);
            appointments.add(newAppointment);
            return newAppointment;

        } catch (IllegalArgumentException e) {
            System.err.println("Scheduling failed: " + e.getMessage());
            return null;
        }
    }


    /**
     * Processes a payment and updates the patient's outstanding balance.
     */
    public Payment processPayment(String patientId, double amount, PaymentMethod method, String appointmentId) {
        // 1. Find the Patient
        Patient patient = findPatientById(patientId);

        if (patient == null) {
            throw new IllegalArgumentException("Patient not found for ID: " + patientId);
        }

        // 2. Create a Payment
        // The Payment constructor validates patientId is not null/empty
        Payment newPayment = new Payment(patientId, appointmentId, amount, method);
        payments.add(newPayment);

        // 3. Update Patient's Balance
        // The Patient.applyPayment method handles updating the balance
        patient.applyPayment(amount);

        return newPayment;
    }

    // -------------------------------------------------------------------
    // --- 6. Reporting and Retrieval ---
    // -------------------------------------------------------------------

    /**
     * Gets all appointments for a specific doctor.
     */
    public List<Appointment> getDoctorSchedule(String doctorId) {
        return appointments.stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .sorted() // Uses the Appointment's compareTo (by dateTime)
                .collect(Collectors.toList());
    }

    // --- Getters for all lists ---

    public List<Patient> getAllPatients() {
        return patients;
    }

    public List<Doctor> getAllDoctors() {
        return doctors;
    }

    public List<Appointment> getAllAppointments() {
        return appointments;
    }

    public List<Payment> getAllPayments() {
        return payments;
    }
}