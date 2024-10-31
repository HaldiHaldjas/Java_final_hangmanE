package views.panels;

import models.Database;
import models.Model;
import views.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * See on edetabeli klass. See näitab andmebaasist loetud edetabelit. Seda ei saa mängimise ajal
 * vaadata.
 */
public class LeaderBoard extends JPanel {
    /**
     * Klassisisene mudel, mille väärtus saadakse View konstruktorist ja loodud MainApp-is
     */
    private final Model model;
    /**
     * Klassisisene vaade, mille väärtus saadakse otse View-st
     */
    private final View view;
    /**
     *  Tabeli päis, mida naeb edetabeli vahelehel
     */
    private static String[] heading = new String[]{"Kuupäev", "Nimi", "Sõna", "Tähed", "Mänguaeg"};
    /**
     * Loome tabeli teostuse päisega, kuid andmeid ei ole
     * */
    private DefaultTableModel dtm;
    /**
     * Loome tabeli dtm baasil
     * */
    private static JTable table;

    /**
     * Leaderboard konstruktor
     * @param model loodud mudel MainAppis
     * @param view loodud view MainAppis
     * Need on loodud mainAppis ja töös koguaeg, kui rakendus töötab
     * argumendina tuleb kaasa anda, kuna neid kasutatakse pidevalt erinevates kohtades
     * */
    public LeaderBoard(Model model, View view) {
        this.model = model;
        this.view = view;

        setLayout(new BorderLayout()); // kuna tegemist on paneeliga, tuleb paneeli layout panna
        setBackground(new Color(250, 150, 215)); // Leaderboard paneeli taustavärv
        setBorder(new EmptyBorder(5, 5, 5, 5));

        this.dtm = new DefaultTableModel(heading, 0);
        this.table = new JTable(dtm);

        model.setDtm(dtm); // dtm on klassisisene
        createLeaderboard(); // Loob edetabeli alumise meetodi alusel

    }

    private void createLeaderboard() {
        // vajadusel aktiveerub paremas servas kerimisriba
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER); // keskele, kuna CENTER ise suureneb

        // Tabeli esimene veerg 120 px
        table.getColumnModel().getColumn(0).setPreferredWidth(120);

        // Tabeli sisu pole muudetav
        table.setDefaultEditor(Object.class, null);

        // Lahtri keskele joondamine
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        loadData();
        // table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        // cellRenderer.setHorizontalAlignment(JLabel.LEFT);
        // table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
    }
        // Kirjuta tabelist sisu mudelisse
    private void loadData() {
        Database database = model.getDatabase();
        database.selectScores(); // teeb andmebaasi faili meetodi
        // kontrolli, kas on andmeid ja uuenda tabelit
        if (!model.getDataScores().isEmpty()) { // kui list pole tyhi
        view.updateScoresTable();
        // view.showLeaderboard();

        } else {
            JOptionPane.showMessageDialog(view, "Esmalt tuleb mangida!");
        }
    }
}
