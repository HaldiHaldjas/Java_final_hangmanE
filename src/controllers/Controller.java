package controllers;

import listeners.*;
import models.Model;
import views.View;

import javax.swing.*;

public class Controller {

    // konstruktor
    public Controller(Model model, View view) {

        // combobox
        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));
        // uue mängu funktsionaalsus
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        // katkesta nupu funktsionaalsus
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
        // saada nupu funktsionaalsus
        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view));
        // enter saada nupu asemel
        view.getGameBoard().getTxtChar().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendAction");

        // Sätete lehe edetabeli nupu funktsionaalsus
        view.getSettings().getBtnLeaderboard().addActionListener(new ButtonScores(model, view));
    }
}
