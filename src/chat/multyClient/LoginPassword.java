package chat.multyClient;

import java.io.*;
import java.util.*;

/**
 * Created by Екатерина on 13.05.2017.
 */
public class LoginPassword {
    private HashMap<String, String> logpass = new HashMap<>();
    private File file;

    // private String log;
    // private String pass;
    public LoginPassword(File f) {
        file = f;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                logpass.put(scanner.nextLine(), scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
        }


    }

    public void addUser(String log, String pass) {
        if (this.containsLogin(log))
            System.out.println("This username is allready taken");
        else logpass.put(log, pass);

    }

    public void createTXT() {
        try {
            PrintStream ps = new PrintStream(file.getName());
            for (Map.Entry<String, String> entry :
                    logpass.entrySet()) {
                String l = entry.getKey(); //login
                String p = entry.getValue(); //password
                ps.println(l);
                ps.println(p);
                ps.close();
            }

        } catch (IOException e) {
        }
    }

    private String getLogin() {
        return null;
    }

    public String getPassword(String login) {
        if (logpass.containsKey(login))
            return logpass.get(login);
        else {
            System.out.println("There is no such user");
            return null;
        }
    }

    public boolean containsLogin(String log) {
        return logpass.containsKey(log);
    }

    public boolean passIsCorrect(String log, String pass) {
        if (this.containsLogin(log))
            return pass.equals(this.getPassword(log));
        else return false;
    }


}
