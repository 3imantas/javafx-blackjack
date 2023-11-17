package com.example.blackjack.Controllers;
import com.example.blackjack.Models.*;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import java.util.*;

public class MainController {


    private Player player;
    private List<Player> playerList = new ArrayList<>();
    private Dealer dealer;
    private Deck deck;
    private int nextPlayerIndex;
    private UIHandler uiHandler;

    public MainController(Player player, Dealer dealer, List<Player> playerList, UIHandler uiHandler){
        this.player = player;
        this.dealer = dealer;
        this.playerList = playerList;
        deck = new Deck();
        nextPlayerIndex = 1;
        this.uiHandler = uiHandler;
    }
    public MainController(){}



    public void resetGame(){
        deck = new Deck();
        dealer.reset();

        for (Player player : playerList){
            player.reset();
        }

        playerList.subList(1, playerList.size()).clear();
        player = playerList.get(0);
        nextPlayerIndex = 1;

        uiHandler.resetUI();
    }

    public void startGame() {
        hit(dealer);
        dealer.hideCard();
        hit(dealer);

        String suit = "hearts";
        String value = "3";
        String img = "assets/SVG-cards/" + value + "_of_" + suit + ".svg";
        Card card = new Card(suit, value, img, false);
        player.addCard(card);

        suit = "clubs";
        value = "3";
        img = "assets/SVG-cards/" + value + "_of_" + suit + ".svg";
        card = new Card(suit, value, img, false);
        player.addCard(card);

        countHandScore(player);

        //hit(player);
        //hit(player);
    }

    public boolean is21orMore() {
        return player.getScore() >= 21;
    }

    public void dealerTurn(int playerScore) {
        if (playerScore > 21 || dealer.getScore() >= 21) {
            return;
        }

        while (dealer.getScore() <= playerScore || dealer.getScore() < 21) {
            if (dealer.getScore() == playerScore && dealer.getScore() >= 15) {
                break;
            }
            hit(dealer);
        }
    }

    public void checkResult(Player player) {
        if (player.getScore() > 21) {
            player.setWon(0);
        } else if (dealer.getScore() > 21) {
            player.setWon(player.getBet() * 2);
        } else if (dealer.getScore() == player.getScore()) {
            player.setWon(player.getBet());
        } else if (dealer.getScore() > player.getScore()) {
            player.setWon(0);
        } else {
            player.setWon(player.getBet() * 2);
        }
    }

    public void hit(Participant participant) {
        Card dealtCard = deck.dealCard();
        participant.addCard(dealtCard);

        countHandScore(participant);

        if (is21orMore() && !(participant instanceof Dealer)) {
            stand();
        }
    }

    public void stand() {
        if(player.getHand().isEmpty()){
            return;
        }

        if (isMorePlayersAvailable()) {
            iteratePlayer();
        } else {

            dealer.revealCard();
            dealerTurn(findLowestScore());
            playerList.forEach(this::checkResult);
            countTotalWinnings();

            UIController.displayHandAndScore(player, dealer);
            Result.getResult(player);
            resetGame();
        }
    }

    public boolean isMorePlayersAvailable(){
        return playerList.size() > nextPlayerIndex;
    }

    public void iteratePlayer(){
        player = playerList.get(nextPlayerIndex);
        nextPlayerIndex++;
    }

    public int findLowestScore(){
        int lowestScore = Integer.MAX_VALUE;

        for (Player player : playerList) {
            int playerScore = player.getScore();

            if (playerScore < lowestScore) {
                lowestScore = playerScore;
            }
        }

        return lowestScore;
    }

    public void countHandScore(Participant participant){
        List<Card> hand = participant.getHand();
        int handValue = Game.countHandValue(hand);
        participant.setScore(handValue);
    }

    public void countTotalWinnings(){
        double initialBet = playerList.stream().mapToDouble(Player::getBet).sum();
        double amountWon = playerList.stream().mapToDouble(Player::getWon).sum();

        player = playerList.get(0);
        player.setBet(initialBet);
        player.setWon(amountWon);
    }

    public void split(HBox playerCardContainer) {
        Card leftCard = player.getHand().get(0);
        Card rightCard = player.getHand().get(1);
        double splitBet = player.getBet() / 2.0;

        Player player2 = new Player(0);
        player2.setHand(new ArrayList<>(Arrays.asList(rightCard)));
        player2.setBet(splitBet);
        playerCardContainer.getChildren().add(player2.getCardContainer());
        playerList.add(player2);

        System.out.println(player2.getCardContainer());

        player.setHand(new ArrayList<>(Arrays.asList(leftCard)));
        player.setBet(splitBet);
    }

    public void doubleDown() {
        double initialBet = player.getBet();
        player.bet(initialBet);

        hit(player);
        stand();
    }
}
