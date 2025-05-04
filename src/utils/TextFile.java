/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author lunwe
 */
public class TextFile {
    
    public static void createFileIfNotExists(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
        
    public static void appendTo(String file, String appendWord){
        try{
            FileWriter fr = new FileWriter(file, true);
            fr.write(System.lineSeparator());
            fr.write(appendWord);
            fr.close();
        } catch (IOException e) {
            System.out.println("error");
        }
    }
    
    public static List<String> readFile(String file) {
        List<String> list = new ArrayList<>();
        File data = new File(file);
        try {
            Scanner lines = new Scanner(data);
            while (lines.hasNextLine()){
                String line = lines.nextLine().strip();
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static void replaceLine(String file, String currentLine, String newLine) {
        List<String> list = readFile(file);
        int size = list.size();
        int i = 1;
        try {
            FileWriter fr = new FileWriter(file);
            for (String line : list) {
                if (line.equals(currentLine)){
                    fr.write(newLine);
                }
                else {
                    fr.write(line);
                }
                if (i<size) {
                    fr.write(System.lineSeparator());
                    i++;
                }
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("Error");
        }
    }
    

    public static void deleteLine(String file, String lineToRemove) {
        List<String> lines = new ArrayList<>();

        // Read all lines from the file, except the one to remove
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals(lineToRemove)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write remaining lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                if (i < lines.size() - 1) {
                    writer.newLine(); // Add a new line after each line except the last one
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
