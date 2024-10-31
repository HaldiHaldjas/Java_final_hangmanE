package models;

import models.datastructures.DataScore;
import views.View;
import helpers.GameTimer;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class Model {
    private final String chooseCategory = "Kõik kategooriad";
    private String databaseFile = "hangman_words_ee_test.db";
    private Database database;
    private String selectedCategory;
    private String[] cmbCategories;
    private final List<String> imageFiles = new ArrayList<>();
    private DefaultTableModel dtm;
    private List<DataScore> dataScores = new ArrayList<>();
    private String randomWord;
    private String guessedWord;
    private char[] guessedChars;
    private int currentWrongGuesses = 0;
    private List<Character> wrongGuesses;
    private String playerName = "";
    // private String playerTime = "";
    // private GameTimer gameTimer;

    // Variables for timing
    private long startTime; // Store the game start time
    private int elapsedTime; // Store the elapsed time in seconds

    public Model(String dbName) {
        if (dbName != null) {
            this.databaseFile = dbName;
        }
        System.out.println(this.databaseFile);
        this.database = new Database(this);
        readImagesFolder();
        selectedCategory = chooseCategory;
        this.elapsedTime = 0;
    }



    private void readImagesFolder() {
        String imagesFolder = "images";
        File folder = new File(imagesFolder);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                imageFiles.add(file.getAbsolutePath());
            }
            Collections.sort(imageFiles);
        }
    }

    public void startNewGame(String randomWord) {
        this.randomWord = randomWord;
        this.guessedWord = "";
        // this.playerTime = 0;
        this.guessedChars = new char[randomWord.length()];
        Arrays.fill(guessedChars, '_');
        this.wrongGuesses = new ArrayList<>();
        currentWrongGuesses = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void processUserInput(char inputChar, View view) {
        String word = getRandomWord().toLowerCase();
        StringBuilder guessedWord = new StringBuilder(view.getGameBoard().getLblResult().getText());
        boolean found = false;

        // Iterate over the letters in the word to find if char exists
        for (int i = 0; i < word.length(); i++) {
            // Check if the current letter matches the input character
            if (Character.toLowerCase(word.charAt(i)) == inputChar) {
                found = true;
                guessedWord.setCharAt(2 * i, word.charAt(i));
            }
        }
        if (!found) {
            view.getGameBoard().getLblError().setText(view.getGameBoard().getLblError().getText() + inputChar + " ");
            incrementWrongGuesses();
            view.updateImage();

            // Check if the number of wrong guesses has reached the limit
            if (getCurrentWrongGuesses() >= 11) {
                // End the game if maximum wrong guesses reached
                view.showGameEndScreen();
                return; // Exit early to prevent further processing
            }
        } else {
            // uuendab äraarvatavat sõna
            view.getGameBoard().getLblResult().setText(guessedWord.toString());
        }

        // kontrollib, kas mäng on läbi

        if (guessedWord.toString().replaceAll("\\s+", "").equals(word)) { // Remove spaces before comparison
            int elapsedTime = getElapsedTimeInSeconds();

            // if (model.checkGameEnd(guessedWord.toString().replaceAll("\\s+", ""))) {
            view.showGameEndScreen();
            view.showButtons();
            System.out.println("Mang labi!");
            // Create a DataScore record here
//            DataScore score = new DataScore(
//                    LocalDateTime.now(), // Current time as game end time
//                    this.getPlayerName(),
//                    this.getRandomWord().toLowerCase(),
//                    this.getWrongGuesses(),
//                    elapsedTime // Use elapsed time from GameTimer
//            );
//            System.out.println("Datascore from model" + score);
//            saveScoreToDatabase(score);
            // Example of creating a new DataScore instance when the game ends

            DataScore score = new DataScore(
                    LocalDateTime.now(),  // Capturing end time
                    "Player1",            // Player name
                    "puzzleWord",         // Word that was guessed
                    "aeiou",              // Incorrect guesses
                    69                    // Game time in seconds
            );

// Accessing fields
            System.out.println("Player Name: " + score.playerName());
            System.out.println("End Time: " + score.playerTime());
            System.out.println("Game Time: " + score.gameTime() + " seconds");
            saveScoreToDatabase(score);

        }
    }

    public void createAndSaveScore(String playerName, int elapsedTime) {
        DataScore score = new DataScore(
                LocalDateTime.now(), // Current date and time
                playerName,
                getRandomWord(),
                getWrongGuesses(),
                elapsedTime
        );

        // Save the score to the database
        saveScoreToDatabase(score);

        // Optionally, add the score to the dataScores list
        dataScores.add(score);

        // Print DataScore to console for debugging
        System.out.println("New DataScore created:");
        System.out.println(score);
    }


    public void saveScoreToDatabase(DataScore score) {
        if (this.database != null) {
            this.database.saveScoreToDatabase(
                    score.playerName(),
                    score.guessWord(),
                    score.wrongCharacters(),
                    score.gameTime()
            );
        } else {
            System.err.println("Andmebaasiga ei saanud ühendust!");
        }

    }

    // Method to update elapsed time
    public int getElapsedTimeInSeconds() {
        // Calculate the elapsed time in seconds
        elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
        return elapsedTime;
    }

    public String formatWordForDisplay(String word) {
        StringBuilder displayWord = new StringBuilder();
        word = word.toLowerCase();
        for (char c : word.toCharArray()) {
            if (Character.isLetter(c)) {
                displayWord.append("_ ");
            } else {
                displayWord.append(c).append(" ");
            }
        }
        System.out.println("displayword from model: " + displayWord);
        return displayWord.toString().trim();
    }

    public void incrementWrongGuesses() {
        int MAX_WRONG_GUESSES = 11;
        if (currentWrongGuesses < MAX_WRONG_GUESSES) {
            currentWrongGuesses++;
        }
    }

    public String[] getCurrentImagePath() {
        int index = getCurrentWrongGuesses();
        if (index >= 0 && index < imageFiles.size()) {
            return new String[]{imageFiles.get(index)};
        }
        return null;
    }

    public void resetWrongGuesses() {
        currentWrongGuesses = 0;
    }


    public boolean checkGameEnd(String guessedWord) {
        if (guessedWord != null && guessedWord.equalsIgnoreCase(randomWord) || currentWrongGuesses == 11) {
            // System.out.println("Mäng läbi! Sõna on: " + randomWord);
            return true;
        }
        if (currentWrongGuesses >= 11) {
            // System.out.println("Mäng läbi valed arvamised");
            return true;
        }
        // mäng ei ole läbi
        return false;
    }

    public boolean isGameOver() {
        // Check if the game is over based on current wrong guesses or if the guessed word matches the random word
        return checkGameEnd(getGuessedWord()); // Use a getter method to retrieve the current guessed word
    }


    public int getCurrentWrongGuesses() {
        return currentWrongGuesses;
    }

    public String getChooseCategory() {
        return chooseCategory;
    }

    public String getDatabaseFile() {
        return databaseFile;
    }

    public Database getDatabase() {
        return database;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String[] getCmbCategories() {
        return cmbCategories;
    }

    public void setCmbCategories(String[] cmbCategories) {
        this.cmbCategories = cmbCategories;
    }

    public List<String> getImageFiles() {
        return imageFiles;
    }

    public DefaultTableModel getDtm() {
        return dtm;
    }

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }

    public List<DataScore> getDataScores() {
        return dataScores;
    }

    public void setDataScores(List<DataScore> dataScores) {
        this.dataScores = dataScores;
    }

    public void setRandomWord(String word) {
        this.randomWord = word;
    }

    public String getRandomWord() {
        return randomWord;
    }

    public void setGuessedWord(String word) {
        this.guessedWord = word;
    }


    public String getWrongGuesses() {
        return wrongGuesses.toString();
    }

    public String getGuessedWord() {
        return new String(guessedChars); // Assuming guessedChars holds the current state of the guessed word
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return this.playerName;
    }

}
