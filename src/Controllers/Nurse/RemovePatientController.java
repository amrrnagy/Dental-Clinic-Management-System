package Controllers.Nurse;

import Models.ClinicManager;
import Models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RemovePatientController implements Initializable {

    @FXML
    private ComboBox<Patient> cmbPatient; // Type set to Patient for easy access

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Link the ComboBox directly to the static list in ClinicManager
        // This ensures the list is always up-to-date
        cmbPatient.setItems(ClinicManager.getPatients());
    }

    @FXML
    private void handleRemovePatient(ActionEvent event) {
        Patient selectedPatient = cmbPatient.getSelectionModel().getSelectedItem();

        if (selectedPatient != null) {
            // Remove from the master list in ClinicManager
            ClinicManager.getPatients().remove(selectedPatient);

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Patient " + selectedPatient.getFullName() + " has been removed.");

            // Optional: Clear selection after removal
            cmbPatient.getSelectionModel().clearSelection();
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a patient from the list first.");
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            // Path tailored to your Nurse dashboard location
            Parent dashboardView = FXMLLoader.load(getClass().getResource("/Views/Nurse/NurseDashboard.fxml"));
            Scene scene = new Scene(dashboardView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle("Nurse Portal");
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load the dashboard.");
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