package sashas;

import java.sql.*;
/**
 * Created by Екатерина on 12.05.2017.
 */
public class Database {


    public static void main(String[] args) {
                try {
                            Connection con = DriverManager.getConnection("jdbc:postgresql://194.87.187.238/semenova",
                                            "semenova", "semenova");
                //            Создание таблицы, где хранятся логины и пароли пользователей.

                                //            PreparedStatement preparedStatement = con.prepareStatement("CREATE TABLE loginsAndPasswords(" +
                                        //                    "id serial PRIMARY KEY," +
                                                //                    "login text UNIQUE," +
                                                        //                    "password text NOT NULL)");
                                                                //            preparedStatement.execute();

                                                                                //            PreparedStatement add = con.prepareStatement("INSERT INTO loginsAndPasswords (login, password) VALUES" +
                                                                                        //                    "('abc', 'abc')," +
                                                                                                //                    "('123', '123')");
                                                                                                        //            add.execute();

                                                                                                                        //            Создание таблицы, где хранятся сообщения.
                                                                                                                                //            Я приняла решение не хранить файлы в базе, среди сообщений просто хранится факт отправки

                                                                                                                                                //            PreparedStatement preparedStatement = con.prepareStatement("CREATE TABLE message(" +
                                                                                                                                                        //                    "id serial PRIMARY KEY," +
                                                                                                                                                                //                    "login text NOT NULL," +
                                                                                                                                                                        //                    "message text NOT NULL)");
                                                                                                                                                                                //            preparedStatement.execute();
                                                                                                                                                                                        //
                                                                                                                                                                                                //            PreparedStatement add = con.prepareStatement("INSERT INTO message (login, message) VALUES" +
                                                                                                                                                                                                        //                    "('abc', 'Hello!')," +
                                                                                                                                                                                                                //                    "('123', 'Hi!')");
                                                                                                                                                                                                                        //            add.execute();

                    PreparedStatement select = con.prepareStatement("SELECT * FROM message");
                            ResultSet result = select.executeQuery();
                            while (result.next()) {
                                    System.out.println(result.getString("login") + ": " + result.getString("message"));
                                }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                }
}

