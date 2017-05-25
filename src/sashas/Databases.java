package sashas;

import java.sql.*;

public class Databases {
//    public static void main(String[] args) throws SQLException {
//        Connection conn = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/dunaenko", "dunaenko", "dunaenko");
//        conn = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/dunaenko", "dunaenko", "dunaenko");
//
//        PreparedStatement preparedStatement4 = conn.prepareStatement("INSERT INTO message2 (login, message) VALUES (?, ?);");   //процедура занесения сообщения в историю
//        preparedStatement4.setString(1, "<Server>");
//        preparedStatement4.setString(2, "message");
//        preparedStatement4.executeUpdate();
//        PreparedStatement preparedStatement3 = conn.prepareStatement("SELECT * FROM message2");
//        ResultSet result = preparedStatement3.executeQuery();
//        while (result.next()){
//            System.out.println(result.getString("login") + ": " + result.getString("message"));
//        }
//
//    }
    public void add (String login, String message) throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/dunaenko", "dunaenko", "dunaenko");
            PreparedStatement preparedStatement4 = conn.prepareStatement("INSERT INTO message2 (login, message) VALUES (?, ?);");   //процедура занесения сообщения в историю
            preparedStatement4.setString(1, login);
            preparedStatement4.setString(2, message);
            preparedStatement4.executeUpdate();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void printMessages() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/dunaenko", "dunaenko", "dunaenko");
            PreparedStatement preparedStatement3 = conn.prepareStatement("SELECT * FROM message2");
            ResultSet result = preparedStatement3.executeQuery();
            while (result.next()) {
                System.out.println(result.getString("login") + ": " + result.getString("message"));
            }
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void getMessages() throws SQLException{
//        try {
//            Class.forName("org.postgresql.Driver");
//            Connection conn = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/dunaenko", "dunaenko", "dunaenko");
//            PreparedStatement preparedStatement3 = conn.prepareStatement("SELECT * FROM message2");
//            ResultSet result = preparedStatement3.executeQuery();
//            String str = "";
//            while (result.next()) {
//                System.out.println(result.getString("login") + ": " + result.getString("message"));
//                str +=
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
 //   }

}
