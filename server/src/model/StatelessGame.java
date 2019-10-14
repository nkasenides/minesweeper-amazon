package model;

public class StatelessGame {

    private final GameSpecification gameSpecification;
    private final GameState gameState;
    private final String token;

    public StatelessGame(GameSpecification gameSpecification, GameState gameState, String gameToken) {
        this.gameSpecification = gameSpecification;
        this.gameState = gameState;
        this.token = gameToken;
    }

    public GameSpecification getGameSpecification() {
        return gameSpecification;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getToken() {
        return token;
    }

    public static StatelessGame fromGame(Game game) {
        return new StatelessGame(game.getGameSpecification(), game.getGameState(), game.getToken());
    }

}