package Controllers.Nurse;

import Models.ClinicManager;
import Models.Patient;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PatientViewController {

    @FXML private TableView<Patient> tblPatients;
    @FXML private TableColumn<Patient, String> colPatientId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, String> colGender;
    @FXML private TableColumn<Patient, String> colPhone;
    @FXML private TableColumn<Patient, String> colUsername;
    @FXML private TableColumn<Patient, String> colPassword;
    @FXML private TextField txtSearchId;
    @FXML public Label lblError;
    @FXML private HBox errorContainer;

    public void initialize() {
        setupTableColumns();
        refreshTableData();
        errorContainer.managedProperty().bind(errorContainer.visibleProperty());
        errorContainer.setVisible(false);


    }

    private void setupTableColumns() {
        colPatientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    private void refreshTableData() {
        tblPatients.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getPatients()));
    }


    @FXML
    private void handleSearch(ActionEvent event) {
        String idToSearch = txtSearchId.getText().trim();
        if (idToSearch.isEmpty()) {
            refreshTableData();
            return;
        }
        Patient foundPatient = ClinicManager.getInstance().findPatientById(idToSearch);
        if (foundPatient != null) {
            tblPatients.setItems(FXCollections.observableArrayList(foundPatient));
            lblError.setText("");
            errorContainer.setVisible(false);
        } else {
            tblPatients.setItems(FXCollections.observableArrayList());

            lblError.setText("No patient found with ID: " + idToSearch);
            lblError.setStyle("-fx-text-fill: red;");
            errorContainer.setVisible(true);
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(e -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();
//            refreshTableData();

        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        loadScene(event, "/Views/Nurse/AddPatient.fxml");
    }

    @FXML
    private void handleRemove(ActionEvent event) {
        Patient selectedPatient = tblPatients.getSelectionModel().getSelectedItem();

        if (selectedPatient != null) {
            tblPatients.getItems().remove(selectedPatient);
            ClinicManager.getInstance().removePatient(selectedPatient);
            Patient.minusNextID();

            lblError.setText("Patient " + selectedPatient.getFullName() + " has been successfully removed.");
            lblError.setStyle("-fx-text-fill: green;");
            errorContainer.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(e -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();

        } else {
            lblError.setText("No Patient selected to delete.");
            lblError.setStyle("-fx-text-fill: red;");
            errorContainer.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(e -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        loadScene(event, "/Views/Dashboards/NurseDashboard.fxml");
    }

    private void loadScene(ActionEvent event, String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + path);
            e.printStackTrace();
        }
    }
}