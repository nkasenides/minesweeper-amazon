package servlets;

import com.panickapps.response.ErrorResponse;
import model.Game;
import model.PartialBoardState;
import model.PartialStatePreference;
import model.Session;
import model.exception.InvalidCellReferenceException;
import model.response.GetStateResponse;
import model.response.MissingParameterResponse;
import model.response.UnknownFailureResponse;
import util.APIUtils;
import util.MinesweeperDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

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
        try {
            session = MinesweeperDB.getSession(sessionID);
        } catch (SQLException e) {
            response.getWriter().write(new UnknownFailureResponse("Could not find session." + e.getMessage()).toJSON());
            return;
        }

        if (session == null) {
            response.getWriter().write(new ErrorResponse("Session not found", "Could not find session with ID '" + sessionID + "'").toJSON());
            return;
        }

        Game game = null;
        try {
            game = MinesweeperDB.getGame(session.getGameToken());
        } catch (SQLException e) {
            response.getWriter().write(new UnknownFailureResponse("Could not find game." + e.getMessage()).toJSON());
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
