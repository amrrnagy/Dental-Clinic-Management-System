package Controllers.Patient;

import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddAppointmentController  {

    @FXML private ComboBox<Doctor> cmbDoctor;
    @FXML private DatePicker dpDate;
    @FXML private TextArea txtReason;
    @FXML private ComboBox<AppointmentSlot> cmbSlots;
    private final ClinicManager clinicManager = ClinicManager.getInstance();
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public void initialize() {

        cmbDoctor.getItems().addAll(clinicManager.getDoctors());

        // Set ComboBox display for Doctor
        cmbDoctor.setCellFactory(lv -> new ListCell<Doctor>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getFullName() + " (" + item.getSpecialization().name() + ")");
            }
        });

        // --- Add Listeners for Dynamic Slot Filtering ---
        // Listener for Doctor selection change
        cmbDoctor.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableSlots());

        // Listener for Date selection change
        dpDate.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableSlots());

        // Set date picker minimum date to today (optional, but good practice)
        dpDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 ); // Disable past dates
            }
        });

        // Initial setup for empty slots list
        cmbSlots.setDisable(true);
    }

    /**
     * Finds the available AppointmentSlots based on the selected Doctor and Date.
     * Duration is now FIXED.
     */
    private void updateAvailableSlots() {
        Doctor selectedDoctor = cmbDoctor.getValue();
        LocalDate selectedDate = dpDate.getValue();

        cmbSlots.getItems().clear();
        cmbSlots.getSelectionModel().clearSelection();
        cmbSlots.setDisable(true); // Disable slots until valid doctor/date is picked

        // Ensure both a doctor and a date are selected
        if (selectedDoctor == null || selectedDate == null) {
            return;
        }


        List<AppointmentSlot> availableSlots = getFilteredSlots(selectedDoctor, selectedDate);

        if (!availableSlots.isEmpty()) {
            cmbSlots.getItems().addAll(availableSlots);
            cmbSlots.setDisable(false);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "No Slots Available",
                    "The selected doctor has no available slots on this date."
            );
        }
    }

    /**
     * Core logic to check all possible slots against the doctor's existing appointments.
     */
    private List<AppointmentSlot> getFilteredSlots(Doctor doctor, LocalDate date) {
        List<AppointmentSlot> availableSlots = new ArrayList<>();
        String doctorId = doctor.getId();

        // Iterate through all defined standard slots
        for (AppointmentSlot slot : AppointmentSlot.values()) {
            LocalTime startTime = slot.getStartTime();

            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = startDateTime.plusMinutes(60);

            // Check if the doctor is available for this slot
            if (clinicManager.isDoctorAvailable(doctorId, startDateTime, endDateTime)) {
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }


    // --- Existing handleScheduleAppointment method (Updated) ---
    @FXML
    private void handleScheduleAppointment() {
        Patient currentPatient = (Patient) ClinicManager.getInstance().getCurrentUser();
        Doctor selectedDoctor = cmbDoctor.getValue();
        LocalDate date = dpDate.getValue();
        AppointmentSlot slot = cmbSlots.getValue();

        // 1. Input Validation (Duration is now implicitly fixed and doesn't need field check)
        if (selectedDoctor == null || date == null || slot == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please select a doctor, date, and a valid time slot.");
            return;
        }

        // 2. Schedule using the Manager's Core Logic
        Appointment newAppointment = clinicManager.scheduleAppointment(
                currentPatient.getId(),
                selectedDoctor.getId(),
                date, // Pass LocalDate
                slot // Pass AppointmentSlot
        );

        // 3. Handle Result
        if (newAppointment != null) {
            LocalDateTime startDateTime = LocalDateTime.of(date, slot.getStartTime());
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Appointment successfully scheduled for " + currentPatient.getFullName() +
                            " with " + selectedDoctor.getFullName() + " at " +
                            startDateTime.format(DATETIME_FORMATTER) + " (" + "minutes)"
            );
        } else {
            // Failure usually due to availability conflict or invalid data in ClinicManager
            showAlert(Alert.AlertType.WARNING, "Scheduling Conflict",
                    "The selected doctor is unavailable at that time or a system error occurred. Please re-check the schedule."
            );
        }
    }


    private void clearForm() {
        cmbDoctor.getSelectionModel().clearSelection();
        dpDate.setValue(null);
        cmbSlots.getSelectionModel().clearSelection();
        cmbSlots.setDisable(true); // Disable slots until new selections are made
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // -------------------------------------------------------------------
    // --- NAVIGATION LOGIC ---
    // -------------------------------------------------------------------

    /**
     * Handles the action to go back to the main dashboard.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event, "/Views/Patient/PatientAppointment.fxml");
    }

    /**
     * Reusable method to switch the current scene to a new FXML view.
     */
    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            // Load FXML relative to the classpath
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Scene scene = new Scene(view);

            // Get the stage (window) from the source component
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML file: " + fxmlPath);
        }
    }
}