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

public class NurseDashboardController {

    @FXML
    private void handleGoToPatientView(ActionEvent event) {
        switchScene(event, "/Views/Nurse/PatientView.fxml"); // [cite: 38]
    }

    @FXML
    private void handleGoToDoctorView(ActionEvent event) {
        switchScene(event, "/Views/Nurse/DoctorView.fxml"); // [cite: 41]
    }

    @FXML
    private void handleGoToAppointmentView(ActionEvent event) {
        switchScene(event, "/Views/Nurse/AppointmentView.fxml"); // [cite: 44]
    }

    @FXML
    private void handleGoToPaymentView(ActionEvent event) {
        switchScene(event, "/Views/Nurse/PaymentView.fxml"); // [cite: 47]
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        ClinicManager.getInstance().setCurrentUser(null); //
        switchScene(event, "/Views/Dashboards/LoginView.fxml"); // [cite: 48]
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}