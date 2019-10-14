package model.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import model.CellState;

public class CellStateConverter implements DynamoDBTypeConverter<String, CellState> {

    @Override
    public String convert(CellState object) {
        return new Gson().toJson(object);
    }

    @Override
    public CellState unconvert(String object) {
        return new Gson().fromJson(object, CellState.class);
    }

}
