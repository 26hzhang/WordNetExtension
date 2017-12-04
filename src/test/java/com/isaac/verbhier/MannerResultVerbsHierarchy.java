package com.isaac.verbhier;

import com.isaac.phrases.SynsetElement;
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
        List<String> resultSynsetsString = Files.readAllLines(Paths.get(WordNetUtils.GLOBALPATH
                .concat("data/104-result-verbs.txt")));
        // convert those synset string to ISynset (only if the those synsets are at the top level )
        List<ISynset> synsets = convert2SynsetsById(resultSynsetsString);
        /*synsets.forEach(synset -> { // testing
        	System.out.println(WordNetUtils.synset2String(synset, false) + "\t" + synset.getGloss().split(";")[0].trim());
        });
        System.out.println(synsets.size());*/
        List<LinkedList<String>> results = new ArrayList<>();
        synsets.forEach(iSynset -> results.addAll(getTroponyms(iSynset)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(WordNetUtils.GLOBALPATH.concat("data/verbs_hierarchies.txt")));
        for (List<String> list : results)
            writer.write(String.join("\t", list).concat("\n"));
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
            if (count == 0) continue; // if this line only contain NONE, remove this line
            if (res.getLast().contains("NONE")) continue; // if tails is NONE, remove this line
            //while (res.getLast().contains("NONE")) res.removeLast(); // remove tails, deprecated
            if (unique.add(res.toString())) troponyms.add(res);
        }
        // print the troponym size
        //System.out.println(WordNetUtils.synset2String(iSynset, false) + "\t" + troponyms.size());
        return troponyms;
    }

    private static List<ISynset> convert2SynsetsById (List<String> resultSynsetsString) {
        List<ISynset> synsets = new ArrayList<>();
        resultSynsetsString.forEach(str -> {
            String id = str.split("\t")[0].trim();
            ISynset synset = WordNetUtils.getSynsetBySynsetId(WordNetUtils.parseSynsetIDString(id));
            synsets.add(synset);
        });
        return synsets;
    }

    @SuppressWarnings("unused")
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
