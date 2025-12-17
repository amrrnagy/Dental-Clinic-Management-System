package Controllers.Nurse;

import Models.ClinicManager;
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

public class AddDoctorController implements Initializable {

    // FXML fields matching your fx:id attributes
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtSpecialization;
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<String> cmbGender;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the Gender ComboBox options
        cmbGender.getItems().addAll("Male", "Female", "Other");
    }

    @FXML
    private void handleAddDoctor(ActionEvent event) {
        // 1. Collect data from input fields
        String fName = txtFirstName.getText().trim();
        String lName = txtLastName.getText().trim();
        String spec = txtSpecialization.getText().trim();
        String gender = cmbGender.getValue();
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();

        // 2. Validation: Ensure required fields are not empty
        if (fName.isEmpty() || lName.isEmpty() || user.isEmpty() || pass.isEmpty() || spec.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all fields.");
            return;
        }

        // 3. Add to ClinicManager
        // The ClinicManager will auto-generate the ID and add the doctor to the ObservableList
        ClinicManager.getInstance().addDoctor(fName, lName, fName + "@dentalclinic.com", user, pass, spec);

        // 4. Success Notification and Field Reset
        showAlert(Alert.AlertType.INFORMATION, "Success", "Dr. " + lName + " has been registered.");
        clearFields();
    }

    private void clearFields() {
        txtFirstName.clear();
        txtLastName.clear();
        txtSpecialization.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbGender.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Views/Nurse/NurseDashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Nurse Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not return to dashboard.");
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