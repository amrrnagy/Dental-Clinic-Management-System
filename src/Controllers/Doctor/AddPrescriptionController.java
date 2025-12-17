package Controllers.Doctor;

import Models.*;
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
import java.util.ResourceBundle;

public class AddPrescriptionController implements Initializable {

    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private ComboBox<String> cmbDosage;
    @FXML private ComboBox<String> cmbFreq;
    @FXML private TextField txtMed;
    @FXML private TextField txtDays;
    @FXML private TextField txtNotes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate patient list from ClinicManager
        cmbPatient.setItems(ClinicManager.getPatients());

        // Initialize dropdown options
        cmbDosage.getItems().addAll("50mg", "100mg", "250mg", "500mg");
        cmbFreq.getItems().addAll("Once daily", "Twice daily", "Thrice daily", "As needed");
    }

    @FXML
    private void handleAddPrescription(ActionEvent event) {
        Patient selectedPatient = cmbPatient.getValue();
        String medication = txtMed.getText();
        String dosage = cmbDosage.getValue();
        String frequency = cmbFreq.getValue();
        String days = txtDays.getText();
        String notes = txtNotes.getText();

        if (selectedPatient == null || medication.isEmpty() || dosage == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill required fields (Patient, Medication, Dosage).");
            return;
        }

        // Get prescribing doctor from current session
        Doctor prescribingDoctor = (Doctor) ClinicManager.getCurrentUser();

        // Create new prescription and add to patient's list
        Prescription newPrescription = new Prescription(
                medication, dosage, frequency, days, notes, prescribingDoctor
        );

        selectedPatient.getMyPrescriptions().add(newPrescription);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Prescription added for " + selectedPatient.getFullName());
        clearFields();
    }

    private void clearFields() {
        txtMed.clear();
        txtDays.clear();
        txtNotes.clear();
        cmbPatient.getSelectionModel().clearSelection();
        cmbDosage.getSelectionModel().clearSelection();
        cmbFreq.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Dashboards/DoctorDashboard.fxml"));
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