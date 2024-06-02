package org.example.cab302project;

import java.sql.*;

public class CreateDB {
    public static void createDatabase() {

        String dbFilePath = "src/main/resources/org/example/cab302project/ToDo.db";

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);

            Statement stmt = conn.createStatement();

            String sqlUserDetails = "CREATE TABLE IF NOT EXISTS UserDetails (" +
                    "UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fName TEXT NOT NULL," +
                    "lName TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "pass TEXT NOT NULL," +
                    "securityQuestion TEXT," +
                    "securityAnswer TEXT)";
            stmt.executeUpdate(sqlUserDetails);


            String sqlGroup = "CREATE TABLE IF NOT EXISTS Groups (" +
                    "GroupID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Groupname TEXT NOT NULL," +
                    "userID INT NOT NULL)";
            stmt.executeUpdate(sqlGroup);


            String sqlSubGroup = "CREATE TABLE IF NOT EXISTS SubGroup (" +
                    "subGroupID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "groupID INT NOT NULL)";
            stmt.executeUpdate(sqlSubGroup);


            String sqlNotes = "CREATE TABLE IF NOT EXISTS Notes (" +
                    "NoteID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "content TEXT NOT NULL)";
            stmt.executeUpdate(sqlNotes);


            String sqlToDo = "CREATE TABLE IF NOT EXISTS ToDo (" +
                    "ToDoID INTEGER PRIMARY KEY," +
                    "Task TEXT NOT NULL," +
                    "Completed INTEGER," +
                    "SubGroupID INTEGER)";
            stmt.executeUpdate(sqlToDo);


            String sqlReports = "CREATE TABLE IF NOT EXISTS Reports (" +
                    "sessReportID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "totalTime INTEGER NOT NULL," +
                    "numberOfBreaks INTEGER NOT NULL," +
                    "lengthOfBreaks INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "userID INTEGER," +
                    "groupID INTEGER," +
                    "subGroupID INTEGER," +
                    "FOREIGN KEY(userID) REFERENCES UserDetails(UserID)," +
                    "FOREIGN KEY(groupID) REFERENCES Groups(GroupID)," +
                    "FOREIGN KEY(subGroupID) REFERENCES SubGroup(subGroupID))";
            stmt.executeUpdate(sqlReports);


            String sqlBlackLists = "CREATE TABLE IF NOT EXISTS BlackLists (" +
                    "blackListID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "groupID INTEGER NOT NULL," +
                    "fileName TEXT NOT NULL," +
                    "reason TEXT NOT NULL)";
            stmt.executeUpdate(sqlBlackLists);

            System.out.println("Database created successfully at: " + dbFilePath);

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error creating database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        createDatabase();
    }
}

