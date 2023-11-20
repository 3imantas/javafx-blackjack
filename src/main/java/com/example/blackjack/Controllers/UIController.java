package com.example.blackjack.Controllers;

import com.example.blackjack.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UIController implements Initializable, UIHandler {
    private static final String HIDDEN_CARD_PATH = "assets/SVG-cards/red2.svg";
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;
    private Player player;
    private Dealer dealer;
    private GameState currentState;
    private GameLogic game;

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
    public AnchorPane controllsContainer;
    @FXML
    public Button splitButton;
    @FXML
    public Button doubleButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentState = new NotPlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);

        player = new Player();
        playerCardContainer.getChildren().add(player.getHand().getHandContainer());

        dealer = Dealer.getInstance();
        dealerCardContainer.getChildren().add(dealer.getHand().getHandContainer());

        game = new GameLogic(player, this);

        updateBetAndBalanceFields();
    }

    @Override
    public void resetUI(){
        currentState = new NotPlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);

        updateBetAndBalanceFields();

        doubleButton.setVisible(true);
        splitButton.setVisible(false);
    }

    @FXML
    public void bet(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        double betAmount = Integer.parseInt(clickedButton.getText());
        player.bet(betAmount);

        updateBetAndBalanceFields();
    }

    public void clearBet(ActionEvent actionEvent) {
        player.clearBet();
        updateBetAndBalanceFields();
    }

    @FXML
    public void handlePlay(){

        if (player.getBet() == 0) {
            return;
        }

        currentState = new PlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);

        doubleButton.setVisible(true);
        game.startGame();


        if(player.cardsAreEqual()){
            splitButton.setVisible(true);
        }
    }


    public void handleHit() {
        hideSplitAndDouble();
        game.hit(player);
    }

    public void handleStand() {
        hideSplitAndDouble();
        game.stand();
    }

    public void handleSplit(ActionEvent actionEvent) {
        hideSplitAndDouble();
        game.split(playerCardContainer);
    }

    public void handleDouble() {
        hideSplitAndDouble();
        game.doubleDown();
        updateBetAndBalanceFields();
    }

    @Override
    public void displayHandAndScore(Participant... participants){
        for (Participant participant : participants){
            displayHand(participant);
            displayScore(participant);
        }
    }

    public static void displayHand(Participant participant) {
        try {
            List<Hand> totalHands = participant.getHands();
            for (Hand hand : totalHands) {

                HBox cardContainer = hand.getCardContainer();
                cardContainer.getChildren().clear();

                List<Card> cards = hand.getCards();

                for (Card card : cards) {
                    String svgPath = card.isHidden() ? HIDDEN_CARD_PATH : card.getImage();
                    byte[] cardByteImage = ImageConverter.convertSvgToPng(svgPath, CARD_WIDTH, CARD_HEIGHT);
                    Image cardImage = new Image(new ByteArrayInputStream(cardByteImage));
                    ImageView cardImageView = new ImageView(cardImage);

                    cardContainer.getChildren().add(cardImageView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void displayScore(Participant participant) {
        List<Hand> totalHands = participant.getHands();
        for (Hand hand : totalHands) {
            int handValue = hand.getScore();
            Text scoreText = hand.getScoreText();
            scoreText.setText(String.valueOf(handValue));
        }
    }
    private void updateBetAndBalanceFields() {
        betField.setText("Bet: " + player.getBet());
        balanceField.setText("Balance: " + player.getBalance());
    }
    private void hideSplitAndDouble(){
        splitButton.setVisible(false);
        doubleButton.setVisible(false);
    }

}
