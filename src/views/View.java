package views;

import helpers.GameTimer;
import helpers.RealTimer;
import models.Model;
import models.datastructures.DataScore;

import views.panels.GameBoard;
import views.panels.LeaderBoard;
import views.panels.Settings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * See on põhivaade ehk JFrame kuhu peale pannakse kõik muud JComponendid mida on mänguks vaja.
 * JFrame vaikimisi (default) aknahaldur (Layout Manager) on BorderLayout
 */
public class View extends JFrame {
    /**
     * Klassisisene, mille väärtus saadakse VIew konstruktorist ja loodud MainApp-is
     */
    private final Model model;
    /**
     * Vaheleht (TAB) Seaded ehk avaleht
     */
    private final Settings settings;
    /**
     * Vaheleht (TAB) Mängulaud
     */
    private final GameBoard gameBoard;
    /**
     * Vaheleht (TAB) Edetabel
     */
    private final LeaderBoard leaderBoard;
    /**
     * Sellele paneelile tulevad kolm eelnevalt loodud vahelehte (Settings, GameBoard ja LeaderBoard)
     */
    private JTabbedPane tabbedPane;
    private final GameTimer gameTimer;
    private RealTimer realTimer;

    /**
     * View konstruktor. Põhiakna (JFrame) loomine ja sinna paneelide (JPanel) lisamine ja JComponendid
     * @param model mudel mis loodi MainApp-is
     */
    public View(Model model) {
        this.model = model; // MainApp-is loodud mudel

        setTitle("Poomismäng 2024 õpilased"); // JFrame titelriba tekst
        setPreferredSize(new Dimension(500, 250));

        setResizable(false);
        getContentPane().setBackground(new Color(250,210,205)); // JFrame taustavärv (rõõsa)

        // Loome kolm vahelehte (JPanel)
        settings = new Settings(model);
        gameBoard = new GameBoard(model);
        leaderBoard = new LeaderBoard(model, this);

        createTabbedPanel(); // Loome kolme vahelehega tabbedPaneli

        add(tabbedPane, BorderLayout.CENTER); // Paneme tabbedPaneli JFramele. JFrame layout on default BorderLayout
        // Loome mänguaja objekti sekundites ja minutites
        // getter, et saaks mujalt ligi
        gameTimer = new GameTimer(this);
        // loome ja käivitame päris aja - mängimise kuupäev ja kellaaeg
        realTimer = new RealTimer(this);
        realTimer.start();

    }

    private void createTabbedPanel() {
        tabbedPane = new JTabbedPane(); // Tabbed paneli loomine

        tabbedPane.addTab("Seaded", settings); // Vaheleht Seaded paneeliga settings
        tabbedPane.addTab("Mängulaud", gameBoard); // Vaheleht Mängulaud paneeliga gameBoard
        tabbedPane.addTab("Edetabel", leaderBoard); // Vaheleht Mängulaud paneeliga gameBoard

        tabbedPane.setEnabledAt(1, false); // Vahelehte mängulaud ei saa klikkida
    }

    /**
     * Meetod mis tekitab mängimise olukorra.
     */
    public void hideButtons() {
        tabbedPane.setEnabledAt(0, false); // Keela seaded vaheleht
        tabbedPane.setEnabledAt(2, false); // Keela edetabel vaheleht
        tabbedPane.setEnabledAt(1, true); // Luba mängulaud vaheleht
        tabbedPane.setSelectedIndex(1); // Tee mängulaud vaheleht aktiivseks

        gameBoard.getBtnSend().setEnabled(true); // Nupp Saada on klikitav
        gameBoard.getBtnCancel().setEnabled(true); // Nupp Katkesta on klikitav
        gameBoard.getTxtChar().setEnabled(true); // Sisestuskast on aktiivne

    }

