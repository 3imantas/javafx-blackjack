package com.example.blackjack.Models;
import com.example.blackjack.Controllers.UIHandler;
import javafx.scene.layout.HBox;

import java.util.*;

public class GameLogic {

    private static int TARGET_SCORE = 21;
    private static int DEALER_STOP_THRESHOLD = 15;
    private Player player;
    private Dealer dealer;
    private Deck deck;
    private UIHandler uiHandler;

    public GameLogic(Player player, UIHandler uiHandler){
        this.player = player;
        this.uiHandler = uiHandler;
        deck = new Deck();
        dealer = Dealer.getInstance();
    }

    public void resetGame(){
        deck = new Deck();
        dealer.reset();
        player.reset();

        uiHandler.resetUI();
    }

    public void startGame() {
        hit(dealer);
        dealer.hideCard();
        hit(dealer);

        hit(player);
        hit(player);

    }

    public void hit(Participant participant) {
        Card dealtCard = deck.dealCard();
        participant.getHand().addCard(dealtCard);

        updateAndDisplayParticipantInfo(participant);

        if (!player.getHasStood() && is21orMore(player)) {
            player.setHasStood(true);
            stand();
        }
    }

    public void stand() {
        if (player.isMoreHandsAvailable()) {
            player.iterateHand();
        } else {
            dealer.revealCard();
            updateAndDisplayParticipantInfo(dealer);
            dealerTurn(player.findLowestScore());
            checkResult();
            Result.showResult(player);
            resetGame();
        }
    }


    public void split(HBox playerCardContainer) {
        Card leftCard = player.getHand().getCards().get(0);
        Card rightCard = player.getHand().getCards().get(1);

        player.getHand().setCards(new ArrayList<>(Arrays.asList(leftCard)));

        Hand rightHand = new Hand();
        rightHand.addCard(rightCard);
        playerCardContainer.getChildren().add(rightHand.getHandContainer());

        player.getHands().add(rightHand);

        updateAndDisplayParticipantInfo(player);
    }

    public void doubleDown() {
        double initialBet = player.getBet();
        player.bet(initialBet);

        hit(player);
        if(!player.getHasStood()){
            stand();
        }
    }
    public void dealerTurn(int handScore) {
        if (handScore > TARGET_SCORE || is21orMore(dealer)) {
            return;
        }

        while (dealer.getHand().getScore() <= handScore) {
            if (dealer.getHand().getScore() == handScore && dealer.getHand().getScore() >= DEALER_STOP_THRESHOLD) {
                break;
            }
            hit(dealer);
        }
    }
    public boolean is21orMore(Participant participant) {
        return participant.getHand().getScore() >= TARGET_SCORE;
    }

    public void checkResult() {
        List<Hand> totalHands = player.getHands();
        int dealerScore = dealer.getHand().getScore();
        double playerBet = player.getBet() / totalHands.size();
        double amountWon = 0;

        for (Hand hand : totalHands) {
            int handScore = hand.getScore();

            if (handScore > TARGET_SCORE) {
                amountWon += 0;
            } else if (dealerScore > TARGET_SCORE || handScore > dealerScore) {
                amountWon += playerBet * 2;
            } else if (handScore == dealerScore) {
                amountWon += playerBet;
            }
        }

        player.setWon(amountWon);
    }

    public void updateAndDisplayParticipantInfo(Participant participant){
        ValueCounter.countHandsValues(participant);
        uiHandler.displayHandAndScore(participant);
    }
}
