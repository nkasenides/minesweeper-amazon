package model.response;

import com.google.gson.JsonObject;
import com.panickapps.response.SuccessResponse;

public class JoinedGameResponse extends SuccessResponse {

    public JoinedGameResponse(String gameToken, String sessionID) {
        super("Joined game", "You have joined game with token '" + gameToken + "'.");
        JsonObject data = new JsonObject();
        data.addProperty("sessionID", sessionID);
        setData(data);
    }

}
