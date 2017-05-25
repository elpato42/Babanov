package smth;

import java.io.*;

/**
 * Created by Екатерина on 12.05.2017.
 */
public class fileWriter {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new FileWriter(reader.readLine()));

        while (true) {
            String nextLine = reader.readLine();
            if (nextLine.equals("exit")) break;
            bw.write(nextLine);
        }
        reader.close();
        bw.close();
    }
}
