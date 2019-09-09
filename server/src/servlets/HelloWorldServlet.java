package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloWorldServlet extends javax.servlet.http.HttpServlet {

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        response.getWriter().println("HELLO WORLD!");
        response.getWriter().println("Testing database connectivity...");

        final String driver = "com.mysql.jdbc.Driver";
        final String dbURL = "jdbc:mysql://minesweeperdb.cdrlmrmeqih0.us-east-2.rds.amazonaws.com/minesweeper";
        final String username = "admin";
        final String password = "nk123nk123";

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(dbURL, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Game");
            int count = 0;
            while (rs.next()) {
                response.getWriter().println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
                count++;
            }
            response.getWriter().println("Count: " + count);
            con.close();
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
        }
    }

}