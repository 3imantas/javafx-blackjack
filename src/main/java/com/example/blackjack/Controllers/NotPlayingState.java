package com.example.blackjack.Controllers;

import javafx.scene.layout.AnchorPane;

class NotPlayingState implements GameState {
    @Override
    public void toggleButtons(AnchorPane buttonContainer, AnchorPane controllsContainer) {
        buttonContainer.setVisible(true);
        controllsContainer.setVisible(false);
    }
}
