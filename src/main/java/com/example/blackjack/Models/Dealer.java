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

    private static Dealer instance;
    private Dealer() {
        super();
    }

    public static Dealer getInstance(){
        if(instance == null){
            instance = new Dealer();
        }
        return instance;
    }
    public void hideCard(){
        hand.get(0).setHidden(true);
    }
    public void revealCard(){
        hand.get(0).setHidden(false);
    }

    @Override
    public void reset(){
        hand = new ArrayList<>();
        score = 0;
        cardContainer.getChildren().clear();
    }
}
