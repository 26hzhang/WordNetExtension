package com.isaac.examples.wnexamples;

import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.*;

import java.util.List;

public class SenseKeyExample {

	public static void main(String[] args) {
		ISenseKey key = WordNet.parseSenseKeyString("make_clean%2:35:00::");
		System.out.println("SynsetID Pattern Result: " + WordNet.senseKeyPattern("make_clean%2:35:00::"));

		IWord word = WordNet.getWordBySenseKey(key);
		System.out.println(word.getLemma());
		System.out.println(word.getLexicalID());
		System.out.println(word.getID());
		String wordId = "WID-01535377-V-02-make_clean";
		System.out.println("WordID Pattern Result: " + WordNet.wordIdPattern(wordId));

		ISynsetID synsetId = WordNet.parseSynsetIDString("SID-02081903-V");
		System.out.println("SynsetID Pattern Result: " + WordNet.synsetIdPattern("SID-02081903-V"));

		ISynset synset = WordNet.getSynsetBySynsetId(synsetId);
		System.out.println(synset.getOffset());
		System.out.println(synset.getType());

		String lemma = "take";
		List<IWordID> ids = WordNet.getIndexWord(lemma, POS.VERB).getWordIDs();
		int index = 0;
		for (IWordID id : ids) {
			index++;
			IWord iWord = WordNet.getWordByWordId(id);
			System.out.println(iWord.getLemma() + "\t" + iWord.getSenseKey() + "\t" + WordNet.getTagCount4IWord(iWord) + "\t"
					+ WordNet.getSenseNumber4IWord(iWord));
		}
		System.out.println(index);
		System.out.println(WordNet.getNumberOfSense4Word(lemma, POS.VERB));
	}

}
