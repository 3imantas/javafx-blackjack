package com.example.blackjack.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> deck;

    public Deck() {
        deck = new ArrayList<>();

        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"};

        for (String suit : suits) {
            for (String value : values) {
                try {
                    String imagePath = generateImagePath(value, suit);
                    Card card = new Card(suit, value, imagePath, false);
                    deck.add(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        shuffle();
    }

    private String generateImagePath(String value, String suit) {
        return "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\SVG-cards\\" + value + "_of_" + suit + ".svg";
    }

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Card dealCard() {
        Random random = new Random();

        int index = random.nextInt(deck.size());
        Card card = deck.remove(index);

        return card;
    }
}
