package listeners;

import models.Database;
import models.Model;
import views.View;
import views.panels.LeaderBoard;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * See klass tegeleb edetabeli nupu funktsionaalsusega
 * Kui nuppu klikata, kuvatakse edetabeli andmed uues dialoogaknas
 */
public class ButtonScores implements ActionListener {
    /**
     *Mudel, mis sisaldab andmeid ja rakenduse loogikat
     */
    private final Model model;

    /**
     * Kuvab rakenduse kasutajaliidese
     */
    private final View view;
    /**
     * Tabeli päis, mida kuvatakse dialoogaknas
     */
    private final String[] header = new String[] {"Kuupäev", "Nimi", "Sõna", "Tähed", "Mänguaeg"};
    /**
     * See sisaldab edetabeli andmeid
     */
    private final DefaultTableModel dtm = new DefaultTableModel(header,0);
    /**
     * Kuvab edetabeli andmed
     */
    private final JTable table = new JTable(dtm);
    /**
     * Dialoogaken, kuvab edetabeli
     */
    private JDialog dialogScore;

    /**
     * Konstruktor, mis seab mudeli ja vaate ja loob edetabeli uues aknas
     * @param model Mudel sisaldab andmeid ja rakenduse loogikat
     * @param view Vaade, mis kuvab rakenduse kasutajaliidest
     */
    public ButtonScores(Model model, View view) {
        this.model = model;
        this.view = view;
        model.setDtm(dtm);
        createDialog();
    }

    /**
     * Meetod, mis loob edetabeli uues aknas ja seab selle omadused
     */
    private void createDialog() {
        dialogScore = new JDialog();
        dialogScore.setPreferredSize(new Dimension(530, 180));

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(center);

        dialogScore.add(new JScrollPane(table));
        dialogScore.setTitle("Edetabel");
        dialogScore.pack();
        dialogScore.setLocationRelativeTo(null);
        dialogScore.setModal(true);
    }

    /**
     * Meetod, mida kutsutakse, kui nuppu klikatakse
     * Kuvab edetabeli andmed uues aknas
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Database database = model.getDatabase();
        database.selectScores();

        if (!model.getDataScores().isEmpty()) {
            view.updateScoresTable();
            dialogScore.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(view, "Edetabel on veel tühi! Alusta mänguga!");
        }
    }
}
