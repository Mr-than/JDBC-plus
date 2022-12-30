package jdbc.factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    public static Connection createConnection(String URL,String USER,String PASS){
        Connection connection;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
