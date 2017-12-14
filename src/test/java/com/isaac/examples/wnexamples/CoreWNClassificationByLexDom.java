package com.isaac.examples.wnexamples;

import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.ISenseKey;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CoreWNClassificationByLexDom {
    public static void main (String[] args) throws IOException {
        String tagStr = "v"; // POS.VERB: v, POS.NOUN: n, POS.ADJECTIVE & POS.ADVERB: a
        List<String> cwn = Files.readAllLines(Paths.get(WordNet.GLOBALPATH.concat("data/core-wordnet-list.txt")));
        Map<String, List<String>> map = cwn.stream()
                .filter(line -> line.split(" ")[0].equals(tagStr))
                .map(line -> line.split(" ")[1].substring(1, line.split(" ")[1].length() - 1))
                .map(WordNet::parseSenseKeyString)
                .collect(Collectors.groupingBy(WordNet::getLexicalFileBySenseKey,
                        Collectors.mapping(ISenseKey::getLemma, Collectors.toList())));
        // Save to file
        String directory = "/Users/zhanghao/Desktop/verbs-lexical/";
        map.forEach((key, value) -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(directory.concat(key).concat(".txt")));
                value.forEach(word -> {
                    try { writer.write(word.concat("\n"));
                    } catch (IOException e) { e.printStackTrace(); }
                });
                writer.close();
            } catch (IOException e) { e.printStackTrace(); }
        });
        System.out.println("done...");
    }
}
