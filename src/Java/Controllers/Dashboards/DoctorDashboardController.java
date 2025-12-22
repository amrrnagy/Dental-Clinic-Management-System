package Controllers.Dashboards;

import Models.ClinicManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class DoctorDashboardController {

    public void handlePrescriptionView(ActionEvent actionEvent) throws IOException {
        switchScene(actionEvent, "/Views/Doctor/DoctorPrescription.fxml");
    }

    public void handleViewSchedule(ActionEvent actionEvent) throws IOException {
        switchScene(actionEvent, "/Views/Doctor/DoctorAppointment.fxml");
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        ClinicManager.getInstance().setCurrentUser(null); //
        switchScene(actionEvent, "/Views/Dashboards/LoginView.fxml");
    }

    private void switchScene(ActionEvent event, String path) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}