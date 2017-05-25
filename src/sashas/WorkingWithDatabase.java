package sashas;

/**
 * Created by Екатерина on 12.05.2017.
 */

import java.sql.*;

public class WorkingWithDatabase {
    private Connection con;

    public WorkingWithDatabase() throws SQLException {
        con = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/semenova",
                "semenova", "semenova");
    }

    public boolean checkLoginAndPassword(String login, String password) throws SQLException {
        PreparedStatement select = con.prepareStatement("SELECT * " +
                "FROM loginsAndPasswords " +
                "WHERE login = ? AND password = ?");
        select.setString(1, login);
        select.setString(2, password);
        ResultSet result = select.executeQuery();
        return result.next();
    }

    public void putMessageInDatabase(String author, String message) throws SQLException {
        PreparedStatement insert = con.prepareStatement("INSERT INTO message (login, message) VALUES" +
                "(?, ?)");
        insert.setString(1, author);
        insert.setString(2, message);
        insert.execute();
    }

    public ResultSet getMessages() throws SQLException {
        PreparedStatement select = con.prepareStatement("SELECT * " +
                "FROM message ");
        return select.executeQuery();
    }
}