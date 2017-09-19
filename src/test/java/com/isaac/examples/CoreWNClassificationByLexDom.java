package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.ISenseKey;
import edu.mit.jwi.item.POS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CoreWNClassificationByLexDom {
    public static void main (String[] args) throws IOException {
        String tagStr = POSMap.get(POS.VERB);
        List<String> cwn = Files.readAllLines(Paths.get(WordNetUtils.GLOBALPATH.concat("data/core-wordnet-list.txt")));
        Map<String, List<String>> map = cwn.stream()
                .filter(line -> line.split(" ")[0].equals(tagStr))
                .map(line -> line.split(" ")[1].substring(1, line.split(" ")[1].length() - 1))
                .map(WordNetUtils::parseSenseKeyString)
                .collect(Collectors.groupingBy(WordNetUtils::getLexicalFileBySenseKey,
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

    /** POS tag pairs */
    private static final Map<POS, String> POSMap = new HashMap<POS, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(POS.VERB, "v");
            put(POS.NOUN, "n");
            put(POS.ADJECTIVE, "a");
            put(POS.ADVERB, "a");
        }
    };
}
