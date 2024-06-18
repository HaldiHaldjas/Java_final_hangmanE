package views.panels;

import models.Model;

import javax.swing.*;
import java.awt.*;

/**
 * See on vaheleht Seaded paneel ehk avaleht. Siit saab valida mängu jaoks sõna kategooria ja käivitada mängu. See on
 * üks kolmest vahelehest (esimene). JPanel vaikimisi (default) aknahaldur (Layout Manager) on FlowLayout.
 */
public class Settings extends JPanel {
    /**
     * Klassisisene mudel, mille väärtus saadakse View konstruktorist ja loodud MainApp-is
     */
    private Model model;
    /**
     * GridBagLayout jaoks JComponent paigutamiseks "Excel" variandis
     */
    private GridBagConstraints gbc = new GridBagConstraints();
    /**
     * See silt (JLabel) näitab reaalset kuupäeva ja jooksvat kellaaega
     */
    private JLabel lblRealTime;
    /**
     * Sisaldab teksti "Sõna kategorgooria"
     */
    private JLabel lblCategory;
    /**
     * Sisaldab äraarvatava sõna kategooriat (andmebaasist). Algul "Kõik kategooriad"
     */
    private JComboBox<String> cmbCategory;
    /**
     * Uue mängu alustamise nupp
     */
    private JButton btnNewGame;
    /**
     * TODO Selle nupu vajalikkus on küsimärgi all :)
     * Suunab vahelehele Edetabel
     */
    private JButton btnLeaderboard;

    /**
     * Silt failinime jaoks*/
    private JLabel lblFile;

    /**
     * Settings JPanel konstruktor
     * @param model mudel mis loodud MainApp-is
     */
    public Settings(Model model) {
        this.model = model;

        setBackground(new Color(255,250,200)); // JSettings paneeli taustavärv

        gbc.fill = GridBagConstraints.HORIZONTAL; // Täidab lahtri horisontaalselt (kõik lahtrid on "sama laiad")
        gbc.insets = new Insets(2,2,2,2); // Iga lahtri ümber 2px tühja ruumi

        JPanel components = new JPanel(new GridBagLayout()); // Siia pannakse kõik komponendid settings paneeli omad
        components.setBackground(new Color(140,185,250)); // Komponentide paneeli tausta värv

        /*
         Kuna components panel on Settings konstruktoris loodud ei saa ma seda paneeli mujal kasutada, kui annan
         argumendina kaasa JPaneli meetodile ja saan teises meetodis seda sama paneeli kasutada. Siia on vaja ju
         komponendid peale panna
         */
        setupUIComponents(components);

        add(components); // Paiguta JPanel component settings (this.) panelile
    }

    /**
     * Meetod mis loob kõik komponendid settings paneelile
     * @param components paneel kuhu komponendid paigutada
     */
    private void setupUIComponents(JPanel components) {
        // Esimene rida üle kahhe veeru kuupäev ja kellaaja JLabel
        lblRealTime = new JLabel("Siia tuleb reaalne aeg", JLabel.CENTER);
        gbc.gridx = 0; // Esimene veerg (column)
        gbc.gridy = 0; // Esimene rida (row)
        gbc.gridwidth = 2; // Pane kaks veergu kokku (merge)
        components.add(lblRealTime, gbc); // Pane objekt paneelile

        // Teine rida Silt ja Rippmenüü
        lblCategory = new JLabel("Sõna kategooria");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Muuda tagasi üks komponent veergu
        components.add(lblCategory, gbc);

        // TODO muuta JComboBox sulgude sisu
        // cmbCategory = new JComboBox<>(new String[]{model.getChooseCategory()}); // Teksti massiiv ühe elemendiga
        cmbCategory = new JComboBox<>(model.getCmbCategories()); // Teksti massiiv kõikide elementidega
        gbc.gridx = 1;
        gbc.gridy = 1;
        components.add(cmbCategory, gbc);

        // Label failinime jaoks kolmas rida
        lblFile = new JLabel(model.getDatabaseFile(), JLabel.CENTER);
        lblFile.setForeground(Color.RED);
        lblFile.setFont(new Font("Verdana", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // 2 veergu
        components.add(lblFile, gbc);



        // Kolmas rida kaks nuppu kõrvuti. Teine nupp küsitav :)
        btnNewGame = new JButton("Uus mäng");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // laius 1 veerg
        components.add(btnNewGame, gbc);

        btnLeaderboard = new JButton("Edetabel");
        gbc.gridx = 1;
        gbc.gridy = 3;
        components.add(btnLeaderboard, gbc);

    }

    // Komponentide getterid

    public JLabel getLblRealTime() {
        return lblRealTime;
    }

    public JComboBox<String> getCmbCategory() {
        return cmbCategory;
    }

    public JButton getBtnNewGame() {
        return btnNewGame;
    }

    public JButton getBtnLeaderboard() {
        return btnLeaderboard;
    }
}
