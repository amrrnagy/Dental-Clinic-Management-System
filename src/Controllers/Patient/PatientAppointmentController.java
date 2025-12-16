package Controllers.Patient;

import Models.Appointment;
import Models.ClinicManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Handles viewing and scheduling appointments ONLY for the authenticated patient.
 */
public class PatientAppointmentController implements Initializable {

    @FXML private TableView<Appointment> tblAppointments;
    // Assume necessary TableColumns are defined in the FXML

    private final ClinicManager clinicManager = ClinicManager.getInstance();
    private String patientIdContext; // The ID of the currently logged-in patient

    // Path to the generic scheduler (which will need to pre-fill the patient ID)
    private static final String SCHEDULER_VIEW_PATH = "/Views/Patient/AddAppointment.fxml";
    private static final String PATIENT_DASHBOARD_PATH = "/Views/Dashboards/PatientDashboard.fxml";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization for table columns (omitted for brevity, assume linked via FXML)
    }

    /**
     * VITAL METHOD: Called by the PatientDashboardController to inject the context.
     * @param patientId The ID of the authenticated patient.
     */
    public void setPatientContext(String patientId) {
        this.patientIdContext = patientId;
        loadPatientAppointments();
    }

    private void loadPatientAppointments() {
        if (patientIdContext == null) return;

        List<Appointment> filteredList = clinicManager.getAllAppointments().stream()
                .filter(a -> a.getPatientId().equals(patientIdContext)) // Filter by current patient ID
                .sorted(Comparator.comparing(Appointment::getDateTime).reversed())
                .collect(Collectors.toList());

        tblAppointments.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void handleReserveNewAppointment(ActionEvent event) {
        if (patientIdContext == null) {
            showAlert(Alert.AlertType.ERROR, "Session Lost", "Please return to the dashboard and sign in again.");
            return;
        }

        // Navigate to the scheduler, and pass the patient ID so the scheduler can pre-fill it.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SCHEDULER_VIEW_PATH));
            Parent schedulerView = loader.load();

            // Assume the scheduler controller has a method to pre-set the patient ID
            // AppointmentController schedulerController = loader.getController();
            // schedulerController.preselectPatient(patientIdContext);

            Scene scene = new Scene(schedulerView);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle("Reserve New Appointment");
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load FXML.");
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        navigateTo(event, PATIENT_DASHBOARD_PATH, "Patient Portal");
    }

    // --- Utility Methods ---

    private void navigateTo(ActionEvent event, String fxmlPath, String title) { /* ... implementation omitted ... */ }
    private void showAlert(Alert.AlertType type, String title, String message) { /* ... implementation omitted ... */ }
}