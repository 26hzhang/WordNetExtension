package com.isaac.examples.wordnet.verbhierarchy;

import com.isaac.phrases.SynsetElement;
import com.isaac.wordnet.BaseExtraction;
import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.stanford.nlp.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MannerResultVerbsHierarchyUpdate {
    private static List<String> stopPhrases = stopPhrases();

    public static void main(String[] args) throws IOException {
        List<ISynset> synsets = new ArrayList<>();
        WordNet.getSynsetIterator(POS.VERB).forEachRemaining(synsets::add);
        // get top synsets of verbs
        synsets = synsets.stream().filter(synset -> synset.getRelatedSynsets(Pointer.HYPERNYM).size() == 0)
                .collect(Collectors.toList());
        System.out.println("Number of verb synsets at top level: " + synsets.size());
        // filter the synsets without children
        synsets = synsets.stream().filter(synset -> synset.getRelatedSynsets(Pointer.HYPONYM).size() != 0)
                .collect(Collectors.toList());
        System.out.println("Number of top level verb synsets contain children: " + synsets.size());
        List<Pair<LinkedList<String>, SynsetElement>> results = new ArrayList<>();
        synsets.forEach(iSynset -> results.addAll(getTroponyms(iSynset)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(WordNet.GLOBALPATH.concat("data/verbs_hierarchies.txt")));
        for (Pair<LinkedList<String>, SynsetElement> pair : results) {
            String str = String.join("\t", pair.first);
            ISynset iSynset = pair.second.getSynset();
            //int size = countChildren(iSynset);
            int size = WordNet.countNumberOfChildren(iSynset);
            writer.write(str.concat("\t").concat(String.valueOf(size)).concat("\n"));
        }
        writer.close();
    }

    private static List<Pair<LinkedList<String>, SynsetElement>> getTroponyms (ISynset iSynset) {
        List<Pair<LinkedList<String>, SynsetElement>> troponyms = new ArrayList<>();
        Set<String> unique = new HashSet<>();
        List<LinkedList<SynsetElement>> hyponyms = BaseExtraction.hyponymList(iSynset);
        for (LinkedList<SynsetElement> hyponym : hyponyms) {
            Collections.reverse(hyponym);
            LinkedList<String> res = new LinkedList<>();
            SynsetElement lastManner = null;
            for (int i = 0, length = hyponym.size(); i < length; i++) {
                if (i == 0) {
                    res.addLast(hyponym.get(i).getSynsetStr().concat("-NONE"));
                } else {
                    if (isMannerVerb(hyponym.get(i - 1), hyponym.get(i))) {
                        res.addLast(hyponym.get(i).getSynsetStr().concat("-").concat(hyponym.get(i).getGloss().split(";")[0]));
                        lastManner = hyponym.get(i);
                        break;
                    } else {
                        res.addLast(hyponym.get(i).getSynsetStr().concat("-NONE"));
                    }
                }
            }
            if (res.getLast().contains("NONE")) {
                continue; // if tails is NONE, remove this line
            }
            if (unique.add(res.toString())) {
                troponyms.add(Pair.makePair(res, lastManner));
            }
        }
        return troponyms;
    }

    private static boolean isMannerVerb(SynsetElement preSynsetElement, SynsetElement synsetElement) {
        String glossStr = synsetElement.getGloss().split(";")[0];
        for (String str : stopPhrases) {
            if (glossStr.contains(str)) return false;
        }
        List<String> gloss = Arrays.asList(synsetElement.getGloss().split(";")[0].split("\\s*(\\.|;|,|\\s)\\s*"));
        int by_idx = gloss.indexOf("by");
        int with_idx = gloss.indexOf("with");
        if (by_idx + with_idx == -2) return false;
        if (by_idx == gloss.size() - 1) return false;
        if (with_idx == gloss.size() - 1) return false;
        List<String> words = preSynsetElement.getSynset().getWords().stream().map(IWord::getLemma).collect(Collectors.toList());
        for (String word : words) {
            int word_idx = gloss.indexOf(word);
            if ((by_idx != -1 && word_idx != -1) && word_idx < by_idx) return true;
            if ((with_idx != -1 && word_idx != -1) && word_idx < with_idx) return true;
        }
        return false;
    }

    private static List<String> stopPhrases() {
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(Paths.get(WordNet.GLOBALPATH.concat("stopwords/stop_phrases.txt")), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
