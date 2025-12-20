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

public class DoctorAppointmentController{

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private ComboBox<String> cmbStatusFilter;

    @FXML private TableColumn<Appointment, String> colAppointmentId;
    @FXML private TableColumn<Appointment, LocalDateTime> colDateTime;
    @FXML private TableColumn<Appointment, String> colPatient;
    @FXML private TableColumn<Appointment, AppointmentStatus> colStatus;

    public void initialize() {
        setupTableColumns();
        setupFilterOptions();

        // Initial data load from ClinicManager
        tblAppointments.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getAppointments()));
    }

    private void setupTableColumns() {

        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom mapping to show Patient Names instead of IDs
        colPatient.setCellValueFactory(cellData -> {
            String pId = cellData.getValue().getPatientId();
            Patient p = ClinicManager.getInstance().findPatientById(pId);
            return new ReadOnlyStringWrapper(p != null ? p.getFullName() : "Unknown");
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        colDateTime.setCellFactory(_ -> new TableCell<>() {
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

    private void setupFilterOptions() {
        cmbStatusFilter.getItems().addAll("ALL", "SCHEDULED", "COMPLETED", "CANCELLED");
        cmbStatusFilter.setValue("ALL");

        cmbStatusFilter.getSelectionModel().selectedItemProperty().addListener((_, oldVal, newVal) -> {
            if (newVal == null || newVal.equals("ALL")) {
                tblAppointments.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getAppointments()));
            } else {
                ObservableList<Appointment> filtered = ClinicManager.getInstance().getAppointments().stream()
                        .filter(a -> a.getStatus().toString().equals(newVal))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                tblAppointments.setItems(filtered);
            }
        });
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cmbStatusFilter.setValue("ALL");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/NurseDashboard.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}