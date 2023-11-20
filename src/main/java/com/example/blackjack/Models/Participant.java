package com.example.blackjack.Models;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
public abstract class Participant {

    List<Hand> hands;
    int currentHandIndex = 0;

    public Participant(){
        hands = new ArrayList<>();
        hands.add(new Hand());
    }

    public Hand getHand(){
        return hands.get(currentHandIndex);
    }

    abstract public void reset();
}
