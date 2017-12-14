package com.isaac.wordnet;

import com.isaac.phrases.SynsetElement;
import edu.mit.jwi.item.POS;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class WordNetExtraction {

    /**
     * Extract hierarchy path of given list of word and synset id pairs within the specific POS tag, for each word,
     * the path should be:
     * 		{{@link SynsetElement} Hyponyms} --> word --> {{@link SynsetElement} Hypernyms}
     * @param wordSynIdPairs given list of word and synset id pairs
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path
     */
    public static List<LinkedList<SynsetElement>> hierarchyPaths4Pairs (List<Pair<String, String>> wordSynIdPairs, POS tag) {
        List<LinkedList<SynsetElement>> hierarchies = new ArrayList<>();
        wordSynIdPairs.forEach(pair -> {
            if (pair.getValue().isEmpty() || !WordNet.synsetIdPattern(pair.getValue()))
                hierarchies.addAll(BaseExtraction.hierarchyList(pair.getKey(), tag));
            else hierarchies.addAll(BaseExtraction.hierarchyList(pair.getKey(), pair.getValue(), tag));
        });
        return hierarchies;
    }

    /**
     * Extract hierarchy path of given list of words within the specific POS tag, for each word, the path should be:
     * 		{{@link SynsetElement} Hyponyms} --> word --> {{@link SynsetElement} Hypernyms}
     * @param words given list of words
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path
     */
    public static List<LinkedList<SynsetElement>> hierarchyPaths4Words (List<String> words, POS tag) {
        List<LinkedList<SynsetElement>> hierarchies = new ArrayList<>();
        words.forEach(word -> hierarchies.addAll(BaseExtraction.hierarchyList(word, tag)));
        return hierarchies;
    }

    /**
     * Extract hypernym hierarchy path of given list of word and synset id pairs within the specific POS tag, for each word,
     * the path should be:
     * 		word --> {{@link SynsetElement} Hypernyms}
     * @param wordSynIdPairs given list of word and synset id pairs
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hypernyms
     */
    public static List<LinkedList<SynsetElement>> hypernymPaths4Pairs (List<Pair<String, String>> wordSynIdPairs, POS tag) {
        List<LinkedList<SynsetElement>> hypernyms = new ArrayList<>();
        wordSynIdPairs.forEach(pair -> {
            if (pair.getValue().isEmpty() || !WordNet.synsetIdPattern(pair.getValue()))
                hypernyms.addAll(BaseExtraction.hypernymList(pair.getKey(), tag));
            else hypernyms.addAll(BaseExtraction.hypernymList(pair.getKey(), pair.getValue(), tag));
        });
        return hypernyms;
    }

    /**
     * Extract hypernym hierarchy path of given list of words within the specific POS tag, for each word, the path should be:
     * 		word --> {{@link SynsetElement} Hypernyms}
     * @param words given list of words
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hypernyms
     */
    public static List<LinkedList<SynsetElement>> hypernymPaths4Words (List<String> words, POS tag) {
        List<LinkedList<SynsetElement>> hypernyms = new ArrayList<>();
        words.forEach(word -> hypernyms.addAll(BaseExtraction.hypernymList(word, tag)));
        return hypernyms;
    }

    /**
     * Extract hyponym hierarchy path of given list of word and synset id pairs within the specific POS tag, for each word,
     * the path should be:
     * 		{{@link SynsetElement} Hyponyms} --> word
     * @param wordSynIdPairs given list of word and synset id pairs
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hyponyms
     */
    public static List<LinkedList<SynsetElement>> hyponymPaths4Pairs (List<Pair<String, String>> wordSynIdPairs, POS tag) {
        List<LinkedList<SynsetElement>> hyponyms = new ArrayList<>();
        wordSynIdPairs.forEach(pair -> {
            if (pair.getValue().isEmpty() || !WordNet.synsetIdPattern(pair.getValue()))
                hyponyms.addAll(BaseExtraction.hyponymList(pair.getKey(), tag));
            else hyponyms.addAll(BaseExtraction.hyponymList(pair.getKey(), pair.getValue(), tag));
        });
        return hyponyms;
    }

    /**
     * Extract hyponym hierarchy path of given list of words within the specific POS tag, for each word, the path should be:
     * 		{{@link SynsetElement} Hyponyms} --> word
     * @param words given list of words
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hyponyms
     */
    public static List<LinkedList<SynsetElement>> hyponymPaths4Words (List<String> words, POS tag) {
        List<LinkedList<SynsetElement>> hyponyms = new ArrayList<>();
        words.forEach(word -> hyponyms.addAll(BaseExtraction.hyponymList(word, tag)));
        return hyponyms;
    }

    /** @return {@link List} of {@link LinkedList} of {@link SynsetElement}, filtered by polysemy count (sense number) */
    public static List<LinkedList<SynsetElement>> filterSynsetElementListsByPolysemy (List<LinkedList<SynsetElement>> lists,
                                                                                      String method, double threshold) {
        return lists.stream().map(l -> filterSynsetElementListByPolysemy(l, method, threshold)).collect(Collectors.toList());
    }

    /** @return {@link LinkedList} of {@link SynsetElement}, filtered by polysemy count (sense number) */
    private static LinkedList<SynsetElement> filterSynsetElementListByPolysemy (LinkedList<SynsetElement> lists,
                                                                               String method, double threshold) {
        return lists.stream().filter(l -> l.getPolysemy(method) >= threshold).collect(Collectors.toCollection(LinkedList::new));
    }
}
