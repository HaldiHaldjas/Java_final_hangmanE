package models;

import models.datastructures.DataScore;
import views.View;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Model {
    private final String chooseCategory = "Kõik kategooriad";
    /**
     * See on vaikimisi andmebaasi fail kui käsurealt uut ei leotud. Andmebaasi tabelit nimed ja struktuurid peavad
     * samad olema, kuid andmed võivad erinevad olla.
     *  hangman_words_ee.db - Eestikeelsed sõnad, edetabel on tühi
     *  hangman_words_en.db - Inglisekeelsed sõnad, edetabel on tühi
     *  hangman_words_ee_test.db - Eestikeelsed sõnad, edetabel EI ole tühi
     */
    private String databaseFile = "hangman_words_ee_test.db";
    private String selectedCategory; // Vaikimisi valitud kategooria
    private String[] cmbCategories; // Rippmenüü sisu
    private String randomWord;

    /**
     * Kaust, kus on võllapuu pildid
     */
    private String imagesFolder = "images";

    private final int MAX_WRONG_GUESSES = 11; // maksimaalne valesti arvamiste nr
    private int currentWrongGuesses = 0; // valesti arvatud korrad
    private List<Character> wrongGuesses = new ArrayList<>(); // valesti arvatud tähed

    /**
     * Pildid õiges järjekorras
     */
    private List<String> imageFiles = new ArrayList<>();
    private String playerName = "";

    // edetabeliga seotud asjad
    /**
     * Edetabeli mugavaks kasutamiseks
     */
    private DefaultTableModel dtm;

    /**
     * Edetabeli andmed listis
     */
    private List<DataScore> dataScores = new ArrayList<>();
    private List<String> words = new ArrayList<>();
    private Database database;

    public Model(String dbName) {
        if(dbName != null) {
            this.databaseFile = dbName;
        }

        // System.out.println(this.databaseFile); // testib käsurealt käivitamist

        this.database = new Database(this); // Create and store the Database instance

        // new Database(this); // Loome andmebaasi ühenduse
        readImagesFolder();
        selectedCategory = chooseCategory; // Vaikimisi "Kõik kategooriad"

    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

    public List <Character> wrongGuesses() {
        this.wrongGuesses = new ArrayList<>();
        return wrongGuesses;
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

    private void readImagesFolder() {
        File folder  = new File(imagesFolder); // loo kausta objekt
        File[] files = folder.listFiles(); // loeb koik failid objekti list massiivina
        // lisab piltide listi
        for (File file : Objects.requireNonNull(files)) { // tuleb valida replace, et poleks tyhi
            imageFiles.add(file.getAbsolutePath());
        }
        Collections.sort(imageFiles);
        // System.out.println(imageFiles);

    }

    public void incrementWrongGuesses() {
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
        if (guessedWord != null && guessedWord.equalsIgnoreCase(randomWord)) {
            return true; // Guessed word matches the random word, game ends
        }
        if (currentWrongGuesses >= 11) {
            return true; // Reached maximum wrong guesses, game ends
        }
        return false; // Game continues
    }

    public void saveScoreToDatabase(DataScore score) {
        database.saveScoreToDatabase(score);
    }

    // Method to retrieve current wrong guesses count
    public int getCurrentWrongGuesses() {
        return currentWrongGuesses;
    }

    public List<Character> getWrongCharacters() {
        return wrongGuesses;
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
    public void setDatabaseFile(String databaseFile) {
        this.databaseFile = databaseFile;
    }

    /**
     * Valitud kategoori
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
     * Loeb sõnad andmebaasist
     * @return sõnad
     */

//    public void getDatabaseWords(List<String> words) {
//        this.words = words;
//    }

    /**
     * andmebaasi sõnad edasiseks kui vaja
     */
//    public void setDataWords(List<String> words) {
//        this.words = words;
//    }
//

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








