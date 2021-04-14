package com.wordteacher.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;


import static com.wordteacher.utils.Colors.GREEN;
import static com.wordteacher.utils.Colors.RED;
import static com.wordteacher.utils.Colors.RESET;

public class WordTeacher {

    final String dictionaryPath = "D:\\DyrkWork\\WordTeacher\\engWords.csv";

//    private int wordCounter() {
//        //számokat kell keresni, a legnagyobbat kell megtalálni, és ahhoz kell adni egyet, hogy meglegyen a sorszám.
////        String largestNumber = largestNumber();
////      //kell egy iterálás, számokat kell keresni. Iterálással végignézem a filet, megnézem az iterálónál mi volt a lgnagyobb szám amit talált. Az iterálás addig folytatódik, amíg nincs vége a file-nak.
//        //iterálásnál a feltétel boolean lesz, van-e olyan szám vagy nincs, while ciklussal, addig lépeget előre a számokon, míg ki nem jön, hogy nincs olyan szám. Az utolsó szám fog kelleni.
//        Integer test = 0;
//        String searchTerm = test.toString();
//
//        String bla = "rat";
//        int counter = 0;
//        boolean found = false;
////        int id = 1;
//        String id = "";
//
//        try {
//            Scanner scanner = new Scanner(new File(engFilePath)); //TODO miért kell ez a new File?
//            scanner.useDelimiter("[,\n]");
//
////            while (scanner.hasNext()){
////                test++;
////            }
//
//            while (scanner.hasNext() && !found) {
//                id = scanner.next();
////            while (scanner.hasNext() && !found) {
////                id = scanner.next();
////                if (id == searchTerm) {
//                if (id.equals(searchTerm)) {
//                    found = true;
//                }
//            }
//            if (found) {
//                counter = 20;
//                System.out.println("van találat erre: " + searchTerm);
//            } else {
//                System.out.println("nincs találat");
//            }
//
//        } catch (
//                Exception e) {
//
//        }
//        return counter;

    //    }
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
        boolean existingWords = false;
        String existingEngWord = "";
        String existingHunWord = "";

        try {
            FileWriter fw = new FileWriter(dictionaryPath, true); //ha ez nincs itt, és nincs file, akkor exceptiont dob.
            fw.close();
            Scanner scanner = new Scanner(new File(dictionaryPath));
            scanner.useDelimiter("[,\n]"); //TODO ez mire kell? Ez állítja be, hogy a "," legyen ami mutatja hogy hol a határ a két szó között?

            while (scanner.hasNext() && !existingWords) {
                existingEngWord = scanner.next();
                existingHunWord = scanner.next();

                if (existingEngWord.equals(engWord) && existingHunWord.equals(hunWord)) {
                    existingWords = true;
                }
            }
            if (existingWords) {
                System.out.println(RED.typeOfColor + "The specified word pair already exists in the dictionary!" + RESET.typeOfColor);
            } else {
                wordsToFiles(engWord,hunWord);
            }
        } catch (Exception e) {
            System.out.println("knownWordsTest() exception");
        }
    }

    private void wordsToFiles(String engWord, String hunWord) {
        try {
            FileWriter fw = new FileWriter(dictionaryPath, true); //ez a true csinálja hogy mellé ír, nem rá
            fw.write(engWord + "," + hunWord + "," + "\n");
            fw.close(); //bezárja a filebaírás műveletét, a PrintWriter t.
            System.out.println(GREEN.typeOfColor + "The specified word pair is added to the dictionary." + RESET.typeOfColor);
        } catch (Exception e) {
            System.out.println("Record not saved");
        }
    }

    public void translateEngHun() {

    }

    public void translateHunEng() {

    }
}
