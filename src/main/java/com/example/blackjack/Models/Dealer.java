package com.example.blackjack.Models;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Dealer extends Participant {

    public Dealer() {
        super();
        System.out.println(hand);
    }
    public void hideCard(){
        hand.get(0).setHidden(true);
    }

    @Override
    public void reset(){
        hand = new ArrayList<>();
        score = 0;
        cardContainer.getChildren().clear();
    }
}
