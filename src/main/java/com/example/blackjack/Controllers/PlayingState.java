package com.example.blackjack.Controllers;

import javafx.scene.layout.AnchorPane;

class PlayingState implements GameState {
    @Override
    public void toggleButtons(AnchorPane buttonContainer, AnchorPane controllsContainer) {
        buttonContainer.setVisible(false);
        controllsContainer.setVisible(true);
    }
}
