//package org.example.cab302project.ManageApplications;
//
//import javafx.scene.control.ComboBox;
//import org.example.cab302project.Controller.ManageApplicationController;
//import org.example.cab302project.DbConnection;
//import org.example.cab302project.LoginPageController;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Dictionary;
//import java.util.Hashtable;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class ManageApplication {
//    private Dictionary<String, Integer> groupPairing = new Hashtable<>();
//    private Connection connection; private LoginPageController loginPage;
//
//    public ManageApplication() {
//        try {
//            connection = DbConnection.getInstance().getConnection();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public class DisplayObject {
//        private String name; private String reason;
//
//        public void setName(String name) { this.name = name; }
//        public String getName() {
//            return name;
//        }
//        public void setReason(String reason) { this.reason = reason; }
//        public String getReason() {
//            return reason;
//        }
//        public DisplayObject(String name, String reason)  {
//            this.name = name; this.reason = reason;
//        } }
//
//
//    public ArrayList<String> returnDataToGroupList() {
//        ArrayList<String> returnedData = new ArrayList<>();
//        try {
//            String sql = "SELECT * FROM Groups WHERE userID = ?";
//
//            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                pstmt.setInt(1, loginPage.userID );
//                ResultSet rs = pstmt.executeQuery();
//                while (rs.next()) {
//                    returnedData.add(rs.getString("Groupname"));
//                    groupPairing.put(rs.getString("groupName"), rs.getInt("GroupID"));
//                }
//
//            } catch (SQLException e) {
//                System.err.println("Error adding blocked applications: " + e.getMessage());
//            }
//        } catch (NullPointerException e) {System.err.println(e.getMessage());}
//        return returnedData;
//    }
//
//
//    public ArrayList<DisplayObject> returnDataToApplicationList(String selectGroupValue) {
//        ArrayList<DisplayObject> returnedData = new ArrayList<>();
//
//        try {
//            int currentGroupID = groupPairing.get(selectGroupValue);
//            String sql = "SELECT fileName, reason FROM BlackLists WHERE groupID = ?";
//
//            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                pstmt.setInt(1, currentGroupID);
//                ResultSet rs = pstmt.executeQuery();
//
//                while (rs.next()) {
//                    String appPath = ReturnFileShortened(rs.getString("fileName"));
//                    DisplayObject displayObject = new DisplayObject(appPath, rs.getString("reason"));
//                    returnedData.add(displayObject);
//                }
//
//            } catch (SQLException e) {
//                System.err.println("Error adding blocked applications: " + e.getMessage());
//            }
//        } catch (NullPointerException e) {System.err.println(e.getMessage());}
//        return  returnedData;
//    }
//
//
//    public String ReturnFileExtended(String fileShort) {
//        String extendedPath = null;
//
//        try {
//            String sql = "SELECT fileName, reason FROM BlackLists WHERE groupID = ?";
//            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                pstmt.setInt(1, loginPage.userID );
//                ResultSet rs = pstmt.executeQuery();
//
//                while (rs.next()) {
//                    if (Pattern.compile("[^\\\\]*.exe").matcher(fileShort).find()) { extendedPath = fileShort; }
//                }
//            } catch (SQLException e) {
//                System.err.println("Error adding blocked applications: " + e.getMessage());
//            }
//        } catch (NullPointerException e) {System.err.println(e.getMessage());}
//        return extendedPath;
//    }
//
//
//    public String ReturnFileShortened(String filePath) {
//        Pattern pattern = Pattern.compile( "[^\\\\]*.exe");
//        Matcher matcher = pattern.matcher(filePath);
//        matcher.find();
//        return matcher.group();
//    }
//
//    public void DeleteDataFromDb(String result) {
//        try {
//            String query = "DELETE FROM BlackLists WHERE fileName = ?";
//
//            try ( PreparedStatement pstmt = connection.prepareStatement(query)) {
//                pstmt.setString(1, result );
//                pstmt.execute();
//            } catch (SQLException e) {
//                System.err.println("Error deleting blocked applications: " + e.getMessage());
//            }
//        } catch (NullPointerException e) {System.err.println(e.getMessage());}
//    }
//
//}
