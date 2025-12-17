package Controllers.Dashboards;

import Models.ClinicManager; // Import the manager
import Models.Patient;      // Import the Patient entity
import Models.UserRole;
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

public class LoginController implements Initializable {

    // FXML Fields for Authentication
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrorMessage;
    @FXML private ComboBox<UserRole> cmbRole;

    // FXML paths for the subsequent role-specific dashboards/menus
    private static final String DOCTOR_VIEW_PATH = "/Views/Dashboards/DoctorDashboard.fxml";
    private static final String NURSE_VIEW_PATH = "/Views/Dashboards/NurseDashboard.fxml";
    private static final String PATIENT_VIEW_PATH = "/Views/Dashboards/PatientDashboard.fxml";
    private static final String MAIN_TITLE = "Dental Clinic Management System";

    // --- System Dependency ---
    private final ClinicManager clinicManager = ClinicManager.getInstance();

    // --- Hardcoded Credentials (Testing purposes) ---
    private static final String DOCTOR_USER = "doc1";
    private static final String DOCTOR_PASS = "dpass";

    private static final String NURSE_USER = "nurse1";
    private static final String NURSE_PASS = "npass";

    private static final String PATIENT_USER = "pat1";
    private static final String PATIENT_PASS = "ppass";

    // Placeholder: The ID of the patient record corresponding to PATIENT_USER="pat1"
    private static final String AUTHENTICATED_PATIENT_ID = "1"; // Assuming Patient "pat1" has ID 1


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbRole.getItems().addAll(UserRole.values());
        cmbRole.setValue(UserRole.DOCTOR);
        lblErrorMessage.setText("");
    }

    // --- Authentication Handler (Updated to pass context for Patient) ---

    @FXML
    private void handleSignIn(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        UserRole role = cmbRole.getValue();

        lblErrorMessage.setText("");

        if (username.isEmpty() || password.isEmpty() || role == null) {
            lblErrorMessage.setText("Please enter username, password, and select a role.");
            return;
        }

        boolean isAuthenticated = false;
        String targetPath = null;
        String windowTitle = null;
        Patient authenticatedPatient = null; // Variable to hold the Patient object

        // 1. Doctor Authentication
        if (UserRole.DOCTOR.equals(role) && username.equals(DOCTOR_USER) && password.equals(DOCTOR_PASS)) {
            isAuthenticated = true;
            targetPath = DOCTOR_VIEW_PATH;
            windowTitle = MAIN_TITLE + " - Doctor Portal";

            // 2. Nurse Authentication
        } else if (UserRole.NURSE.equals(role) && username.equals(NURSE_USER) && password.equals(NURSE_PASS)) {
            isAuthenticated = true;
            targetPath = NURSE_VIEW_PATH;
            windowTitle = MAIN_TITLE + " - Staff/Scheduling Portal";

            // 3. Patient Authentication (New Logic)
        } else if (UserRole.PATIENT.equals(role) && username.equals(PATIENT_USER) && password.equals(PATIENT_PASS)) {
            isAuthenticated = true;
            targetPath = PATIENT_VIEW_PATH;
            windowTitle = MAIN_TITLE + " - Patient Portal";

            // Fetch the specific Patient object using the known ID
            // NOTE: clinicManager.findPatientById must return the Patient object using String ID.
            authenticatedPatient = clinicManager.findPatientById(AUTHENTICATED_PATIENT_ID);

            if (authenticatedPatient == null) {
                lblErrorMessage.setText("Authentication successful, but patient record not found in system (ID " + AUTHENTICATED_PATIENT_ID + ").");
                return;
            }
        }

        if (isAuthenticated) {
            // Use the updated navigateTo which handles Patient context injection
            navigateTo(event, targetPath, windowTitle, authenticatedPatient);
        } else {
            lblErrorMessage.setText("Invalid username or password for the selected role.");
        }
    }

    // --- Navigation Utilities (UPDATED) ---

    // Wrapper to maintain compatibility with existing non-context calls
    private void navigateTo(ActionEvent event, String fxmlPath, String title) {
        navigateTo(event, fxmlPath, title, null);
    }

    /**
     * General navigation method that handles loading the view and injecting the Patient object if needed.
     * @param patientContext The Patient object to pass, or null for Doctor/Nurse roles.
     */
    private void navigateTo(ActionEvent event, String fxmlPath, String title, Patient patientContext) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Check if the target is the Patient Dashboard AND we have a context object
            if (patientContext != null && fxmlPath.equals(PATIENT_VIEW_PATH)) {
                // Ensure the target controller is correctly cast and implements setAuthenticatedPatient
                Controllers.Dashboards.PatientDashboardController controller = loader.getController();
                controller.setAuthenticatedPatient(patientContext);
            }

            Scene scene = new Scene(view);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load FXML file: " + fxmlPath + ". Please check console.");
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