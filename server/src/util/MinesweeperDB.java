package util;

import com.google.gson.Gson;
import model.*;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

public class MinesweeperDB {

    private static final String DB_URL = "jdbc:mysql://minesweeperdb.cdrlmrmeqih0.us-east-2.rds.amazonaws.com";
    private static final String DB_NAME = "minesweeper";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "nk123nk123";

    public static void addGame(final Game game) throws SQLException {
        Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        String query = "INSERT INTO Game (boardState, difficulty, maxPlayers, height, width, gameState, token) VALUES (?,?,?,?,?,?,?);";

        Gson gson = new Gson();
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, gson.toJson(game.getFullBoardState()));
        preparedStmt.setString(2, game.getGameSpecification().getDifficulty().toString());
        preparedStmt.setInt(3, game.getGameSpecification().getMaxPlayers());
        preparedStmt.setInt(4, game.getGameSpecification().getHeight());
        preparedStmt.setInt(5, game.getGameSpecification().getWidth());
        preparedStmt.setString(6, game.getGameState().toString());
        preparedStmt.setString(7, game.getGameSpecification().getGameToken());
        preparedStmt.execute();
        DBConnection.close();
    }

    public static ArrayList<StatelessGame> listGames(boolean startedOnly) throws SQLException {
        final Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        String query = "SELECT * FROM Game";
        final String postfix = (startedOnly) ? " WHERE gameState='STARTED';" : ";";
        query += postfix;

        ArrayList<StatelessGame> games = new ArrayList<>();

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            final Difficulty difficulty = Difficulty.valueOf(rs.getString("difficulty"));
            final int maxPlayers = rs.getInt("maxPlayers");
            final int width = rs.getInt("width");
            final int height = rs.getInt("height");
            final GameState gameState = GameState.valueOf(rs.getString("gameState"));
            final String token = rs.getString("token");
            StatelessGame game = new StatelessGame(new GameSpecification(maxPlayers, width, height, difficulty, token), gameState);
            games.add(game);
        }
        statement.close();
        DBConnection.close();
        return games;
    }

    public static StatelessGame getGame(final String gameToken) throws SQLException {
        final Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        final String query = "SELECT * FROM Game WHERE token='" + gameToken + "' LIMIT 1;";
        final Statement statement = connection.createStatement();
        final ResultSet rs = statement.executeQuery(query);
        StatelessGame statelessGame = null;
        while (rs.next()) {
            final Difficulty difficulty = Difficulty.valueOf(rs.getString("difficulty"));
            final int maxPlayers = rs.getInt("maxPlayers");
            final int width = rs.getInt("width");
            final int height = rs.getInt("height");
            final GameState gameState = GameState.valueOf(rs.getString("gameState"));
            final String token = rs.getString("token");
            statelessGame = new StatelessGame(new GameSpecification(maxPlayers, width, height, difficulty, token), gameState);
        }
        statement.close();
        DBConnection.close();
        return statelessGame;
    }

}
