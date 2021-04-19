package com.wordteacher.tools;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import sun.awt.SunToolkit;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    private int repeatNum;
    private int goodAnswer = 0;

    private boolean booleanExistingWord = false;

    final private String PATH_DICTIONARY_ENGHUN = "src/main/resources/dictionaryenghun.csv";
    final private String PATH_ENGVALUES = "src/main/resources/engValues.csv";
    final private String PATH_HUNVALUES = "src/main/resources/hunValues.csv";

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
            Scanner scanner = new Scanner(new File(PATH_DICTIONARY_ENGHUN));
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
            FileWriter fwWords = new FileWriter(PATH_DICTIONARY_ENGHUN, true);
            fwWords.write(engWord + "," + hunWord + "," + "\n");
            fwWords.close();

            FileWriter fwEngValues = new FileWriter(PATH_ENGVALUES, true);
            fwEngValues.write(engWord + "," + 25 + "," + "\n");
            fwEngValues.close();

            FileWriter fwHunValues = new FileWriter(PATH_HUNVALUES, true);
            fwHunValues.write(hunWord + "," + 25 + "," + "\n");
            fwHunValues.close();

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
            Scanner scanner = new Scanner(new File(PATH_DICTIONARY_ENGHUN));
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
            try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) {
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
            try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) {
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

            try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) { //ez rakja össze a file-ból a nagy listát
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

            try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) {
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

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) { //ez rakja össze a file-ból a nagy listát
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
            File oldFile = new File(PATH_DICTIONARY_ENGHUN);
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
                File dump = new File(PATH_DICTIONARY_ENGHUN);
                newFile.renameTo(dump);
            } catch (IOException e) {
                System.out.println("wordRewriter() exception: " + e);
            }
            Menu.menu();

        } else {
            inputNotExisting(toBeRewrittenWord);
        }
    }

    public void wordRemover() {
        List<List<String>> dictionaryInList = new ArrayList<>();
        List<String> engWordsList = new ArrayList<>();
        List<String> hunWordsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) { //ez rakja össze a file-ból a nagy listát
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                dictionaryInList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("wordRemover() exception: " + e);
        }

        for (int i = 0; i < dictionaryInList.size(); i++) {
            engWordsList.add(dictionaryInList.get(i).get(0));
            hunWordsList.add(dictionaryInList.get(i).get(1));
        }
        //bekérem a szót amit törölni szeretnék
        System.out.println("Enter the word you want to remove: ");
        String toBeRemovedWord = scanner();

        if (engWordsList.contains(toBeRemovedWord) || hunWordsList.contains(toBeRemovedWord)) {
            System.out.println("The " + toBeRemovedWord + " word is deleted.");
            for (int i = 0; i < engWordsList.size(); i++) {
                Pattern pattern = Pattern.compile(toBeRemovedWord);
                Matcher matcher = pattern.matcher(engWordsList.get(i));
                if (matcher.find()) {
                    engWordsList.remove(i);
                    hunWordsList.remove(i);

                } else {
                    Matcher matcher2 = pattern.matcher(hunWordsList.get(i));
                    if (matcher2.find()) {
                        engWordsList.remove(i);
                        hunWordsList.remove(i);

                    }
                }
            }
            //a listákból újraírja a filet
            String tempfile = "temp.txt";
            File oldFile = new File(PATH_DICTIONARY_ENGHUN);
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
                File dump = new File(PATH_DICTIONARY_ENGHUN);
                newFile.renameTo(dump);
            } catch (IOException e) {
                System.out.println("wordRemover() exception: " + e);
            }
            Menu.menu();

        } else {
            System.out.println(RED.typeOfColor + "Can't find this word to remove: " + RESET.typeOfColor + toBeRemovedWord);
        }
    }

    public void repeaterEng() {
        System.out.println("How many words you need?");
        repeatNum = scannerNum();
        for (int i = 0; i < repeatNum; i++) {
            System.out.println("Question " + (i + 1));
            wordQuizEng();
        }
        repeaterEnd();
    }

    public void repeaterHun() {
        System.out.println("How many words you need?");
        repeatNum = scannerNum();
        for (int i = 0; i < repeatNum; i++) {
            System.out.println("Question " + (i + 1));
            wordQuizHun();
        }
        repeaterEnd();
    }

    private int scannerNum() {
        while (true) {
            try {
                Scanner reader = new Scanner(System.in);
                return reader.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(RED.typeOfColor + "wrong input data!" + RESET.typeOfColor);
            }
        }
    }

    private void repeaterEnd() {
        System.out.println("Asked words / correct answers: " + repeatNum + "/" + goodAnswer); //TODO miért kékül ez be futtatásnál a perjel után?
        goodAnswer = 0;
        repeatNum = 0;
        Menu.menu();
    }

    private void wordQuizEng() {

        List<List<String>> dictionaryInList = new ArrayList<>();
        MultiValuedMap<String, String> engDictionaryWithHunSynonyms = new ArrayListValuedHashMap<>(); //Multi Valued Map leírása: https://www.baeldung.com/apache-commons-multi-valued-map

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) { //ez rakja össze a szótárból a tömbös listát
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                dictionaryInList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("wordQuizEng() exception: " + e);
        }

        for (int i = 0; i < dictionaryInList.size(); i++) { //feltölti a multiMapet a fileból kiolvasott lista elemeivel
            engDictionaryWithHunSynonyms.put(dictionaryInList.get(i).get(0), dictionaryInList.get(i).get(1));
        }

        List<List<String>> engValuesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_ENGVALUES))) { //ez rakja össze az engvalues file-ból a tömbös listát
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                engValuesList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("wordQuizEng() exception: " + e);
        }

        List<String> engValuesWords = new ArrayList<>(); //csinál egy String listát a tömbös lista első oszlopából
        for (int i = 0; i < engValuesList.size(); i++) {
            engValuesWords.add(engValuesList.get(i).get(0));
        }

        List<String> engValuesNumbers = new ArrayList<>(); //csinál egy String listát a tömbös lista második oszlopából
        for (int i = 0; i < engValuesList.size(); i++) {
            engValuesNumbers.add(engValuesList.get(i).get(1));
        }
        List<Integer> engValues = engValuesNumbers.stream().map(Integer::valueOf).collect(Collectors.toList()); //Integer listává alakítja a String listát

        Map<String, Integer> engMap = new HashMap<>(); //létrehoztam a map-et a random kérdezés miatt
        for (int i = 0; i < engValuesWords.size(); i++) {
            engMap.put(engValuesWords.get(i), engValues.get(i));
        }

        Set<String> setEngValuesWords = new HashSet(engValuesWords); //ez csinálja meg a values set listát hogy ne legyenek duplán szavak

        List<String> engWordsListNoDuplicates = new ArrayList<>(setEngValuesWords); //ez a lista már nem tartalmaz dupla szavakat.

        List<String> questionList = new ArrayList<>(); //ebből a listából fog kérdezni

        while (questionList.size() < 1) { //ezzel védem le, hogy hogyha olyan kicsi a random szám, hogy egy szó sincs olyan alacsony értéken, akkor ne fagyjon le a progi
            int randomNum = (int) (Math.random() * (100)) + 1; //random szám generátor 1-100-ig
            for (int i = 0; i < engMap.size(); i++) { //megnézi, hogy melyik values kisebb mint a dobott érték, és bemásolja a szót a kérdezéshez létrehozott listába
                if (engMap.get(engWordsListNoDuplicates.get(i)) < randomNum) {
                    questionList.add(engWordsListNoDuplicates.get(i));
                }
            }
        }

        Collections.shuffle(questionList);
        String askedWord = questionList.get(0);
        System.out.println("What does the hungarian word mean: " + BLUE.typeOfColor + askedWord + RESET.typeOfColor + "?");
        System.out.print("Write the correct answer here: ");
        String answeredWord = scanner();

        if (engDictionaryWithHunSynonyms.get(askedWord).

                contains(answeredWord)) { //ezzel nézem meg, hogy a kérdezett szóhoz tartozik-e olyan value, mint a válasz szó a könyvtármapban.
            System.out.println(GREEN.typeOfColor + "jár a jutifalat!" + RESET.typeOfColor);
            goodAnswer++;
            engMap.put(questionList.get(0), engMap.get(askedWord) + 1); //ezzel módosítom a mapban a kérdezett szó-hoz (key) tartozó value-t.
        } else {
            System.out.println(RED.typeOfColor + "hülye vagy fiam mint szódás a lovát!" + RESET.typeOfColor);
            engMap.put(questionList.get(0), engMap.get(askedWord) - 2);
            System.out.println("The possible answer is: " + GREEN.typeOfColor +
                    engDictionaryWithHunSynonyms.get(askedWord) + RESET.typeOfColor);
        }

        valueFilesRewriter(engMap, PATH_ENGVALUES);
    }

    private void wordQuizHun() {

        List<List<String>> dictionaryInList = new ArrayList<>();
        MultiValuedMap<String, String> hunDictionaryWithEngSynonyms = new ArrayListValuedHashMap<>(); //Multi Valued Map leírása: https://www.baeldung.com/apache-commons-multi-valued-map

        try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) { //ez rakja össze a szótárból a tömbös listát
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                dictionaryInList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("wordQuizHun() exception: " + e);
        }

        for (int i = 0; i < dictionaryInList.size(); i++) { //feltölti a multiMapet a fileból kiolvasott lista elemeivel
            hunDictionaryWithEngSynonyms.put(dictionaryInList.get(i).get(1), dictionaryInList.get(i).get(0));
        }

        List<List<String>> hunValuesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH_HUNVALUES))) { //ez rakja össze a hunvalues file-ból a tömbös listát
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                hunValuesList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("wordQuizHun() exception: " + e);
        }

        List<String> hunValuesWords = new ArrayList<>(); //csinál egy String listát a tömbös lista első oszlopából
        for (int i = 0; i < hunValuesList.size(); i++) {
            hunValuesWords.add(hunValuesList.get(i).get(0));
        }

        List<String> hunValuesNumbers = new ArrayList<>(); //csinál egy String listát a tömbös lista második oszlopából
        for (int i = 0; i < hunValuesList.size(); i++) {
            hunValuesNumbers.add(hunValuesList.get(i).get(1));
        }
        List<Integer> hunValues = hunValuesNumbers.stream().map(Integer::valueOf).collect(Collectors.toList()); //Integer listává alakítja a String listát

        Map<String, Integer> hunMap = new HashMap<>(); //létrehoztam a map-et a random kérdezés miatt
        for (int i = 0; i < hunValuesWords.size(); i++) {
            hunMap.put(hunValuesWords.get(i), hunValues.get(i));
        }

        Set<String> setHunValuesWords = new HashSet(hunValuesWords); //ez csinálja meg a values set listát hogy ne legyenek duplán szavak

        List<String> hunWordsListNoDuplicates = new ArrayList<>(setHunValuesWords); //ez a lista már nem tartalmaz dupla szavakat.

        List<String> questionList = new ArrayList<>(); //ebből a listából fog kérdezni

        while (questionList.size() < 1) { //ezzel védem le, hogy hogyha olyan kicsi a random szám, hogy egy szó sincs olyan alacsony értéken, akkor ne fagyjon le a progi
            int randomNum = (int) (Math.random() * (100)) + 1; //random szám generátor 1-100-ig
            for (int i = 0; i < hunMap.size(); i++) { //megnézi, hogy melyik values kisebb mint a dobott érték, és bemásolja a szót a kérdezéshez létrehozott listába
                if (hunMap.get(hunWordsListNoDuplicates.get(i)) < randomNum) {
                    questionList.add(hunWordsListNoDuplicates.get(i));
                }
            }
        }

        Collections.shuffle(questionList);
        String askedWord = questionList.get(0);
        System.out.println("What does the hungarian word mean: " + BLUE.typeOfColor + askedWord + RESET.typeOfColor + "?");
        System.out.print("Write the correct answer here: ");
        String answeredWord = scanner();

        if (hunDictionaryWithEngSynonyms.get(askedWord).

                contains(answeredWord)) { //ezzel nézem meg, hogy a kérdezett szóhoz tartozik-e olyan value, mint a válasz szó a könyvtármapban.
            System.out.println(GREEN.typeOfColor + "jár a jutifalat!" + RESET.typeOfColor);
            goodAnswer++;
            hunMap.put(questionList.get(0), hunMap.get(askedWord) + 1); //ezzel módosítom a mapban a kérdezett szó-hoz (key) tartozó value-t.
        } else {
            System.out.println(RED.typeOfColor + "hülye vagy fiam mint szódás a lovát!" + RESET.typeOfColor);
            hunMap.put(questionList.get(0), hunMap.get(askedWord) - 2);
            System.out.println("The possible answer is: " + GREEN.typeOfColor +
                    hunDictionaryWithEngSynonyms.get(askedWord) + RESET.typeOfColor);
        }

        valueFilesRewriter(hunMap, PATH_HUNVALUES);
    }

    private void valueFilesRewriter(Map<String, Integer> hunMap, String path) {
        String tempfile = "temp.txt";
        File oldFile = new File(path);
        File newFile = new File(tempfile);

        try {
            FileWriter fw = new FileWriter(tempfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter((bw));

            Iterator<Map.Entry<String, Integer>> iterator = hunMap.entrySet().iterator(); //iterátor, hogy végig tudjak iterálni a hunMap keyein és valuein.
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                pw.println(entry.getKey() + "," + entry.getValue() + ",");
            }

            pw.flush();
            pw.close();
            oldFile.delete();
            File dump = new File(path);
            newFile.renameTo(dump);
        } catch (
                IOException e) {
            System.out.println("valueFilesRewriter(Map<String, Integer> hunMap, String path) exception: " + e);
        }
    }


}
