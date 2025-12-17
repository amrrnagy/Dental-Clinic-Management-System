package Controllers.Nurse;

import Models.ClinicManager;
import Models.Gender;
import Models.Patient;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class AddPatientController implements Initializable {

    // FXML fields matching your fx:id attributes
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<Gender> cmbGender;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the Gender ComboBox
        cmbGender.getItems().addAll(Gender.values());
    }

    @FXML
    private void handleAddPatient(ActionEvent event) {
        // 1. Collect data from fields
        String fName = txtFirstName.getText();
        String lName = txtLastName.getText();
        String phone = txtPhone.getText();
        Gender gender = cmbGender.getValue();
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        // 2. Simple validation check
        if (fName.isEmpty() || lName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all required fields.");
            return;
        }

        // 3. Use ClinicManager to create and add the patient
        // Note: Update your addPatient method in ClinicManager to include Phone/Gender if needed
        ClinicManager.getInstance().addPatient(new Patient(fName, lName, gender, phone, user, pass));

        // 4. Confirmation and Field Reset
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful!",
                "Patient " + fName + " " + lName + " has been added to the system.");
        clearFields();
    }

    private void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbGender.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/Nurse/NurseDashboard.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Nurse Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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