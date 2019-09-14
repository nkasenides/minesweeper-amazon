package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class DBConnection {

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static ArrayList<Connection> ACTIVE_CONNECTIONS = new ArrayList<>();

    public static Connection connect(String serverURL, String dbName, String username, String password) {
        try {
            Class.forName(MYSQL_DRIVER);
            final Connection connection = DriverManager.getConnection(serverURL + "/" + dbName, username, password);
            ACTIVE_CONNECTIONS.add(connection);
            return connection;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
