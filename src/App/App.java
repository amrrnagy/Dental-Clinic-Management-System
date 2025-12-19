package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Dashboards/LoginView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        // Set window icon
       // Image icon = new Image("file:D:\\Amr\\Desktop\\tooth.png");
      //  stage.getIcons().add(icon);

      //  stage.getIcons().add(icon);
        stage.setTitle("The Clinic");
       stage.show();
    }

    @SuppressWarnings({"unused", "UnnecessaryModifier"})
    public static void main(String[] args) {
        launch(args);
    }
}
