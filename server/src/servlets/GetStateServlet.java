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

        try {
            game = games.get(0);
        }
        catch (IndexOutOfBoundsException e) {
            response.getWriter().write(new ErrorResponse("Game not found", "Could not find game with token '" + session.getGameToken() + "'").toJSON());
            return;
        }

        if (game == null) {
            response.getWriter().write(new ErrorResponse("Game not found", "Could not find game with token '" + session.getGameToken() + "'").toJSON());
            return;
        }

        PartialStatePreference partialStatePreference = session.getPartialStatePreference();

        try {
            PartialBoardState partialBoardState = new PartialBoardState(partialStatePreference.getWidth(), partialStatePreference.getHeight(), session.getPositionRow(), session.getPositionCol(), game.getFullBoardState());
            response.getWriter().write(new GetStateResponse(partialBoardState, game.getGameState(), sessionID).toJSON());
        }
        //If failed to get the partial state, return error:
        catch (InvalidCellReferenceException e) {
            response.getWriter().write(new ErrorResponse("Error fetching partial state for session '" + sessionID + "'.", e.getMessage()).toJSON());
        }


    }
}
