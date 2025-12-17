package Controllers.Nurse;

import Models.*;
import javafx.beans.property.ReadOnlyStringWrapper;
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

public class AppointmentViewController implements Initializable {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private ComboBox<String> cmbStatusFilter;

    // Table Columns
    @FXML private TableColumn<Appointment, String> colDateTime;
    @FXML private TableColumn<Appointment, String> colPatient;
    @FXML private TableColumn<Appointment, String> colDoctor;
    @FXML private TableColumn<Appointment, String> colReason;
    @FXML private TableColumn<Appointment, String> colStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupFilterOptions();

        // Initial data load from ClinicManager
        tblAppointments.setItems(ClinicManager.getAllAppointments());
    }

    private void setupTableColumns() {
        // Simple property factories for basic strings
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTimeString"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom mapping to show Patient Names instead of IDs
        colPatient.setCellValueFactory(cellData -> {
            String pId = cellData.getValue().getPatientId();
            Patient p = ClinicManager.getInstance().findPatientById(pId);
            return new ReadOnlyStringWrapper(p != null ? p.getFullName() : "Unknown");
        });

        // Custom mapping to show Doctor Names instead of IDs
        colDoctor.setCellValueFactory(cellData -> {
            String dId = cellData.getValue().getDoctorId();
            Doctor d = ClinicManager.getInstance().findDoctorById(dId);
            return new ReadOnlyStringWrapper(d != null ? d.getFullName() : "Unknown");
        });
    }

    private void setupFilterOptions() {
        cmbStatusFilter.getItems().addAll("ALL", "SCHEDULED", "COMPLETED", "CANCELLED");
        cmbStatusFilter.setValue("ALL");

        // Filter logic using streams on the global list
        cmbStatusFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("ALL")) {
                tblAppointments.setItems(ClinicManager.getAllAppointments());
            } else {
                ObservableList<Appointment> filtered = ClinicManager.getAllAppointments().stream()
                        .filter(a -> a.getStatus().toString().equals(newVal))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tblAppointments.setItems(filtered);
            }
        });
    }

    @FXML
    private void handleRefreshList(ActionEvent event) {
        // Force the table to refresh its view of the static list
        tblAppointments.refresh();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Nurse/NurseDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Nurse Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}