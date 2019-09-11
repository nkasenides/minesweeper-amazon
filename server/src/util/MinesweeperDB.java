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

    public static StatelessGame getStatelessGame(final String gameToken) throws SQLException {
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

    public static Game getGame(final String gameToken) throws SQLException {
        final Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        final String query = "SELECT * FROM Game WHERE token='" + gameToken + "' LIMIT 1;";
        final Statement statement = connection.createStatement();
        final ResultSet rs = statement.executeQuery(query);
        Game game = null;
        while (rs.next()) {
            final Difficulty difficulty = Difficulty.valueOf(rs.getString("difficulty"));
            final int maxPlayers = rs.getInt("maxPlayers");
            final int width = rs.getInt("width");
            final int height = rs.getInt("height");
            final GameState gameState = GameState.valueOf(rs.getString("gameState"));
            final String token = rs.getString("token");
            game = new Game(new GameSpecification(maxPlayers, width,height, difficulty, token));
            game.setGameState(gameState);
        }
        statement.close();
        DBConnection.close();
        return game;
    }

    public static Session getSession(final String sessionID) throws SQLException {
        final Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        final String query = "SELECT * FROM Session WHERE sessionID='" + sessionID + "' LIMIT 1;";
        final Statement statement = connection.createStatement();
        final ResultSet rs = statement.executeQuery(query);
        Session session = null;
        while (rs.next()) {
            final String playerName = rs.getString("playerName");
            final int sessionWidth = rs.getInt("partialStateWidth");
            final int sessionHeight = rs.getInt("partialStateHeight");
            final String gameToken = rs.getString("gameToken");
            final boolean spectator = rs.getBoolean("spectator");
            session = new Session(new PartialStatePreference(sessionWidth, sessionHeight), playerName, gameToken, spectator);
        }
        statement.close();
        DBConnection.close();
        return session;
    }

    public static boolean playerIsInGame(final String playerName, final String gameToken) throws SQLException {
        final Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        final String query = "SELECT * FROM Session WHERE playerName='" + playerName + "' AND gameToken='" + gameToken + "';";
        final Statement statement = connection.createStatement();
        final ResultSet rs = statement.executeQuery(query);
        int count = 0;
        while (rs.next()) {
            count++;
        }
        statement.close();
        DBConnection.close();
        return count > 0;
    }

    public static int numOfSessionsInGame(final String gameToken) throws SQLException {
        final Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        final String query = "SELECT * FROM Session WHERE gameToken='" + gameToken + "';";
        final Statement statement = connection.createStatement();
        final ResultSet rs = statement.executeQuery(query);
        int count = 0;
        while (rs.next()) {
            count++;
        }
        statement.close();
        DBConnection.close();
        return count;
    }

    public static void createSession(final Session session) throws SQLException {
        Connection connection = DBConnection.connect(DB_URL, DB_NAME, USERNAME, PASSWORD);
        String query = "INSERT INTO Session (gameToken, partialStateWidth, partialStateHeight, playerName, points, positionCol, positionRow, sessionID, spectator) VALUES (?,?,?,?,?,?,?,?,?);";
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        preparedStmt.setString(1, session.getGameToken());
        preparedStmt.setInt(2, session.getPartialStatePreference().getWidth());
        preparedStmt.setInt(3, session.getPartialStatePreference().getHeight());
        preparedStmt.setString(4, session.getPlayerName());
        preparedStmt.setInt(5, session.getPoints());
        preparedStmt.setInt(6, session.getPositionCol());
        preparedStmt.setInt(7, session.getPositionRow());
        preparedStmt.setString(8, session.getSessionID());
        preparedStmt.setBoolean(9, session.isSpectator());
        preparedStmt.execute();
        DBConnection.close();
    }

}
