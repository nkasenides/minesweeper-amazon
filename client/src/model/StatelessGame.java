package model;

public class StatelessGame {

    private final GameSpecification gameSpecification;
    private final GameState gameState;

    public StatelessGame(GameSpecification gameSpecification, GameState gameState) {
        this.gameSpecification = gameSpecification;
        this.gameState = gameState;
    }

    public GameSpecification getGameSpecification() {
        return gameSpecification;
    }

    public GameState getGameState() {
        return gameState;
    }

}