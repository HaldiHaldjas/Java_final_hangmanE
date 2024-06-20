package views;

import helpers.GameTimer;
import helpers.RealTimer;
import models.Model;
import models.datastructures.DataScore;
import views.panels.GameBoard;
import views.panels.LeaderBoard;
import views.panels.Settings;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * See on põhivaade ehk JFrame kuhu peale pannakse kõik muud JComponendid mida on mänguks vaja.
 * JFrame vaikimisi (default) aknahaldur (Layout Manager) on BorderLayout
 */
public class View extends JFrame {
    /**
     * Klassisisene, mille väärtus saadakse VIew konstruktorist ja loodud MainApp-is
     */
    private Model model;
    /**
     * Vaheleht (TAB) Seaded ehk avaleht
     */
    private Settings settings;
    /**
     * Vaheleht (TAB) Mängulaud
     */
    private GameBoard gameBoard;
    /**
     * Vaheleht (TAB) Edetabel
     */
    private LeaderBoard leaderBoard;
    /**
     * Sellele paneelile tulevad kolm eelnevalt loodud vahelehte (Settings, GameBoard ja LeaderBoard)
     */
    private JTabbedPane tabbedPane;
    // TODO Realtimer ka
    private GameTimer gameTimer;
    private RealTimer realTimer;

    /**
     * View konstruktor. Põhiakna (JFrame) loomine ja sinna paneelide (JPanel) lisamine ja JComponendid
     * @param model mudel mis loodi MainApp-is
     */
    public View(Model model) {
        this.model = model; // MainApp-is loodud mudel

        setTitle("Poomismäng 2024 õpilased"); // JFrame titelriba tekst
        setPreferredSize(new Dimension(500, 250));
        // TODO arenduse lõpus keela akna suurendamine
        // setResizable(false);
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

        // TODO arenduse lõpus mängulaua vahelehte klikkida ei saa
        // tabbedPane.setEnabledAt(1, false); // Vahelehte mängulaud ei saa klikkida
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

    public void clearErrorLabel(){
        gameBoard.getBtnSend().setEnabled(false);
    }

    // GETTERID Paneelide (vahelehetede)
    public Settings getSettings() {
        return settings;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public LeaderBoard getLeaderBoard() {
        return leaderBoard;
    }

    /**
     * Mänguaja objekt .stop() . setRunning() jne
     * @return mänguaja objekt
     */

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public void updateScoresTable() {
        for(DataScore ds : model.getDataScores()) {
            String gameTime = ds.gameTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            // System.out.println(gameTime);
            String name = ds.playerName();
            String word = ds.word();
            String chars = ds.missedChars();
            String humanTime = convertSecToMMSS(ds.timeSeconds()); // sekundid taisarvuna pandud meetodi sisse
            model.getDtm().addRow(new Object[]{gameTime, name, word, chars, humanTime});

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

    public void showGameEndScreen() {
        // Perform actions to show game end screen
        JOptionPane.showMessageDialog(this,
                "Arvasid õige sõna ära!",  // Message displayed in the dialog
                "Mäng läbi",   // Title of the dialog
                JOptionPane.INFORMATION_MESSAGE);  // Message type

        // Stop the game timer
        if (gameTimer != null) {
            gameTimer.stopTime();

        }

        // muudab tabid taas klikitavals/halvab mängunupud
        this.showButtons();  // This method is assumed to be implemented to disable certain UI components
    }


}
