package Controllers.Dashboards;

import Models.ClinicManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class PatientDashboardController {

    /**
     * Navigates to the Patient's appointment list view.
     */
    @FXML
    private void handleViewAppointments(ActionEvent event) {
        switchScene(event, "/Views/Patient/PatientAppointment.fxml");
    }

    /**
     * Navigates to the Patient's prescription history view.
     */
    @FXML
    private void handleViewPrescriptions(ActionEvent event) {
        switchScene(event, "/Views/Patient/PrescriptionView.fxml");
    }

    /**
     * Navigates to the payment processing screen.
     */
    @FXML
    private void handleAddPayment(ActionEvent event) {
        switchScene(event, "/Views/Patient/AddPayment.fxml");
    }

    /**
     * Clears the current session and returns to the login screen.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        ClinicManager.getInstance().setCurrentUser(null);
        switchScene(event, "/Views/Dashboards/LoginView.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}