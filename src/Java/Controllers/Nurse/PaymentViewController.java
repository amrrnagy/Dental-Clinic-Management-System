package Controllers.Nurse;

import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class PaymentViewController{

    @FXML private ComboBox<Patient> cmbPatient;
    @FXML private Label lblBalance;
    @FXML private TableView<Payment> tblPayments;

    // Table Columns
    @FXML private TableColumn<Payment, String> colPaymentId;
    @FXML private TableColumn<Payment, String> colPatientId;
    @FXML private TableColumn<Payment, String> colAppointmentId;
    @FXML private TableColumn<Payment, Double> colAmount;
    @FXML private TableColumn<Payment, PaymentMethod> colMethod;
    @FXML private TableColumn<Payment, LocalDateTime> colTime;

    public void initialize() {
        setupTable();
        cmbPatient.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getPatients()));
        tblPayments.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getPayments()));

        // Listener to filter table when a patient is selected
        cmbPatient.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            if (newVal != null) {
                filterPaymentsByPatient(newVal);
            }
        });
    }

    private void setupTable() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        colTime.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });
    }

    private void filterPaymentsByPatient(Patient patient) {
        ObservableList<Payment> filteredList = ClinicManager.getInstance().getPayments().stream()
                .filter(p -> p.getPayer().getId().equals(patient.getId()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblPayments.setItems(filteredList);
        lblBalance.setText(String.format("%s's Balance: $%.2f", patient.getFirstName(), patient.getBalance()));
    }

    @FXML
    private void handleReset() {
        cmbPatient.getSelectionModel().clearSelection();
        tblPayments.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getPayments()));
        lblBalance.setText("No Patient is selected");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/NurseDashboard.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}