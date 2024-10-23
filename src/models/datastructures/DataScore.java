package models.datastructures;

import java.time.LocalDateTime;

/**
 * See klass on edetabeli jaoks
 *
 * @param playerTime    Mägu lõpu aeg (mis kuupäeval ja kellaajal mäng lõppes)
 * @param playerName  Mängija nimi
 * @param guessWord        Äraarvatav sõna
 * @param wrongCharacters Valesti sisestatud märgid
 * @param gameTime Mängu aeg sekundites. Näiteks 69 (s.o. 1 min ja 9 sek)
 */
public record DataScore(LocalDateTime playerTime, String playerName, String guessWord, String wrongCharacters, int gameTime) {
    /**
     * Klassi konstruktor
     *
     * @param playerTime    mänguaegu lõpu aeg
     * @param playerName  mängija nimi
     * @param guessWord        äraarvatav sõna
     * @param wrongCharacters puuduvad tähed
     * @param gameTime mängu aeg sekundites
     */
    public DataScore {
    }
}
