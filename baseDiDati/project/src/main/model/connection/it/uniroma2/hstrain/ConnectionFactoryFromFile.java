package it.uniroma2.hstrain;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.DriverManager;
public class ConnectionFactoryFromFile {
    private static Connection connection;

    private ConnectionFactoryFromFile() {}

    static {
        // Does not work if generating a jar file
        try (InputStream input = new FileInputStream("src/main/resources/it/uniroma2/hstrain/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            String connection_url = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty("LOGIN_USER");
            String pass = properties.getProperty("LOGIN_PASS");
            String driverclassname = "com.mysql.cj.jdbc.Driver";
            Class.forName(driverclassname);
            connection = DriverManager.getConnection(connection_url, user, pass);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static void changeRole(Role role) throws SQLException {
        connection.close();

        try (InputStream input = new FileInputStream("src/main/resources/it/uniroma2/hstrain/db.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String connection_url = properties.getProperty("CONNECTION_URL");
            String user = properties.getProperty(role.name() + "_USER");
            String pass = properties.getProperty(role.name() + "_PASS");
            connection = DriverManager.getConnection(connection_url, user, pass);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
