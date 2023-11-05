package com.example.blackjack.Models;

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

    public Player() {
    }

}
