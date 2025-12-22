package Controllers.Doctor;

import Models.*;
import javafx.beans.property.ReadOnlyStringWrapper;
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

public class DoctorPrescriptionController{

    @FXML private TableView<Prescription> tblPrescriptions;
    @FXML private TableColumn<Prescription, String> colMedication;
    @FXML private TableColumn<Prescription, LocalDateTime> colDate;
    @FXML private TableColumn<Prescription, String> colPatient;
    @FXML private TableColumn<Prescription, String> colDosage;
    @FXML private TableColumn<Prescription, String> colInstructions;

    private Doctor currentDoctor;

    public void initialize() {

        this.currentDoctor = (Doctor) ClinicManager.getInstance().getCurrentUser();

        setupTableColumns();
        refreshTable();
    }

    private void refreshTable() {

        ObservableList<Prescription> filteredList = ClinicManager.getInstance().getPrescriptions().stream()
                // Filter by Current Doctor ID
                .filter(a -> a.getDoctorId().equals(currentDoctor.getId()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblPrescriptions.setItems(filteredList);
    }

    private void setupTableColumns() {

        colDate.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));

        // Get Medication from the nested PrescriptionItem
        colMedication.setCellValueFactory(cellData -> {
            PrescriptionItem item = cellData.getValue().getItem();
            return new ReadOnlyStringWrapper(item != null ? item.medicineName() : "");
        });

        // Get Dosage from the nested PrescriptionItem
        colDosage.setCellValueFactory(cellData -> {
            PrescriptionItem item = cellData.getValue().getItem();
            return new ReadOnlyStringWrapper(item != null ? item.dose() : "");
        });

        // Custom mapping to show Patient Names instead of IDs
        colPatient.setCellValueFactory(cellData -> {
            String pId = cellData.getValue().getPatientId();
            Patient p = ClinicManager.getInstance().findPatientById(pId);
            return new ReadOnlyStringWrapper(p != null ? p.getFullName() : "Unknown");
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        colDate.setCellFactory(_ -> new TableCell<>() {
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

        colInstructions.setCellValueFactory(cellData -> {
            Prescription p = cellData.getValue();
            PrescriptionItem item = p.getItem();

            if (item != null) {
                // Constructing the string using record accessors
                String instruction = String.format("Take %s, for %d days",
                        item.frequency(),   // e.g., "Twice Daily"
                        item.days()         // e.g., 7
                );
                return new ReadOnlyStringWrapper(instruction);
            }
            return new ReadOnlyStringWrapper("No instructions provided");
        });
    }

    @FXML
    private void handleAddPrescription(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Doctor/AddPrescription.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/DoctorDashboard.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}