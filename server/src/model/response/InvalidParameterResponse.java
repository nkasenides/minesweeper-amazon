package model.response;

import com.panickapps.response.ErrorResponse;

public class InvalidParameterResponse extends ErrorResponse {

    public InvalidParameterResponse(String message) {
        super("Invalid parameter", message);
    }

}
