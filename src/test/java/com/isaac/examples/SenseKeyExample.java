package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.*;

import java.util.List;

public class SenseKeyExample {

	public static void main(String[] args) throws Exception {
		ISenseKey key = WordNetUtils.parseSenseKeyString("make_clean%2:35:00::");
		System.out.println("SynsetID Pattern Result: " + WordNetUtils.senseKeyPattern("make_clean%2:35:00::"));

		IWord word = WordNetUtils.getWordBySenseKey(key);
		System.out.println(word.getLemma());
		System.out.println(word.getLexicalID());
		System.out.println(word.getID());
		String wordId = "WID-01535377-V-02-make_clean";
		System.out.println("WordID Pattern Result: " + WordNetUtils.wordIdPattern(wordId));

		ISynsetID synsetId = WordNetUtils.parseSynsetIDString("SID-02081903-V");
		System.out.println("SynsetID Pattern Result: " + WordNetUtils.synsetIdPattern("SID-02081903-V"));

		ISynset synset = WordNetUtils.getSynsetBySynsetId(synsetId);
		System.out.println(synset.getOffset());
		System.out.println(synset.getType());

		String lemma = "take";
		List<IWordID> ids = WordNetUtils.getIndexWord(lemma, POS.VERB).getWordIDs();
		int index = 0;
		for (IWordID id : ids) {
			index++;
			IWord iWord = WordNetUtils.getWordByWordId(id);
			System.out.println(iWord.getLemma() + "\t" + iWord.getSenseKey() + "\t" + WordNetUtils.getTagCount4IWord(iWord) + "\t"
					+ WordNetUtils.getSenseNumber4IWord(iWord));
		}
		System.out.println(index);
		System.out.println(WordNetUtils.getNumberOfSense4Word(lemma, POS.VERB));
	}

}
