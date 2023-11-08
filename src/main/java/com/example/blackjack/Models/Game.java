package com.example.blackjack.Models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Game {
    private int bet;

    public int countCardValue(Card dealtCard) {
        String cardValue = dealtCard.getValue();
        int numericValue;

        switch (cardValue) {
            case "ace":
                numericValue = 1;
                break;
            case "king":
            case "queen":
            case "jack":
                numericValue = 10;
                break;
            default:
                numericValue = Integer.parseInt(cardValue);
                break;
        }

        return numericValue;
    }

    public int countHandValue(List<Card> hand) {
        int handValue = 0;
        int numAces = 0;

        for (Card card : hand) {

            if(card.isHidden()){
                handValue+=0;
            }
            else {
                int cardValue = countCardValue(card);
                if (cardValue == 1) { // If the card is an Ace
                    numAces++;
                    handValue += 11; // Assume the Ace has a value of 11 by default
                } else {
                    handValue += cardValue;
                }
            }
        }

        // Check if the hand value needs to consider Aces as 1 instead of 11 to avoid busting
        while (numAces > 0 && handValue > 21) {
            handValue -= 10; // Convert the value of one Ace from 11 to 1
            numAces--;
        }

        return handValue;
    }

}
