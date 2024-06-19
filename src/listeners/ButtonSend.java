package listeners;

import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ButtonSend implements ActionListener, KeyListener {

    private Model model;
    private View view;

    public ButtonSend(Model model, View view) {
        this.model = model;
        this.view = view;

        // Register ActionListener for the send button
        view.getGameBoard().getBtnSend().addActionListener(this);

        // Register KeyListener for the text field to handle Enter key press
        view.getGameBoard().getTxtChar().addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        handleSendAction();
    }
    /**
     * Registreerib ka ENTER key vajutuse nupuvajutusena
     * */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            handleSendAction();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but must be implemented due to KeyListener interface
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for handling Enter key
    }

    private void handleSendAction() {
        System.out.println("Klikk SAADA");

        // Get user input in lowercase
        String input = view.getGameBoard().getTxtChar().getText().toLowerCase().trim();

        // Clear the text field and reset focus
        view.getGameBoard().getTxtChar().setText("");
        view.getGameBoard().getTxtChar().requestFocus();

        if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
            processUserInput(input.charAt(0));
        } else {
            JOptionPane.showMessageDialog(view.getGameBoard(), "Sisesta üks täht! Numbrid jm on keelatud!");
        }
    }

    private void processUserInput(char inputChar) {
        String word = model.getRandomWord().toLowerCase();
        StringBuilder guessedWord = new StringBuilder(view.getGameBoard().getLblResult().getText());
        boolean found = false;
        for (int i = 0; i < word.length(); i++) {
            if (Character.toLowerCase(word.charAt(i)) == inputChar) {
                found = true;
                guessedWord.setCharAt(2 * i, word.charAt(i));
            }
        }
        if (!found) {
            view.getGameBoard().getLblError().setText(view.getGameBoard().getLblError().getText() + inputChar + " ");
        }
        view.getGameBoard().getLblResult().setText(guessedWord.toString());
    }
}
