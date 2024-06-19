package listeners;

import models.Database;
import models.Model;
import views.View;
import views.panels.GameBoard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonSend implements ActionListener {

    // klassisisesed muutujad
    private Model model;
    private View view;

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Klikk SAADA");
        // kasutajatasisestus väiketähtedeks
        String input = view.getGameBoard().getTxtChar().getText().toLowerCase();
        // sisestuse kast tühjaks
        view.getGameBoard().getTxtChar().setText("");

        if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
            processUserInput(input.charAt(0));
        } else {
            JOptionPane.showMessageDialog(view.getGameBoard(), "Sisesta üks täht!");
        }

    }
        private void processUserInput(char inputChar) {
            String word = model.getRandomWord().toLowerCase();
            // kui sõna on olemas - stringbuilder
            if (word != null) {
                StringBuilder guessedWord = new StringBuilder(view.getGameBoard().getLblResult().getText());
                boolean found = false;
                for (int i = 0; i < word.length(); i++) {
                    if (Character.toLowerCase(word.charAt(i)) == inputChar) {
                        found = true;
                        guessedWord.setCharAt(2 * i, word.charAt(i));
                    }
                }
                if (!found) {
                    // kui ei leidnud
                    view.getGameBoard().getLblError().setText(view.getGameBoard().getLblError().getText() + inputChar + " ");
                }
                // värskendab labelil oleva sõna äraaarvatud tähti
                view.getGameBoard().getLblResult().setText(guessedWord.toString());

            }
        }
    }

