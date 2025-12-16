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

public class NurseDashboardController implements Initializable {

    // Paths to Nurse/Staff specific views
    private static final String APPOINTMENT_MANAGEMENT_PATH = "/Views/Nurse/AppointmentView.fxml";
    private static final String PAYMENT_PROCESSING_PATH = "/Views/PaymentProcessingView.fxml";
    private static final String ROLE_SELECTION_PATH = "/Views/DashboardView.fxml"; // Back to sign-in

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic for the Nurse Dashboard
    }

    /**
     * Handles navigation to the central Appointment List view, which allows
     * viewing all appointments and navigating to the scheduler to set new ones.
     */
    @FXML
    private void handleManageAppointments(ActionEvent event) {
        navigateTo(event, APPOINTMENT_MANAGEMENT_PATH, "Appointment Scheduling & Management");
    }

    /**
     * Handles navigation to the dedicated Payment Processing screen.
     */
    @FXML
    private void handleProcessPayment(ActionEvent event) {
        navigateTo(event, PAYMENT_PROCESSING_PATH, "Process Patient Payment");
    }

    /**
     * Logs the nurse out and returns to the main sign-in screen.
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