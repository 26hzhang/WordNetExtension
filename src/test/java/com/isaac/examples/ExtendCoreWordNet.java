package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.IWord;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExtendCoreWordNet {

    public static void main(String[] args) throws IOException {
        // load bnc data
        Map<String, Integer> bnc = Files.readAllLines(Paths.get(WordNetUtils.GLOBALPATH.concat("data/bnc.txt"))).stream()
                // replace all "adv" to "a" (since core wordnet list does not differentiate adv and adjective, both are denoted as "a")
                .map(line -> line.replace("\tadv", "\ta"))
                .map(line -> line.split("\t")) // split line to array
                .map(arr -> new Pair<>(arr[2] + "::" + arr[3], Integer.parseInt(arr[1]))) // arr[1]: count, arr[2]: word, arr[3]: POS
                // use summingInt, since after replace "adv" to "a", some words are repeated, in this case, add their count.
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.summingInt(Pair::getValue)));
        // load core word net list
        List<String> extCoreWN = Files.readAllLines(Paths.get(WordNetUtils.GLOBALPATH.concat("data/core-wordnet-list.txt"))).stream()
                        .map(line -> line.split(" "))
                        .filter(ele -> WordNetUtils.getWordBySenseKeyStr(ele[1].substring(1, ele[1].length() - 1)) != null)
                        .map(ele -> {
                            String senseKeyStr = ele[1].substring(1, ele[1].length() - 1);
                            IWord iword = WordNetUtils.getWordBySenseKey(WordNetUtils.parseSenseKeyString(senseKeyStr));
                            int count = 0;
                            if (bnc.containsKey(iword.getLemma() + "::" + ele[0])) count = bnc.get(iword.getLemma() + "::" + ele[0]);
                            return String.join("\t", ele[0], iword.getLemma(), senseKeyStr, String.valueOf(WordNetUtils.getSenseNumber4IWord(iword)),
                                    String.valueOf(WordNetUtils.getNumberOfSense4Word(iword.getLemma(), iword.getPOS())), String.valueOf(count),
                                    String.valueOf(WordNetUtils.getTagCount4IWord(iword)), String.valueOf(WordNetUtils.getSumOfTagCount4Word(iword.getLemma(), iword.getPOS())),
                                    WordNetUtils.synset2String(iword.getSynset(), true), iword.getSynset().getGloss()).concat("\n");
                        })
                        .collect(Collectors.toList());
        // write to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(WordNetUtils.GLOBALPATH.concat("data/result-test.txt")));
        writer.write(String.join("\t", "POS", "Word", "SenseKey", "Sense_Number", "Total_Senses", "BNC_Count",
                "Tag_Count", "Sum_Tag_Count", "Synset", "Gloss").concat("\n"));
        for (String str : extCoreWN)
            writer.write(str);
        writer.close();
        System.out.println("Done...");
    }
}
