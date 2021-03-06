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
import static com.wordteacher.utils.Colors.CYAN;
import static com.wordteacher.utils.Colors.GREEN;
import static com.wordteacher.utils.Colors.RED;
import static com.wordteacher.utils.Colors.RESET;

public class WordTeacher {

    private int repeatNum;
    private int goodAnswer = 0;
    private int newWordsRecordCounter = 0;
    private int chanceNumber = 0;
    private int noMoreWords = 0;

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
        repeatAsk();

    }

    private void repeatAsk() {
        System.out.println("Do you want to recording a new word immediately? If yes, type "
                + RED.typeOfColor + "\"yes\" " + RESET.typeOfColor + "or just " + RED.typeOfColor +
                "\"y\"" + RESET.typeOfColor + "! Anything else returns back to the menu.");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if (answer.equals("yes") || answer.equals("y")) {
            enteringAWordToLearn();
        } else {
            try {
                FileWriter fw = new FileWriter("src/main/resources/results.csv", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter((bw));

                pw.print("new words recorded :" + newWordsRecordCounter + " ");
                pw.println(java.time.LocalDate.now() + " " + java.time.LocalTime.now());
                pw.flush();
                pw.close();
            } catch (IOException e) {
                System.out.println("repeatAsk() exception: " + e);
            }
            Menu.menu();
        }
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

            newWordsRecordCounter++;
            System.out.println(GREEN.typeOfColor + "The specified word pair is added to the dictionary." + RESET.typeOfColor);
        } catch (Exception e) {
            System.out.println("Record not saved");
        }
    }

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
            MultiValuedMap<String, String> engDictionaryWithHunSynonyms = new ArrayListValuedHashMap<>(); //Multi Valued Map le??r??sa: https://www.baeldung.com/apache-commons-multi-valued-map

            try (BufferedReader br = new BufferedReader(new FileReader(PATH_DICTIONARY_ENGHUN))) { //ez rakja ??ssze a file-b??l a nagy list??t
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    dictionaryInList.add(Arrays.asList(values));
                }
            }

            for (int i = 0; i < dictionaryInList.size(); i++) { //ez csin??lja meg az angol set list??t
                setEngWords.add(dictionaryInList.get(i).get(0));
            }

            List<String> orderedEngWordsList = setEngWords.stream().sorted().collect(Collectors.toList()); //a setet streameli, abc sorrendbe rakja, majd list??v?? alak??tja

            for (int i = 0; i < dictionaryInList.size(); i++) { //felt??lti a multiMapet a fileb??l kiolvasott lista elemeivel
                engDictionaryWithHunSynonyms.put(dictionaryInList.get(i).get(0), dictionaryInList.get(i).get(1));
            }

            for (int i = 1; i < orderedEngWordsList.size() + 1; i++) { //nyomtatja a multiMap value elemeit az abc sorrendbe tett set h??v??sa szerint.
                System.out.println(BLUE.typeOfColor + orderedEngWordsList.get(i - 1) + RESET.typeOfColor
                        + GREEN.typeOfColor + engDictionaryWithHunSynonyms.get(orderedEngWordsList.get(i - 1)) + RESET.typeOfColor);
                if (i % 20 == 0) { //ezzel ??rem el azt, hogy ne h??nyja tele a k??perny??t szavakkal, ha m??r nagy a sz??t??r
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

        readFromFileToListList(dictionaryInList, PATH_DICTIONARY_ENGHUN);

        for (int i = 0; i < dictionaryInList.size(); i++) {
            engWordsList.add(dictionaryInList.get(i).get(0));
            hunWordsList.add(dictionaryInList.get(i).get(1));
        }
        //bek??rem a sz??t, ??s hogy mire akarom cser??lni
        System.out.println("Enter the word you want to rewrite: ");
        String toBeRewrittenWord = scanner();
        System.out.println("Enter the word rewritten form: ");
        String newWord = scanner();

        if (engWordsList.contains(toBeRewrittenWord) || hunWordsList.contains(toBeRewrittenWord)) {
            System.out.println("The " + toBeRewrittenWord + " word is " + newWord + " now.");
            Collections.replaceAll(engWordsList, toBeRewrittenWord, newWord);
            Collections.replaceAll(hunWordsList, toBeRewrittenWord, newWord);
            //a list??kb??l ??jra??rja a filet

            fileRewriter(engWordsList, hunWordsList);
            Menu.menu();

        } else {
            inputNotExisting(toBeRewrittenWord);
        }
    }

    private void fileRewriter(List<String> engWordsList, List<String> hunWordsList) {
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
            System.out.println("fileRewriter(List<String> engWordsList, List<String> hunWordsList) exception: " + e);
        }
    }

    public void wordRemover() {
        List<List<String>> dictionaryInList = new ArrayList<>();
        List<String> engWordsList = new ArrayList<>();
        List<String> hunWordsList = new ArrayList<>();

        readFromFileToListList(dictionaryInList, PATH_DICTIONARY_ENGHUN);

        for (int i = 0; i < dictionaryInList.size(); i++) {
            engWordsList.add(dictionaryInList.get(i).get(0));
            hunWordsList.add(dictionaryInList.get(i).get(1));
        }
        //bek??rem a sz??t amit t??r??lni szeretn??k
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
            //a list??kb??l ??jra??rja a filet
            fileRewriter(engWordsList, hunWordsList);
            Menu.menu();

        } else {
            System.out.println(RED.typeOfColor + "Can't find this word to remove: " + RESET.typeOfColor + toBeRemovedWord);
        }
    }

    public void repeaterUnlearnedEng() {
        howManyWordsYouNeed();
        chanceNumber = 26;
        wordQuizEngIterator(chanceNumber);
        String quizType = "Unlearned Eng-Hun";
        repeaterEnd(quizType);
    }

    public void repeaterUnlearnedHun() {
        howManyWordsYouNeed();
        chanceNumber = 26;
        wordQuizHunIterator(chanceNumber);
        String quizType = "Unlearned Hun-Eng";
        repeaterEnd(quizType);
    }

    private void howManyWordsYouNeed() {
        System.out.println("How many words you need?");
        repeatNum = scannerNum();
    }

    private void wordQuizEngIterator(int chanceNumber) {
        for (int i = 0; i < repeatNum; i++) {
            if (noMoreWords >= 1000) {
                i = repeatNum;
            } else {
                System.out.println("Question " + (i + 1));
                wordQuizEng(chanceNumber);
            }
        }
    }

    private void wordQuizHunIterator(int chanceNumber) {
        for (int i = 0; i < repeatNum; i++) {
            if (noMoreWords >= 1000) {
                i = repeatNum;
            } else {
                System.out.println("Question " + (i + 1));
                wordQuizHun(chanceNumber);
            }
        }
    }

    public void repeaterEng() {
        howManyWordsYouNeed();
        chanceNumber = 100;
        wordQuizEngIterator(chanceNumber);
        String quizType = "Eng-Hun";
        repeaterEnd(quizType);
    }

    public void repeaterHun() {
        howManyWordsYouNeed();
        chanceNumber = 100;
        wordQuizHunIterator(chanceNumber);
        String quizType = "Hun-Eng";
        repeaterEnd(quizType);
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

    private void repeaterEnd(String quizType) {
        int result = goodAnswer * 100 / repeatNum;
        System.out.println(quizType + " Asked words - correct answers: " + repeatNum + " - " + goodAnswer + ". This is " + result + "%."); //TODO mi??rt k??k??l ez be futtat??sn??l a perjel ut??n?

        try {
            FileWriter fw = new FileWriter("src/main/resources/results.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter((bw));

            if (noMoreWords >= 1000) {
                if (chanceNumber == 26) {
                    pw.print("Congratulation! You learned the unlearned words! Date: ");
                }
                if (chanceNumber == 100) {
                    pw.print("Congratulation! You learned all words! Date: ");
                }
            } else {
                pw.print(quizType + " " + repeatNum + "/" + goodAnswer + " = " + result + "% Quiz end date: ");
            }
            pw.println(java.time.LocalDate.now() + " " + java.time.LocalTime.now());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            System.out.println("repeaterEnd() exception: " + e);
        }

        repeatNum = 0;
        goodAnswer = 0;
        chanceNumber = 100;
        noMoreWords = 0;
        Menu.menu();
    }

    private void wordQuizEng(int chanceNumber) {

        List<List<String>> dictionaryInList = new ArrayList<>();
        MultiValuedMap<String, String> engDictionaryWithHunSynonyms = new ArrayListValuedHashMap<>(); //Multi Valued Map le??r??sa: https://www.baeldung.com/apache-commons-multi-valued-map

        readFromFileToListList(dictionaryInList, PATH_DICTIONARY_ENGHUN);

        for (int i = 0; i < dictionaryInList.size(); i++) { //felt??lti a multiMapet a fileb??l kiolvasott lista elemeivel
            engDictionaryWithHunSynonyms.put(dictionaryInList.get(i).get(0), dictionaryInList.get(i).get(1));
        }

        List<List<String>> engValuesList = new ArrayList<>();
        readFromFileToListList(engValuesList, PATH_ENGVALUES);

        List<String> engValuesWords = new ArrayList<>(); //csin??l egy String list??t a t??mb??s lista els?? oszlop??b??l
        for (int i = 0; i < engValuesList.size(); i++) {
            engValuesWords.add(engValuesList.get(i).get(0));
        }

        List<String> engValuesNumbers = new ArrayList<>(); //csin??l egy String list??t a t??mb??s lista m??sodik oszlop??b??l
        for (int i = 0; i < engValuesList.size(); i++) {
            engValuesNumbers.add(engValuesList.get(i).get(1));
        }
        List<Integer> engValues = engValuesNumbers.stream().map(Integer::valueOf).collect(Collectors.toList()); //Integer list??v?? alak??tja a String list??t

        Map<String, Integer> engMap = new HashMap<>(); //l??trehoztam a map-et a random k??rdez??s miatt
        for (int i = 0; i < engValuesWords.size(); i++) {
            engMap.put(engValuesWords.get(i), engValues.get(i));
        }

        Set<String> setEngValuesWords = new HashSet(engValuesWords); //ez csin??lja meg a values set list??t hogy ne legyenek dupl??n szavak

        List<String> engWordsListNoDuplicates = new ArrayList<>(setEngValuesWords); //ez a lista m??r nem tartalmaz dupla szavakat.

        List<String> questionList = new ArrayList<>(); //ebb??l a list??b??l fog k??rdezni

        while (questionList.size() < 1 && noMoreWords < 1000) { // ez dobatja ??jra a random sz??mot, ha nincs r?? sz?? tal??lat. Ha t??l sokszor fut le, akkor azt jelenti, hogy nincs m??r olyan t??pus?? sz??, ami tanulhat??.
            noMoreWords++;
            int randomNum = (int) (Math.random() * (chanceNumber)) + 1; //random sz??m gener??tor 1-100-ig
            for (int i = 0; i < engMap.size(); i++) { //megn??zi, hogy melyik values kisebb mint a dobott ??rt??k, ??s bem??solja a sz??t a k??rdez??shez l??trehozott list??ba
                if (engMap.get(engWordsListNoDuplicates.get(i)) < randomNum) {
                    questionList.add(engWordsListNoDuplicates.get(i));

                }
            }
        }

        Collections.shuffle(questionList);
        if (questionList.size() == 0) {
            ifNoMoreQuestion(chanceNumber);
        } else {
            String askedWord = questionList.get(0);
            System.out.println("What does the hungarian word mean: " + BLUE.typeOfColor + askedWord + RESET.typeOfColor);
            System.out.print("Write the correct answer here: ");
            String answeredWord = scanner();

            answerChecker(engDictionaryWithHunSynonyms, engMap, questionList, askedWord, answeredWord);

            valueFilesRewriter(engMap, PATH_ENGVALUES);
        }
    }

    private void ifNoMoreQuestion(int chanceNumber) {
        if (noMoreWords >= 1000) {
            if (chanceNumber == 26) {
                System.out.println(CYAN.typeOfColor + "Congratulation! You learned the unlearned words!" + RESET.typeOfColor);
            }
            if (chanceNumber == 100) {
                System.out.println(CYAN.typeOfColor + "Congratulation! You learned all words!" + RESET.typeOfColor);
            }
        }
    }

    private void wordQuizHun(int chanceNumber) {

        List<List<String>> dictionaryInList = new ArrayList<>();
        MultiValuedMap<String, String> hunDictionaryWithEngSynonyms = new ArrayListValuedHashMap<>(); //Multi Valued Map le??r??sa: https://www.baeldung.com/apache-commons-multi-valued-map

        readFromFileToListList(dictionaryInList, PATH_DICTIONARY_ENGHUN);

        for (int i = 0; i < dictionaryInList.size(); i++) { //felt??lti a multiMapet a fileb??l kiolvasott lista elemeivel
            hunDictionaryWithEngSynonyms.put(dictionaryInList.get(i).get(1), dictionaryInList.get(i).get(0));
        }

        List<List<String>> hunValuesList = new ArrayList<>();
        readFromFileToListList(hunValuesList, PATH_HUNVALUES);

        List<String> hunValuesWords = new ArrayList<>(); //csin??l egy String list??t a t??mb??s lista els?? oszlop??b??l
        for (int i = 0; i < hunValuesList.size(); i++) {
            hunValuesWords.add(hunValuesList.get(i).get(0));
        }

        List<String> hunValuesNumbers = new ArrayList<>(); //csin??l egy String list??t a t??mb??s lista m??sodik oszlop??b??l
        for (int i = 0; i < hunValuesList.size(); i++) {
            hunValuesNumbers.add(hunValuesList.get(i).get(1));
        }
        List<Integer> hunValues = hunValuesNumbers.stream().map(Integer::valueOf).collect(Collectors.toList()); //Integer list??v?? alak??tja a String list??t

        Map<String, Integer> hunMap = new HashMap<>(); //l??trehoztam a map-et a random k??rdez??s miatt
        for (int i = 0; i < hunValuesWords.size(); i++) {
            hunMap.put(hunValuesWords.get(i), hunValues.get(i));
        }

        Set<String> setHunValuesWords = new HashSet(hunValuesWords); //ez csin??lja meg a values set list??t hogy ne legyenek dupl??n szavak

        List<String> hunWordsListNoDuplicates = new ArrayList<>(setHunValuesWords); //ez a lista m??r nem tartalmaz dupla szavakat.

        List<String> questionList = new ArrayList<>(); //ebb??l a list??b??l fog k??rdezni
        while (questionList.size() < 1 && noMoreWords < 1000) { // ez dobatja ??jra a random sz??mot, ha nincs r?? sz?? tal??lat. Ha t??l sokszor fut le, akkor azt jelenti, hogy nincs m??r olyan t??pus?? sz??, ami tanulhat??.
            noMoreWords++;
            int randomNum = (int) (Math.random() * (chanceNumber)) + 1; //random sz??m gener??tor 1-100-ig
            for (int i = 0; i < hunMap.size(); i++) { //megn??zi, hogy melyik values kisebb mint a dobott ??rt??k, ??s bem??solja a sz??t a k??rdez??shez l??trehozott list??ba
                if (hunMap.get(hunWordsListNoDuplicates.get(i)) < randomNum) {
                    questionList.add(hunWordsListNoDuplicates.get(i));
                }
            }
        }

        Collections.shuffle(questionList);
        if (questionList.size() == 0) {
            ifNoMoreQuestion(chanceNumber);
        } else {
            String askedWord = questionList.get(0);
            System.out.println("What does the hungarian word mean: " + BLUE.typeOfColor + askedWord + RESET.typeOfColor);
            System.out.print("Write the correct answer here: ");
            String answeredWord = scanner();

            answerChecker(hunDictionaryWithEngSynonyms, hunMap, questionList, askedWord, answeredWord);

            valueFilesRewriter(hunMap, PATH_HUNVALUES);
        }
    }

    private void readFromFileToListList(List<List<String>> dictionaryInList, String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) { //ez rakja ??ssze a sz??t??rb??l a t??mb??s list??t
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                dictionaryInList.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            System.out.println("readFromFileToListList(List<List<String>> dictionaryInList, String path) exception: " + e);
        }
    }

    private void answerChecker
            (MultiValuedMap<String, String> hunDictionaryWithEngSynonyms, Map<String, Integer> hunMap, List<String> questionList, String
                    askedWord, String answeredWord) {
        if (hunDictionaryWithEngSynonyms.get(askedWord).contains(answeredWord)) { //ezzel n??zem meg, hogy a k??rdezett sz??hoz tartozik-e olyan value, mint a v??lasz sz?? a k??nyvt??rmapban.
            System.out.print(GREEN.typeOfColor + "j??r a jutifalat!" + RESET.typeOfColor);
            goodAnswer++;
            hunMap.put(questionList.get(0), hunMap.get(askedWord) + 1); //ezzel m??dos??tom a mapban a k??rdezett sz??-hoz (key) tartoz?? value-t.
            System.out.println("The value of word increased to: " + hunMap.put(questionList.get(0), hunMap.get(askedWord)));
        } else {
            hunMap.put(questionList.get(0), hunMap.get(askedWord) - 2);
            System.out.println(RED.typeOfColor + "h??lye vagy fiam mint sz??d??s a lov??t! " + RESET.typeOfColor + "The value of word decreased to: " + hunMap.put(questionList.get(0), hunMap.get(askedWord)));
            System.out.println("The possible answer is: " + GREEN.typeOfColor +
                    hunDictionaryWithEngSynonyms.get(askedWord) + RESET.typeOfColor);
        }
    }

    private void valueFilesRewriter(Map<String, Integer> hunMap, String path) {
        String tempfile = "temp.txt";
        File oldFile = new File(path);
        File newFile = new File(tempfile);

        try {
            FileWriter fw = new FileWriter(tempfile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter((bw));

            Iterator<Map.Entry<String, Integer>> iterator = hunMap.entrySet().iterator(); //iter??tor, hogy v??gig tudjak iter??lni a hunMap keyein ??s valuein.
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
