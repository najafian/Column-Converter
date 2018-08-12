package douran;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("DouranUI.fxml"));
        primaryStage.setTitle("Attachment Converter");
        primaryStage.setFullScreen(false);
        primaryStage.setIconified(false);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 435, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
