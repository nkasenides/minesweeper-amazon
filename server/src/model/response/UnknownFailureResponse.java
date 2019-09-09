package model.response;

import com.panickapps.response.ErrorResponse;

public class UnknownFailureResponse extends ErrorResponse {

    public UnknownFailureResponse(String message) {
        super("Unknown failure", message);
    }

}
