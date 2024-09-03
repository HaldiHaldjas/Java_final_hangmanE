package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import models.datastructures.DataScore;
import models.datastructures.DataWords;

/**
 * See klass tegeleb andmebaasi ühenduse ja "igasuguste" päringutega tabelitest.
 * Alguses on ainult ühenduse jaoks funktsionaalsus
 */
public class Database {
    private static Database instance;
    /**
     * Algselt ühendust pole
     */
    private Connection connection;
    /**
     * Andmebaasi ühenduse string
     */
    private final String databaseUrl;
    /**
     * Loodud mudel
     */
    private final Model model;

    /**
     * Klassi andmebaas konstruktor
     * @param model loodud mudel
     */
    public Database (Model model) {
        this.model = model;
        this.databaseUrl = "jdbc:sqlite:" + model.getDatabaseFile();
        this.connection = dbConnection();
        this.selectUniqueCategories();
    }

    /**
     * Loob andmebaasiga ühenduse
     * @return andmebaasi ühenduse
     * Meetod kasutab Singletoni mustrit, mis tagab, et klassil on vaid üks ühendus korraga avatud
     * ning sellele saab terve projekti ulatuses ligi.*/

    public static Database getInstance(Model model) {
        if (instance == null) {
            instance = new Database(model);
        }
        return instance;
    }

    private Connection dbConnection() {
        try {
            Connection connection = DriverManager.getConnection(databaseUrl);
            // loob sql-lause sisestamiseks objekti
            Statement stmt = connection.createStatement();
            // timeout - kui kaua peaks ühendust üritama kui tabel on lukus
            stmt.execute("PRAGMA busy_timeout = 10000");
            stmt.close();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Andmebaasiga ühendumine ebaõnnestus");
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = dbConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check or re-establish connection");
        }
        return connection;
    }

