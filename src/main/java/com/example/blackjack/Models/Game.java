package com.example.blackjack.Models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Game {

    private static final Map<String, Integer> CARD_VALUES = new HashMap<>();

    static {
        CARD_VALUES.put("ace", 1);
        CARD_VALUES.put("king", 10);
        CARD_VALUES.put("queen", 10);
        CARD_VALUES.put("jack", 10);
        CARD_VALUES.put("10", 10);
        CARD_VALUES.put("9", 9);
        CARD_VALUES.put("8", 8);
        CARD_VALUES.put("7", 7);
        CARD_VALUES.put("6", 6);
        CARD_VALUES.put("5", 5);
        CARD_VALUES.put("4", 4);
        CARD_VALUES.put("3", 3);
        CARD_VALUES.put("2", 2);
    }

    public static int countCardValue(Card dealtCard) {
        String cardValue = dealtCard.getValue();
        return CARD_VALUES.get(cardValue);
    }

    public static int countHandValue(List<Card> hand) {
        int handValue = 0;
        int numAces = 0;

        for (Card card : hand) {
            if (!card.isHidden()) {
                int cardValue = countCardValue(card);
                if (cardValue == 1) {
                    numAces++;
                    handValue += 11;
                } else {
                    handValue += cardValue;
                }
            }
        }

        while (numAces > 0 && handValue > 21) {
            handValue -= 10;
            numAces--;
        }

        return handValue;
    }
}
