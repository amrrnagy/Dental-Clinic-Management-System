package Controllers.Patient;

import Models.*;
import javafx.animation.PauseTransition;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class PatientAppointmentController {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableColumn<Appointment, String> colAppointmentId;
    @FXML private TableColumn<Appointment, LocalDateTime> colDateTime;
    @FXML private TableColumn<Appointment, String> colDoctor;
    @FXML private TableColumn<Appointment, AppointmentStatus> colStatus;
    @FXML private ComboBox<String> cmbStatusFilter;

    @FXML public Label lblError;
    @FXML private HBox errorContainer;

    @FXML
    public void initialize() {
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom mapping to show Doctor Names instead of IDs
        colDoctor.setCellValueFactory(cellData -> {
            String dId = cellData.getValue().getDoctorId();
            Doctor d = ClinicManager.getInstance().findDoctorById(dId);
            return new ReadOnlyStringWrapper(d != null ? d.getFullName() : "Unknown");
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

        setupFilterOptions();
        loadAppointments();
    }

    private void loadAppointments() {
        Patient currentPatient = (Patient) ClinicManager.getInstance().getCurrentUser();
        if (currentPatient == null) return;
        ObservableList<Appointment> patientApps = FXCollections.observableArrayList(
                ClinicManager.getInstance().getAppointments().stream()
                        .filter(a -> a.getPatientId().equals(currentPatient.getId()))
                        .collect(Collectors.toList())
        );

        tblAppointments.setItems(patientApps);
    }

    private void setupFilterOptions() {
        cmbStatusFilter.getItems().addAll("ALL", "SCHEDULED", "COMPLETED", "CANCELLED");
        cmbStatusFilter.setValue("ALL");

        cmbStatusFilter.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
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
    private void handleReset() {
        cmbStatusFilter.setValue("ALL");
    }

    @FXML
    private void handleNewAppointment(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Patient/AddAppointment.fxml");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Dashboards/PatientDashboard.fxml");
    }

    @FXML
    private void handleCancelAppointment() {
        Appointment selectedAppointment = tblAppointments.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            if(ClinicManager.getInstance().cancelAppointment(selectedAppointment)) {
                tblAppointments.refresh();
                lblError.setText("Appointment " + selectedAppointment + " has been successfully cancelled.");
                lblError.setStyle("-fx-text-fill: green;");
                errorContainer.setVisible(true);

                PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
                visiblePause.setOnFinished(_ -> {
                    lblError.setText("");
                    errorContainer.setVisible(false);
                });
                visiblePause.play();
            }
            else {
                lblError.setText("Can't Cancel this Appointment.");
                lblError.setStyle("-fx-text-fill: red;");
                errorContainer.setVisible(true);

                PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
                visiblePause.setOnFinished(_ -> {
                    lblError.setText("");
                    errorContainer.setVisible(false);
                });
                visiblePause.play();
            }

        } else {
            lblError.setText("No Appointment selected to cancel.");
            lblError.setStyle("-fx-text-fill: red;");
            errorContainer.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(_ -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();
        }

    }

    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}