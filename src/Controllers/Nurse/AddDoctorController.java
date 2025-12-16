package Controllers.Nurse;

import Models.*;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddDoctorController implements Initializable {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox<Specialization> cmbSpecialization;
    @FXML private TableView<Doctor> tblDoctors;
    @FXML private ComboBox<Gender> cmbGender;

    @FXML private TableColumn<Doctor, Integer> colID;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, Specialization> colSpecialization;

    private final ClinicManager clinicManager = ClinicManager.getInstance();
    private ObservableList<Doctor> doctorList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate ComboBox with all Specialization enum values
        cmbSpecialization.getItems().addAll(Specialization.values());

        // Set up the ObservableList and TableView
        doctorList = FXCollections.observableArrayList(clinicManager.getAllDoctors());
        setupTableColumns();
        tblDoctors.setItems(doctorList);
    }

    private void setupTableColumns() {
        colID.setCellValueFactory(new PropertyValueFactory<>("id")); // Assumes Doctor inherits getId()
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
    }

    @FXML
    private void handleAddDoctor() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        Specialization specialization = cmbSpecialization.getValue();
        Gender gender = cmbGender.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || specialization == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
            return;
        }

        // Create new Doctor (assuming a suitable constructor exists)
        Doctor newDoctor = new Doctor(firstName, lastName, gender, specialization);

        // Add to the manager (assuming ClinicManager has an addDoctor method)
        clinicManager.addDoctor(newDoctor);

        doctorList.add(newDoctor); // Update the ObservableList
        clearForm();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Dr. " + newDoctor.getFullName() + " added.");
    }

    // --- Navigation and Utility ---
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        // Reusing the navigation pattern
        navigateTo(event, "/Views/Dashboards/LoginView.fxml", "Dental Clinic Management System");
    }

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
        }
    }

    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        cmbSpecialization.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}