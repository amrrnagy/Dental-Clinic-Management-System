package Controllers.Nurse;

import Models.ClinicManager;
import Models.Gender;
import Models.Specialization;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AddDoctorController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox<Specialization> cmbSpecialization;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<Gender> cmbGender;

    @FXML
    public void initialize() {
        cmbGender.getItems().setAll(Gender.values()); //
    }

    @FXML
    private void handleAddDoctor(ActionEvent event) throws IOException {

        ClinicManager.getInstance().addDoctor(
                txtFirstName.getText(),
                txtLastName.getText(),
                cmbGender.getValue(),
                txtUsername.getText(),
                txtPassword.getText(),
                cmbSpecialization.getValue()
        );
        handleBackToDashboard(event);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        switchScene(event);
    }

    private void switchScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/DoctorView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}