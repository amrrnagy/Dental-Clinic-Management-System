package Controllers.Dashboards;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DoctorDashboardController implements Initializable {

    // Paths to the Doctor's specific views
    private static final String APPOINTMENT_VIEW_PATH = "/Views/Doctor/DoctorAppointment.fxml"; // Doctor needs to see appointments
    private static final String PRESCRIPTION_CREATION_PATH = "/Views/Doctor/AddPrescription.fxml";
    private static final String ROLE_SELECTION_PATH = "/Views/Dashboards/LoginView.fxml"; // Back to sign-in

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic for the Doctor Dashboard (e.g., displaying the Doctor's name)
    }

    /**
     * Handles navigation to the Appointment List view (where the Doctor can see their schedule).
     */
    @FXML
    private void handleViewSchedule(ActionEvent event) {
        navigateTo(event, APPOINTMENT_VIEW_PATH, "Doctor's Appointment Schedule");
    }

    /**
     * Handles navigation to the Prescription Creation view.
     */
    @FXML
    private void handleCreatePrescription(ActionEvent event) {
        navigateTo(event, PRESCRIPTION_CREATION_PATH, "Create New Prescription");
    }

    /**
     * Logs the doctor out and returns to the main sign-in screen.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ROLE_SELECTION_PATH, "Dental Clinic Management System");
    }


    // --- Navigation Utility ---

    private void navigateTo(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(view);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load FXML file: " + fxmlPath + ". Please ensure the file exists.");
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
