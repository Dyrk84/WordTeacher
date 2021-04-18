package com.wordteacher.tools;

import java.io.File;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import static com.wordteacher.utils.Colors.RESET;
import static com.wordteacher.utils.Colors.RED;

public class Menu {
    static WordTeacher wt = new WordTeacher();

    public void checkForFile() {
        String dictionaryPath = ("src/main/resources/engWords.csv"); //relative path
        File f = new File(dictionaryPath);
        if (f.exists() && f.isFile()) {
            menu();
        } else {
            System.out.println(RED.typeOfColor + "Dictionary missing!" + RESET.typeOfColor + " Do you want to make one?" + "" +
                    " If yes, type " + RED.typeOfColor + "\"yes\" " + RESET.typeOfColor + "or just " + RED.typeOfColor +
                    "\"y\" " + RESET.typeOfColor + " and record the first word pair!");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.next();
            if (answer.equals("yes") || answer.equals("y")) {
                try {
                    Formatter file = new Formatter(dictionaryPath);
                    file.close();
                    wt.enteringAWordToLearn();
                } catch (Exception e) {
                    System.out.println("checkForFile() exception: " + e);
                }
            } else Menu.exit();
        }
    }

    public static void menu() {
        printMenu();
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wordQuiz();
                break;
            case 2:
                operationsWithWords();
                break;
            case 3:
                dictionaryOverviews();
                break;
            case 4:
                translate();
                break;
            case 5:
                exit();
                break;
            default:
                yourChooseIsNotAppropriate();
                menu();
        }
    }

    private static void wordQuiz(){
        printWordQuiz();
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wt.repeaterEng();
                break;
            case 2:
                wt.repeaterHun();
                break;
            case 3:
                menu();
                break;
            default:
                yourChooseIsNotAppropriate();
                wordQuiz();
        }
    }

    private static void operationsWithWords() {
        printOperationsWithWords();
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wt.enteringAWordToLearn();
                break;
            case 2:
                wt.wordRewriter();
                break;
            case 3:
                wt.wordRemover();
                break;
            case 4:
                menu();
                break;
            default:
                yourChooseIsNotAppropriate();
                operationsWithWords();
        }
    }

    private static void dictionaryOverviews() {
        printDictionaryOverviews();
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wt.dictionaryOverviewEngHun();
                break;
            case 2:
                wt.dictionaryOverviewHunEng();
                break;
            case 3:
                menu();
                break;
            default:
                yourChooseIsNotAppropriate();
                dictionaryOverviews();
        }
    }

    private static void translate() {
        printTranslate();
        int chosenNumber = chosenNumber();
        switch (chosenNumber) {
            case 1:
                wt.inputForTranslateEng();
                break;
            case 2:
                wt.inputForTranslateHun();
                break;
            case 3:
                menu();
                break;
            default:
                yourChooseIsNotAppropriate();
                translate();
        }
    }

    private static int chosenNumber() {
        do {
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
        System.out.println("********************* Main menu **************************");
        System.out.println("*" + RED.typeOfColor + " Choose one of the following possibilities:            " + RESET.typeOfColor +
                " *");
        System.out.println("* " + RED.typeOfColor + "1." + RESET.typeOfColor + " Word Quiz                                           *");
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Operations with Words                               *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Dictionary overviews                                *");
        System.out.println("* " + RED.typeOfColor + "4." + RESET.typeOfColor + " Translate from english to hungarian                 *");
        System.out.println("* " + RED.typeOfColor + "5." + RESET.typeOfColor + " Exit                                                *");
        System.out.println("**********************************************************");
    }

    private static void printWordQuiz() {
        System.out.println("********************* Word Quiz **************************");
        System.out.println("*" + RED.typeOfColor + " Choose one of the following possibilities:            " + RESET.typeOfColor +
                " *");
        System.out.println("* " + RED.typeOfColor + "1." + RESET.typeOfColor + " Word Quiz Eng-Hun                                   *");
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Word Quiz Hun-Eng                                   *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Back to the main menu                               *");
        System.out.println("**********************************************************");
    }

    private static void printOperationsWithWords() {
        System.out.println("**************** Operations with Words *******************");
        System.out.println("*" + RED.typeOfColor + " Choose one of the following possibilities:            " + RESET.typeOfColor +
                " *");
        System.out.println("* " + RED.typeOfColor + "1." + RESET.typeOfColor + " Entering a word to learn                            *");
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Rewrite a word                                      *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Delete a word                                       *");
        System.out.println("* " + RED.typeOfColor + "4." + RESET.typeOfColor + " Back to the main menu                               *");
        System.out.println("**********************************************************");
    }

    private static void printDictionaryOverviews() {
        System.out.println("***************** Dictionary overviews *******************");
        System.out.println("*" + RED.typeOfColor + " Choose one of the following possibilities:            " + RESET.typeOfColor +
                " *");
        System.out.println("* " + RED.typeOfColor + "1." + RESET.typeOfColor + " Dictionary overview Eng-Hun                         *");
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Dictionary overview Hun-Eng                         *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Back to the main menu                               *");
        System.out.println("**********************************************************");
    }

    private static void printTranslate() {
        System.out.println("********** Translate from english to hungarian ***********");
        System.out.println("*" + RED.typeOfColor + " Choose one of the following possibilities:            " + RESET.typeOfColor +
                " *");
        System.out.println("* " + RED.typeOfColor + "1." + RESET.typeOfColor + " Translate from english to hungarian                 *");
        System.out.println("* " + RED.typeOfColor + "2." + RESET.typeOfColor + " Translate from hungarian to english                 *");
        System.out.println("* " + RED.typeOfColor + "3." + RESET.typeOfColor + " Back to the main menu                               *");
        System.out.println("**********************************************************");
    }

    private static void yourChooseIsNotAppropriate() {
        System.out.println(RED.typeOfColor + "Your choose is not appropriate!" + RESET.typeOfColor);
    }

    public static void exit() {
        System.out.println("Good bye!");
    }

}
