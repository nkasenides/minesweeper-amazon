package model.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import model.CellState;
import model.GameState;

public class GameStateConverter implements DynamoDBTypeConverter<String, GameState> {

    @Override
    public String convert(GameState object) {
        return new Gson().toJson(object);
    }

    @Override
    public GameState unconvert(String object) {
        return new Gson().fromJson(object, GameState.class);
    }

}
