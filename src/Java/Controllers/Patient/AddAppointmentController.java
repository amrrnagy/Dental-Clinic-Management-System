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
    @FXML private ComboBox<AppointmentSlot> cmbSlots;
    private final ClinicManager clinicManager = ClinicManager.getInstance();
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public void initialize() {

        cmbDoctor.getItems().addAll(clinicManager.getDoctors());

        cmbDoctor.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getFullName() + " (" + item.getSpecialization().name() + ")");
            }
        });

        cmbDoctor.valueProperty().addListener((_, _, _) -> updateAvailableSlots());
        dpDate.valueProperty().addListener((_, _, _) -> updateAvailableSlots());

        dpDate.setDayCellFactory(_ -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today)); // Disable past dates
            }
        });
        cmbSlots.setDisable(true);
    }

    private void updateAvailableSlots() {
        Doctor selectedDoctor = cmbDoctor.getValue();
        LocalDate selectedDate = dpDate.getValue();
        cmbSlots.getItems().clear();
        cmbSlots.getSelectionModel().clearSelection();
        cmbSlots.setDisable(true);

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

    private List<AppointmentSlot> getFilteredSlots(Doctor doctor, LocalDate date) {
        List<AppointmentSlot> availableSlots = new ArrayList<>();
        String doctorId = doctor.getId();

        for (AppointmentSlot slot : AppointmentSlot.values()) {
            LocalTime startTime = slot.getStartTime();

            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            LocalDateTime endDateTime = startDateTime.plusMinutes(60);

            if (clinicManager.isDoctorAvailable(doctorId, startDateTime, endDateTime)) {
                availableSlots.add(slot);
            }
        }

        return availableSlots;
    }


    @FXML
    private void handleScheduleAppointment(ActionEvent event) {
        Patient currentPatient = (Patient) ClinicManager.getInstance().getCurrentUser();
        Doctor selectedDoctor = cmbDoctor.getValue();
        LocalDate date = dpDate.getValue();
        AppointmentSlot slot = cmbSlots.getValue();

        if (selectedDoctor == null || date == null || slot == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please select a doctor, date, and a valid time slot.");
            return;
        }

        Appointment newAppointment = clinicManager.scheduleAppointment(
                currentPatient.getId(),
                selectedDoctor.getId(),
                date,
                slot
        );

        if (newAppointment != null) {
            LocalDateTime startDateTime = LocalDateTime.of(date, slot.getStartTime());
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Appointment successfully scheduled for " + currentPatient.getFullName() +
                            " with Dr: " + selectedDoctor.getFullName() + " at " +
                            startDateTime.format(DATETIME_FORMATTER)
            );
            handleBack(event);

        } else {
            showAlert(Alert.AlertType.WARNING, "Scheduling Conflict",
                    "The selected doctor is unavailable at that time or a system error occurred. Please re-check the schedule."
            );
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleBack(ActionEvent event) {
        navigateTo(event);
    }

    private void navigateTo(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Patient/PatientAppointment.fxml")));
            Scene scene = new Scene(view);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Could not load FXML file: " + "/Views/Patient/PatientAppointment.fxml");
        }
    }
}