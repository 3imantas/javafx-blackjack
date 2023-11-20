package com.example.blackjack.Models;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
        changeContainerOrder();
    }

    private void changeContainerOrder(){
        VBox handContainer = getHand().getHandContainer();
        HBox cardContainer = getHand().getCardContainer();
        Text scoreText = getHand().getScoreText();
        handContainer.getChildren().setAll(cardContainer, scoreText);

    }

    public static Dealer getInstance(){
        if(instance == null){
            instance = new Dealer();
        }
        return instance;
    }
    public void hideCard(){
        //hand.get(0).setHidden(true);
        this.getHand().getCards().get(0).setHidden(true);
    }
    public void revealCard(){
        //hand.get(0).setHidden(false);
        this.getHand().getCards().get(0).setHidden(false);
    }

    @Override
    public void reset(){
        getHand().getCardContainer().getChildren().clear();
        getHand().getScoreText().setText("");
        getHand().getCards().clear();
        getHand().setScore(0);
    }
}
