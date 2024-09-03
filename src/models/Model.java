package models;

import models.datastructures.DataScore;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.*;

public class Model {

    private final String chooseCategory = "Kõik kategooriad";
    /**
     * See on vaikimisi andmebaasi fail
     * kui käsurealt uut ei leitud. Andmebaasi tabelit nimed ja struktuurid peavad
     * samad olema, kuid andmed võivad erinevad olla.
     *  hangman_words_ee.db - Eestikeelsed sõnad, edetabel on tühi
     *  hangman_words_en.db - Inglisekeelsed sõnad, edetabel on tühi
     *  hangman_words_ee_test.db - Eestikeelsed sõnad, edetabel EI ole tühi
     */

    private String databaseFile = "hangman_words_ee_test.db";

    private String selectedCategory; // Vaikimisi valitud kategooria
    private String[] cmbCategories; // Rippmenüü sisu

    /**
     * Siia pannakse võllapuu pildid õiges järjekorras
     */
    private final List<String> imageFiles = new ArrayList<>();

    /**
     * Edetabeli mugavaks kasutamiseks
     */
    private DefaultTableModel dtm;

    /**
     * Edetabeli andmed listis
     */
    private List<DataScore> dataScores = new ArrayList<>();
    private String randomWord; // väljavalitud sõna
    private char[] guessedChars;
    private int currentWrongGuesses = 0; // valesti arvatud korrad
    private List<Character> wrongGuesses;

    private String playerName = "";


    public Model(String dbName) {
        if(dbName != null) {
            this.databaseFile = dbName;
        }

        // System.out.println(this.databaseFile); // testib käsurealt DB käivitamist
        new Database(this); // Loome andmebaasi ühenduse
        readImagesFolder(); // loeb võllapuu pildid mällu
        selectedCategory = chooseCategory; // Valib vaikimisi "Kõik kategooriad"
    }

    /**
     * Loeb võllapuu pildid kaustast ja lisab need imageFiles listi
     */
    private void readImagesFolder() {
        String imagesFolder = "images";
        File folder  = new File(imagesFolder); // loo kausta objekt
        File[] files = folder.listFiles(); // loeb koik failid objekti list massiivina
        // lisab piltide listi
        for (File file : Objects.requireNonNull(files)) { // tuleb valida replace, et poleks tyhi
            imageFiles.add(file.getAbsolutePath());
        }
        Collections.sort(imageFiles); // sorteerib suurenevasse järjekorda
        // System.out.println(imageFiles);
    }

    /**
     * Uue mängu seadistus
     * @param randomWord sõna asendamine alakriipsudega
     */
    public void startNewGame(String randomWord){
        this.randomWord = randomWord;
        this.guessedChars = new char[randomWord.length()];
        Arrays.fill(guessedChars, '_');
        this.wrongGuesses = new ArrayList<>();
        currentWrongGuesses = 0;
    }

//    public List<String> getWords() {
//        return words;
//    }
//
//    public void setWords(List<String> words) {
//        this.words = words;
//    }

//    public String <Character> wrongGuesses() {
//        this.wrongGuesses = new ArrayList<>();
//        return wrongGuesses;
//    }

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
        // todo - kas seda on ka vaja? getterid ja setterid v'lja
        // private List<String> words = new ArrayList<>();
        // todo max wrong guesses - kas on ikka vajalik?
        // maksimaalne valesti arvamiste nr
        int MAX_WRONG_GUESSES = 11;
        if (currentWrongGuesses < MAX_WRONG_GUESSES) {
            currentWrongGuesses++;
        }
    }

    public String[] getCurrentImagePath() {
        // piltide indeks on sama, mis valede arvamiste arv
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
            System.out.println("Mäng läbi!");

            // model.saveScoreToDatabase(new DataScore);

            return true; // Guessed word matches the random word, game ends
        }

        // Reached maximum wrong guesses, game ends
        if (currentWrongGuesses >= 11) {
            System.out.println("Mäng läbi valed arvamised");
            return true; // Reached maximum wrong guesses, game ends
            // model.saveScoreToDatabase(score);

        }
        return false; // Game continues
    }

//    public void saveScoreToDatabase(DataScore score) {
//        if (this.database != null) { // Safety check to avoid NullPointerException
//            this.database.saveScoreToDatabase(score);
//        } else {
//            System.err.println("Andmebaasiga ei saanud ühendust!");
//            }
//        }
//    }

//    public void saveScoresToDatabase(DataScore dataScore) {
//    }

    // Method to retrieve current wrong guesses count
    public int getCurrentWrongGuesses() {
        return currentWrongGuesses;
    }

    /**
     * Rippmenüü esimene valik enne kategooriaid
     * @return teksti "Kõik kategooriad"
     */
    public String getChooseCategory() {
        return chooseCategory;
    }

    /**
     * Millise andmebaasiga on tegemist
     * @return andmebaasi failinimi
     */
    public String getDatabaseFile() {
        return databaseFile;
    }

    /**
     * Seadistab uue andmebaasi failinime, kui see saadi käsurealt
     * @param databaseFile uus andmebaasi failinimi
     */

//    public void setDatabaseFile(String databaseFile) {
//        this.databaseFile = databaseFile;
//    }

    /**
     * Valitud kategooria
     * @return tagastab valitud kategooria
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Seadistab valitud kategooria
     * @param selectedCategory uus valitud kategooria
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * kategooriate nimed
     * @return kategooriate nimed
     * */

    public String[] getCmbCategories() {
        return cmbCategories;
    }
    /**
     * Seadistab uued kategooriate nimed
     * @param cmbCategories kategooriate massiiv
     * */

    public void setCmbCategories(String[] cmbCategories) {
        this.cmbCategories = cmbCategories;
    }
    /**
     * Võllapuu pildid
     * @return võllapuu pildid listina List<String>
     */
    public List<String> getImageFiles() {
        return imageFiles;
    }

    /**
     * @return DefaulTableModeli
     * */

    public DefaultTableModel getDtm() {
        return dtm;
    }
    /**
     * Seadistab uue DefaultTableModeli
     * @param dtm uus dtm
     * */

    public void setDtm(DefaultTableModel dtm) {
        this.dtm = dtm;
    }
    /**
     * Loeb edetabeli andmeid andmebaasist
     * @return edetabeli andmed
     * */

    public List<DataScore> getDataScores() {
        return dataScores;
    }
    /**
     * Muudab, tyhjendab edetabeli andmeid
     * @params dataScores uued andmed edetabeli jaoks
     * */
    public void setDataScores(List<DataScore> dataScores) {
        this.dataScores = dataScores;
    }

    /**
     * Loeb sisse database klassist tuleva juhusliku sõna
     * @return word
     * */
    public void setRandomWord(String word) {
        this.randomWord = word;
    }
    /**
     * Ligipääs andmebaasist kategooria alusel juhuslikult valitud sõnale
     * @params randomWord
     */
    public String getRandomWord() {
        return randomWord;
    }

    public String getWrongGuesses() {
        return wrongGuesses.toString();}


    /**Salvestam mängija nime
     * @param playerName
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    /**
     * Meetod tagastab mängija nime
     * @return
     */
    public String getPlayerName(){
        return this.playerName;
    }

}