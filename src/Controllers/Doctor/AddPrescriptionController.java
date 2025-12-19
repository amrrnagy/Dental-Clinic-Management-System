package Controllers.Doctor;

import Models.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPrescriptionController implements Initializable {

    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private ComboBox<Appointment> cmbAppointment;
    @FXML private ComboBox<String> cmbDosage;
    @FXML private ComboBox<String> cmbFreq;
    @FXML private ComboBox<Integer> cmbDays;
    @FXML private TextField txtMed;
    @FXML private TextField txtNotes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cast to ObservableList for ComboBox compatibility
        cmbPatient.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getPatients()));

        cmbDosage.getItems().addAll("50mg", "100mg", "250mg", "500mg");
        cmbFreq.getItems().addAll("Once daily", "Twice daily", "Thrice daily", "As needed");
    }

    @FXML
    private void handleAddPrescription(ActionEvent event) {
        Patient selectedPatient = cmbPatient.getValue();
        Appointment appointment = cmbAppointment.getValue();
        String medication = txtMed.getText();
        String dosage = cmbDosage.getValue();
        String frequency = cmbFreq.getValue();
        int days = cmbDays.getValue();
        String notes = txtNotes.getText();

        if (selectedPatient == null || medication.isEmpty() || dosage == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill required fields (Patient, Medication, Dosage).");
            return;
        }

        // Get the current Doctor from the ClinicManager session
        // Note: This assumes your UserRole can be cast to Doctor
        Doctor prescribingDoctor = (Doctor) ClinicManager.getInstance().getCurrentUser();

        if (prescribingDoctor == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "Could not identify prescribing doctor.");
            return;
        }

        try {
            // 1. Create the main Prescription container
            // We pass null for appointmentId if it's not linked to a specific visit
            Prescription newPrescription = new Prescription(
                    appointment.getId(),
                    selectedPatient.getId(),
                    prescribingDoctor.getId(),
                    notes
            );

            // 2. Create the PrescriptionItem (Medication details)
            // Assuming your PrescriptionItem constructor takes (name, dosage, frequency, duration)
            PrescriptionItem item = new PrescriptionItem(medication, dosage, frequency, days);
            newPrescription.addItem(item);

            // 3. Save to the Patient's record
            ClinicManager.getInstance().getPrescriptions().add(newPrescription);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription added for " + selectedPatient.getFullName());
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to create prescription: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtMed.clear();
        cmbDays.getSelectionModel().clearSelection();
        txtNotes.clear();
        cmbPatient.getSelectionModel().clearSelection();
        cmbDosage.getSelectionModel().clearSelection();
        cmbFreq.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/DoctorDashboard.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Doctor Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}