package model.response;

import com.google.gson.JsonObject;
import com.panickapps.response.JsonUtil;
import com.panickapps.response.SuccessResponse;
import model.Game;
import model.StatelessGame;

import java.util.List;

public class ListGamesResponse extends SuccessResponse {

    public ListGamesResponse(List<StatelessGame> games) {
        super("Games fetched", "A list of games has been fetched.");
        JsonObject data = new JsonObject();
        data.add("games", JsonUtil.listToJsonArray(games));
        setData(data);
    }

}
