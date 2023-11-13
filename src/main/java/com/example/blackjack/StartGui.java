package com.example.blackjack;

import com.example.blackjack.Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGui extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main.fxml"));
        Scene scene = new  Scene(fxmlLoader.load(), MainController.SCREEN_WIDTH, MainController.SCREEN_HEIGHT);
        stage.setTitle(MainController.TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}