package com.wordteacher.tools;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.WordTeacher.utils.Colors.RED;
import static com.WordTeacher.utils.Colors.RESET;


public class Menu {
    //adja meg az angol szót:
    //adja meg a magyar jelentését:
    //felírja a listákba az inputot
    static WordTeacher wt = new WordTeacher();

    public static void menu() {
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wt.enteringAWordToLearn();
                break;
            case 2:
                wt.InterrogationOfWords();
                break;
            case 3:
                exit();
                break;
            default:
                yourChooseIsNotAppropriate();
                menu();
        }
    }

    private static int chosenNumber() {
        do {
            printMenu();
            Scanner scanner = new Scanner(System.in);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException hibafogo) {
                yourChooseIsNotAppropriate();
            }
        } while (true);
    }

    private static void printMenu() {
        System.out.println("*********************** Menu *****************************");
        System.out.println("*" + RED.typeOfColor + " Choose one of the following possibilities:            " + RESET.typeOfColor +
                " *");
        System.out.println("* " + RED.typeOfColor + "1." + RESET.typeOfColor + " Entering a word to learn                            *");
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Interrogation of words                              *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Exit                                                *");
        System.out.println("**********************************************************");
    }

    private static void yourChooseIsNotAppropriate() {
        System.out.println(RED.typeOfColor + "Your choose is not appropriate!" + RESET.typeOfColor);
    }

    public static void exit() {
        System.out.println("Good bye!");
    }

}
