package com.wordteacher.tools;

import java.util.InputMismatchException;
import java.util.Scanner;


import static com.wordteacher.utils.Colors.RESET;
import static com.wordteacher.utils.Colors.RED;


public class Menu {
    static WordTeacher wt = new WordTeacher();

    public static void menu() {
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wt.enteringAWordToLearn();
                break;
            case 2:
                wt.translateEngHun();
                break;
            case 3:
                wt.translateHunEng();
                break;
            case 4:
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
            System.out.print("Chosen one from the menu options: ");
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
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Translate from english to hungarian                 *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Translate from hungarian to english                 *");
        System.out.println("* " + RED.typeOfColor + "4." + RESET.typeOfColor + " Exit                                                *");
        System.out.println("**********************************************************");
    }

    private static void yourChooseIsNotAppropriate() {
        System.out.println(RED.typeOfColor + "Your choose is not appropriate!" + RESET.typeOfColor);
    }

    public static void exit() {
        System.out.println("Good bye!");
    }

}
