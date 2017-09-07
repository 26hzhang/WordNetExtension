package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import com.isaac.utils.IOUtils;
import edu.mit.jwi.item.ISenseKey;
import edu.mit.jwi.item.POS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClassifyCoreWordNetByDomain {
    public static void main (String[] args) throws IOException {
        String tagStr = POSMap.get(POS.VERB);
        List<String> cwn = IOUtils.loadFile2List("data/core-wordnet-list.txt");
        Map<String, List<String>> map = new HashMap<>();
        for (String line : cwn) {
            if (!line.split(" ")[0].equals(tagStr)) continue;
            String str = line.split(" ")[1];
            String word = str.substring(1, str.indexOf("%"));
            ISenseKey senseKey = WordNetUtils.parseSenseKeyString(str.substring(1, str.length() - 1));
            String domain = senseKey.getLexicalFile().toString();
            if (map.containsKey(domain)) map.get(domain).add(word);
            else map.put(domain, new ArrayList<>(Collections.singletonList(word)));
        }
        map.entrySet().stream().map(e -> e.getKey()+ "\t" + e.getValue()).forEach(System.out::println);
        String path = "/Users/zhanghao/Desktop/verbs-lexical/";
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path.concat(entry.getKey()).concat(".txt")));
            entry.getValue().forEach(word -> {
                try {
                    writer.write(word + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        }
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
