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

    private static final double INITIAL_BALANCE = 5000;
    private final static double PREVIOUS_HAND_OPACITY = 0.5;
    private boolean hasStood = false;
    private double balance;
    private double bet;
    private double won;

    public Player() {
        super();
        this.balance = INITIAL_BALANCE;
    }

    public boolean getHasStood(){
        return hasStood;
    }

    public void setHasStood(boolean condition){
        hasStood = condition;
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

        if (this.getHand().getCards().isEmpty()) {
            return false;
        }

        String cardValue1 = this.getHand().getCards().get(0).getValue();
        String cardValue2 = this.getHand().getCards().get(1).getValue();

        return cardValue1.equals(cardValue2);
    }

    public boolean isMoreHandsAvailable(){
        return hands.size() > currentHandIndex + 1;
    }

    public void iterateHand(){
        getHand().getHandContainer().setOpacity(PREVIOUS_HAND_OPACITY);
        currentHandIndex++;
    }

    public int findLowestScore(){
        int lowestScore = hands.stream()
                .mapToInt(Hand::getScore)
                .min()
                .orElse(Integer.MAX_VALUE);
        return lowestScore;
    }

    @Override
    public void reset(){
        currentHandIndex = 0;
        for(Hand hand : hands){
            hand.getHandContainer().setOpacity(1);
            hand.getCardContainer().getChildren().clear();
            hand.getScoreText().setText("");
            hand.getCards().clear();
            hand.setScore(0);
        }
        hands.subList(1, hands.size()).clear();
        bet = 0;
        won = 0;
        hasStood = false;
    }

}
