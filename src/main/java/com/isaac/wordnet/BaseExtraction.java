package com.isaac.wordnet;

import com.isaac.representation.SynsetElement;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.item.POS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.isaac.wordnet.WordNetUtils.*;

public class BaseExtraction {

    /**
     * Extract the full hierarchy path of a given word string
     * @param wordString given word string
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path
     */
    public static List<LinkedList<SynsetElement>> hierarchyList (String wordString, POS tag) {
        List<LinkedList<SynsetElement>> hypernyms = hypernymList(wordString, tag);
        List<LinkedList<SynsetElement>> hyponyms = hyponymList(wordString, tag);
        return mergeHypernymsAndHyponyms(hypernyms, hyponyms);
    }

    /**
     * Extract the full hierarchy path of a given word string and synset id
     * @param wordString given word string
     * @param synsetId given synset id string
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path
     */
    public static List<LinkedList<SynsetElement>> hierarchyList (String wordString, String synsetId, POS tag) {
        List<LinkedList<SynsetElement>> hypernyms = hypernymList(wordString, synsetId, tag);
        List<LinkedList<SynsetElement>> hyponyms = hyponymList(wordString, synsetId, tag);
        return mergeHypernymsAndHyponyms(hypernyms, hyponyms);
    }

    /**
     * Extract the full hierarchy path of a given synset
     * @param synset given synset
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path
     */
    public static List<LinkedList<SynsetElement>> hierarchyList (ISynset synset) {
        List<LinkedList<SynsetElement>> hypernyms = hypernymList(synset);
        List<LinkedList<SynsetElement>> hyponyms = hyponymList(synset);
        return mergeHypernymsAndHyponyms(hypernyms, hyponyms);
    }

    /**
     * Extract hypernym hierarchy of a given word string, in this case, using the word and its POS
     * to find all the possible synsets that the word may appear, then consider and extract all the
     * potential paths.
     * @param wordString given word string
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hypernyms
     */
    public static List<LinkedList<SynsetElement>> hypernymList (String wordString, POS tag) {
        List<LinkedList<SynsetElement>> hypernyms = new ArrayList<>();
        IIndexWord idxWord = getIndexWord(wordString, tag);
        if (idxWord == null) return hypernyms;
        List<IWordID> wordIDs = idxWord.getWordIDs(); // obtain word ids of the given word string
        for (IWordID wordId : wordIDs) {
            ISynset synset = getWordByWordId(wordId).getSynset();
            hypernyms.addAll(hypernymList(synset));
        }
        return hypernyms;
    }

    /**
     * Extract hypernym hierarchy of a given word string and given a synset id, in this case,
     * find the {@link ISynset} according to the word and synset id first, then extract the hypernym paths
     * @param wordString given word string
     * @param synsetId given synset id string
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hypernyms
     */
    public static List<LinkedList<SynsetElement>> hypernymList (String wordString, String synsetId, POS tag) {
        List<LinkedList<SynsetElement>> hypernyms = new ArrayList<>();
        IIndexWord idxWord = getIndexWord(wordString, tag);
        if (idxWord == null) return hypernyms;
        ISynset synset = getSynsetBySynsetId(parseSynsetIDString(synsetId));
        if (synset == null) return hypernyms;
        return hypernymList(synset);
    }

    /**
     * Extract hypernym hierarchy of a given synset
     * @param synset given synset
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hypernyms
     */
    public static List<LinkedList<SynsetElement>> hypernymList (ISynset synset) {
        List<LinkedList<SynsetElement>> lists = new ArrayList<>();
        LinkedList<SynsetElement> list = new LinkedList<>();
        list.addLast(new SynsetElement(synset));
        hypeRecursion(lists, synset, list);
        return lists;
    }

