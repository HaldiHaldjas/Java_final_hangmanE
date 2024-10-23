package helpers;

import views.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * See klass tegeleb ainult mängu ajal nähtava ajaga
 */
public class GameTimer {
    /**
     * Sekundid
     */
    private int seconds;
    /**
     * Minutid
     */
    private int minutes;
    /**
     * Mängu aeg kas käib või mitte
     */
    private boolean running;
    /**
     * Java sisseehitatud klass aja jaoks Swing mitte util!
     * <a href="https://docs.oracle.com/javase/8/docs/api/javax/swing/Timer.html">Timer dokumentatsioon</a>
     */
    private Timer timer;

    /**
     * Mänguaja kontruktor. See objekt luuakse alati uuesti, kui mäng algab
     * @param view
     */
    public GameTimer(View view) {
        this.minutes = 0; // Alg väärtustamine
        this.seconds = 0; // Alg väärtustamine
        this.running = false; // Alg väärtustamine
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds += 1; // suurendame sekundeid ühe võrra
                if(seconds >= 60) { // Kui sekundeid on 60 või rohkem
                    seconds = 0; // sekundid nulli
                    minutes += 1; // minutid kasvavad ühe võrra
                }
                view.getGameBoard().getLblGameTime().setText(formatGameTime()); // Näita aega labelil
            }
        });
    }

    /**
     * Vorminda aeg (minutid ja sekundid) inimlikule vormingule 00:00
     * @return vormindatud mänguaeg
     */
    private String formatGameTime() {
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Käivitame mänguaja
     */
    public void startTime() {
        this.timer.start();
    }

    /**
     * Peatame mänguaja
     */
    public void stopTime() {
        this.timer.stop();
    }

    /**
     * Kas mängu aeg jookseb?
     * @return tagastab true (jah) või false (ei)
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Seadistab mänguaja kas käima või sesima
     * @param running true või false
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Seadistab minutid
     * @param minutes on täisarv (0-59)
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Seadistab sekundid
     * @param seconds on täisarv (0-59)
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Teeb mänguaja sekunditeks. Vajalik edetabeli jaoks
     * @return minutid korrutada 60 pluss sekundid
     */
    public int getPlayedTimeInSeconds() {
        return (this.minutes * 60) + seconds;
    }

    public int getElapsedTimeInSeconds() {
        return getPlayedTimeInSeconds();
        // Calculate and return the elapsed time in seconds
        // The implementation depends on how you're tracking time in this class
    }

    // TODO Seda meetodit ilmselt ei lähe vaja, kuid edasiarenduse eesmärgil jätame. See pole "lollikindel".
    /**
     * Lisada mängu ajale aega (karistus)
     * @param seconds on täisarv (0-59)
     */
    public void addSeconds(int seconds) {
        if((this.seconds + seconds) >= 60) {
            minutes++;
            this.seconds = (this.seconds + seconds) - 60;
        } else {
            this.seconds += seconds;
        }
    }
}
