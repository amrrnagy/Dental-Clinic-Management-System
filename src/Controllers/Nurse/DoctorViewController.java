package Controllers.Nurse;

import Models.ClinicManager;
import Models.Doctor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DoctorViewController implements Initializable {

    // Matches the fx:id in FXML
    @FXML private TableView<Doctor> tblDoctors;
    @FXML private TableColumn<Doctor, String> colDoctorId;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, String> colGender;
    @FXML private TableColumn<Doctor, String> colPhone;
    @FXML private TableColumn<Doctor, String> colUsername;
    @FXML private TableColumn<Doctor, String> colPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();

        // Link the table to the shared list in ClinicManager
        // This will display doctors seeded in the seedInitialData() method
        tblDoctors.setItems(ClinicManager.getDoctors());
    }

    private void setupTableColumns() {
        // Map columns to Doctor model fields
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    @FXML
    private void handleAddDoctor(ActionEvent event) {
        loadScene(event, "/Views/Nurse/AddDoctorView.fxml", "Add New Doctor");
    }

    @FXML
    private void handleRemoveDoctor(ActionEvent event) {
        loadScene(event, "/Views/Nurse/RemoveDoctorView.fxml", "Remove Doctor");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        loadScene(event, "/Views/Nurse/NurseDashboard.fxml", "Nurse Dashboard");
    }

    private void loadScene(ActionEvent event, String path, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}