package Controllers.Nurse;

import Models.ClinicManager;
import Models.Doctor;
import javafx.collections.ObservableList;
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

public class RemoveDoctorController implements Initializable {

    @FXML
    private ComboBox<Doctor> cmbDoctor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Link ComboBox to the global static doctor list
        cmbDoctor.setItems((ObservableList<Doctor>) ClinicManager.getInstance().getDoctors());
    }

    @FXML
    private void handleRemoveDoctor(ActionEvent event) {
        Doctor selectedDoctor = cmbDoctor.getSelectionModel().getSelectedItem();

        if (selectedDoctor != null) {
            // Remove the doctor object from the manager's list
            ClinicManager.getInstance().getDoctors().remove(selectedDoctor);

            showAlert(Alert.AlertType.INFORMATION, "Doctor Removed",
                    "Dr. " + selectedDoctor.getFullName() + " has been successfully removed.");

            cmbDoctor.getSelectionModel().clearSelection();
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select a doctor to remove.");
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            // Navigate back to the specific Nurse Dashboard location
            Parent dashboardView = FXMLLoader.load(getClass().getResource("/Views/Nurse/NurseDashboard.fxml"));
            Scene scene = new Scene(dashboardView);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle("Nurse Portal");
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not return to dashboard.");
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