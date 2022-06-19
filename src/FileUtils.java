package src;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class FileUtils {
    public static void writeToFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Vector<String> readFromFile(String fileName) {
        try {
            FileReader reader = new FileReader(fileName);
            // read the file into a string array
            Vector<String> lines = new Vector<>();
            int c;
            StringBuilder line = new StringBuilder();
            while ((c = reader.read()) != -1) {
                if (c == '\n') {
                    lines.add(line.toString());
                    line = new StringBuilder();
                } else {
                    line.append((char) c);
                }
            }
            // add the last line
            lines.add(line.toString());
            reader.close();
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
