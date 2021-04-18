package com.wordteacher.tools;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wordteacher.utils.Colors.BLUE;
import static com.wordteacher.utils.Colors.GREEN;
import static com.wordteacher.utils.Colors.RED;
import static com.wordteacher.utils.Colors.RESET;

public class WordTeacher {

    private boolean booleanExistingWord = false;

    final private String DICTIONARY_PATH = "src/main/resources/engWords.csv";

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
            FileWriter fw = new FileWriter(DICTIONARY_PATH, true);
            fw.write(engWord + "," + hunWord + "," + "\n");
            fw.close();

            System.out.println(GREEN.typeOfColor + "The specified word pair is added to the dictionary." + RESET.typeOfColor);
        } catch (Exception e) {
            System.out.println("Record not saved");
        }
    }

//    private void filebolListabaKezeles() { //tanuláshoz kell, nincs funkciója a programban
//        try { // így tudom a csv file sorait tömbökként bepakolni listába.
//            List<List<String>> dictionaryInList = new ArrayList<>();
//            try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    String[] values = line.split(",");
//                    dictionaryInList.add(Arrays.asList(values));
//                }
//            }
//            for (int i = 0; i < dictionaryInList.size(); i++) {
//                System.out.println("angol szavak: " + dictionaryInList.get(i).get(0));
//            }
//            System.out.println("második sor szópár (második String tömb): " + dictionaryInList.get(1));
//            System.out.println("második sor angol szava: " +dictionaryInList.get(1).get(0));
//
//        } catch (FileNotFoundException e) {
//            System.out.println("translateEngHun() exception: " + e);
//        } catch (IOException e) {
//            System.out.println("translateEngHun() exception: " + e);
//        }
//    }

    public void inputForTranslateEng() {
        System.out.println("Enter the english form of the word:");
        String engWord = scanner();
        inputIsInDictionaryTest(engWord);
        if (booleanExistingWord) {
            booleanExistingWord = false; //TODO ez lehet nem kell ide
            translateEngHun(engWord);
        }
    }

    private void inputIsInDictionaryTest(String inputWord) {
        booleanExistingWord = false;
        String existingWord = "";

        try {
            Scanner scanner = new Scanner(new File(DICTIONARY_PATH));
            scanner.useDelimiter("[,\n]");

            while (scanner.hasNext() && !booleanExistingWord) {
                existingWord = scanner.next();

                if (existingWord.equals(inputWord)) {
                    booleanExistingWord = true;
                }
            }
            if (booleanExistingWord) {
                System.out.println("The requested word (" + BLUE.typeOfColor + inputWord + RESET.typeOfColor + ") "
                        + GREEN.typeOfColor + "exists" + RESET.typeOfColor + " in the dictionary.");
            } else {
                inputNotExisting(inputWord);
            }
        } catch (FileNotFoundException e) {
            System.out.println("knownWordsTest() exception: " + e);
        }
    }

    private void inputNotExisting(String inputWord) {
        System.out.println("The requested word (" + inputWord + ") does" + RED.typeOfColor + " not exist"
                + RESET.typeOfColor + " in the dictionary. Do you want to recording a new word? If yes, type "
                + RED.typeOfColor + "\"yes\" " + RESET.typeOfColor + "or just " + RED.typeOfColor +
                "\"y\"" + RESET.typeOfColor + "! Anything else returns back to the menu.");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if (answer.equals("yes") || answer.equals("y")) {
            enteringAWordToLearn();
        } else {
            Menu.menu();
        }
    }

    public void translateEngHun(String engWord) {
        System.out.print("The meaning of the English word " + BLUE.typeOfColor + engWord + RESET.typeOfColor + " : ");

        try {
            List<List<String>> dictionaryInList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    dictionaryInList.add(Arrays.asList(values));
                }
            }
            for (int i = 0; i < dictionaryInList.size(); i++) {
                Pattern pattern = Pattern.compile(engWord);
                Matcher matcher = pattern.matcher(dictionaryInList.get(i).get(0));
                if (matcher.find()) {
                    System.out.print(GREEN.typeOfColor + dictionaryInList.get(i).get(1) + RESET.typeOfColor + ", ");
                }
            }
            System.out.println();
            pressEnterToContinue();
            Menu.menu();
        } catch (IOException e) {
            System.out.println("translateEngHun() exception: " + e);
        }
    }

    private void pressEnterToContinue() {
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("pressEnterToContinue() exception: " + e);
        }
    }

    public void inputForTranslateHun() {
        System.out.println("Enter the hungarian form of the word:");
        String hunWord = scanner();
        inputIsInDictionaryTest(hunWord);
        if (booleanExistingWord) {
            booleanExistingWord = false;
            translateHunEng(hunWord);
        }
    }

    public void translateHunEng(String hunWord) {
        System.out.print("The meaning of the English word " + BLUE.typeOfColor + hunWord + RESET.typeOfColor + " : ");

        try {
            List<List<String>> dictionaryInList = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    dictionaryInList.add(Arrays.asList(values));
                }
            }
            for (int i = 0; i < dictionaryInList.size(); i++) {
                Pattern pattern = Pattern.compile(hunWord);
                Matcher matcher = pattern.matcher(dictionaryInList.get(i).get(1));
                if (matcher.find()) {
                    System.out.print(GREEN.typeOfColor + dictionaryInList.get(i).get(0) + RESET.typeOfColor + ", ");
                }
            }
            System.out.println();
            pressEnterToContinue();
            Menu.menu();
        } catch (IOException e) {
            System.out.println("translateEngHun() exception: " + e);
        }
    }

    public void dictionaryOverviewEngHun() {
        try {
            List<List<String>> dictionaryInList = new ArrayList<>();
            Set<String> setEngWords = new HashSet();
            MultiValuedMap<String, String> engDictionaryWithHunSynonyms = new ArrayListValuedHashMap<>(); //Multi Valued Map leírása: https://www.baeldung.com/apache-commons-multi-valued-map

            try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) { //ez rakja össze a file-ból a nagy listát
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    dictionaryInList.add(Arrays.asList(values));
                }
            }

            for (int i = 0; i < dictionaryInList.size(); i++) { //ez csinálja meg az angol set listát
                setEngWords.add(dictionaryInList.get(i).get(0));
            }

            List<String> orderedEngWordsList = setEngWords.stream().sorted().collect(Collectors.toList()); //a setet streameli, abc sorrendbe rakja, majd listává alakítja

            for (int i = 0; i < dictionaryInList.size(); i++) { //feltölti a multiMapet a fileból kiolvasott lista elemeivel
                engDictionaryWithHunSynonyms.put(dictionaryInList.get(i).get(0), dictionaryInList.get(i).get(1));
            }
            System.out.println("teszt: " + engDictionaryWithHunSynonyms);

            for (int i = 1; i < orderedEngWordsList.size() + 1; i++) { //nyomtatja a multiMap value elemeit az abc sorrendbe tett set hívása szerint.
                System.out.println(BLUE.typeOfColor + orderedEngWordsList.get(i - 1) + RESET.typeOfColor
                        + GREEN.typeOfColor + engDictionaryWithHunSynonyms.get(orderedEngWordsList.get(i - 1)) + RESET.typeOfColor);
                if (i % 20 == 0) { //ezzel érem el azt, hogy ne hányja tele a képernyőt szavakkal, ha már nagy a szótár
                    System.out.println("The dictionary overviewed content : " + setEngWords.size() + "/" + (i));
                    pressEnterToContinue();
                }
            }

            pressEnterToContinue();
            Menu.menu();

        } catch (IOException e) {
            System.out.println("dictionaryOverviewEngHun() exception: " + e);
        }
    }

    public void dictionaryOverviewHunEng() {
        try {
            List<List<String>> dictionaryInList = new ArrayList<>();
            List<String> hunWordsList = new ArrayList<>();
            Set<String> setHunWords = new HashSet();
            MultiValuedMap<String, String> hunDictionaryWithEngSynonyms = new ArrayListValuedHashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    dictionaryInList.add(Arrays.asList(values));
                }
            }

            for (int i = 0; i < dictionaryInList.size(); i++) {
                hunWordsList.add(dictionaryInList.get(i).get(1));
            }

            for (int i = 0; i < dictionaryInList.size(); i++) {
                setHunWords.add(hunWordsList.get(i));
            }
            List<String> orderedHunWordsList = setHunWords.stream().sorted().collect(Collectors.toList());

            for (int i = 0; i < dictionaryInList.size(); i++) {
                hunDictionaryWithEngSynonyms.put(dictionaryInList.get(i).get(1), dictionaryInList.get(i).get(0));
            }
            System.out.println("teszt: " + hunDictionaryWithEngSynonyms);

            for (int i = 1; i < orderedHunWordsList.size() + 1; i++) {
                System.out.println(BLUE.typeOfColor + orderedHunWordsList.get(i - 1) + RESET.typeOfColor
                        + GREEN.typeOfColor + hunDictionaryWithEngSynonyms.get(orderedHunWordsList.get(i - 1)) + RESET.typeOfColor);
                if (i % 20 == 0) {
                    System.out.println("The dictionary overviewed content : " + hunWordsList.size() + "/" + (i));
                    pressEnterToContinue();
                }
            }
            pressEnterToContinue();
            Menu.menu();
        } catch (IOException e) {
            System.out.println("dictionaryOverviewHunEng() exception: " + e);
        }
    }

    public void wordRewriter() {
        List<List<String>> dictionaryInList = new ArrayList<>();
        List<String> engWordsList = new ArrayList<>();
        List<String> hunWordsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) { //ez rakja össze a file-ból a nagy listát
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                dictionaryInList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("wordRewriter() exception: " + e);
        }

        for (int i = 0; i < dictionaryInList.size(); i++) {
            engWordsList.add(dictionaryInList.get(i).get(0));
            hunWordsList.add(dictionaryInList.get(i).get(1));
        }
        //bekérem a szót, és hogy mire akarom cserélni
        System.out.println("Enter the word you want to rewrite: ");
        String toBeRewrittenWord = scanner();
        System.out.println("Enter the word rewritten form: ");
        String newWord = scanner();

        if (engWordsList.contains(toBeRewrittenWord) || hunWordsList.contains(toBeRewrittenWord)) {
            System.out.println("The " + toBeRewrittenWord + " word is " + newWord + " now.");
            Collections.replaceAll(engWordsList, toBeRewrittenWord, newWord);
            Collections.replaceAll(hunWordsList, toBeRewrittenWord, newWord);
            //a listákból újraírja a filet
            String tempfile = "temp.txt";
            File oldFile = new File(DICTIONARY_PATH);
            File newFile = new File(tempfile);

            try {
                FileWriter fw = new FileWriter(tempfile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter((bw));

                for (int i = 0; i < engWordsList.size(); i++) {
                    pw.println(engWordsList.get(i) + "," + hunWordsList.get(i) + ",");
                }
                pw.flush();
                pw.close();
                oldFile.delete();
                File dump = new File(DICTIONARY_PATH);
                newFile.renameTo(dump);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Menu.menu();

        } else {
            inputNotExisting(toBeRewrittenWord);
        }
        //lecseréli mindkét listában

    }
}
