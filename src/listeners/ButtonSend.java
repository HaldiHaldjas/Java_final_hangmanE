package listeners;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

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
        System.out.println("SAADA");
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

        // kasutaja sisestus väikeste tähtedega
        String input = view.getGameBoard().getTxtChar().getText().toLowerCase().trim();
        // kontroll, kas on 1-kohaline sisestus ja täht
        if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
            processUserInput(input.charAt(0));

        } else {
            // kui input ei ole tühi ja kui ei vasta esimese tingimuse nõuetele
            if (!input.isEmpty()) {
                JOptionPane.showMessageDialog(view.getGameBoard(), "Sisesta üks täht! Numbrid jm on keelatud!");
            }
        }
        view.getGameBoard().getTxtChar().setText("");
        view.getGameBoard().getTxtChar().requestFocus();
    }

    private void processUserInput(char inputChar) {
        String word = model.getRandomWord().toLowerCase();
        StringBuilder guessedWord = new StringBuilder(view.getGameBoard().getLblResult().getText());
        boolean found = false;

        // Iterate over the letters in the word
        for (int i = 0; i < word.length(); i++) {
            // Check if the current letter matches the input character
            if (Character.toLowerCase(word.charAt(i)) == inputChar) {
                found = true;
                guessedWord.setCharAt(2 * i, word.charAt(i));
            }
        }
        if (!found) {
            view.getGameBoard().getLblError().setText(view.getGameBoard().getLblError().getText() + inputChar + " ");
            model.incrementWrongGuesses();
            view.updateImage();

            // Check if the number of wrong guesses has reached the limit
            if (model.getCurrentWrongGuesses() >= 11) {
                // End the game if maximum wrong guesses reached
                view.showGameEndScreen();
                return; // Exit early to prevent further processing
            }
        }
        view.getGameBoard().getLblResult().setText(guessedWord.toString());
        // Database.getInstance(model).saveScoreToDatabase(model.getPlayerName(), model.getRandomWord().toLowerCase(), model.getWrongGuesses(), view.getGameTimer().getPlayedTimeInSeconds());
        // Database.getInstance(model).saveScore(playerName, model.getCurrentWord().toLowerCase(), model.getWrongGuesses().toString(), view.getGameTimer().getPlayedTimeInSeconds());
        // String formattedWrongGuesses = model.getWrongGuesses().toString(); // Adjust this if needed

        // Database.getInstance(model).saveScoreToDatabase(view.getName(), guessedWord.toString(), model.getWrongGuesses(), view.getGameTimeInSeconds());
        // Check if the game should end based on the guessed word
        if (model.checkGameEnd(guessedWord.toString().replaceAll("\\s+", ""))) {
            // Game should end, perform end game actions
            view.showGameEndScreen();
            view.showButtons();
            System.out.println("Mang labi!");
        }
    }
}
