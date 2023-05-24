package hr.java.musicshop.controller;

import hr.java.musicshop.sucelja.LoadFile;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelloApplication extends Application implements LoadFile {

    public static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {
        mainStage = stage;
        startDogadaj();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getMainStage(){
        return mainStage;
    }

    private static String stageName;
    private static String stageTitle;

    public static void showStage(String name, String title, double width, double height) throws IOException {
        stageName = name;
        stageTitle = title;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(name));
        new FadeTransition().play();
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        HelloApplication.getMainStage().setTitle(title);
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public static String getStageName(){
        return stageName;
    }

    public static String getStageTitle(){
        return stageTitle;
    }
}