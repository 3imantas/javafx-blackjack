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

    private static final String HIDDEN_CARD_PATH = "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\SVG-cards\\red2.svg";
    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 500;

    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;
    public static final String TITLE = "Blackjack";

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player = new Player(PLAYER_BALANCE);
        playerCardContainer.getChildren().add(player.getCardContainer());

        dealer = new Dealer();
        dealer.setCardContainer(dealerCardContainer);
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
        buttonContainer.setVisible(true);
        controllsContainer.setVisible(false);

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

        buttonContainer.setVisible(false);
        controllsContainer.setVisible(true);
        doubleButton.setVisible(true);


        dealOneCard(player);
        dealOneCard(player);
        displayHandAndScore(player);

        dealOneCard(dealer);
        dealOneCard(dealer);
        dealer.hideCard();
        displayHandAndScore(dealer);

//        String value = "7";
//        String suit = "hearts";
//        String imagePath = "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\SVG-cards\\" + value + "_of_" + suit + ".svg";
//        Card card = new Card(suit, value, imagePath, false);
//        player.addCard(card);
//
//        value = "7";
//        suit = "clubs";
//        imagePath = "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\SVG-cards\\" + value + "_of_" + suit + ".svg";
//        card = new Card(suit, value, imagePath, false);
//        player.addCard(card);

        List<Card> hand = player.getHand();
        if (hand.get(0).getValue().equals(hand.get(1).getValue())) {
            splitButton.setVisible(true);
        }

        checkScore();
    }

    public void displayHandAndScore(Participant participant){
        displayHand(participant);
        displayScore(participant);
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
                byte[] cardByteImage = convertSvgToPng(svgPath, CARD_WIDTH, CARD_HEIGHT);
                Image cardImage = new Image(new ByteArrayInputStream(cardByteImage));
                ImageView cardImageView = new ImageView(cardImage);

                participant.getCardContainer().getChildren().add(cardImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] convertSvgToPng(String svgFilePath, int width, int height) throws Exception {
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);

        TranscoderInput input = new TranscoderInput(new FileInputStream(svgFilePath));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TranscoderOutput transcoderOutput = new TranscoderOutput(output);

        transcoder.transcode(input, transcoderOutput);

        byte[] pngImageData = output.toByteArray();
        output.close();

        return pngImageData;
    }

    public void dealOneCard(Participant participant) {
        Card dealtCard = deck.dealCard();
        participant.addCard(dealtCard);
    }

    private void displayScore(Participant participant) {
        List<Card> hand = participant.getHand();
        int handValue = Game.countHandValue(hand);
        participant.setScore(handValue);
        participant.getScoreText().setText(String.valueOf(handValue));
    }

    public void revealDealerHand() {
        List<Card> hand = dealer.getHand();
        for (Card card : hand) {
            card.setHidden(false);
        }
        displayHandAndScore(dealer);
    }

    public void checkScore() {
        if (player.getScore() >= 21) {
            stand();
        }
    }

    public void dealerTurn(int playerScore) {

        if (playerScore > 21) {
            return;
        }

        while (dealer.getScore() <= playerScore) {
            if (dealer.getScore() == playerScore && dealer.getScore() >= 15) {
                break;
            }

            dealOneCard(dealer);
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

    public void hit() {
        doubleButton.setVisible(false);

        dealOneCard(player);
        displayHandAndScore(player);
        checkScore();
    }

    public void stand() {
        if(player.getHand().isEmpty()){
            return;
        }

        doubleButton.setVisible(false);

        if (isMorePlayersAvailable()) {
            iteratePlayer();
        } else {
            revealDealerHand();
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
        splitButton.setVisible(false);
        doubleButton.setVisible(false);

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
        double initialBet = player.getBet();
        player.setBet(initialBet * 2);
        player.setBalance(player.getBalance() - initialBet);

        updateBetAndBalanceFields();

        hit();
        stand();
    }
}
