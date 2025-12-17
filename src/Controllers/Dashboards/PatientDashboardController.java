package Controllers.Dashboards;

import Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientDashboardController implements Initializable {

    // Store the authenticated patient object for context-aware filtering
    private Patient authenticatedPatient;

    private static final String ROLE_SELECTION_PATH = "/Views/Dashboards/LoginView.fxml"; // Path corrected to match usage

    // Placeholder paths for patient-specific detail views
    private static final String PATIENT_APPOINTMENTS_PATH = "/Views/PatientAppointment.fxml";
    private static final String PATIENT_PRESCRIPTIONS_PATH = "/Views/PrescriptionView.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic
    }

    /**
     * VITAL METHOD: Called by the DashboardController (via FXMLLoader) after successful login
     * to inject the authenticated Patient object.
     */
    public void setAuthenticatedPatient(Patient patient) {
        this.authenticatedPatient = patient;
        if (patient != null) {
            System.out.println("Patient " + patient.getFullName() + " authenticated for dashboard session.");
            // TODO: Update a welcome message Label in the FXML (if one is added)
        }
    }

    // --- Utility to ensure context is present ---
    private boolean ensurePatientContext(ActionEvent event) {
        if (authenticatedPatient == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "Patient identity lost. Please sign in again.");
            handleLogout(event);
            return false;
        }
        return true;
    }


    // --- 1. View Appointments Button (Updated to pass context) ---
    @FXML
    private void handleViewAppointments(ActionEvent event) {
        if (!ensurePatientContext(event)) return;

        // Use the context-aware navigation method
        navigateToPatientView(event, PATIENT_APPOINTMENTS_PATH, "My Upcoming Appointments", authenticatedPatient.getId().toString());
    }

    // --- 2. View Prescriptions Button (Updated to pass context) ---
    @FXML
    private void handleViewPrescriptions(ActionEvent event) {
        if (!ensurePatientContext(event)) return;

        // Use the context-aware navigation method
        navigateToPatientView(event, PATIENT_PRESCRIPTIONS_PATH, "My Prescription History", authenticatedPatient.getId().toString());
    }

    // REMOVED: handleManagePayments(ActionEvent event)

    // --- 3. Logout Button ---
    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, ROLE_SELECTION_PATH, "Dental Clinic Management System");
    }

    // --- Navigation Utility (CONTEXT-AWARE VERSION) ---

    /**
     * Specialized navigation method to load a view and set the Patient ID context in the target controller.
     * * NOTE: This requires the target controller (e.g., PatientAppointmentController)
     * to have a public method named setPatientContext(String patientId).
     */
    private void navigateToPatientView(ActionEvent event, String fxmlPath, String title, String patientId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Assumption: The target controller has a setPatientContext(String patientId) method
            Object controller = loader.getController();

            // Example of how you would call the context method dynamically (requires casting based on the target controller type)
            // if (controller instanceof PatientAppointmentController) {
            //     ((PatientAppointmentController) controller).setPatientContext(patientId);
            // } else if (controller instanceof PatientPrescriptionsController) {
            //     ((PatientPrescriptionsController) controller).setPatientContext(patientId);
            // }

            // Temporary placeholder for direct navigation (since we don't know the specific controller names here)
            // You must implement the context setting logic after casting the controller object.

            Scene scene = new Scene(view);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load FXML file: " + fxmlPath + ". Check console for details.");
        }
    }

    // --- Navigation Utility (Simple version for Logout) ---
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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load FXML file: " + fxmlPath + ". Check console for details.");
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