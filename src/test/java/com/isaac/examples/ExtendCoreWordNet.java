package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import com.isaac.utils.IOUtils;
import edu.mit.jwi.data.parse.SenseKeyParser;
import edu.mit.jwi.item.ISenseKey;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendCoreWordNet {

    public static void main(String[] args) throws IOException {
        String inFilename = "data/core-wordnet-list.txt";
        String outFilename = "result-test.txt";
        extendCoreWordNet(inFilename, outFilename);
    }

    /** Extend core wordnet list */
    private static void extendCoreWordNet(String inFilename, String outFilename) throws IOException {
        Map<String, Integer> bnc = loadBNCData();
        List<String> cwn = IOUtils.loadFile2List(inFilename);
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/zhanghao/Desktop/".concat(outFilename)));
        try {
            writer.write(String.join("\t", "POS", "Word", "SenseKey", "Sense_Number", "Total_Senses",
                    "BNC_Count", "Tag_Count", "Sum_Tag_Count", "Synset", "Gloss").concat("\n"));
            for (String line : cwn) {
                String[] element = line.split(" ");
                String senseKeyStr = element[1].substring(1, element[1].length() - 1); // obtain ISenseKey string
                String word = element[1].substring(1, element[1].indexOf("%")); // obtain word lemma
                int count = 0;
                if (bnc.containsKey(word + "::" + element[0])) count = bnc.get(word + "::" + element[0]); // get word count
                ISenseKey key = SenseKeyParser.getInstance().parseLine(senseKeyStr); // convert to ISenseKey
                IWord iword = WordNetUtils.getWordBySenseKey(key); // get IWord
                if (iword == null) continue;
                ISynset synset = iword.getSynset(); // get ISynset
                int senseNumber = WordNetUtils.getSenseNumber4IWord(iword); // get sense number
                int tagCount = WordNetUtils.getTagCount4IWord(iword); // get tag count
                int totalSense = WordNetUtils.getNumberOfSense4Word(iword.getLemma(), iword.getPOS());
                int sumTagCount = WordNetUtils.getSumOfTagCount4Word(iword.getLemma(), iword.getPOS());
                writer.write(String.join("\t", element[0], word, senseKeyStr, String.valueOf(senseNumber),
                        String.valueOf(totalSense), String.valueOf(count), String.valueOf(tagCount),
                        String.valueOf(sumTagCount), WordNetUtils.synset2String(synset, true), synset.getGloss())
                        .concat("\n"));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finished!!");
    }

    private static Map<String, Integer> loadBNCData() throws IOException {
        Map<String, Integer> bnc = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(WordNetUtils.GLOBALPATH.concat("data/bnc.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] element = line.split("\t"); // element[1]: count, element[2]: word lemma, element[3]: POS
            if (element[3].equals("adv")) element[3] = "a";
            bnc.put(element[2] + "::" + element[3], Integer.parseInt(element[1]));
        }
        reader.close();
        return bnc;
    }
}
