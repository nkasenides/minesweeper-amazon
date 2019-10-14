package model.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import model.FullBoardState;
import model.GameState;

public class FullBoardStateConverter implements DynamoDBTypeConverter<String, FullBoardState> {

    @Override
    public String convert(FullBoardState object) {
        return new Gson().toJson(object);
    }

    @Override
    public FullBoardState unconvert(String object) {
        return new Gson().fromJson(object, FullBoardState.class);
    }

}
