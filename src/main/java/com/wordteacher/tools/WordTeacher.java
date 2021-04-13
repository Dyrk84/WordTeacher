package com.wordteacher.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;


import static com.WordTeacher.utils.Colors.RED;
import static com.WordTeacher.utils.Colors.RESET;

public class WordTeacher {
    private int wordCounter = -1;

    public void enteringAWordToLearn() {
        System.out.println("Enter the english form of the word:");
        String engWord = scanner();
        wordCounter++;
        System.out.println("Enter the hungary form of the word:");
        String hunWord = scanner();
        //teszt
        // System.out.println("Teszt: A szó angol formája: \"" + engWord + "\" és a magyar formája: \"" + hunWord + "\"");
        testcsv(engWord, hunWord);
        Menu.menu();
    }

    public void testcsv(String engWord, String hunWord) {
        try {
            FileWriter fw = new FileWriter("D:\\DyrkWork\\WordTeacher\\tesztcsv.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(wordCounter + "," + engWord + "," + hunWord);
            pw.flush();
            pw.close();

            System.out.println("Record saved");
        } catch (Exception e) {
            System.out.println("Record not saved");
        }
    }

    public void InterrogationOfWords() {

    }

    public String scanner() {
        while (true) {
            try {
                Scanner reader = new Scanner(System.in);
                return reader.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(RED.typeOfColor + "wrong input data!" + RESET.typeOfColor);
            }
        }
    }
}
