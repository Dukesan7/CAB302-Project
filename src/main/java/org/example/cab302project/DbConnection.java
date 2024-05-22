package org.example.cab302project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection instance;
    private Connection connection;
    private static final String DB_FILE_PATH = "src/main/resources/org/example/cab302project/ToDo.db";

    private DbConnection() throws SQLException {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE_PATH);
    }
    public Connection getConnection(){
        return connection;
    }

    public static DbConnection getInstance() throws SQLException{
        if (instance == null){
            instance = new DbConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DbConnection();
        }
        return instance;
    }
}
