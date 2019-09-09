package model.response;

import com.google.gson.JsonObject;
import com.panickapps.response.ErrorResponse;

public class MissingParameterResponse extends ErrorResponse {

    public MissingParameterResponse(String parameterName) {
        super("Missing parameter(s)", "Parameter '" + parameterName + "' is missing.");
    }

}
