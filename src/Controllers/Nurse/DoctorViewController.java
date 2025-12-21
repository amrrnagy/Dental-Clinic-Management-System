package Controllers.Nurse;

import Models.ClinicManager;
import Models.Doctor;
import Models.Gender;
import Models.Specialization;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class DoctorViewController  {

    @FXML private TableView<Doctor> tblDoctors;
    @FXML private TableColumn<Doctor, String> colDoctorId;
    @FXML private TableColumn<Doctor, String> colName;
    @FXML private TableColumn<Doctor, Gender> colGender;
    @FXML private TableColumn<Doctor, Specialization> colSpecialization;
    @FXML private TableColumn<Doctor, String> colUsername;
    @FXML private TableColumn<Doctor, String> colPassword;
    @FXML public Label lblError;
    @FXML private HBox errorContainer;

    public void initialize() {
        setupTableColumns();
        tblDoctors.setItems(FXCollections.observableArrayList(ClinicManager.getInstance().getDoctors()));

        errorContainer.managedProperty().bind(errorContainer.visibleProperty());
        errorContainer.setVisible(false);
    }

    private void setupTableColumns() {
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        loadScene(event, "/Views/Nurse/AddDoctor.fxml");
    }

    @FXML
    private void handleRemove(ActionEvent event) {
        Doctor selectedDoctor = tblDoctors.getSelectionModel().getSelectedItem();

        if (selectedDoctor != null) {
            tblDoctors.getItems().remove(selectedDoctor);
            ClinicManager.getInstance().removeDoctor(selectedDoctor);

            Doctor.minusNextID();

            lblError.setText("Dr. " + selectedDoctor.getFullName() + " has been successfully removed.");
            lblError.setStyle("-fx-text-fill: green;");
            errorContainer.setVisible(true);

            PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
            visiblePause.setOnFinished(e -> {
                lblError.setText("");
                errorContainer.setVisible(false);
            });
            visiblePause.play();

        } else {
            lblError.setText("No Doctor selected to delete.");
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
            e.printStackTrace();
        }
    }
}