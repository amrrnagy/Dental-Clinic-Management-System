package Controllers.Nurse;

import Models.*;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentViewController implements Initializable {

    @FXML private TableView<Appointment> tblAppointments;
    @FXML private ComboBox<String> cmbStatusFilter;

    // Table Columns
    @FXML private TableColumn<Appointment, LocalDateTime> colDateTime;
    @FXML private TableColumn<Appointment, String> colPatient;
    @FXML private TableColumn<Appointment, String> colDoctor;
    @FXML private TableColumn<Appointment, String> colReason;
    @FXML private TableColumn<Appointment, AppointmentStatus> colStatus;
    @FXML private TableColumn<Appointment, Void> colActions;

    // --- NEW: Role Context Field ---
    private UserRole userRole = null;

    private final ClinicManager clinicManager = ClinicManager.getInstance();
    private ObservableList<Appointment> masterAppointmentList;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // --- Define Dynamic Navigation Paths ---
    private static final String DOCTOR_DASHBOARD_PATH = "/Views/Dashboards/DoctorDashboard.fxml";
    private static final String NURSE_DASHBOARD_PATH = "/Views/Dashboards/NurseDashboard.fxml";
    private static final String SIGN_IN_PATH = "/Views/Dashboards/LoginView.fxml";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupStatusFilter();
        loadAppointments(null);
    }

    /**
     * VITAL METHOD: Called by the dashboard controllers to set the user's role.
     */
    public void setAccessingUserRole(UserRole role) {
        this.userRole = role;
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
            System.err.println("Could not load FXML file: " + fxmlPath);
        }
    }

    /**
     * Handles the action to go back to the appropriate dashboard based on the stored role.
     */
    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        String targetPath;
        String title;

        // Determine the correct dashboard path
        if (userRole == UserRole.DOCTOR) {
            targetPath = DOCTOR_DASHBOARD_PATH;
            title = "Doctor Portal";
        } else if (userRole == UserRole.NURSE) {
            targetPath = NURSE_DASHBOARD_PATH;
            title = "Nurse/Staff Portal";
        } else {
            // Default: If role is lost or not set, send to sign-in screen
            targetPath = SIGN_IN_PATH;
            title = "Dental Clinic Management System";
        }

        navigateTo(event, targetPath, title);
    }


    private void setupTableColumns() {
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colDateTime.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colDateTime.setCellFactory(column -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : DATE_TIME_FORMATTER.format(item));
            }
        });

        // Custom mapping for Patient Name
        colPatient.setCellValueFactory(cellData -> {
            String patientIdStr = cellData.getValue().getPatientId();
            Patient p = clinicManager.findPatientById(patientIdStr);
            return new ReadOnlyStringWrapper(p != null ? p.getFullName() : "Unknown Patient");
        });

        // Custom mapping for Doctor Name
        colDoctor.setCellValueFactory(cellData -> {
            String doctorIdStr = cellData.getValue().getDoctorId();
            Doctor d = clinicManager.findDoctorById(doctorIdStr);
            return new ReadOnlyStringWrapper(d != null ? d.getFullName() : "Unknown Doctor");
        });

        // Setup Action Buttons (e.g., Complete, Cancel)
        // Omitted for brevity.
    }

    private void setupStatusFilter() {
        cmbStatusFilter.getItems().add("ALL");
        for (AppointmentStatus status : AppointmentStatus.values()) {
            cmbStatusFilter.getItems().add(status.name());
        }
        cmbStatusFilter.setValue("ALL");

        cmbStatusFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadAppointments(newVal.equals("ALL") ? null : AppointmentStatus.valueOf(newVal));
            }
        });
    }

    private void loadAppointments(AppointmentStatus filterStatus) {
        // Fetch the master list of appointments
        List<Appointment> filteredList = clinicManager.getAllAppointments().stream()
                .sorted(Comparator.comparing(Appointment::getDateTime).reversed())
                .collect(Collectors.toList());

        // Apply filter if necessary
        if (filterStatus != null) {
            filteredList = filteredList.stream()
                    .filter(a -> a.getStatus() == filterStatus)
                    .collect(Collectors.toList());
        }

        masterAppointmentList = FXCollections.observableArrayList(filteredList);
        tblAppointments.setItems(masterAppointmentList);
    }

    @FXML
    private void handleRefreshList() {
        String selectedStatus = cmbStatusFilter.getValue();
        loadAppointments(selectedStatus.equals("ALL") ? null : AppointmentStatus.valueOf(selectedStatus));
    }

    @FXML
    private void handleNewAppointment(ActionEvent event) {
        // Navigate to the Appointment Scheduler screen
        try {
            Parent schedulerView = FXMLLoader.load(getClass().getResource("/Views/Patient/AddAppointment.fxml"));
            Scene scene = new Scene(schedulerView);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle("Schedule New Appointment");
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error loading FXML
        }
    }
}