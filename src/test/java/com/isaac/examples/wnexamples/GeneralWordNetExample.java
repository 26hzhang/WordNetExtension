package com.isaac.examples.wnexamples;

import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.*;

import java.util.Iterator;
import java.util.List;

public class GeneralWordNetExample {
    public static void main (String[] args) {
        // get Verb Synset Iterator
        Iterator<ISynset> synsetIterator = WordNet.getSynsetIterator(POS.VERB);
        // validate and parse Sense Key String
        String senseKeyString = "make_clean%2:35:00::";
        // if senseKeyString is valid, convert it to ISenseKey, otherwise null
        ISenseKey key = WordNet.senseKeyPattern(senseKeyString) ? WordNet.parseSenseKeyString(senseKeyString) : null;
        assert key != null;
        IWord iWord = WordNet.getWordBySenseKey(key); // obtain IWord
        System.out.println(iWord.getLemma() + "\t" + iWord.getSynset() + "\t" + iWord.getLexicalID() + "...");
        // validate and parse Word ID
        String wordIdString = "WID-01535377-V-02-make_clean";
        System.out.println("WordID Pattern Result: " + WordNet.wordIdPattern(wordIdString));
        IWordID wordId = WordNet.wordIdPattern(wordIdString) ? WordNet.parseIWordIDString(wordIdString) : null;
        iWord =  WordNet.getWordByWordId(wordId);
        // validate and parse Synset ID
        ISynsetID synsetId = WordNet.parseSynsetIDString("SID-02081903-V");
        System.out.println("SynsetID Pattern Result: " + WordNet.synsetIdPattern("SID-02081903-V"));
        // others
        String lemma = "take";
        List<IWordID> ids = WordNet.getIndexWord(lemma, POS.VERB).getWordIDs();
        int index = 0;
        for (IWordID id : ids) {
            index++;
            iWord = WordNet.getWordByWordId(id);
            System.out.println(iWord.getLemma() + "\t" + WordNet.getTagCount4IWord(iWord) + "\t" + WordNet.getSenseNumber4IWord(iWord));
        }
        System.out.println(index);
        System.out.println(WordNet.getNumberOfSense4Word(lemma, POS.VERB));
        // ......
    }
}
