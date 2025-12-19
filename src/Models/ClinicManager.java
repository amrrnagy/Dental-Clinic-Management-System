package Models;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicManager {

    private static ClinicManager instance;

    private final List<Patient> patients;
    private final List<Doctor> doctors;
    private final List<Nurse> nurses;
    private final List<Appointment> appointments;
    private final List<Payment> payments;
    private final List<Prescription> prescriptions;

    private Person currentUser;

    private ClinicManager() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.nurses = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.currentUser = null;
        loadInitialData();
    }

    public static ClinicManager getInstance() {
        if (instance == null) {
            instance = new ClinicManager();
        }
        return instance;
    }

    public void setCurrentUser(Person person) {
        this.currentUser = person;
    }
    public Person getCurrentUser() {
        return currentUser;
    }

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

        // Add 2 Nurses (Hana & Jana)
        Nurse nur1 = new Nurse("Jana", "Ashraf", Gender.FEMALE,
                "jana", "jana");
        Nurse nur2 = new Nurse("Hana", "Nagy", Gender.FEMALE,
                "hana", "hana");
        nurses.add(nur1);
        nurses.add(nur2);

        // Initial appointment
        scheduleAppointment(
                pat1.getId(),
                doc1.getId(),
                java.time.LocalDate.now().plusDays(1),
                AppointmentSlot.SLOT_10_00_AM
        );
    }

    // Instance Methods

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

    // Getters
    public List<Patient> getPatients() {
        return this.patients;
    }
    public List<Doctor> getDoctors() {
        return this.doctors;
    }
    public List<Nurse> getNurses() { return nurses; }
    public List<Appointment> getAppointments() {
        return this.appointments;
    }
    public List<Payment> getPayments() {
        return this.payments;
    }
    public List<Prescription> getPrescriptions() { return this.prescriptions; }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }


    public void processPayment(String string, double amount, PaymentMethod method, String appointmentId) {
        this.payments.add(new Payment(string, appointmentId, amount, method));
    }

    public void addDoctor(String fName, String lName, Gender gender, String user, String pass, Specialization spec) {
        this.doctors.add(new Doctor(fName, lName, gender, user, pass, spec));
    }
}