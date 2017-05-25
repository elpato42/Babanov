package chat.multyClient;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;

/*
 * A chat server that delivers public and private messages.
 */

/**
 * Created by Екатерина on 07.05.2017.
 */
public class MultiThreadChatServer {


    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    public static void main(String args[]) {

        // The default port number.
        int portNumber = 2222;
        if (args.length < 1) {
            System.out
                    .println("Usage: java MultiThreadChatServer <portNumber>\n"
                            + "Now using port number=" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]);
        }

    /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
     */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

    /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new clientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();

                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
class clientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private File fileChat = new File("chat.txt");
    private Scanner scanFile = null;
    private FileWriter fileWriter = null;
    private File fileLogin = new File("login.txt");
    private LoginPassword loginPassword = new LoginPassword(fileLogin);


    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
      /*
       * Create input and output streams for this client.
       */
            is = new DataInputStream(clientSocket.getInputStream());
            os = new PrintStream(clientSocket.getOutputStream());

            scanFile = new Scanner(fileChat);

            while (scanFile.hasNextLine()) {
                os.println(scanFile.nextLine());
            }
            scanFile.close();
            fileWriter = new FileWriter(fileChat, true);
            String name;
            String pass;
            do {
                os.println("Enter your name.");
                name = is.readLine().trim();
                os.println("Enter your password");
                pass = is.readLine().trim();
                if (!loginPassword.containsLogin(name)) loginPassword.addUser(name, pass); //if there is no such name we add it
                if (!loginPassword.passIsCorrect(name, pass)) os.println("Check your password6 or change your name");
            } while (!loginPassword.passIsCorrect(name, pass));
            loginPassword.createTXT();
            os.println("Hello " + name
                    + " to our chat room.\nTo leave enter /quit in a new line");
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** A new user " + name
                            + " entered the chat room !!! ***");
                    threads[i].fileWriter.write("*** A new user " + name
                            + " entered the chat room !!! ***" + "\n");
                }
            }
            while (true) {
                String line = is.readLine();
                if (line.startsWith("/quit")) {
                    break;
                }
                for (int i = 0; i < maxClientsCount; i++) {
                    if (threads[i] != null) {
                        threads[i].os.println("<" + name + "> " + line);
                        threads[i].fileWriter.write("<" + name + "> " + line + "\n");
                    }
                }
            }
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].os.println("*** The user " + name
                            + " is leaving the chat room !!! ***");
                    threads[i].fileWriter.write("*** The user " + name
                            + " is leaving the chat room !!! ***" + "\n");
                }
            }
            os.println("*** Bye " + name + " ***");

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

      /*
       * Close the output stream, close the input stream, close the socket.
       */
            is.close();
            os.close();
            clientSocket.close();
            fileWriter.close();

        } catch (IOException e) {
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*void rek(DataInputStream is, PrintStream os, String name, String pass) throws Exception{


        os.println("Enter your name.");
        name = is.readLine().trim();
        if (loginPassword.containsLogin(name)) {
            os.println("Enter your password");
            pass = is.readLine().trim();
            if(loginPassword.passIsCorrect(name, pass)) {
                os.println("Password is correct!");
            } else rek(is, os, name, pass);
        }
        else {
            os.println("You are a new user! Create a password: ");
            pass = is.readLine().trim();
            loginPassword.addUser(name,pass);
        }
    }*/
}
