package com.example.blackjack.Controllers;

import javafx.scene.layout.AnchorPane;

interface GameState {
    void toggleButtons(AnchorPane buttonContainer, AnchorPane controllsContainer);
}
