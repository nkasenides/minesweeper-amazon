package model.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import model.GameState;
import model.PartialStatePreference;

public class PartialStatePreferenceConverter implements DynamoDBTypeConverter<String, PartialStatePreference> {

    @Override
    public String convert(PartialStatePreference object) {
        return new Gson().toJson(object);
    }

    @Override
    public PartialStatePreference unconvert(String object) {
        return new Gson().fromJson(object, PartialStatePreference.class);
    }

}
