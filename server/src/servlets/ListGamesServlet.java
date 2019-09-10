package servlets;

import com.panickapps.response.ErrorResponse;
import com.panickapps.response.SuccessResponse;
import model.Game;
import model.StatelessGame;
import model.response.ListGamesResponse;
import util.APIUtils;
import util.MinesweeperDB;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListGamesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        APIUtils.setResponseHeader(response);

        //1 - Get parameters:
        String startedOnlyStr = request.getParameter("startedOnly");

        boolean startedOnly = startedOnlyStr != null;

        //5 - Process request:
        try {
            ArrayList<StatelessGame> statelessGames;
            if (startedOnly) {
                statelessGames = MinesweeperDB.listGames(true);

            } else {
                statelessGames = MinesweeperDB.listGames(false);
            }
            if (statelessGames.size() < 1) {
                response.getWriter().write(new SuccessResponse("Games fetched", "No games found").toJSON());
            }
            else {
                response.getWriter().write(new ListGamesResponse(statelessGames).toJSON());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write(new ErrorResponse("Operation failed", "Unable to fetch games.").toJSON());
        }

    }

}
