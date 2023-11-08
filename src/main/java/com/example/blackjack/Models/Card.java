package com.example.blackjack.Models;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Card {
    private String suit;
    private String value;
    private String image;
    private boolean isHidden;
}
