package com.wordteacher.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.wordteacher.utils.Colors.GREEN;
import static com.wordteacher.utils.Colors.RED;
import static com.wordteacher.utils.Colors.RESET;

public class WordTeacher {

    final String DICTIONARY_PATH = "D:\\DyrkWork\\WordTeacher\\engWords.csv";

    public void checkForFile() {
        File f = new File(DICTIONARY_PATH);
        if (f.exists() && f.isFile()) {
            enteringAWordToLearn();
        } else {
            System.out.println(RED.typeOfColor + "Dictionary missing!" + RESET.typeOfColor + " Do you want to make one?" + "" +
                    " If yes, type " + RED.typeOfColor + "\"yes\" " + RESET.typeOfColor + "or just " + RED.typeOfColor +
                    "\"y\" " + RESET.typeOfColor + "!");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.next();
            if (answer.equals("yes") || answer.equals("y")) {
                try {
                    Formatter file = new Formatter(DICTIONARY_PATH);
                    file.close();
                    enteringAWordToLearn();
                } catch (Exception e) {
                    System.out.println("checkForFile() exception: " + e);
                }
            } else Menu.exit();
        }
    }

    public void enteringAWordToLearn() {
        System.out.println("Enter the english form of the word:");
        String engWord = scanner();
        System.out.println("Enter the hungary form of the word:");
        String hunWord = scanner();
        knownWordsTest(engWord, hunWord);
        Menu.menu();
    }

    private String scanner() {
        while (true) {
            try {
                Scanner reader = new Scanner(System.in);
                return reader.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(RED.typeOfColor + "wrong input data!" + RESET.typeOfColor);
            }
        }
    }

    private void knownWordsTest(String engWord, String hunWord) {
        boolean booleanEngWord = false;
        boolean booleanHunWord = false;
        String existingEngWord = "";
        String existingHunWord = "";

        try {
            Scanner scanner = new Scanner(new File(DICTIONARY_PATH));
            scanner.useDelimiter("[,\n]");

            while (scanner.hasNext() && !booleanEngWord) {
                existingEngWord = scanner.next();

                if (existingEngWord.equals(engWord)) {
                    booleanEngWord = true;
                    while (scanner.hasNext() && !booleanHunWord) {
                        existingHunWord = scanner.next();
                        if (existingHunWord.equals(hunWord)) {
                            booleanHunWord = true;
                        }
                    }
                }
            }
            if (booleanEngWord && booleanHunWord) {
                System.out.println(RED.typeOfColor + "The specified word pair already exists in the dictionary!" + RESET.typeOfColor);
            } else {
                wordsToFiles(engWord, hunWord);
            }
        } catch (FileNotFoundException e) {
            System.out.println("knownWordsTest() exception: " + e);
        }
    }

    private void wordsToFiles(String engWord, String hunWord) {
        try {
            FileWriter fw = new FileWriter(DICTIONARY_PATH, true); //ez a true csinálja hogy mellé ír, nem rá
            fw.write(engWord + "," + hunWord + "," + "\n");
            fw.close(); //bezárja a filebaírás műveletét, a PrintWriter t.

            System.out.println(GREEN.typeOfColor + "The specified word pair is added to the dictionary." + RESET.typeOfColor);
        } catch (Exception e) {
            System.out.println("Record not saved");
        }
    }

    public void translateEngHun() {
        String line = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH));

            while ((line = br.readLine()) != null) {
                ArrayList<String> engList = new ArrayList<>();
                engList.add(line.intern());

//                String[] values = line.split(",");
                //System.out.println("English: " + values[0] + " Hungarian: " + values[1]);
                System.out.println(engList);
            }
        } catch (FileNotFoundException e) {
            System.out.println("translateEngHun() exception: " + e);
        } catch (IOException e) {
            System.out.println("translateEngHun() exception: " + e);
        }
    }

    public void translateHunEng() {

    }
}
