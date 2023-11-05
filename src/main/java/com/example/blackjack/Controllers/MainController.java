package com.example.blackjack.Controllers;

import com.example.blackjack.Models.Card;
import com.example.blackjack.Models.Deck;
import com.example.blackjack.Models.Game;
import com.example.blackjack.Models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;



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


    private Game game;
    private Player player;

    private Deck deck;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        showButtons();

        player = new Player(10000);
        balanceField.setText("Balance: " + player.getBalance());
        game = new Game();
        deck = new Deck();

    }

    @FXML
    public void bet(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        int betAmount = Integer.parseInt(clickedButton.getText());

        game.setBet(game.getBet() + betAmount);
        player.setBalance(player.getBalance() - betAmount);

        betField.setText("Bet: " + game.getBet());
        balanceField.setText("Balance: " + player.getBalance());
    }

    public void clearBet(ActionEvent actionEvent) {
        balanceField.setText(String.valueOf(player.getBalance() + game.getBet()));
        game.setBet(0);
        betField.setText("Bet: " + game.getBet());
    }

    public void startGame(ActionEvent actionEvent) {
        hideButtons();

        List<Card> givenHand = new ArrayList<> ();;
        givenHand.add(deck.dealCard());
        givenHand.add(deck.dealCard());
        givenHand.add(deck.dealCard());
        givenHand.add(deck.dealCard());
        givenHand.add(deck.dealCard());

        player.setHand(givenHand);
        updatePlayerHand();


        List<Card> cards = player.getHand();
        for(Card card : cards){
            System.out.println(card.getValue());
            System.out.println(card.getSuit());
            System.out.println(card.getImage());
        }
    }

    @FXML
    public void updatePlayerHand() {
        playerCardContainer.getChildren().clear(); // Clear existing ImageViews

        List<Card> hand = player.getHand();

        for (Card card : hand) {

//            Image cardImage = card.getImage();
//            ImageView cardImageView = new ImageView(cardImage);
//            cardImageView.setFitWidth(100);
//            cardImageView.setFitHeight(145);
//            playerCardContainer.getChildren().add(cardImageView);

            try {
                WebView webView = new WebView();
                String svgPath = card.getImage();
                String content = new String(Files.readAllBytes(Paths.get(svgPath)));

                String htmlContent = "<html><head><style>svg { width: " + 120 + "px; height: " + 150 + "px; }</style></head><body>" + content + "</body></html>";
                webView.getEngine().loadContent(htmlContent);

                playerCardContainer.getChildren().add(webView);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void hideButtons(){
        buttonContainer.setVisible(false);
    }

    private void showButtons(){
        buttonContainer.setVisible(true);
    }
}
