package com.example.blackjack.Controllers;

import com.example.blackjack.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;



public class MainController implements Initializable {
    @FXML
    public Text balanceField;
    @FXML
    public Text betField;
    @FXML
    public AnchorPane buttonContainer;
    @FXML
    public HBox playerCardContainer;
    @FXML
    public ImageView card1;
    @FXML
    public HBox dealerCardContainer;
    @FXML
    public Text dealerScore;
    @FXML
    public Text playerScore;
    public AnchorPane controllsContainer;
    @FXML
    public Button standButton;


    private Game game;
    private Player player;
    private Player dealer;

    private Deck deck;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        player = new Player(5000);
        dealer = new Dealer();
        game = new Game();

        mainView();

    }

    @FXML
    public void bet(ActionEvent actionEvent) {

        if(player.getBalance() <= 0){
            return;
        }

        Button clickedButton = (Button) actionEvent.getSource();
        int betAmount = Integer.parseInt(clickedButton.getText());

        game.setBet(game.getBet() + betAmount);
        player.setBalance(player.getBalance() - betAmount);

        betField.setText("Bet: " + game.getBet());
        balanceField.setText("Balance: " + player.getBalance());

    }

    public void clearBet(ActionEvent actionEvent) {

        player.setBalance(player.getBalance() + game.getBet());
        balanceField.setText(String.valueOf(player.getBalance()));

        game.setBet(0);
        betField.setText("Bet: " + game.getBet());

        betField.setText("Bet: " + game.getBet());
        balanceField.setText("Balance: " + player.getBalance());
    }

    public void mainView(){
        buttonContainer.setVisible(true);
        controllsContainer.setVisible(false);

        playerCardContainer.getChildren().clear();
        dealerCardContainer.getChildren().clear();

        game.setBet(0);
        betField.setText("Bet: " + game.getBet());

        dealer.setHand(new ArrayList<>());
        dealer.setScore(0);
        player.setHand(new ArrayList<>());
        player.setScore(0);

        deck = new Deck();

        balanceField.setText("Balance: " + player.getBalance());
    }

    public void startGame(ActionEvent actionEvent) {

        if(game.getBet() == 0){
            return;
        }

        buttonContainer.setVisible(false);
        controllsContainer.setVisible(true);

        dealOneCard(player, false);
        dealOneCard(player, false);
        dealOneCard(dealer, true);
        dealOneCard(dealer, false);

        checkScore();

    }


    @FXML
    public void updatePlayerHand(Player participant, Card dealtCard){
        try {

            String svgPath;

            if(dealtCard.isHidden()){
                svgPath = "C:\\Users\\Eimantas\\Desktop\\Java practice\\blackjack\\assets\\SVG-cards\\red2.svg";
            }
            else{
                svgPath = dealtCard.getImage();
            }
            byte[] cardByteImage = convertSvgToPng(svgPath, 100, 140);
            Image cardImage = new Image(new ByteArrayInputStream(cardByteImage));
            ImageView cardImageView = new ImageView(cardImage);

            if(participant instanceof Dealer)
            {
                dealerCardContainer.getChildren().add(cardImageView);
            }
            else{
                playerCardContainer.getChildren().add(cardImageView);
            }

        }
        catch (Exception e){
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

    public void dealOneCard(Player participant, boolean isHiddenCard) {
        Card dealtCard = deck.dealCard();
        if(isHiddenCard){
            dealtCard.setHidden(true);
        }
        participant.addCard(dealtCard);

        updatePlayerHand(participant, dealtCard);
        updateScore(participant);
    }

    private void updateScore(Player participant) {

        List<Card> hand = participant.getHand();

        int handValue = game.countHandValue(hand);

        participant.setScore(handValue);

        if(participant instanceof Dealer)
        {
            dealerScore.setText(String.valueOf(participant.getScore()));
        }
        else{
            playerScore.setText(String.valueOf(participant.getScore()));
        }
    }

    public void revealDealerHand() {
        List<Card> hand = dealer.getHand();
        dealerCardContainer.getChildren().clear();

        for (Card card : hand)
        {
            card.setHidden(false);
            updateScore(dealer);
            updatePlayerHand(dealer, card);
        }
    }

    public void checkScore(){
        if(player.getScore() > 21){
            revealDealerHand();

            System.out.println("Dealer won");
            Result.dealerWon();
            mainView();
        }

        if(player.getScore() == 21){
            revealDealerHand();
            if(dealer.getScore() == 21){
                System.out.println("Draw");
                player.setBalance(player.getBalance() + game.getBet());
                Result.draw();
            }
            else{
                System.out.println("You won");
                player.setBalance(player.getBalance() + 2*game.getBet());
                Result.playerWon();
            }
            mainView();
        }
    }

    public void dealerPlay(){

        if(dealer.getScore() > player.getScore()){
            System.out.println("Dealer won");
            Result.dealerWon();
        }
        else{
            while (dealer.getScore() <= player.getScore()){
                if(dealer.getScore() == player.getScore() && dealer.getScore() == 16){
                    System.out.println("Draw");
                    player.setBalance(player.getBalance() + game.getBet());
                    Result.draw();
                    break;
                }
                dealOneCard(dealer, false);
            }

            if(dealer.getScore() > 21){
                System.out.println("You won");
                player.setBalance(player.getBalance() + 2*game.getBet());
                Result.playerWon();
            }
            else{
                System.out.println("Dealer won");
                Result.dealerWon();
            }
        }
        mainView();

    }

    public void hit(ActionEvent actionEvent) {
        dealOneCard(player, false);
        checkScore();
    }

    public void stand(ActionEvent actionEvent) {
        revealDealerHand();
        dealerPlay();
    }

    public void split(ActionEvent actionEvent) {
    }
}
