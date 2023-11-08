package com.example.blackjack.Models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Player {
    private String name;
    @NonNull
    private int balance;
    private List<Card> hand;
    private int score = 0;

    public Player() {
    }


    public void addCard(Card dealtCard) {
        hand.add(dealtCard);
    }

    public void addScore(int cardNumericValue) {
        this.score += cardNumericValue;
    }
}
