package servlets;

import com.panickapps.response.ErrorResponse;
import model.StatelessGame;
import model.response.GetGameResponse;
import model.response.MissingParameterResponse;
import model.response.UnknownFailureResponse;
import util.APIUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetGameServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        APIUtils.setResponseHeader(response);
//
//        //1 - Get parameters:
//        String gameToken = request.getParameter("gameToken");
//
//        //3 - Required params:
//
//        if (gameToken == null) {
//            response.getWriter().write(new MissingParameterResponse("gameToken").toJSON());
//            return;
//        }
//
//        //5 - Process request:
//        try {
//            final StatelessGame game = MinesweeperDB.getStatelessGame(gameToken);
//            if (game == null) {
//                response.getWriter().write(new ErrorResponse("Game not found", "The game with token '" + gameToken + "' was not found.").toJSON());
//            }
//            else {
//                response.getWriter().write(new GetGameResponse(game).toJSON());
//            }
//        } catch (Exception e) {
//            response.getWriter().write(new UnknownFailureResponse("Failed to create a game." + e.getMessage()).toJSON());
//        }

    }
}
