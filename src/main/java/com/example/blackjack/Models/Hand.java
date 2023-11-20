package com.example.blackjack.Models;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Hand {
    private List<Card> cards;
    private int score;
    private VBox handContainer;
    private HBox cardContainer;
    private Text scoreText;

    public Hand(){
        cards = new ArrayList<>();
        score = 0;
        handContainer = new VBox();
        handContainer.setAlignment(Pos.CENTER);
        cardContainer = new HBox();
        scoreText = new Text();
        handContainer.getChildren().add(scoreText);
        handContainer.getChildren().add(cardContainer);
    }


    public void addCard(Card dealtCard) {
        cards.add(dealtCard);
    }
}
