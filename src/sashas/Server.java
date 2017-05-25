package sashas;

/**
 * Created by Екатерина on 12.05.2017.
 */

import sashas.xmlProtocol.WorkingWithXMLChatMessage;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Server {
    static final int PORT = 5556;
    private static ArrayList<Socket> users = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException, SQLException {
        ServerSocket s = new ServerSocket(PORT);
        while (true) {
            Socket socket = s.accept();
            new ServerOne(socket);
        }
    }


    public static class ServerOne extends Thread {
        private Socket socket;
        private BufferedReader socketReader;
        private PrintWriter socketWriter;
        private XMLServerEditor xmlEditor = new XMLServerEditor();
        private WorkingWithDatabase database = new WorkingWithDatabase();
        private WorkingWithXMLChatMessage chatMessage = new WorkingWithXMLChatMessage();

        ServerOne(Socket s) throws IOException, ParserConfigurationException, SAXException, TransformerException, SQLException {
            socket = s;
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            // Проверяем логин и пароль
            boolean loginChecked = false;
            while (!loginChecked) {
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = socketReader.readLine();
                loginChecked = checkLogin(xmlEditor.getLogin(str), xmlEditor.getPassword(str));
                socketWriter.println(xmlEditor.makeAuthorisationMessage(loginChecked));
            }
            users.add(socket);

            // Выводим всю историю чата новому подключению
            String str;
            ResultSet result = database.getMessages();
            while (result.next()) {
                Document document = chatMessage.makeChatMessage(result.getString("login"),
                        result.getString("message"));
                // TODO посылать document
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                StringWriter outWriter = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(outWriter));
                StringBuffer sb = outWriter.getBuffer();
                socketWriter.println(sb.toString());
            }

            start();
        }

        // Проверка на существование пары логин-пароль в файле login.txt
        private boolean checkLogin(String login, String password) throws IOException, SQLException {
            return database.checkLoginAndPassword(login, password);
        }

        public void run() {
            try {
                String str;
                while ((str = socketReader.readLine()) != null) {
                    // Записываем в историю чата новое сообщение
                    System.out.println(chatMessage.getAuthor(str));
                    System.out.println(chatMessage.getMessage(str));
                    database.putMessageInDatabase(chatMessage.getAuthor(str), chatMessage.getMessage(str));

                    // Отправляем новое сообщение всем, кроме автора сообщения
                    for (Socket s: users) {
                        if (!s.equals(socket)) {
                            PrintWriter sWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
                            sWriter.println(str);
                        }
                    }
                }
            }
            catch (IOException | SAXException | SQLException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}