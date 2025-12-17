package Controllers.Nurse;

import Models.ClinicManager;
import Models.Gender;
import Models.Specialization;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddDoctorController {

    @FXML private TextField txtFirstName; // [cite: 119]
    @FXML private TextField txtLastName; // [cite: 120]
    @FXML private TextField txtSpecialization; // [cite: 120]
    @FXML private TextField txtUsername; // [cite: 120]
    @FXML private TextField txtPassword; // [cite: 121]
    @FXML private ComboBox<Gender> cmbGender; // [cite: 121]

    @FXML
    public void initialize() {
        cmbGender.getItems().setAll(Gender.values()); //
    }

    @FXML
    private void handleAddDoctor(ActionEvent event) {
        // Use ClinicManager to persist the new Doctor
        ClinicManager.getInstance().addDoctor(
                txtFirstName.getText(),
                txtLastName.getText(),
                cmbGender.getValue().toString(),
                txtUsername.getText(),
                txtPassword.getText(),
                txtSpecialization.getText()
        );
        handleBackToDashboard(event);
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        // Navigation logic omitted for brevity, similar to switchScene above [cite: 118]
    }
}