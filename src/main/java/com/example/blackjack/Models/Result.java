package com.example.blackjack.Models;

import javafx.scene.control.Alert;
public class Result {

    public static void dealerWon(Player player)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dealer won!");
        alert.setHeaderText("");
        alert.setContentText("You lost: " + (player.getBet() - player.getWon()));
        alert.showAndWait();
    }

    public static void playerWon(Player player)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Player won!");
        alert.setHeaderText("");
        alert.setContentText("You Won: " + player.getWon());
        alert.showAndWait();
    }

    public static void draw(Player player)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Draw!");
        alert.setHeaderText("");
        alert.setContentText("You got back: " + player.getWon());
        alert.showAndWait();
    }

    public static void getResult(Player player) {
        if(player.getWon() == player.getBet()){
            draw(player);
        } else if (player.getWon() > player.getBet()) {
            playerWon(player);
        }
        else{
            dealerWon(player);
        }

        player.setBalance(player.getBalance() + player.getWon());

    }
}