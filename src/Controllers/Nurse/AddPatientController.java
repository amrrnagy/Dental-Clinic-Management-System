package Controllers.Nurse;

import Models.ClinicManager;
import Models.Doctor;
import Models.Gender;
import Models.Patient;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;

public class AddPatientController  {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<Gender> cmbGender;
    @FXML public Label lblError;

@FXML
    public void initialize() {
        cmbGender.getItems().addAll(Gender.values());
    }


    @FXML
    private void handleAddPatient(ActionEvent event) throws IOException {
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
        }
        else if (txtPassword.getText().trim().isEmpty()) {
            errorMessage = "Password is required.";
        }

        if (!errorMessage.isEmpty()) {
            lblError.setText(errorMessage);
            lblError.setStyle("-fx-text-fill: red;");

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> lblError.setText(""));
            delay.play();
            return;
        }

        Patient newPatient = new Patient (
                txtFirstName.getText(),
                txtLastName.getText(),
                cmbGender.getValue(),
                txtUsername.getText(),
                txtPassword.getText(),
                txtPhone.getText()
        );

        ClinicManager.getInstance().addPatient(newPatient);
        handleBack(event);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        switchScene(event);
    }

    private void switchScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Nurse/PatientView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}


