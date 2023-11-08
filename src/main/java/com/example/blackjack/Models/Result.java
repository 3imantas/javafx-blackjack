package com.example.blackjack.Models;

import javafx.scene.control.Alert;
public class Result {

    public static void dealerWon()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Dealer won!");
        alert.showAndWait();


    }

    public static void playerWon()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Player won!");
        alert.showAndWait();
    }

    public static void draw()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Draw!");
        alert.showAndWait();
    }
}