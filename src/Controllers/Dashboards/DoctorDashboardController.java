package Controllers.Dashboards;

import Models.ClinicManager;
import Models.Doctor;
import Models.Gender;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class DoctorDashboardController {

    @FXML private TableView<Doctor> tblDoctors; // Defined in FXML [cite: 104]
    @FXML private TableColumn<Doctor, Integer> colDoctorId;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, Gender> colGender;
    @FXML private TableColumn<Doctor, String> colUsername;

    @FXML
    public void initialize() {
        // Mapping columns to Doctor model fields
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        refreshTable();
    }

    private void refreshTable() {
        tblDoctors.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getDoctors())); //
    }

    @FXML
    private void handleAddDoctor(ActionEvent event) throws IOException {
        switchScene(event, "/Views/AddDoctor.fxml"); // [cite: 102]
    }

    @FXML
    private void handleRemoveDoctor(ActionEvent event) throws IOException {
        switchScene(event, "/Views/RemoveDoctor.fxml"); // [cite: 103]
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) throws IOException {
        switchScene(event, "/Views/NurseDashboard.fxml"); // [cite: 100]
    }

    private void switchScene(ActionEvent event, String path) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}