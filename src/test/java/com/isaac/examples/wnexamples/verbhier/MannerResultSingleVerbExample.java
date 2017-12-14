package com.isaac.examples.wnexamples.verbhier;

import com.isaac.phrases.SynsetElement;
import com.isaac.wordnet.WordNet;
import com.isaac.wordnet.BaseExtraction;
import edu.mit.jwi.item.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MannerResultSingleVerbExample {
    public static void main (String[] args) throws IOException {
        String keyStr = "change%2:30:01::";
        ISenseKey key = WordNet.parseSenseKeyString(keyStr);
        IWord word = WordNet.getWordBySenseKey(key);
        ISynset synset = word.getSynset();
        System.out.println(synset.getGloss());
        List<ISynset> synsets = synset.getRelatedSynsets(Pointer.HYPONYM).stream().map(WordNet.wndict::getSynset)
                .collect(Collectors.toList());
        synsets.forEach(syn -> {
            List<String> gloss = Arrays.asList(syn.getGloss().split(";")[0].split(" "));
            int idx1 = gloss.indexOf(keyStr.split("%")[0]);
            int idx2 = gloss.indexOf("by");
            if (idx2 == -1) idx2 = gloss.indexOf("with");
            if (idx1 != -1 && idx2 != -1 && idx1 < idx2) System.out.println(WordNet.synset2String(syn, false));
        });
        List<LinkedList<SynsetElement>> hyponyms = BaseExtraction.hyponymList(synset);
        Set<String> unique = new HashSet<>();
        List<LinkedList<String>> result = new ArrayList<>();
        for (LinkedList<SynsetElement> list : hyponyms) {
            LinkedList<String> res = new LinkedList<>();
            int count = 0;
            for (int i = list.size() - 2; i >= 0; i--) {
                if (isResultSynset(list.get(i))) {
                    res.addLast(list.get(i).getSynsetStr().concat("-").concat(list.get(i).getGloss().split(";")[0]));
                    count++;
                }
                else res.addLast(list.get(i).getSynsetStr().concat("-NONE"));
            }
            if (count == 0) continue;
            while (res.getLast().contains("NONE")) res.removeLast(); // remove tails
            if (unique.add(res.toString())) result.add(res);
        }
        result.forEach(System.out::println);
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/zhanghao/Desktop/test.txt"));
        for (List<String> list : result) {
            for (String str : list) {
                writer.write(str.concat("\t"));
            }
            writer.write("\n");
        }
        writer.close();
        System.out.println("Done...");
    }

    private static boolean isResultSynset (SynsetElement synsetElement) {
        List<String> gloss = Arrays.asList(synsetElement.getGloss().split(";")[0].split(" "));
        int idx = gloss.indexOf("by") + gloss.indexOf("with");
        return idx != -2;
    }
}
