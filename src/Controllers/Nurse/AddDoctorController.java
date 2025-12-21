package Controllers.Nurse;

import Models.ClinicManager;
import Models.Doctor;
import Models.Gender;
import Models.Specialization;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Objects;

public class AddDoctorController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox<Specialization> cmbSpecialization;
    @FXML private TextField txtFee;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<Gender> cmbGender;

    @FXML public Label lblError;

    @FXML
    public void initialize() {
        cmbGender.getItems().setAll(Gender.values());
        cmbSpecialization.getItems().setAll(Specialization.values());
    }

    @FXML
    private void handleAddDoctor(ActionEvent event) throws IOException {
        String errorMessage = "";

        if (txtFirstName.getText().trim().isEmpty()) {
            errorMessage = "First Name is required.";
        } else if (txtLastName.getText().trim().isEmpty()) {
            errorMessage = "Last Name is required.";
        } else if (cmbGender.getValue() == null) {
            errorMessage = "Please select a Gender.";
        } else if (!txtFee.getText().isEmpty() && !txtFee.getText().trim().matches("\\d+")) {
            errorMessage = "Fees must be a number.";
        } else if (txtUsername.getText().trim().isEmpty()) {
            errorMessage = "Username is required.";
        } else if (txtPassword.getText().trim().isEmpty()) {
            errorMessage = "Password is required.";
        }

        if (!errorMessage.isEmpty()) {
            lblError.setText(errorMessage);
            lblError.setStyle("-fx-text-fill: red;");

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(_ -> lblError.setText(""));
            delay.play();
            return;
        }

        Doctor newDoctor = new Doctor(
                txtFirstName.getText(),
                txtLastName.getText(),
                cmbGender.getValue(),
                txtUsername.getText(),
                txtPassword.getText(),
                cmbSpecialization.getValue()
        );

        if(!txtFee.getText().trim().isEmpty())
            newDoctor.setConsultationFee(Integer.parseInt(txtFee.getText()));

        ClinicManager.getInstance().addDoctor(newDoctor);
        handleBack(event);
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        switchScene(event);
    }

    private void switchScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Nurse/DoctorView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}