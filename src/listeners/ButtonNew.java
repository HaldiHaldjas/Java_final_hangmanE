package listeners;

import models.Database;
import models.Model;
import models.Database;
import models.datastructures.DataWords;
import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ButtonNew implements ActionListener {
    static {
        Logger.getLogger(ButtonNew.class.getName());
    }
    // klassisisesed muutujad
    private Model model;
    private View view;

    public ButtonNew(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("Klikk new game");
        view.hideButtons();
        // kontroll, kas aeg jookseb
        if(!view.getGameTimer().isRunning()) { // kui mängu aeg ei jookse
            view.getGameTimer().setSeconds(0); // pane aeg sekundites nulli
            view.getGameTimer().setMinutes(0);
            view.getGameTimer().setRunning(true); // aeg jooksma
            view.getGameTimer().startTime();
        } else {
            view.getGameTimer().stopTime();
            view.getGameTimer().setRunning(false);
        }

        /**
         * loob andmebasiga ühenduse ja genereerib andmebaasist valitud kategooria alusel juhusliku sõna
         */
        Database database = model.getDatabase(); // uus DB
        DataWords word = null; // words andmebaasist
        try {
            word = database.getWord(model.getSelectedCategory());
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (word != null) {
            // Alustab mängu uue sõnaga
            model.startNewGame(word.word());
            view.setFirstPicture();
            view.getGameBoard().displayWord(word.word());
            view.getGameBoard().getLblError().setText("");
            model.resetWrongGuesses();
            model.formatWordForDisplay(model.getRandomWord());

        } else {
            System.out.println("No word found for the chosen category.");
            JOptionPane.showMessageDialog(view, "Sõna ei ole valitud!");
        }

    }

}