    /**
     * Extract hypernym hierarchy of a given word string, in this case, using the word and its POS
     * to find all the possible synsets that the word may appear, then consider and extract all the
     * potential paths.
     * @param wordString given word string
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hypernyms
     */
    public static List<LinkedList<SynsetElement>> hyponymList (String wordString, POS tag) {
        List<LinkedList<SynsetElement>> hyponyms = new ArrayList<>();
        IIndexWord idxWord = getIndexWord(wordString, tag);
        if (idxWord == null) return hyponyms;
        List<IWordID> wordIDs = idxWord.getWordIDs(); // obtain word ids of the given word string
        for (IWordID wordId : wordIDs) {
            ISynset synset = getWordByWordId(wordId).getSynset();
            hyponyms.addAll(hyponymList(synset));
        }
        return hyponyms;
    }

    /**
     * Extract hyponym hierarchy of a given word string and given a synset id, in this case,
     * find the {@link ISynset} according to the word and synset id first, then extract the hyponym paths
     * @param wordString given word string
     * @param synsetId given synset id string
     * @param tag part of speech tag
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hyponyms
     */
    public static List<LinkedList<SynsetElement>> hyponymList (String wordString, String synsetId, POS tag) {
        List<LinkedList<SynsetElement>> hyponyms = new ArrayList<>();
        IIndexWord idxWord = getIndexWord(wordString, tag);
        if (idxWord == null) return hyponyms;
        ISynset synset = getSynsetBySynsetId(parseSynsetIDString(synsetId));
        if (synset == null) return hyponyms;
        return hyponymList(synset);
    }

    /**
     * Extract hyponym hierarchy of a given synset
     * @param synset given synset
     * @return {@link List} of {@link LinkedList} of {@link SynsetElement}, each {@link LinkedList} is a path of hyponyms
     */
    public static List<LinkedList<SynsetElement>> hyponymList (ISynset synset) {
        List<LinkedList<SynsetElement>> lists = new ArrayList<>();
        LinkedList<SynsetElement> list = new LinkedList<>();
        list.addFirst(new SynsetElement(synset));
        hypoRecursion(lists, synset, list);
        return lists;
    }

    /** extract hypernyms by tail recursion */
    private static void hypeRecursion (List<LinkedList<SynsetElement>> lists, ISynset synset, LinkedList<SynsetElement> list) {
        if (synset.getRelatedSynsets(Pointer.HYPERNYM).isEmpty()) {
            lists.add(new LinkedList<>(list));
            return;
        }
        List<ISynsetID> ids = synset.getRelatedSynsets(Pointer.HYPERNYM);
        for (ISynsetID id : ids) {
            ISynset syn = getSynsetBySynsetId(id);
            list.addLast(new SynsetElement(syn));
            hypeRecursion(lists, syn, list);
            list.removeLast();
        }
    }

    /** extract hyponyms by tail recursion */
    private static void hypoRecursion (List<LinkedList<SynsetElement>> lists, ISynset synset, LinkedList<SynsetElement> list) {
        if (synset.getRelatedSynsets(Pointer.HYPONYM).isEmpty()) {
            lists.add(new LinkedList<>(list));
            return;
        }
        List<ISynsetID> ids = synset.getRelatedSynsets(Pointer.HYPONYM);
        for (ISynsetID id : ids) {
            ISynset syn = getSynsetBySynsetId(id);
            list.addFirst(new SynsetElement(syn));
            hypoRecursion(lists, syn, list);
            list.removeFirst();
        }
    }

    /** @return merged hierarchy path */
    private static List<LinkedList<SynsetElement>> mergeHypernymsAndHyponyms (List<LinkedList<SynsetElement>> hypernyms,
                                                                              List<LinkedList<SynsetElement>> hyponyms) {
        List<LinkedList<SynsetElement>> hierarchy = new ArrayList<>();
        hypernyms.forEach(hype -> hyponyms.forEach(hypo -> {
            LinkedList<SynsetElement> tmp = new LinkedList<>(hypo);
            if (tmp.getLast().getSynsetId().equals(hype.getFirst().getSynsetId())) {
                tmp.addAll(hype.subList(1, hype.size()));
                hierarchy.add(tmp);
            }
        }));
        return hierarchy;
    }
}
