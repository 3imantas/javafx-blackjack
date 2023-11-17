package com.example.blackjack.Models;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Player extends Participant{
    private double balance;
    private double bet;
    private double won;


    public Player(double balance) {
        super();
        this.balance = balance;
    }

    @Override
    public void reset(){
        hand = new ArrayList<>();
        cardContainer.getChildren().clear();
        score = 0;
        bet = 0;
        won = 0;
    }

    public void bet(double betAmount){
        if(balance < betAmount){
            return;
        }

        bet += betAmount;
        balance -= betAmount;
    }


    public void clearBet(){
        balance += bet;
        bet = 0;
    }

    public boolean cardsAreEqual(){

        if(hand.isEmpty()){
            return false;
        }
        return hand.get(0).getValue().equals(hand.get(1).getValue());
    }

}
