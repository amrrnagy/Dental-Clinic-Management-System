package Controllers.Dashboards;

import Models.ClinicManager;
import Models.Gender;
import Models.Patient;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;

public class SignupDashboardController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private ComboBox<Gender> cmbGender;
    @FXML public Label lblError;

    @FXML
    public void initialize() {
        cmbGender.getItems().addAll(Gender.values());
    }

    @FXML
    private void handleSignUp(ActionEvent event) throws IOException {
        String errorMessage = "";

        if (txtFirstName.getText().trim().isEmpty()) {
            errorMessage = "First Name is required.";
        }
        else if (txtLastName.getText().trim().isEmpty()) {
            errorMessage = "Last Name is required.";
        }
        else if (txtPhone.getText().trim().isEmpty()) {
            errorMessage = "Phone is required.";
        }
        else if (!txtPhone.getText().trim().matches("\\d+")) {
            errorMessage = "Phone must contain only numbers.";
        }
        else if (cmbGender.getValue() == null) {
            errorMessage = "Please select a Gender.";
        }
        else if (txtUsername.getText().trim().isEmpty()) {
            errorMessage = "Username is required.";
        } else if (ClinicManager.getInstance().findPatientByUser(txtUsername.getText()) != null) {
            errorMessage = "Username is already used";
        } else if (txtPassword.getText().isEmpty()) {
            errorMessage = "Password is required.";
        }
        else if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            errorMessage = "Passwords do not match.";
        }

        if (!errorMessage.isEmpty()) {
            showInlineError(errorMessage); // Used for form validation errors
            return;
        }

        Patient newPatient = new Patient(
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                cmbGender.getValue(),
                txtPhone.getText().trim(),
                txtUsername.getText().trim(),
                txtPassword.getText()
        );

        ClinicManager.getInstance().addPatient(newPatient);

        showAlert(
                "Patient " + newPatient.getUsername() + " has been registered successfully.");

        handleBack(event);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        switchScene(event);
    }

    private void switchScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Dashboards/LoginView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showInlineError(String message) {
        lblError.setText(message);
        lblError.setStyle("-fx-text-fill: red;");
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(_ -> lblError.setText(""));
        delay.play();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}