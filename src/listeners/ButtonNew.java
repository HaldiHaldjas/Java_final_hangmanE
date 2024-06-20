package listeners;

import models.Database;
import models.Model;
import views.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNew implements ActionListener {
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
         * genereerib andmebaasist valitud kategooria alusel juhusliku sõna
         */
        new Database(model).getWord(model.getSelectedCategory());
        view.getGameBoard().initGame();
        model.formatWordForDisplay(model.getRandomWord());
        // nulli valed arvamised
        model.resetWrongGuesses();

        // TODO label tyhjaks, andmebaasist juhuslik sona, sona tahtede arvu jagu allkriipse
    }

//    private void startNewGame(){
//        // mudeli reset
//        model.resetWrongGuesses();
//        view.clearErrorLabel();
//        // new word
//    }
}
