package model;

import model.exception.InvalidGameSpecificationException;

import java.util.UUID;

public class GameSpecification {

    public static final Difficulty DEFAULT_DIFFICULTY = Difficulty.MEDIUM;

    private int maxPlayers; // max number of players
    private int width;
    private int height;
    private Difficulty difficulty;
    private String gameToken;

    private GameSpecification() { }

    public GameSpecification(int maxPlayers, int width, int height, Difficulty difficulty, String gameToken) {
        this.maxPlayers = maxPlayers;
        this.width = width;
        this.height = height;
        this.difficulty = difficulty;
        this.gameToken = gameToken;
    }

    public String getGameToken() {
        return gameToken;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public GameSpecification(int maxPlayers, int width, int height, String gameToken) throws InvalidGameSpecificationException {
        this(maxPlayers, width, height, DEFAULT_DIFFICULTY, gameToken);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

}
