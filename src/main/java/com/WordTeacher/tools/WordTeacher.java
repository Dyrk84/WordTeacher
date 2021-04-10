package com.WordTeacher.tools;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.WordTeacher.utils.Colors.RED;
import static com.WordTeacher.utils.Colors.RESET;

public class WordTeacher {

    public void enteringAWordToLearn(){
        System.out.println("Enter the english form of the word:");
        String engWord = scanner();
        System.out.println("Enter the hungary form of the word:");
        String hunWord = scanner();
        //teszt
        // System.out.println("Teszt: A szó angol formája: \"" + engWord + "\" és a magyar formája: \"" + hunWord + "\"");



    }

    public void InterrogationOfWords(){

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
