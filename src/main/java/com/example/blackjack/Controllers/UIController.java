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
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UIController implements Initializable, UIHandler {

    private static final String HIDDEN_CARD_PATH = "assets/SVG-cards/red2.svg";
    private static final int CARD_WIDTH = 100;
    private static final int CARD_HEIGHT = 140;
    private static final double PLAYER_BALANCE = 5000;
    private Player player;
    private List<Player> playerList = new ArrayList<>();
    private Dealer dealer;
    private GameState currentState;
    private MainController game;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentState = new NotPlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);

        player = new Player(PLAYER_BALANCE);
        playerCardContainer.getChildren().add(player.getCardContainer());
        playerList.add(player);

        dealer = Dealer.getInstance();
        dealer.setCardContainer(dealerCardContainer);

        game = new MainController(player, dealer, playerList, this);

    }

    @Override
    public void resetUI(){
        currentState = new NotPlayingState();
        currentState.toggleButtons(buttonContainer, controllsContainer);

        balanceField.setText(String.valueOf(player.getBalance()));
        betField.setText(String.valueOf(player.getBet()));
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

    private void updateBetAndBalanceFields() {
        betField.setText("Bet: " + player.getBet());
        balanceField.setText("Balance: " + player.getBalance());
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

        displayHandAndScore(player, dealer);
    }

    public static void displayHandAndScore(Participant... participants){
        for (Participant participant : participants){
            displayHand(participant);
            displayScore(participant);
        }
    }

    public static void displayHand(Participant participant) {
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

    public static byte[] convertSvgToPng(String svgFilePath) throws Exception {
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

    private static void displayScore(Participant participant) {
        int handValue = participant.getScore();
        participant.getScoreText().setText(String.valueOf(handValue));
    }

    public void handleHit() {
        doubleButton.setVisible(false);
        game.hit(player);

        for (Player player : playerList){
            System.out.println(player.getCardContainer());
            displayHandAndScore(player);
        }
    }

    public void handleStand() {
        game.stand();
        doubleButton.setVisible(false);

        displayHandAndScore(player, dealer);
    }

    public void handleSplit(ActionEvent actionEvent) {
        splitButton.setVisible(false);
        doubleButton.setVisible(false);

        game.split(playerCardContainer);

        for (Player player : playerList){
            displayHandAndScore(player);
        }
    }

    public void handleDouble() {
        game.doubleDown();
        updateBetAndBalanceFields();
        displayHandAndScore(player, dealer);
    }

}
