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
        // Populate the role dropdown with values from the UserRole enum
        cmbRole.getItems().setAll(UserRole.values()); //
    }

    @FXML
    private void handleSignIn(ActionEvent event) { // [cite: 20]
        UserRole selectedRole = cmbRole.getValue();
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (selectedRole == null || username.isEmpty() || password.isEmpty()) {
            lblErrorMessage.setText("Please fill in all fields."); // [cite: 19]
            return;
        }

        // Logic to validate user against ClinicManager data
        Person user = validateLogin(selectedRole, username, password);

        if (user != null) {
            ClinicManager.getInstance().setCurrentUser(user); // Set the session user
            navigateToDashboard(event, selectedRole);
        } else {
            lblErrorMessage.setText("Invalid credentials for " + selectedRole); // [cite: 19]
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
        // Nurse check
        else if (role == UserRole.NURSE) {
            return manager.getNurses().stream()
                    .filter(p -> p.getUsername().equals(username) && p.getPassword().equals(password))
                    .findFirst().orElse(null);
        }
        return null;
    }


    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/SignupDashboard.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



    private void navigateToDashboard(ActionEvent event, UserRole role) {
        String fxmlFile = switch (role) {
            case PATIENT -> "/Views/Dashboards/PatientDashboard.fxml"; // [cite: 21]
            case DOCTOR -> "/Views/Dashboards/DoctorDashboard.fxml";  // [cite: 56]
            case NURSE -> "/Views/Dashboards/NurseDashboard.fxml";   // [cite: 34]
        };

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            lblErrorMessage.setText("Error loading dashboard.");
            e.printStackTrace();
        }
    }
}