    /**
     * Meetod mis tekitab mitte mängimise olukorra. Vastupidi hideButtons() meetodil
     */
    public void showButtons() {
        tabbedPane.setEnabledAt(0, true); // Luba seaded vaheleht
        tabbedPane.setEnabledAt(2, true); // Luba edetabel vaheleht
        tabbedPane.setEnabledAt(1, false); // Keela mängulaud vaheleht
        // tabbedPane.setSelectedIndex(0); // Tee seaded vaheleht aktiivseks. Peale mängu pole see hea, sest ei näe lõppseisus

        gameBoard.getBtnSend().setEnabled(false); // Nupp Saada ei ole klikitav
        gameBoard.getBtnCancel().setEnabled(false); // Nupp Katkesta ei ole klikitav
        gameBoard.getTxtChar().setEnabled(false); // Sisestuskast ei ole aktiivne
        gameBoard.getTxtChar().setText(""); // teeb sisestuskasti tyhjaks
    }

    // todo kustutada voi rakendada
    public void clearErrorLabel(){
        gameBoard.getBtnSend().setEnabled(false);
    }

    //  Paneelide vahelehtede GETTERID

    public Settings getSettings() {
        return settings;
    }
    // todo 16 gameboard usage 8 asemel - miski on topelt
    public GameBoard getGameBoard() {
        return gameBoard;
    }
    // todo kasutamata funktsioon
    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Mänguaja objekt .stop() . setRunning() jne
     * @return mänguaja objekt
     */

    // todo 11 usaget 14 asemel
    public GameTimer getGameTimer() {
        return gameTimer;
    }

    // todo kasutamata funktsioon
    public int getGameTimeInSeconds() {
        return gameTimer.getElapsedTimeInSeconds(); // Assuming such a method exists
    }

    public void updateScoresTable() {
        // todo - 2 rida lisandusi
        DefaultTableModel dtm = model.getDtm();
        dtm.setRowCount(0);

        for(DataScore ds : model.getDataScores()) {
            String gameTimeFormatted;

            // Assuming ds.gameTime() returns an int representing seconds
            int seconds = ds.gameTime(); // or however you retrieve gametime
            gameTimeFormatted = convertSecToMMSS(seconds); // Convert seconds to MM:SS format
            String name = ds.playerName();
            String word = ds.guessWord();
            String chars = ds.wrongCharacters();
            String humanTime = convertSecToMMSS(ds.gameTime()); // sekundid taisarvuna pandud meetodi sisse
            dtm.addRow(new Object[]{gameTimeFormatted, name, word, chars, humanTime});

        }
    }
    /**
     * Muudab aja min on sekundites kujule mm:ss 90 sek on 01:30
     * @params seconds sekundid, taisarv
     * @return vormindatud string
     */

    private String convertSecToMMSS(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    /**
     * Kuvab uue mängu puhul pildi nr 1
     */
    public void setFirstPicture(){
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().getFirst());
        getGameBoard().getLblImage().setIcon(imageIcon);
    }


    public void updateImage() {
        // int wrongGuesses = model.getCurrentWrongGuesses();
        String[] imagePath = model.getCurrentImagePath();
        if (imagePath != null && imagePath.length > 0) {
            ImageIcon imageIcon = new ImageIcon(imagePath[0]);
            getGameBoard().getLblImage().setIcon(imageIcon);
        }
    }

    public void showGameEndScreen() {
        gameTimer.setRunning(false);
        getGameTimer().stopTime();
        String playerName = "";
        String message;

        if (model.getCurrentWrongGuesses() >= 11) {
            message = "Mäng läbi! See lõpppes sinu jaoks fataalselt. Õige sõna oli: " + model.getRandomWord();
        } else {
            playerName = JOptionPane.showInputDialog(this, "Tubli! Arvasid sõna ära. Sisesta oma nimi: ");
            if (playerName == null || playerName.isEmpty()) {
                playerName = "Nipitiri";
            }
            message = "Palju õnne, " + playerName + ", arvasid õige sõna ära: " + model.getRandomWord();
//            DataScore score = new DataScore(
//                    LocalDateTime.now(), // Current date and time
//                    playerName,
//                    model.getRandomWord(),
//                    model.getWrongGuesses(),
//                    gameTimer.getElapsedTimeInSeconds()
//            );
//
//            // Print DataScore to console
//            System.out.println("New DataScore created:");
//            System.out.println(score);

            model.createAndSaveScore(playerName, gameTimer.getElapsedTimeInSeconds());

            updateScoresTable();
            // muudab tabid taas klikitavals/halvab mängunupud
            showButtons();
        }
        JOptionPane.showMessageDialog(this, message);

    }

}