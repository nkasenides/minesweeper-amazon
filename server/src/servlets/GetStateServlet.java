package servlets;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;
import com.panickapps.response.ErrorResponse;
import com.panickapps.response.SuccessResponse;
import model.*;
import model.exception.InvalidCellReferenceException;
import model.response.GetStateResponse;
import model.response.MissingParameterResponse;
import model.response.UnknownFailureResponse;
import util.APIUtils;
import util.DynamoUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class GetStateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        APIUtils.setResponseHeader(response);

        //1 - Get parameters:
        String sessionID = request.getParameter("sessionID");

        //3 - Required params:

        if (sessionID == null) {
            response.getWriter().write(new MissingParameterResponse("sessionID").toJSON());
            return;
        }

        //4 - Validate params:
        Session session = null;

        DynamoDBMapper mapper = DynamoUtil.getMapper();
        HashMap<String, AttributeValue> eav = new HashMap<>();
        eav.put(":v1", new AttributeValue().withS(sessionID));
        DynamoDBScanExpression gameScanExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with(sessionID,:v1)")
                .withExpressionAttributeValues(eav);

        final List<Session> sessions =  mapper.scan(Session.class, gameScanExpression);
        session = sessions.get(0);

        if (session == null) {
            response.getWriter().write(new ErrorResponse("Session not found", "Could not find session with ID '" + sessionID + "'").toJSON());
            return;
        }

        Game game = null;
        HashMap<String, AttributeValue> eav2 = new HashMap<>();
        eav2.put(":v1", new AttributeValue().withS(session.getGameToken()));
        DynamoDBScanExpression sessionExpression = new DynamoDBScanExpression()
                .withFilterExpression("begins_with(gameToken,:v1)")
                .withExpressionAttributeValues(eav2);

        final List<Game> games =  mapper.scan(Game.class, sessionExpression);
        game = games.get(0);

        if (game == null) {
            response.getWriter().write(new ErrorResponse("Game not found", "Could not find game with token '" + session.getGameToken() + "'").toJSON());
            return;
        }

        PartialStatePreference partialStatePreference = session.getPartialStatePreference();

        if (session.getPositionCol() + partialStatePreference.getWidth() > game.getGameSpecification().getWidth()
                || session.getPositionRow() + partialStatePreference.getHeight() > game.getGameSpecification().getHeight()
                || session.getPositionCol() < 0 || session.getPositionRow() < 0)
        {
            response.getWriter().write(new ErrorResponse("Game not found", "The partial state with row: " + session.getPositionRow() + ", col: " + session.getPositionCol() + ", width: " + game.getGameSpecification().getWidth() + ", height: " + game.getGameSpecification().getHeight() + " is not valid.").toJSON());
            return;
        }

        HashMap<String, AttributeValue> cellVal = new HashMap<>();
        cellVal.put(":v1", new AttributeValue().withN(String.valueOf(session.getPositionRow())));
        cellVal.put(":v2", new AttributeValue().withN(String.valueOf(session.getPositionCol())));
        cellVal.put(":v3", new AttributeValue().withN(String.valueOf(session.getPositionRow() + partialStatePreference.getHeight())));
        cellVal.put(":v4", new AttributeValue().withN(String.valueOf(session.getPositionCol() + partialStatePreference.getWidth())));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("cellRow >= :v1 and cellCol >= :v2 and cellRow < :v3 and cellCol < :v4")
                .withExpressionAttributeValues(cellVal);

        List<Cell> cells =  mapper.scan(Cell.class, scanExpression);

//        //1. Sort by row:
//        cells.sort(new Comparator<Cell>() {
//            @Override
//            public int compare(Cell o1, Cell o2) {
//                return Integer.compare(o1.getRow(), o2.getRow());
//            }
//        });

        //2. Break-off into sub-arrays based on rows:






//        int minRow = Integer.MAX_VALUE;
//        int maxRow = Integer.MIN_VALUE;
//        int minCol = Integer.MAX_VALUE;
//        int maxCol = Integer.MIN_VALUE;
//
//        for (final Cell c : cells) {
//            if (c.getRow() > maxRow) maxRow = c.getRow();
//            if (c.getCol() > maxCol) maxCol = c.getCol();
//            if (c.getRow() < minRow) minRow = c.getRow();
//            if (c.getCol() < minCol) minCol = c.getCol();
//        }



        response.getWriter().write(new SuccessResponse("Success", new Gson().toJson(cells)).toJSON());




//        try {
//            PartialBoardState partialBoardState = new PartialBoardState(partialStatePreference.getWidth(), partialStatePreference.getHeight(), session.getPositionRow(), session.getPositionCol(), game.getFullBoardState());
//            response.getWriter().write(new GetStateResponse(partialBoardState, game.getGameState(), sessionID).toJSON());
//        }
//        //If failed to get the partial state, return error:
//        catch (InvalidCellReferenceException e) {
//            response.getWriter().write(new ErrorResponse("Error fetching partial state for session '" + sessionID + "'.", e.getMessage()).toJSON());
//        }


    }
}
