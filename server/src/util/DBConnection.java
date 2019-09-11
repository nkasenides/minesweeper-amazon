package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private static Connection connection;

    public static Connection connect(String serverURL, String dbName, String username, String password) {
        if (connection != null) {
            return connection;
        }
        else {
            try {
                Class.forName(MYSQL_DRIVER);
                connection = DriverManager.getConnection(serverURL + "/" + dbName, username, password);
                return connection;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
