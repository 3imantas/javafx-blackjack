package com.example.blackjack.Models;

import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
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
//                String imageUrl = "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\" + value + "_of_" + suit + ".svg";
//                Image image = new Image(imageUrl);
//                Card card = new Card(suit, value, image);
//                deck.add(card);

                try {
                    String imagePath = "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\SVG-cards\\" + value + "_of_" + suit + ".svg";
                    File file = new File(imagePath);
                    String imageUrl = file.toURI().toURL().toExternalForm();

                    Image image = new Image(imageUrl);
                    Card card = new Card(suit, value, imagePath);
                    deck.add(card);
                } catch (MalformedURLException e) {
                    // Handle the exception, e.g., log an error or take appropriate action.
                    e.printStackTrace();
                }
            }
        }
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
