package Controllers.Nurse;

import Models.ClinicManager;
import Models.Payment;
import Models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentViewController implements Initializable {

    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private Label lblBalance;
    @FXML private TableView<Payment> tblPayments;

    // Table Columns
    @FXML private TableColumn<Payment, String> colPaymentId;
    @FXML private TableColumn<Payment, String> colPatientId;
    @FXML private TableColumn<Payment, String> colAppointmentId;
    @FXML private TableColumn<Payment, Double> colAmount;
    @FXML private TableColumn<Payment, String> colMethod;
    @FXML private TableColumn<Payment, String> colTime;
    @FXML private TableColumn<Payment, String> colNotes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();

        // Populate ComboBox with patients from ClinicManager
        cmbPatient.setItems((ObservableList<Patient>) ClinicManager.getInstance().getPatients());

        // Show all payments initially
        tblPayments.setItems((ObservableList<Payment>) ClinicManager.getInstance().getPayments());

        // Listener to filter table when a patient is selected
        cmbPatient.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                filterPaymentsByPatient(newVal);
            }
        });
    }

    private void setupTable() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void filterPaymentsByPatient(Patient patient) {
        // Filter the master list from ClinicManager
        ObservableList<Payment> filteredList = ClinicManager.getInstance().getPayments().stream()
                .filter(p -> p.getPayer().getId().equals(patient.getId()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblPayments.setItems(filteredList);

        // Update Balance Label
        double totalPaid = filteredList.stream().mapToDouble(Payment::getAmount).sum();
        lblBalance.setText(String.format("Total Payments by %s: $%.2f", patient.getFirstName(), totalPaid));
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cmbPatient.getSelectionModel().clearSelection();
        tblPayments.setItems((ObservableList<Payment>) ClinicManager.getInstance().getPayments());
        lblBalance.setText("Current Balance: $0.00");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Nurse/NurseDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Nurse Portal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}