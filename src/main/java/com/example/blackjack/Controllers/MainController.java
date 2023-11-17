package com.example.blackjack.Controllers;

import com.example.blackjack.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.*;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;


public class MainController implements Initializable {

    private static final String HIDDEN_CARD_PATH = "assets/SVG-cards/red2.svg";
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;
    private static final double PLAYER_BALANCE = 5000;

    @FXML
    public Text balanceField;
    @FXML
    public Text betField;
    @FXML
    public AnchorPane buttonContainer;
    @FXML
    public HBox playerCardContainer;
    @FXML
    public HBox dealerCardContainer;
    @FXML
    public Text dealerScore;
    public AnchorPane controllsContainer;
    @FXML
    public Button splitButton;
    @FXML
    public Button doubleButton;

    private Player player;
    private List<Player> playerList = new ArrayList<>();
    private Dealer dealer;
    private Deck deck;
    private int nextPlayerIndex;

    private GameState currentState;

    public MainController(Deck deck, Player player, Dealer dealer){
        this.deck = deck;
        this.player = player;
        this.dealer = dealer;
    }
    public MainController(){}


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player = new Player(PLAYER_BALANCE);
        playerCardContainer.getChildren().add(player.getCardContainer());

        dealer = Dealer.getInstance();
        dealer.setCardContainer(dealerCardContainer);

        deck = new Deck();

        mainView();
    }

    @FXML
    public void bet(ActionEvent actionEvent) {
        if (player.getBalance() <= 0) {
            return;
        }

        Button clickedButton = (Button) actionEvent.getSource();
        int betAmount = Integer.parseInt(clickedButton.getText());

        player.setBet(player.getBet() + betAmount);
        player.setBalance(player.getBalance() - betAmount);

        updateBetAndBalanceFields();
    }

    public void clearBet(ActionEvent actionEvent) {
        player.setBalance(player.getBalance() + player.getBet());
        player.setBet(0);
        updateBetAndBalanceFields();
    }

    private void updateBetAndBalanceFields() {
        betField.setText("Bet: " + player.getBet());
        balanceField.setText("Balance: " + player.getBalance());
    }

    public void mainView() {
        currentState = new NotPlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);

        resetGame();
        updateBetAndBalanceFields();

        deck = new Deck();
    }

    public void resetGame(){
        dealer.reset();

        for (Player player : playerList){
            player.reset();
        }

        playerList.clear();
        playerList.add(player);
        nextPlayerIndex = 1;
    }

    public void startGame(ActionEvent actionEvent) {
        if (player.getBet() == 0) {
            return;
        }

        currentState = new PlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);


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

        displayHandAndScore(player);

        //hit(player);
        //hit(player);

        doubleButton.setVisible(true);

        if(cardsAreEqual()){
            splitButton.setVisible(true);
        }
    }

    public boolean cardsAreEqual(){
        List<Card> hand = player.getHand();
        return hand.get(0).getValue().equals(hand.get(1).getValue());
    }
    public void displayHandAndScore(Participant... participants){
        for (Participant participant : participants){
            displayHand(participant);
            displayScore(participant);
        }
    }

    @FXML
    public void displayHand(Participant participant) {
        try {
            participant.getCardContainer().getChildren().clear();
            participant.setScoreText(new Text());
            participant.getCardContainer().getChildren().add(participant.getScoreText());

            List<Card> hand = participant.getHand();

            for (Card card : hand) {
                String svgPath = card.isHidden() ? HIDDEN_CARD_PATH : card.getImage();
                byte[] cardByteImage = convertSvgToPng(svgPath);
                Image cardImage = new Image(new ByteArrayInputStream(cardByteImage));
                ImageView cardImageView = new ImageView(cardImage);

                participant.getCardContainer().getChildren().add(cardImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] convertSvgToPng(String svgFilePath) throws Exception {
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) CARD_WIDTH);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) CARD_HEIGHT);

        TranscoderInput input = new TranscoderInput(new FileInputStream(svgFilePath));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TranscoderOutput transcoderOutput = new TranscoderOutput(output);

        transcoder.transcode(input, transcoderOutput);

        byte[] pngImageData = output.toByteArray();
        output.close();

        return pngImageData;
    }

    private void displayScore(Participant participant) {
        List<Card> hand = participant.getHand();
        int handValue = Game.countHandValue(hand);
        participant.setScore(handValue);
        participant.getScoreText().setText(String.valueOf(handValue));
    }

    public boolean is21orMore() {
        return player.getScore() >= 21;
    }

    public void dealerTurn(int playerScore) {

        if (playerScore > 21) {
            return;
        }

        while (dealer.getScore() <= playerScore) {
            if (dealer.getScore() == playerScore && dealer.getScore() >= 15) {
                break;
            }

            hit(dealer);
            displayHandAndScore(dealer);
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
        hideButtons();

        Card dealtCard = deck.dealCard();
        participant.addCard(dealtCard);

        displayHandAndScore(participant);

        if (is21orMore() && participant instanceof Player) {
            stand();
        }
    }

    public void stand() {
        if(player.getHand().isEmpty()){
            return;
        }

        hideButtons();

        if (isMorePlayersAvailable()) {
            iteratePlayer();
        } else {
            dealer.revealCard();
            dealerTurn(findLowestScore());
            playerList.forEach(this::checkResult);
            countTotalWinnings();

            Result.getResult(player);
            mainView();
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

    public void countTotalWinnings(){
        double initialBet = playerList.stream().mapToDouble(Player::getBet).sum();
        double amountWon = playerList.stream().mapToDouble(Player::getWon).sum();

        player = playerList.get(0);
        player.setBet(initialBet);
        player.setWon(amountWon);
    }

    public void split(ActionEvent actionEvent) {
        hideButtons();

        Card leftCard = player.getHand().get(0);
        Card rightCard = player.getHand().get(1);
        double splitBet = player.getBet() / 2.0;

        Player player2 = new Player(0);
        player2.setHand(new ArrayList<>(Arrays.asList(rightCard)));
        player2.setBet(splitBet);
        playerCardContainer.getChildren().add(player2.getCardContainer());
        playerList.add(player2);

        player.setHand(new ArrayList<>(Arrays.asList(leftCard)));
        player.setBet(splitBet);

        playerList.forEach(this::displayHandAndScore);
    }

    public void handleDouble() {
        hideButtons();

        double initialBet = player.getBet();
        player.setBet(initialBet * 2);
        player.setBalance(player.getBalance() - initialBet);

        updateBetAndBalanceFields();

        hit(player);
        stand();
    }

    public void handleHit(ActionEvent actionEvent) {
        hit(player);
    }

    public void hideButtons(){
        splitButton.setVisible(false);
        doubleButton.setVisible(false);
    }
}
