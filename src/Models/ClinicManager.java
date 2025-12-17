package Models;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClinicManager {

    // --- 1. Singleton Instance ---
    private static ClinicManager instance;

    // --- 2. Data Collections (Instance Fields) ---
    private final List<Patient> patients;
    private final List<Doctor> doctors;
    private final List<Appointment> appointments;
    private final List<Payment> payments;

    private Person currentUser;

    /**
     * Private constructor initializes instance data.
     */
    private ClinicManager() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.currentUser = null;
        loadInitialData();
    }

    /**
     * Global access point for the Singleton.
     */
    public static ClinicManager getInstance() {
        if (instance == null) {
            instance = new ClinicManager();
        }
        return instance;
    }

    // --- 3. Session Management ---

    public void setCurrentUser(Person person) {
        this.currentUser = person;
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    // --- 4. Data Initialization ---

    private void loadInitialData() {
        // Add Doctors
        Doctor doc1 = new Doctor("Alice", "Hany", Gender.MALE,
                "doc1", "dpass1", Specialization.GENERAL_DENTISTRY);
        Doctor doc2 = new Doctor("Wesam", "Carter", Gender.FEMALE,
                "doc2", "dpass2", Specialization.ORTHODONTICS);
        doctors.add(doc1);
        doctors.add(doc2);

        // Add Patients
        Patient pat1 = new Patient("John", "Ashraf", Gender.MALE,
                "010", "pat1", "ppass1");
        Patient pat2 = new Patient("Jane", "Nagy", Gender.FEMALE,
                "010", "pat2", "ppass2");

        pat1.setBalance(150.00);
        patients.add(pat1);
        patients.add(pat2);

        // Initial appointment
        scheduleAppointment(
                pat1.getId().toString(),
                doc1.getId().toString(),
                java.time.LocalDate.now().plusDays(1),
                AppointmentSlot.SLOT_10_00_AM
        );
    }

    // --- 5. Search & Logic Methods (Instance Methods) ---

    public Patient findPatientById(String id) {
        return patients.stream()
                .filter(p -> p.getId().toString().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Doctor findDoctorById(String id) {
        return doctors.stream()
                .filter(d -> d.getId().toString().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Appointment scheduleAppointment(String patientId, String doctorId,
                                           java.time.LocalDate date, AppointmentSlot slot) {

        LocalDateTime dateTime = LocalDateTime.of(date, slot.getStartTime());
        LocalDateTime endTime = dateTime.plusMinutes(60);

        if (findPatientById(patientId) == null || findDoctorById(doctorId) == null) {
            return null;
        }

        if (!isDoctorAvailable(doctorId, dateTime, endTime)) {
            return null;
        }

        try {
            Appointment newAppointment = new Appointment(patientId, doctorId, dateTime, slot);
            appointments.add(newAppointment);
            return newAppointment;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isDoctorAvailable(String doctorId, LocalDateTime start, LocalDateTime end) {
        return appointments.stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .noneMatch(existing -> {
                    LocalDateTime existingStart = existing.getDateTime();
                    LocalDateTime existingEnd = existingStart.plusHours(1);
                    return (start.isBefore(existingEnd) && end.isAfter(existingStart));
                });
    }

    // --- 6. Getters (Instance Methods) ---

    public List<Patient> getPatients() {
        return this.patients;
    }

    public List<Doctor> getDoctors() {
        return this.doctors;
    }

    public List<Appointment> getAllAppointments() {
        return this.appointments;
    }

    public List<Payment> getAllPayments() {
        return this.payments;
    }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }

    public List<Payment> getPayments() {
        return this.payments;
    }

    public void processPayment(String string, double amount, PaymentMethod method, String appointmentId) {
        this.payments.add(new Payment(string, appointmentId, amount, method));
    }

    public void addDoctor(String fName, String lName, String s, String user, String pass, String spec) {
        this.doctors.add(new Doctor(fName, lName, s, user, pass, spec));
    }
}