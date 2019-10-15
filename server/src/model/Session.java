package model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import model.converters.PartialStatePreferenceConverter;

@DynamoDBTable(tableName = "Session")
public class Session {

    private String sessionID;
    private PartialStatePreference partialStatePreference;
    private String playerName;
    private String gameToken;
    private int positionCol;
    private int positionRow;
    private boolean spectator;
    private int points;

    public Session(String sessionID, PartialStatePreference partialStatePreference, String playerName, String gameToken, boolean spectator) {
        this.sessionID = sessionID;
        this.partialStatePreference = partialStatePreference;
        this.playerName = playerName;
        this.gameToken = gameToken;
        this.positionCol = 0;
        this.positionRow = 0;
        this.spectator = spectator;
        this.points = 0;
    }

    public Session() { }

    @DynamoDBHashKey(attributeName = "sessionID")
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    @DynamoDBAttribute(attributeName = "partialStatePreference")
    @DynamoDBTypeConverted(converter = PartialStatePreferenceConverter.class)
    public PartialStatePreference getPartialStatePreference() {
        return partialStatePreference;
    }

    public void setPartialStatePreference(PartialStatePreference partialStatePreference) {
        this.partialStatePreference = partialStatePreference;
    }

    @DynamoDBAttribute(attributeName = "playerName")
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @DynamoDBAttribute(attributeName = "gameToken")
    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    @DynamoDBAttribute(attributeName = "col")
    public int getPositionCol() {
        return positionCol;
    }

    public void setPositionCol(int positionCol) {
        this.positionCol = positionCol;
    }

    @DynamoDBAttribute(attributeName = "row")
    public int getPositionRow() {
        return positionRow;
    }

    public void setPositionRow(int positionRow) {
        this.positionRow = positionRow;
    }

    @DynamoDBAttribute(attributeName = "isSpectator")
    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    @DynamoDBAttribute(attributeName = "points")
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @DynamoDBIgnore
    public void changePoints(int points) {
        this.points += points;
    }

}
