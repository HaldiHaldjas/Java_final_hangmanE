package controllers;

import listeners.*;
import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    // private final Database database;
    // konstruktor
    public Controller(Model model, View view) {
        // this.database = new Database(model); // uus andmebaasiühendus
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
        // piltide vahetus
        // view.getGameBoard().updateImage(0);
        // kontroll, kas actionlistener töötab 1 korra
        // System.out.println("Controller initialized with ButtonSend listener.");
        // Sätete lehe edetabeli nupu funktsionaalsus
        view.getSettings().getBtnLeaderboard().addActionListener(new ButtonScores(model, view));
    }
}