    /**
     * Valib andmebaasist unikaalsed kategooriad ja uuendab mudelit nende kategooriatega
     */
    private void selectUniqueCategories() {
        String sql = "SELECT DISTINCT(category) as category FROM words ORDER BY category;";
        List<String> categories = new ArrayList<>();
        try{

            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String category = rs.getString("category");
                categories.add(category); // lisa kategooria listi kategooriad
            }
            categories.addFirst(model.getChooseCategory()); // "Kõik kategooriad" esimeseks
            String[] result = categories.toArray(new String[0]); // list<String> = string[]
            model.setCmbCategories(result); // seadista kategooriad mudelisse
            // System.out.println(categories.toString()); // test, kas kategooriad on olemas
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Valib edetabeli andmed andmebaasist ja uuendab mudelit nende andmetega
     */
    public synchronized void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername;";
        List<DataScore> data = new ArrayList<>();
        try {

            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            model.getDataScores().clear(); // tühjenda mudeli listi sisu

            while (rs.next()) {
                String datetime = rs.getString("playertime");
                // mänguaeg konverteeritakse anbmebaasi ajaga ühilduvaks
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                // System.out.println(datetime + " | " + playertime);
                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongChar = rs.getString("wrongcharacters");
                int timeSeconds = rs.getInt("gametime");
                data.add(new DataScore(playerTime, playerName, guessWord, wrongChar, timeSeconds));
                System.out.println("Uus datascore: " + playerName + " " + guessWord + " " + wrongChar + " " + timeSeconds);

            }
            model.setDataScores(data); // Muuda andmeid mudelis, kust saab info katte

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Päring juhuslik sõna andmebaasist kategooria alusel
    public DataWords getWord(String chosenCategory) {
        // String sql = "SELECT * FROM words WHERE category = '" + chosenCategory + "';";
        // String sql = "SELECT word FROM words WHERE category LIKE ? ORDER BY random() LIMIT 1;";
        String sql = "SELECT id, word, category FROM words WHERE category LIKE ? ORDER BY random() LIMIT 1;";
        // System.out.println("Valitud kategooria: " + chosenCategory);
        try {

            PreparedStatement randomWord = getConnection().prepareStatement(sql);
            if (chosenCategory.equals("Kõik kategooriad")){
                randomWord.setString(1,"%");
            } else {
                // valib kategooria hulgast juhusliku sõna
                randomWord.setString(1, "%" + chosenCategory + "%");
            }
            ResultSet rs = randomWord.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String wordCategory = rs.getString("category");
                return new DataWords(id, word, wordCategory);
            }

            String word = rs.getString("word");
            model.setRandomWord(word); // juhuslik sõna kirjutatakse modelisse
            // System.out.println("Juhuslik sõna DB klassist: " + word);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    return null;
    }

//    public void saveScoreToDatabase(DataScore score) {
//        // int gameTimer = view.getGameTimer();
//        if (view == null) {
//            System.err.println("Vaade on null Database.saveScoreToDatabase() meetodis");
//            return;
//        }
//
//        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?,?,?,?,?)";
//
//        try (Connection conn = this.dbConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//
//            // stmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            stmt.setString(2, model.getPlayerName());
//            stmt.setString(3, model.getRandomWord());
//            stmt.setString(4, model.getWrongGuesses()); // Assuming this method retrieves the wrong characters
//            stmt.setInt(5, view.getGameTimer().getPlayedTimeInSeconds());
//
//            int rowsInserted = stmt.executeUpdate(); // Execute the insert statement
//
//            if (rowsInserted > 0) {
//                System.out.println("A new score was inserted successfully.");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Error inserting scores into database", e);
//        }
//    }

    /**
     * Salvestab mängu andmed andmebaasi
     * @param playerName    Mängija nimi
     * @param guessWord     Äraarvatud sõna
     * @param wrongCharacters   Valesti pakutud tähed
     * @param gameTime      Mänguaeg sekundites
     */
    public synchronized void saveScoreToDatabase(
            String playerName,
            String guessWord,
            String wrongCharacters,
            int gameTime) {
        // int gameTimer = view.getGameTimer();
//        if (view == null) {
//            System.err.println("Vaade on null Database.saveScoreToDatabase() meetodis");
//            return;
//        }

//        DataScore score = new DataScore(
//                LocalDateTime.now(),
//                playerName,
//                model.getRandomWord(),
//                model.getWrongGuesses(),
//                gameTimer.getElapsedTimeInSeconds()
//        );

        String sql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?,?,?,?,?)";
        System.out.println("Starting to save score to database..." + sql);

        try {
            PreparedStatement stmt = getConnection().prepareStatement(sql);

            stmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            stmt.setString(2, playerName);
            stmt.setString(3, guessWord);
            stmt.setString(4, formatWrongCharacters(wrongCharacters)); // Assuming this method retrieves the wrong characters
            stmt.setInt(5, gameTime);

            int rowsInserted = stmt.executeUpdate(); // Execute the insert statement
            if (rowsInserted > 0) {
                System.out.println("A new score was inserted successfully.");
            }
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving scores into database", e);
        }
    }

    /**
     * @param wrongCharacters Vormistab valesti pakutud tähed array asemel stringina
     * @return Vormindatud string ilma kantsulgudeta.
     */
    private String formatWrongCharacters(String wrongCharacters) {
        return wrongCharacters.replaceAll("[\\[\\]]", "").replaceAll(", ", ", ");
    }

//    private int gametimer() {
//    }


//    private void getWords() {
//
//        String cmbCategories = model.getChooseCategory();
//        System.out.println(cmbCategories);
//
//        String sql = "SELECT word FROM words;";
//        List<String> words = new ArrayList<>();
//        try {
//            Connection connection = this.dbConnection();
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(sql);
//
//            while (rs.next()) {
//                words.add(rs.getString("word"));
//            }
//
//            model.getDatabaseWords(words);
//            System.out.println("sõnad andmebaasist" + words);
//            connection.close();
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public void getWords(String category) {
//        List<String> words = new ArrayList<>();
//        String query;
//
//        try (Connection connection = dbConnection()) {
//            if (category.equalsIgnoreCase(model.getChooseCategory())) {
//                query = "SELECT word FROM words";
//            } else {
//                query = "SELECT word FROM words WHERE category = ?";
//            }
//
//            PreparedStatement stmt = connection.prepareStatement(query);
//            if (!category.equalsIgnoreCase(model.getChooseCategory())) {
//                stmt.setString(1, category);
//            }
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                String word = rs.getString("word");
//                words.add(word);
//            }
//
//            // Print statements for debugging
//            String selectedCategory = model.getSelectedCategory();
//            System.out.println("Selected category: " + selectedCategory);
//            System.out.println("Words from database: " + words);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return words;
//    }

}