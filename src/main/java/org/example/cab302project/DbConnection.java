package org.example.cab302project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A singleton connection to the database
 */
public class DbConnection {
    private static DbConnection instance;
    private Connection connection;
    private static final String DB_FILE_PATH = "src/main/resources/org/example/cab302project/ToDo.db";

    // Private constructor that creates a connection to the database
    private DbConnection() throws SQLException {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE_PATH);
    }

    /**
     * Gets the singleton database connection
     * @return the database connection
     */
    public Connection getConnection(){
        return connection;
    }

    /**
     * Gets the singleton instance of the DbConnection if one exists, else creates a new instance
     * @return The instance of the DbConnection
     * @throws SQLException
     */
    public static DbConnection getInstance() throws SQLException{
        if (instance == null){
            instance = new DbConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DbConnection();
        }
        return instance;
    }
}
