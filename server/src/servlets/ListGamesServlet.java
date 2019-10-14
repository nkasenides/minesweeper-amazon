package servlets;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.panickapps.response.ErrorResponse;
import com.panickapps.response.SuccessResponse;
import model.Game;
import model.StatelessGame;
import model.response.ListGamesResponse;
import util.APIUtils;
import util.DynamoUtil;

import javax.ejb.Stateless;
import javax.naming.ldap.StartTlsRequest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ListGamesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        APIUtils.setResponseHeader(response);

        //1 - Get parameters:
        String startedOnlyStr = request.getParameter("startedOnly");

        boolean startedOnly = startedOnlyStr != null;


        DynamoDBMapper mapper = DynamoUtil.getMapper();

        HashMap<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":v1", new AttributeValue().withS("2015"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//                .withFilterExpression("begins_with(ReplyDateTime,:v1)")
//                .withExpressionAttributeValues(eav);

        //TODO started only?

        final List<Game> games =  mapper.scan(Game.class, scanExpression);
        ArrayList<StatelessGame> statelessGames = new ArrayList<>();

        for (final Game game : games) {
            StatelessGame sg = StatelessGame.fromGame(game);
            statelessGames.add(sg);
        }

        if (statelessGames.size() < 1) {
            response.getWriter().write(new SuccessResponse("Games fetched", "No games found").toJSON());
        }
        else {
            response.getWriter().write(new ListGamesResponse(statelessGames).toJSON());
        }


//        DynamoDB dynamoDB = new DynamoDB(DynamoUtil.get());
//        Table table = dynamoDB.getTable("Game");
//
//        QuerySpec spec = new QuerySpec()
////                .withKeyConditionExpression("Id = :v_id")
////                .withValueMap(new ValueMap()
////                        .withString(":v_id", "Amazon DynamoUtil#DynamoUtil Thread 1"));
//
//        ItemCollection<QueryOutcome> items = table.query(spec);
//
//        ArrayList<StatelessGame> statelessGames = new ArrayList<>();
//
//        Iterator<Item> iterator = items.iterator();
//        Item item = null;
//        while (iterator.hasNext()) {
//            item = iterator.next();
//            statelessGames.add(new StatelessGame())
//        }

        //5 - Process request:
//        try {
//            ArrayList<StatelessGame> statelessGames;
//            if (startedOnly) {
//                statelessGames = MinesweeperDB.listGames(true);
//
//            } else {
//                statelessGames = MinesweeperDB.listGames(false);
//            }
//            if (statelessGames.size() < 1) {
//                response.getWriter().write(new SuccessResponse("Games fetched", "No games found").toJSON());
//            }
//            else {
//                response.getWriter().write(new ListGamesResponse(statelessGames).toJSON());
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            response.getWriter().write(new ErrorResponse("Operation failed", "Unable to fetch games." + e.getMessage()).toJSON());
//        }

    }

}
