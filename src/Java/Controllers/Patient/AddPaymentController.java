package Controllers.Patient;

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
import java.util.Objects;

public class AddPaymentController{

    @FXML private ComboBox<PaymentMethod> cmbMethod;
    @FXML private ComboBox<Appointment> cmbAppointment;
    @FXML private Label lblAmount;
    @FXML private Label lblBalance;

    private final ClinicManager clinicManager = ClinicManager.getInstance();

    Patient currentPatient = (Patient) clinicManager.getCurrentUser();


    public void initialize() {
        loadTable();
        cmbAppointment.valueProperty().addListener((obs, oldVal, newVal) -> updateFee(newVal));

        cmbMethod.getItems().addAll(PaymentMethod.values());
        updateBalanceLabel();
    }

    private boolean loadTable() {
        var patientAppointments = clinicManager.getAppointments().stream()
                .filter(a -> a.getPatientId().equals(currentPatient.getId()))
                .filter(a -> a.getPatientId().equals(
                        ((Patient) clinicManager.getCurrentUser()).getId()
                ))
                .filter(a -> !a.getStatus().equals(AppointmentStatus.PAID))
                .toList();

        if(patientAppointments.isEmpty())
            return false;
        else
            cmbAppointment.getItems().addAll(patientAppointments);
        return true;
    }

    private void Refresh() {
        cmbAppointment.getItems().clear();
        cmbAppointment.getSelectionModel().clearSelection();
        cmbMethod.getSelectionModel().clearSelection();
        lblAmount.setText("");

        if(!loadTable()) {
            showAlert(Alert.AlertType.INFORMATION, "No Appointments", "You have no unpaid appointments.");

            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/PatientDashboard.fxml")));
                Stage stage = (Stage) cmbAppointment.getScene().getWindow(); // Use the ComboBox to find the window
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBalanceLabel() {
        lblBalance.setText(String.format("Current Outstanding Balance: $%.2f", currentPatient.getBalance()));
    }

    private void updateFee(Appointment appointment) {
        String doctorId = clinicManager.findAppointmentById(appointment.getId()).getDoctorId();
        double fees = clinicManager.findDoctorById(doctorId).getConsultationFee();
        lblAmount.setText(String.format("Fees are $%.2f", fees));
    }

    @FXML
    private void handleProcessPayment() {
        String appointmentId = cmbAppointment.getValue().getId();
        PaymentMethod method = cmbMethod.getValue();

        Doctor currentDoctor = clinicManager.findDoctorById(clinicManager.findAppointmentById(appointmentId).getDoctorId());
        double amount = currentDoctor.getConsultationFee();

        if (method == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please select a payment method.");
            return;
        }

        try {
            // Call the core business logic
            clinicManager.processPayment(
                    currentPatient.getId(),
                    appointmentId,
                    amount,
                    method
            );

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    String.format("Payment of $%.2f processed successfully for %s.", amount, currentPatient.getFullName()));

            // Refresh the balance label immediately
            updateBalanceLabel();
            Refresh();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // --- Navigation and Utility ---
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        navigateTo(event, "/Views/Dashboards/PatientDashboard.fxml");
    }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Scene scene = new Scene(view);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
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