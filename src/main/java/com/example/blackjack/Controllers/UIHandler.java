package com.example.blackjack.Controllers;

import com.example.blackjack.Models.Participant;

public interface UIHandler {
    public void resetUI();
    public void displayHandAndScore(Participant... participants);
}
