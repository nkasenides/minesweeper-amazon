package model.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import model.GameSpecification;
import model.GameState;

public class GameSpecificationConverter implements DynamoDBTypeConverter<String, GameSpecification> {

    @Override
    public String convert(GameSpecification object) {
        return new Gson().toJson(object);
    }

    @Override
    public GameSpecification unconvert(String object) {
        return new Gson().fromJson(object, GameSpecification.class);
    }

}
