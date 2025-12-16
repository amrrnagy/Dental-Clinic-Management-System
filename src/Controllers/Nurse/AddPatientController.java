package Controllers.Nurse;

import Models.Gender;
import Models.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Models.ClinicManager;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPatientController implements Initializable {

    // FXML Controls injected from AddPatient.fxml
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TableView<Patient> tblPatients;
    @FXML private ComboBox<Gender> cmbGender;

    // Table Columns
    @FXML private TableColumn<Patient, Integer> colID;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colLastName;
    @FXML private TableColumn<Patient, Gender> colGender;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, Double> colBalance;

    private final ClinicManager clinicManager = ClinicManager.getInstance();
    private ObservableList<Patient> patientList;

    /**
     * Called to initialize a controller after its root element has been completely processed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 1. Set up the ObservableList for the TableView
        patientList = FXCollections.observableArrayList(clinicManager.getAllPatients());

        cmbGender.getItems().addAll(Gender.values());

        // 2. Map Table Columns to Patient class attributes (uses getters like getFirstName())
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Use "outstandingBalance" to match the field name in the Patient class
        colBalance.setCellValueFactory(new PropertyValueFactory<>("outstandingBalance"));

        // 3. Populate the TableView
        tblPatients.setItems(patientList);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    /**
     * Handles the creation of a new patient from the form fields.
     */
    @FXML
    private void handleAddPatient() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        Gender gender = cmbGender.getValue();

        // Basic Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "First Name and Last Name are required.");
            return;
        }



        if (!isNumeric(phone)) {
            showAlert(Alert.AlertType.WARNING, "Error", "Not number. pls write numbers");
            return;
        }

        // Create a new Patient object
        // NOTE: Assuming a constructor in Patient like: Patient(String firstName, String lastName)
        Patient newPatient = new Patient(firstName, lastName, gender);
        newPatient.setEmail(email);
        newPatient.setPhone(phone);

        // Add patient to the central manager
        clinicManager.addPatient(newPatient);

        // Update the ObservableList to refresh the TableView
        patientList.add(newPatient);

        // Clear the form fields
        txtFirstName.clear();
        txtLastName.clear();
        txtPhone.clear();
        txtEmail.clear();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Patient " + newPatient.getFullName() + " added successfully.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- Navigation Helper ---
    private void navigateTo(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(view);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML file: " + fxmlPath);
        }
    }

    /**
     * Handles the action to go back to the main dashboard.
     * This method is called by the "Back to Dashboard" button in the FXML.
     */
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        navigateTo(event, "/Views/Dashboards/LoginView.fxml", "Dental Clinic Management System");
    }
}