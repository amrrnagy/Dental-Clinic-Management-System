package Controllers.Doctor;

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
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPrescriptionController{
//
//    @FXML private ComboBox<Patient> cmbPatient;
//    @FXML private ComboBox<Appointment> cmbAppointment; // Filtered by patient
//    @FXML private TextField txtMed;
//    @FXML private ComboBox<String> cmbDosage;
//    @FXML private ComboBox<Integer> cmbFreq;
//    @FXML private ComboBox<Integer> cmbDays;
//    @FXML private TextField txtNotes;
//
//    private final ClinicManager clinicManager = ClinicManager.getInstance();
//
//    public void initialize() {
//        // 1. Populate Patients
//        cmbPatient.getItems().addAll(clinicManager.getPatients());
//
//        // 2. Populate Dosage/Freq/Days constants (if not done in FXML)
//        cmbDosage.getItems().addAll("mg", "ml", "Tablet(s)", "Capsule(s)");
//        cmbFreq.getItems().addAll(1, 2, 3, 4);
//        cmbDays.getItems().addAll(1, 3, 5, 7, 10, 14, 30);
//
//        // 3. Set Cell Factories for readable names
//        cmbPatient.setCellFactory(lv -> new ListCell<Patient>() {
//            @Override
//            protected void updateItem(Patient item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? null : item.getFullName() + " (ID: " + item.getId() + ")");
//            }
//        });
//
//        // Custom display for the Patient Selection Button
//        cmbPatient.setButtonCell(cmbPatient.getCellFactory().call(null));
//
//        // 4. Listener: When patient changes, update their appointments
//        cmbPatient.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableAppointments(newVal));
//
//        cmbAppointment.setDisable(true);
//    }
//
////    private void updateAvailableAppointments(Patient selectedPatient) {
////        cmbAppointment.getItems().clear();
////        cmbAppointment.getSelectionModel().clearSelection();
////
////        if (selectedPatient == null) {
////            cmbAppointment.setDisable(true);
////            return;
////        }
////
////        // Filter appointments for this specific patient that don't have prescriptions yet
////        // (Assuming your ClinicManager or Patient model can provide this)
////        var patientAppointments = clinicManager.getAppointmentsForPatient(selectedPatient.getId().toString());
////
////        if (patientAppointments.isEmpty()) {
////            cmbAppointment.setDisable(true);
////            showAlert(Alert.AlertType.INFORMATION, "No Appointments", "This patient has no registered appointments.");
////        } else {
////            cmbAppointment.getItems().addAll(patientAppointments);
////            cmbAppointment.setDisable(false);
////        }
////    }
////
////    @FXML
////    private void handleAddPrescription(ActionEvent event) {
////        // 1. Get Values
////        Patient patient = cmbPatient.getValue();
////        Appointment appointment = cmbAppointment.getValue();
////        String medication = txtMed.getText();
////        String dosage = cmbDosage.getValue();
////        Integer freq = cmbFreq.getValue();
////        Integer days = cmbDays.getValue();
////        String notes = txtNotes.getText();
////
////        // 2. Validation
////        if (patient == null || appointment == null || medication.isEmpty() || dosage == null || freq == null || days == null) {
////            showAlert(Alert.AlertType.ERROR, "Form Incomplete", "Please fill in all required fields.");
////            return;
////        }
////
////        // 3. Create and Save Prescription
////        // Adjust the constructor below to match your Models.Prescription class
////        Prescription newPrescription = new Prescription(
////                appointment.getId(),
////                patient.getId(),
////                medication,
////                dosage,
////                freq,
////                days,
////                notes
////        );
////
////        boolean success = clinicManager.addPrescription(newPrescription);
////
////        if (success) {
////            showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription added successfully!");
////            navigateTo(event); // Go back
////        } else {
////            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save prescription. Please try again.");
////        }
////    }
//
//    private void showAlert(Alert.AlertType type, String title, String message) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    @FXML
//    private void handleBack(ActionEvent event) {
//        navigateTo(event);
//    }
//
//    private void navigateTo(ActionEvent event) {
//        try {
//            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Doctor/DoctorPrescription.fxml")));
//            Scene scene = new Scene(view);
//            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//            window.setScene(scene);
//            window.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}