package servlets;

import com.panickapps.response.ErrorResponse;
import model.Game;
import model.PartialBoardState;
import model.PartialStatePreference;
import model.Session;
import model.exception.InvalidCellReferenceException;
import model.response.GetStateResponse;
import model.response.MissingParameterResponse;
import util.APIUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        //TODO GET SESSION BY SESSION ID.
//        final List<Session> sessionList = ofy().load().type(Session.class).filter("sessionID", sessionID).limit(1).list();
//        if (sessionList.size() < 1) {
//            response.getWriter().write(new ErrorResponse("Invalid session ID", "The session ID '" + sessionID + "' is invalid. No session exists with this ID.").toJSON());
//            return;
//        }


        //TODO GET SESSION'S REFERENCED GAME
//        Session referencedSession = sessionList.get(0);
//        Game referencedGame = ofy().load().key(referencedSession.getGameKey()).now();

        //5 - Process request:

        //TODO CHECK IF NO REF GAME EXISTS
//        if (referencedGame == null) {
//            response.getWriter().write(new ErrorResponse("Game not found", "The game referenced by this session does not exist. It may have been deleted.").toJSON());
//            return;
//        }

        //TODO GET PARTIAL STATE FROM THE FULL STATE OF THE SESSION'S GAME
//        PartialStatePreference partialStatePreference = referencedSession.getPartialStatePreference();
//        try {
//            PartialBoardState partialBoardState = new PartialBoardState(partialStatePreference.getWidth(), partialStatePreference.getHeight(), referencedSession.getPositionRow(), referencedSession.getPositionCol(), referencedGame.getFullBoardState());
//            response.getWriter().write(new GetStateResponse(partialBoardState, referencedGame.getGameState(), sessionID).toJSON());
//        }
//        //If failed to get the partial state, return error:
//        catch (InvalidCellReferenceException e) {
//            response.getWriter().write(new ErrorResponse("Error fetching partial state for session '" + sessionID + "'.", e.getMessage()).toJSON());
//        }


    }
}