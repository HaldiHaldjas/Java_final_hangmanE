package views.panels;

import helpers.TextFieldLimit;
import models.Model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameBoard extends JPanel {
    /**
     * Klassisisene mudel, mille väärtus saadakse View konstruktorist ja loodud MainApp-is
     */
    private Model model;

    /**
     * GridBagLayout jaoks JComponent paigutamiseks "Excel" variandis
     */
    private GridBagConstraints gbc = new GridBagConstraints();
    /**
     * See silt (JLabel) näitab mängu aega kujul: mm:ss
     */
    private JLabel lblGameTime;
    /**
     * Sisestuskast kuhu sisestada täht
     */
    private JTextField txtChar;
    /**
     * See silda sisaldab tähti mis on valed äraarvatavas sünas
     */
    private JLabel lblError;
    /**
     * Sisestatud tähe saatmiseks rakendusele kontrolliks
     */
    private JButton btnSend;
    /**
     * Mängu lõpetamine / mängust loobumine
     */
    private JButton btnCancel;
    /**
     * See sisaldab pilti võllapuu jaoks (jah, label on pildi näitamiseks)
     */
    private JLabel lblImage;
    /**
     * See näitab äraarvatavat sõna kasutajale kujul: T _ _ E M _ S
     */
    private JLabel lblResult;

    public GameBoard(Model model) {
        this.model = model;

        setLayout(new BorderLayout()); // Määrame sellele paneelile (GameBoard) teise aknahalduri (Layout Manager)
        setBackground(new Color(200,255,175)); // Määrame paneelile uue taustavärvi (õrn roheline)
        setBorder(new EmptyBorder(5,5,5,5)); // Paneelile ümebrringi tühi ruumi. Näeb taustavärvi

        gbc.fill = GridBagConstraints.HORIZONTAL; // Täidab lahtri horisontaalselt (kõik lahtrid on "sama laiad")
        gbc.insets = new Insets(2,2,2,2); // Iga lahtri ümber 2px tühja ruumi
        //gbc.anchor = GridBagConstraints.WEST; // "Joondab vasakult"

        JPanel components = new JPanel(new GridBagLayout()); // Paneel kuhu pannakse enamus selle paneeli kolmponentidest
        components.setBackground(new Color(140,185,250)); // Komponent paneeli taustavärv (miski sinine)
        components.setBorder(new EmptyBorder(2,2,2,2)); // Paneelile ümebrringi tühi ruumi.

        JPanel pnlResult = new JPanel(new FlowLayout()); // See panell näitab äraarvatavat sõna (T _ _ E M _ S)
        pnlResult.setBackground(new Color(250,200,235)); // Äraarvatava sõna paneeli taustavärv (rõõsa laadne)

        createUIComponents(components); // Enamus komponente siin paneelil
        createImagePlace(components); // Ainult võllapuu pildi osa
        createResultPlace(pnlResult); // Äraarvatava sõna paneeli loomine

        add(components, BorderLayout.CENTER); // Paiguta komponentide paneel mängulauale (keskele)
        add(pnlResult, BorderLayout.NORTH); // Paiguta äraarvatava sõna paneel ülesse serva
    }

    /**
     * Joonistab mänguväljale enamus komponendid (v.a. Rssult ja Image)
     * @param components paneeli kuhu komponendid pannakse
     */
    private void createUIComponents(JPanel components) {
        // Esimene rida (mänguaeg)
        lblGameTime = new JLabel("Siia tuleb mänguaeg", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Mänguaeg üle kahe lahtri keskele
        gbc.weightx = 1.0; // "Suurenevad sujuvalt"
        components.add(lblGameTime, gbc);

        // Teine rida (Silt ja Sisestuskast)
        JLabel lblChar = new JLabel("Sisesta täht: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Iga komponent ühte lahtrisse mitte üle kahe
        components.add(lblChar, gbc);

        /*
         * Loome sisestuskasti ja see on kogu aeg fookuses (JTextFIeld)
         * https://stackoverflow.com/questions/4640138/setting-the-focus-to-a-text-field
         */
        txtChar = new JTextField("", 10) {
            @Override
            public void addNotify() { // tekstikursor vilkuma, fookus
                super.addNotify();
                requestFocus();
            }
        };
        txtChar.setEnabled(false); // Vaikimisi lahtrisse kirjuta ei saa
        txtChar.setHorizontalAlignment(JTextField.CENTER); // Kirjuta lahtri keskele
        // TODO siia rida, et tekstikasti saab ainult ühe tähe kirjutada
        txtChar.setDocument(new TextFieldLimit(1)); // helpersites olemas
        gbc.gridx = 1;
        gbc.gridy = 1;
        components.add(txtChar, gbc);

        // Kolmas rida (Silt üle kahe veeru)
        lblError = new JLabel("Vigased tähed: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Üle kahe lahtri (kaks lahtrit üheks)
        components.add(lblError, gbc);

        // Neljas rida
        btnSend = new JButton("Saada");
        btnSend.setEnabled(false); // Nuppu ei saa klikkida
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Iga komponent ühte lahtrisse
        components.add(btnSend, gbc);

        btnCancel = new JButton("Katkesta");
        btnCancel.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        components.add(btnCancel, gbc);

    }

    /*
     * Loob võllapuu pildikasti kus näitab jooksvat pilti
     * @param components kuhu lisatakse komponent (JLabel)
     */
    private void createImagePlace(JPanel components) {
        lblImage = new JLabel();
        // TODO pildid mällu lugemata, seega võllapuud ei näe vaid värviline pildikast. Asendada temporaryImage() õigega
        // ImageIcon imageIcon = new ImageIcon(temporaryImage()); // Sulgude osa täita õigesti ja pilt on maagiliselt näha
        ImageIcon imageIcon = new ImageIcon(model.getImageFiles().getLast()); // Sulgude osa täita õigesti ja pilt on maagiliselt näha
        lblImage.setIcon(imageIcon);

        gbc.gridx = 2; // Kolmas veerg
        gbc.gridy = 0; // Esimene rida
        gbc.gridheight = 4; // Label üle 4 või 5 rea kõrge (vajab mängimist)
        components.add(lblImage, gbc);

    }

    /*
     * Tegemist on ajutise pildiga kuna piltide kausta (images) pole algselt loetud
     * @return pilt suurusega 125x125 punase värviga
     * <a href="https://stackoverflow.com/questions/47137636/swing-new-imageicon-from-color">Viide õpetusele</a>
     */
    private BufferedImage temporaryImage() {
        BufferedImage image = new BufferedImage(125, 125, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setPaint(new Color(255, 0, 0));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        return image;
    }

    /**
     * Äraarvatava sõna paneeli loomine
     * @param pnlResult paneel kuhu näidatakse äraarvatavat sõna (T _ _ E M _ S)
     */
    private void createResultPlace(JPanel pnlResult) {
        lblResult = new JLabel("T _ _ E M _ S");
        lblResult.setFont(new Font("Courier New", Font.BOLD, 24)); // Kirjastiil ja suurus äraarvataval sõnal
        pnlResult.add(lblResult); // See paneel (pnlResult) on FlowLayout mitte GridBagLayout!
    }

    // Komponentide getterid

    public JLabel getLblGameTime() {
        return lblGameTime;
    }

    public JTextField getTxtChar() {
        return txtChar;
    }

    public JLabel getLblError() {
        return lblError;
    }

    public JButton getBtnSend() {
        return btnSend;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    public JLabel getLblImage() {
        return lblImage;
    }

    public JLabel getLblResult() {
        return lblResult;
    }
}
