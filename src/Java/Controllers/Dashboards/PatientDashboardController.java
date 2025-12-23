package Controllers.Dashboards;

import Models.Appointment;
import Models.AppointmentStatus;
import Models.ClinicManager;
import Models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PatientDashboardController {


    @FXML
    private void handleViewAppointments(ActionEvent event) {
        switchScene(event, "/Views/Patient/PatientAppointment.fxml");
    }

    @FXML
    private void handleViewPrescriptions(ActionEvent event) {
        switchScene(event, "/Views/Patient/PrescriptionView.fxml");
    }

    @FXML
    private void handleAddPayment(ActionEvent event) {

        ClinicManager clinicManager = ClinicManager.getInstance();
        Patient currentPatient = (Patient) clinicManager.getCurrentUser();

        List<Appointment> patientAppointments = clinicManager.getAppointments().stream()
                .filter(a -> a.getPatientId().equals(currentPatient.getId()))
                .filter(a -> a.getPatientId().equals(
                        ((Patient) clinicManager.getCurrentUser()).getId()
                ))
                .filter(a -> !a.getStatus().equals(AppointmentStatus.PAID))
                .filter(a -> !a.getStatus().equals(AppointmentStatus.CANCELLED))
                .toList();

        if(patientAppointments.isEmpty()) {
            showAlert();
        }
        else {
            switchScene(event, "/Views/Patient/AddPayment.fxml");
        }
    }

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
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No Appointments");
        alert.setHeaderText(null);
        alert.setContentText("You have no unpaid appointments.");
        alert.showAndWait();
    }
}