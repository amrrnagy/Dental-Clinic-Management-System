package Controllers.Dashboards;

import Models.ClinicManager;
import Models.Person;
import Models.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class LoginController {

    @FXML private ComboBox<UserRole> cmbRole;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblErrorMessage;

    @FXML
    public void initialize() {
        // Fill the roles combobox
        cmbRole.getItems().setAll(UserRole.values());
    }

    @FXML
    private void handleSignIn(ActionEvent event) {
        UserRole selectedRole = cmbRole.getValue();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (selectedRole == null || username.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Please fill in all fields.");
            return;
        }

        // Validate user
        Person user = validateLogin(selectedRole, username, password);

        if (user != null) {
            ClinicManager.getInstance().setCurrentUser(user); // Set the session user
            navigateToDashboard(event, selectedRole);
        } else {
            lblErrorMessage.setText("Invalid credentials for " + selectedRole);
        }
    }

    private Person validateLogin(UserRole role, String username, String password) {
        ClinicManager manager = ClinicManager.getInstance();

        // Check for Doctors
        if (role == UserRole.DOCTOR) {
            return manager.getDoctors().stream()
                    .filter(d -> d.getUsername().equals(username) && d.getPassword().equals(password))
                    .findFirst().orElse(null);
        }
        // Check for Patients
        else if (role == UserRole.PATIENT) {
            return manager.getPatients().stream()
                    .filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password))
                    .findFirst().orElse(null);
        }
        // Check for Nurse
        else if (role == UserRole.NURSE) {
            return manager.getNurses().stream()
                    .filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password))
                    .findFirst().orElse(null);
        }
        return null;
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/SignupDashboard.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblErrorMessage.setText("Error loading dashboard.");
            System.err.println("Error: " + e.getMessage());
        }

    }

    private void navigateToDashboard(ActionEvent event, UserRole role) {
        String fxmlFile = switch (role) {
            case PATIENT -> "/Views/Dashboards/PatientDashboard.fxml";
            case DOCTOR -> "/Views/Dashboards/DoctorDashboard.fxml";
            case NURSE -> "/Views/Dashboards/NurseDashboard.fxml";
        };

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblErrorMessage.setText("Error loading dashboard.");
            System.err.println("Error: " + e.getMessage());
        }
    }
}