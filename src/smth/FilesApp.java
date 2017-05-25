package smth;

import java.io.*;

public class FilesApp {

    public static void main(String[] args) {

        try(FileWriter writer = new FileWriter("notes3.txt", false))
        {
            // запись всей строки
            String text = "Мама мыла раму, раму мыла мама";
            writer.append(text);
            // запись по символам
            writer.append('\n');
            writer.append('E');

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}