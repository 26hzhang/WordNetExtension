package com.isaac.examples;

import com.isaac.representation.SynsetElement;
import com.isaac.wordnet.BaseExtraction;
import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MannerResultVerbsHierarchy {
    public static void main (String[] args) throws IOException {
        // load verb synset strings to list
        List<String> resultSynsetsString = Files.readAllLines(Paths.get(WordNetUtils.GLOBALPATH.concat("data/result_verbs_synsets.txt")));
        // convert those synset string to ISynset (only if the those synsets are at the top level )
        List<ISynset> synsets = convert2Synsets(resultSynsetsString);
        List<LinkedList<String>> results = new ArrayList<>(200000);
        synsets.forEach(iSynset -> results.addAll(getTroponyms(iSynset)));
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/zhanghao/Desktop/verbs_hierarchies.txt"));
        for (List<String> list : results) {
            writer.write(String.join("\t", list));
            writer.write("\n");
        }
        writer.close();
        System.out.println("Done...");
    }

    private static List<LinkedList<String>> getTroponyms (ISynset iSynset) {
        List<LinkedList<String>> troponyms = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        List<LinkedList<SynsetElement>> hyponyms = BaseExtraction.hyponymList(iSynset);
        for (LinkedList<SynsetElement> list : hyponyms) {
            LinkedList<String> res = new LinkedList<>();
            int count = 0;
            for (int i = list.size() - 1; i >= 0; i--) {
                if (isResultSynset(list.get(i))) {
                    res.addLast(list.get(i).getSynsetStr().concat("-").concat(list.get(i).getGloss().split(";")[0]));
                    count++;
                }
                else res.addLast(list.get(i).getSynsetStr().concat("-NONE"));
            }
            if (count == 0) continue;
            while (res.getLast().contains("NONE")) res.removeLast(); // remove tails
            if (unique.add(res.toString())) troponyms.add(res);
        }
        // print the troponym size
        System.out.println(WordNetUtils.synset2String(iSynset, false) + "\t" + troponyms.size());
        return troponyms;
    }

    private static List<ISynset> convert2Synsets (List<String> resultSynsetsString) {
        LinkedHashSet<ISynset> synsets = new LinkedHashSet<>();
        resultSynsetsString.forEach(synsetStr -> {
            String str = synsetStr.substring(1, synsetStr.length() - 1).split(", ")[0];
            List<ISynset> synset = WordNetUtils.getIndexWord(str, POS.VERB).getWordIDs().stream()
                    .map(IWordID::getSynsetID)
                    .map(WordNetUtils::getSynsetBySynsetId)
                    .filter(iSynset -> iSynset.getRelatedSynsets(Pointer.HYPERNYM).isEmpty() &&
                            WordNetUtils.synset2String(iSynset, false).equals(synsetStr))
                    .collect(Collectors.toList());
            synsets.addAll(synset);
        });
        return new ArrayList<>(synsets);
    }

    private static boolean isResultSynset (SynsetElement synsetElement) {
        List<String> gloss = Arrays.asList(synsetElement.getGloss().split(";")[0].split(" "));
        int idx = gloss.indexOf("by") + gloss.indexOf("with");
        return idx != -2;
    }
}
