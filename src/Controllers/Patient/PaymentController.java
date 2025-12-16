package Controllers.Patient;

import Models.*;
import javafx.beans.value.ChangeListener;
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

public class PaymentController implements Initializable {

    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private TextField txtAmount;
    @FXML private ComboBox<PaymentMethod> cmbMethod;
    @FXML private TextField txtAppointmentId;
    @FXML private Label lblBalance;

    private final ClinicManager clinicManager = ClinicManager.getInstance();

    // Listener to update the balance label when a patient is selected
    private final ChangeListener<Patient> patientChangeListener = (obs, oldVal, newVal) -> {
        if (newVal != null) {
            updateBalanceLabel(newVal);
        } else {
            lblBalance.setText("Current Balance: $0.00");
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate ComboBoxes
        cmbPatient.getItems().addAll(clinicManager.getAllPatients());
        cmbMethod.getItems().addAll(PaymentMethod.values());

        // Set ComboBox to display full name and add listener
        cmbPatient.setCellFactory(lv -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getFullName());
            }
        });
        cmbPatient.valueProperty().addListener(patientChangeListener);

        // Ensure the initial balance is set if the combo box has data
        if (!cmbPatient.getItems().isEmpty()) {
            updateBalanceLabel(cmbPatient.getItems().getFirst());
        }
    }

    private void updateBalanceLabel(Patient patient) {
        lblBalance.setText(String.format("Current Outstanding Balance: $%.2f", patient.getBalance()));
    }

    @FXML
    private void handleProcessPayment() {
        Patient selectedPatient = cmbPatient.getValue();
        PaymentMethod method = cmbMethod.getValue();
        String amountText = txtAmount.getText();
        String appointmentId = txtAppointmentId.getText().isEmpty() ? null : txtAppointmentId.getText();

        if (selectedPatient == null || method == null || amountText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please select a patient, amount, and payment method.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Please enter a positive numeric value for the amount.");
            return;
        }

        try {
            // Call the core business logic
            clinicManager.processPayment(
                    selectedPatient.getId().toString(),
                    amount,
                    method,
                    appointmentId
            );

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    String.format("Payment of $%.2f processed successfully for %s.", amount, selectedPatient.getFullName()));

            // Refresh the balance label immediately
            updateBalanceLabel(selectedPatient);
            txtAmount.clear();
            txtAppointmentId.clear();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // --- Navigation and Utility ---
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        navigateTo(event, "/Views/Dashboards/LoginView.fxml", "Dental Clinic Management System");
    }

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