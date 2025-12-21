package Controllers.Patient;

import Models.Appointment;
import Models.AppointmentStatus;
import Models.ClinicManager;
import Models.Patient;
import javafx.animation.PauseTransition;
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
import java.util.stream.Collectors;

public class PatientAppointmentController {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private TableColumn<Appointment, String> colAppointmentId;
    @FXML private TableColumn<Appointment, String> colDateTime;
    @FXML private TableColumn<Appointment, String> colDoctor;
    @FXML private TableColumn<Appointment, AppointmentStatus> colStatus;
    @FXML private ComboBox<String> cmbStatusFilter;
    @FXML public Label lblError;
    @FXML private HBox errorContainer;
    @FXML
    public void initialize() {
        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        cmbStatusFilter.getItems().addAll("All Statuses", "SCHEDULED", "COMPLETED", "CANCELLED");

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

    @FXML
    private void handleRefreshList() {

        loadAppointments();
    }

    @FXML
    private void handleNewAppointment(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Patient/AddAppointment.fxml");
    }
    @FXML
    private void handleCancelappointment(ActionEvent event) {
        Appointment selectedAppointment = tblAppointments.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            tblAppointments.getItems().remove(selectedAppointment);
            ClinicManager.getInstance().cancelAppointment(selectedAppointment);

            lblError.setText("appointment " + selectedAppointment.getId() + " has been successfully cancelled.");
            lblError.setStyle("-fx-text-fill: green;");
            errorContainer.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(e -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();

        } else {
            lblError.setText("No Appointments selected to delete.");
            lblError.setStyle("-fx-text-fill: red;");
            errorContainer.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(e -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();
        }
    }
    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        switchScene(event, "/Views/Dashboards/PatientDashboard.fxml");
    }


    private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}