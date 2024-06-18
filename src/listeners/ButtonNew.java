package listeners;

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
    // TODO label tyhjaks, andmebaasist juhuslik sona, sona tahtede arvu jagu allkriipse
        // meetod, mis seotud juhusliku sonaga andmebaasist. kategooriast
    }
}
