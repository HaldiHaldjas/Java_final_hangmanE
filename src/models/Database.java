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
        this.connection = createConnection();
        this.selectUniqueCategories();
    }

//    public static Database getInstance(Model model) {
//        if (instance == null) {
//            instance = new Database(model);
//        }
//        return instance;
//    }

    /**
     * Loob andmebaasiga ühenduse
     * @return andmebaasi ühenduse
     * Meetod kasutab Singletoni mustrit, mis tagab, et klassil on vaid üks ühendus korraga avatud
     * ning sellele saab terve projekti ulatuses ligi.*/
    private Connection createConnection() {
        try {
            Connection conn = DriverManager.getConnection(databaseUrl);
            Statement stmt = conn.createStatement();
            // timeout - kui kaua peaks ühendust üritama kui tabel on lukus
            stmt.execute("PRAGMA busy_timeout = 5000");
            stmt.close();
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Andmebaasiga ühendumine ebaõnnestus");
        }
    }

    /**
     * Kontrollib andmebaasi ühenduse olemasolu
     * @return connection
     */
    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
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
        try {
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                String category = rs.getString("category");
                categories.add(category); // lisa kategooria listi kategooriad
            }
            categories.addFirst(model.getChooseCategory()); // "Kõik kategooriad" esimeseks
            String[] result = categories.toArray(new String[0]); // list<String> = string[]
            model.setCmbCategories(result); // seadista kategooriad mudelisse
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Valib edetabeli andmed andmebaasist ja uuendab mudelit nende andmetega
     */
    public void selectScores() {
        String sql = "SELECT * FROM scores ORDER BY gametime, playertime DESC, playername;";
        List<DataScore> scoreData = new ArrayList<>();

        try (Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            model.getDataScores().clear(); // Clear the model's data list

            while (rs.next()) {
                String datetime = rs.getString("playertime");
                LocalDateTime playerTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                String playerName = rs.getString("playername");
                String guessWord = rs.getString("guessword");
                String wrongCharacters = rs.getString("wrongcharacters");
                int gameTime = rs.getInt("gametime");

                scoreData.add(new DataScore(playerTime, playerName, guessWord, wrongCharacters, gameTime));
            }

            model.setDataScores(scoreData); // Update the model with the new data
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Päring juhuslik sõna andmebaasist kategooria alusel
     * @param chosenCategory Kategooria, millest sõna valitakse
     * @return väljastab objekti, mis sisaldab sõna ja selle kategooriat
     */
    public DataWords getWord(String chosenCategory) throws SQLException {
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
            } else {
                // No word found for the chosen category
                // You can either return null or throw an exception here
                System.out.println("No word found for category: " + chosenCategory);
                return null; // Or throw a custom exception
            }

            // String word = rs.getString("word");
            // model.setRandomWord(word); // juhuslik sõna kirjutatakse modelisse
            // System.out.println("Juhuslik sõna DB klassist: " + word);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            // return null;
    }

    public synchronized void saveScoreToDatabase(String playerName, String guessWord, String wrongCharacters, int gameTime) {
        String insertSql = "INSERT INTO scores (playertime, playername, guessword, wrongcharacters, gametime) VALUES (?,?,?,?,?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(insertSql)){

            stmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            stmt.setString(2, playerName);
            stmt.setString(3, guessWord);
            stmt.setString(4, formatWrongCharacters(wrongCharacters));
            stmt.setInt(5, gameTime);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data saved successfully");
            }
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error saving data: " + e.getMessage());
            throw new RuntimeException(e);  // Handle exception by throwing runtime exception
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                // Handle connection closing exception (optional)
            }
        }
    }

    /**
     * @param wrongCharacters Vormistab valesti pakutud tähed array asemel stringina
     * @return Vormindatud string ilma kantsulgudeta.
     */
    private String formatWrongCharacters(String wrongCharacters) {
        return wrongCharacters.replaceAll("[\\[\\]]", "").replaceAll(", ", ", ");
    }
}
