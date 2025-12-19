package Controllers.Nurse;

import Models.ClinicManager;
import Models.Patient;
import javafx.collections.ObservableList;
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
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PatientViewController implements Initializable {

    // Must match fx:id in FXML
    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, String> colPatientId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colGender;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colUsername;
    @FXML private TableColumn<Patient, String> colPassword;
    @FXML private TableColumn<Patient, String> colHistory;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();

        tblPatients.setItems(
                FXCollections.observableArrayList(ClinicManager.getInstance().getPatients())
        );

    }

    private void setupTableColumns() {
        // Mapping columns to Patient model fields
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colHistory.setCellValueFactory(new PropertyValueFactory<>("MedicalHistory"));

    }

    @FXML
    private void handleAddPatient(ActionEvent event) {
        loadScene(event, "/Views/Nurse/AddPatientView.fxml", "Add Patient");
    }

    @FXML
    private void handleRemovePatient(ActionEvent event) {
        loadScene(event, "/Views/Nurse/RemovePatientView.fxml", "Remove Patient");
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        loadScene(event, "/Views/Dashboards/NurseDashboard.fxml", "Nurse Dashboard");
    }

    private void loadScene(ActionEvent event, String path, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + path);
            e.printStackTrace();
        }
    }
}