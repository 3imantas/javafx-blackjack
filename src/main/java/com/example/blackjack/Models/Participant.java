package com.example.blackjack.Models;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
public abstract class Participant {

    protected List<Card> hand;
    protected int score;
    protected HBox cardContainer;
    protected Text scoreText;

    public Participant(){
        hand = new ArrayList<>();
        cardContainer = new HBox();
        scoreText = new Text();
    }

    public void addCard(Card dealtCard) {
        hand.add(dealtCard);
    }

    abstract public void reset();
}
