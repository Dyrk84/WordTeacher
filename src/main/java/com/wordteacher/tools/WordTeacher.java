package com.wordteacher.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    final private String DICTIONARY_PATH = "D:\\DyrkWork\\WordTeacher\\engWords.csv";

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

    public void dictionaryOverview() {
        //Mapet csinálni, String key Array value
        //iterálni az angol szavakon regexel, ha van ugyanolyan angol szó, a mellette lévő magyar szavat rakja egy tömbbe. Rakja az angol szavakat egy Mapbe, a key helyére, a  tömböt a value helyére.
        try {
            List<List<String>> dictionaryInList = new ArrayList<>();
            List<String> engWordsList = new ArrayList<>();
            List<String> hunWordsList = new ArrayList<>();
            Set<String> setEngWords = new HashSet();
            Map<String, String[]> orderedDictionaryInMap = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_PATH))) { //ez rakja össze a file-ból a nagy listát
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    dictionaryInList.add(Arrays.asList(values));
                }
            }

            for (int i = 0; i < dictionaryInList.size(); i++) { //ez csinálja meg az angol és a magyar listát
                engWordsList.add(dictionaryInList.get(i).get(0));
                hunWordsList.add(dictionaryInList.get(i).get(1));
            }

            for (int i = 0; i < dictionaryInList.size(); i++) {
                setEngWords.add(engWordsList.get(i));
            }
            List<String> orderedEngWordsList = setEngWords.stream().sorted().collect(Collectors.toList());


            //ez nyomtatja ki a szótárat, de a többszörös jelentések többször vannak benne.
            for (int i = 1; i < engWordsList.size() + 1; i++) { //azért 1-ről indul, hogy a számláló ne nyomtasson 0-nál egy sort. Így ha lement az első 20, akkor fog feljönni a pressEnter és a számláló.
                System.out.print(engWordsList.get(i - 1) + " - ");
                System.out.println(hunWordsList.get(i - 1));
                if (i % 20 == 0) { //ezzel érem el azt, hogy ne hányja tele a képernyőt szavakkal
                    System.out.println("The dictionary overviewed content : " + engWordsList.size() + "/" + (i));
                    pressEnterToContinue();
                }
            }

            for (int i = 0; i < orderedEngWordsList.size(); i++) {
                Pattern pattern = Pattern.compile(orderedEngWordsList.get(i));
                int matcherCounter = 0;
                for (int j = 0; j < dictionaryInList.size(); j++) {
                    Matcher matcher = pattern.matcher(dictionaryInList.get(j).get(0));
                    while (matcher.find()) {
                        matcherCounter++;
                    }
                    String[] hunSynonyms = new String[matcherCounter];

                    Matcher matcher2 = pattern.matcher(dictionaryInList.get(j).get(0));
                    while (matcher2.find()) {
                        String bla = dictionaryInList.get(j).get(1);
                        for (int k = 0; k < hunSynonyms.length; k++){
                            hunSynonyms[k] = bla;
                        }
                        System.out.println("hunSynonyms kiíratása: " + Arrays.toString(hunSynonyms));
                        System.out.println("orderedDictionaryInMap kiíratása üresen: " + orderedDictionaryInMap);
                        Objects.requireNonNull(Objects.requireNonNull(orderedDictionaryInMap.put(orderedEngWordsList.get(i), hunSynonyms)));
                        System.out.println("orderedDictionaryInMap kiíratása: " + orderedDictionaryInMap);
                    }
                }

            }

//                for (int j = 0; j < dictionaryInList.size(); j++) {
//                    String[] hunSynonyms = dictionaryInList.get(i).get(1);
//                }
//                orderedDictionaryInMap.put(engWordsList.get(i), hunSynonyms.get(i));


//            for (int i = 0; i < orderedDictionaryInMap.size(); i++) {
//                System.out.println("rendezett szótár: " + orderedDictionaryInMap.get("key"));
//            }

            pressEnterToContinue();
            Menu.menu();
        } catch (
                FileNotFoundException e) {
            System.out.println("translateEngHun() exception: " + e);
        } catch (
                IOException e) {
            System.out.println("translateEngHun() exception: " + e);
        }
    }

//kell egy lista, milyen szavak vannak a szótárban, abc sorrendben.

//ha hibásan lett bevíve egy szó, ki kell tudni javítani!
//kell kérni egy inputot. Azt az inputot meg kell keresni a fileban, és át kell írni.
}
