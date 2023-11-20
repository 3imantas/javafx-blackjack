package com.example.blackjack.Models;

import javafx.scene.control.Alert;
public class Result {
    public static void resultAlert(String title, String text, double won)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(text + won);
        alert.showAndWait();
    }

    public static void showResult(Player player) {
        double won = player.getWon();
        if(player.getWon() == player.getBet()){
            resultAlert("Draw!", "You got back: ", won);
        } else if (player.getWon() > player.getBet()) {
            resultAlert("Player won!", "You Won: ", won);
        } else{
            won = player.getBet();
            resultAlert("Dealer won!", "You lost: ", won);
        }

        player.setBalance(player.getBalance() + player.getWon());

    }
}