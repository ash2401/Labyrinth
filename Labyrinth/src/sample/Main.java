package sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) throws FileNotFoundException {
        FileSystem.getAllPlayerProfiles();

        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = (Pane) FXMLLoader.load(getClass().getResource("Menu.fxml"));

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
