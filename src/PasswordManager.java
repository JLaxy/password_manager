import java.io.IOException;

import controllers.MainViewController;
import helpers.JSONManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PasswordManager extends Application {

    @Override
    public void start(Stage mainStage) throws IOException {
        // Intializing
        JSONManager appDataManager = new JSONManager();
        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("views/MainView.fxml"));
        Parent root = rootLoader.load();
        Scene scene = new Scene(root);

        // Retrieving controller
        MainViewController controller = rootLoader.getController();
        // Passing references
        controller.initReferences(appDataManager);

        mainStage.setTitle("Password Manager");
        mainStage.setScene(scene);
        mainStage.show();
    }
}
