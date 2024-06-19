package controllers;

import listeners.ButtonCancel;
import listeners.ButtonNew;
import listeners.ButtonSend;
import listeners.ComboboxChange;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    // konstruktor
    public Controller(Model model, View view) {
        view.getSettings().getCmbCategory().addItemListener(new ComboboxChange(model));
        // uue mängu funktsionaalsus
        view.getSettings().getBtnNewGame().addActionListener(new ButtonNew(model, view));
        // katkesta nupu funktsionaalsus
        view.getGameBoard().getBtnCancel().addActionListener(new ButtonCancel(model, view));
        // saada nupu funktsionaalsus
        view.getGameBoard().getBtnSend().addActionListener(new ButtonSend(model, view));

        // enter saada nupu asemel
        // view.getGameBoard().getTxtChar().addActionListener(new ButtonSend(model, view));
        view.getGameBoard().getTxtChar().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendAction");
    }
}
