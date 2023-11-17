package com.example.blackjack;

import com.example.blackjack.Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGui extends Application {
    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 500;

    public static final String TITLE = "Blackjack";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main.fxml"));
        Scene scene = new  Scene(fxmlLoader.load(),SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}