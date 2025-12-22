package Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicManager {

    // Accessible instance to save data within
    private static ClinicManager instance;

    // Lists to save all data
    private final List<Patient> patients;
    private final List<Doctor> doctors;
    private final List<Nurse> nurses;
    private final List<Appointment> appointments;
    private final List<Payment> payments;
    private final List<Prescription> prescriptions;

    // Current logged in user for role-based methods
    private Person currentUser;

    // Instance constructor
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

    // Public method to access the instance anywhere
    public static ClinicManager getInstance() {
        if (instance == null) {
            instance = new ClinicManager();
        }
        return instance;
    }

    // Used while logging in/out
    public void setCurrentUser(Person person) {
        this.currentUser = person;
    }
    public Person getCurrentUser() {
        return currentUser;
    }

    // Dummy data for the application to start
    private void loadInitialData() {
        // Add Doctors
        Doctor doc1 = new Doctor("Amr", "Nagy", Gender.MALE,
                "amr", "amr", Specialization.GENERAL_DENTISTRY);
        Doctor doc2 = new Doctor("Mohamed", "Ashraf", Gender.FEMALE,
                "ashraf", "ashraf", Specialization.ORTHODONTICS);
        doctors.add(doc1);
        doctors.add(doc2);

        // Add Patients
        Patient pat1 = new Patient("Eslam", "Nader", Gender.MALE,
                "010", "eslam", "eslam");
        Patient pat2 = new Patient("Karim", "Samir", Gender.FEMALE,
                "011", "karim", "karim");
        Patient pat3 = new Patient("Mohamed", "Baiomy", Gender.FEMALE,
                "012", "baiomy", "baiomy");
        Patient pat4 = new Patient("Mohamed", "Hany", Gender.FEMALE,
                "015", "hany", "hany");
        patients.add(pat1);
        patients.add(pat2);
        patients.add(pat3);
        patients.add(pat4);

        // Add Nurses
        Nurse nur1 = new Nurse("Amina", "Khalil", Gender.FEMALE,
                "amina", "amina");
        Nurse nur2 = new Nurse("Dina", "Elsherbiny", Gender.FEMALE,
                "dina", "dina");
        nurses.add(nur1);
        nurses.add(nur2);

        // Initial appointments
        Appointment app1 = scheduleAppointment(
                pat1.getId(),
                doc2.getId(),
                java.time.LocalDate.now().plusDays(1),
                AppointmentSlot.SLOT_10_00_AM
        );
        Appointment app2 = scheduleAppointment(
                pat2.getId(),
                doc1.getId(),
                java.time.LocalDate.now().plusDays(2),
                AppointmentSlot.SLOT_3_00_PM
        );
        Appointment app3 = scheduleAppointment(
                pat1.getId(),
                doc1.getId(),
                java.time.LocalDate.now().plusDays(6),
                AppointmentSlot.SLOT_5_00_PM
        );
        Appointment app4 = scheduleAppointment(
                pat3.getId(),
                doc2.getId(),
                java.time.LocalDate.now().plusDays(9),
                AppointmentSlot.SLOT_1_00_PM
        );

        // Initial Payments
        processPayment(pat1.getId(), app1.getId(), 150, PaymentMethod.CASH);
        processPayment(pat2.getId(), app2.getId(), 150, PaymentMethod.CARD);
        processPayment(pat3.getId(), app3.getId(), 150, PaymentMethod.MOBILE_PAYMENT);

        // Initial Prescription
        PrescriptionItem item1 = new PrescriptionItem("Phenadone", "2mg", "Every 2 Hours", 4);
        Prescription pre1 = new Prescription(app3.getId(), pat1.getId(), doc1.getId(), item1);
        PrescriptionItem item2 = new PrescriptionItem("Penicillin", "20mg", "Every 4 Hours", 14);
        Prescription pre2 = new Prescription(app4.getId(), pat3.getId(), doc2.getId(), item2);
        prescriptions.add(pre1);
        prescriptions.add(pre2);
    }

    // Methods used in searching and comparing
    public Patient findPatientById(String id) {
        return patients.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public Patient findPatientByUser(String username) {
        return patients.stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public Doctor findDoctorById(String id) {
        return doctors.stream()
                .filter(d -> d.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public Doctor findDoctorByUser(String username) {
        return doctors.stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public Appointment findAppointmentById(String id) {
        return appointments.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    // Scheduling an appointment by a Patient
    public Appointment scheduleAppointment(String patientId, String doctorId,
                                           java.time.LocalDate date, AppointmentSlot slot) {

        LocalDateTime dateTime = LocalDateTime.of(date, slot.getStartTime());
        LocalDateTime endTime = LocalDateTime.of(date, slot.getEndTime());

        Patient currentPatient = findPatientById(patientId);
        Doctor currentDoctor = findDoctorById(doctorId);

        // Validation
        if (currentPatient == null || currentDoctor == null) {
            return null;
        }
        if (!isDoctorAvailable(doctorId, dateTime, endTime)) {
            return null;
        }

        // Trying to schedule the appointment
        try {
            Appointment newAppointment = new Appointment(patientId, doctorId, dateTime);
            appointments.add(newAppointment);

            // Update Patient's balance
            currentPatient.setBalance(currentPatient.getBalance() + currentDoctor.getConsultationFee());
            return newAppointment;

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Used to filter slots while choosing an appointment slot
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
    public List<Appointment> getAppointments() { return this.appointments; }
    public List<Payment> getPayments() { return this.payments; }
    public List<Prescription> getPrescriptions() { return this.prescriptions; }

    // Add/Remove Patients/Doctors
    public void addPatient(Patient patient) { this.patients.add(patient); }
    public void removePatient(Patient patient) { this.patients.remove(patient); }

    public void addDoctor(Doctor doctor) {this.doctors.add(doctor); }
    public void removeDoctor(Doctor doctor) {this.doctors.remove(doctor); }

    // Cancel an appointment by the Patient
    public boolean cancelAppointment (Appointment appointment) {
        if(appointment.getStatus().equals(AppointmentStatus.SCHEDULED)) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            return true;
        }
        else
            return false;
    }

    // Add a prescription by a Doctor
    public Prescription addPrescription(String appointmentId, String patientId, String doctorId, PrescriptionItem item) {

        // Validation
        if (findAppointmentById(appointmentId) == null || findPatientById(patientId) == null) {
            return null;
        }
        if (hasPrescription(appointmentId)) return null;

        // Try to add
        try {

            Prescription newPrescription = new Prescription(
                    appointmentId, patientId, doctorId, item
            );

            // Save to list
            prescriptions.add(newPrescription);
            return newPrescription;

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Check if an Appointment has already a Prescription
    private boolean hasPrescription(String appointmentId) {
        return prescriptions.stream()
                .anyMatch(p -> p.getAppointmentId().equals(appointmentId));
    }

    public void processPayment(String patientId, String appointmentId, double amount, PaymentMethod method) {

        Patient currentPatient = findPatientById(patientId);
        Appointment appointment = findAppointmentById(appointmentId);

        // Validation
        if (currentPatient == null || appointment == null) {
            return;
        }

        // Update Patient's balance and save the payment
        currentPatient.setBalance(currentPatient.getBalance() - amount);
        appointment.setStatus(AppointmentStatus.PAID);
        this.payments.add(new Payment(patientId, amount, method));
    }
}
