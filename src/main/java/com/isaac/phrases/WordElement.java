package com.isaac.phrases;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;

import java.util.*;
import java.util.stream.Collectors;

import static com.isaac.wordnet.WordNetUtils.wndict;

public class WordElement {
	/** word lemma */
	private String wordStr;
	/** polysemy count (sense number) of the word as a noun */
	private int nounPolysemy;
	/** polysemy count (sense number) of the word as a verb */
	private int verbPolysemy;
	/** polysemy count (sense number) of the word as an adjective */
	private int adjectivePolysemy;
	/** polysemy count (sense number) of the word as an adverb */
	private int adverbPolysemy;
	/** Part of Speech of the word: any one or several of {NOUN, VERB, ADJECTIVE, ADVERB} */
	private Set<String> POSs = new LinkedHashSet<>();
	/** Synset-Word pair, each word may appears in several synset ({@link ISynset}) with different meaning ({@link IWord}) */
	private Map<ISynset, IWord> map = new HashMap<>();

	/** Constructor, initialize the fields */
	public WordElement(IWord word) {
		this.wordStr = word.getLemma();
		this.map.put(word.getSynset(), word);
		this.POSs.add(word.getPOS().toString());
		// Noun
		IIndexWord idxWord = wndict.getIndexWord(word.getLemma(), POS.NOUN);
		this.nounPolysemy = idxWord == null ? 0 : idxWord.getWordIDs().size();
		// Verb
		idxWord = wndict.getIndexWord(word.getLemma(), POS.VERB);
		this.verbPolysemy = idxWord == null ? 0 : idxWord.getWordIDs().size();
		// Adjective
		idxWord = wndict.getIndexWord(word.getLemma(), POS.ADJECTIVE);
		this.adjectivePolysemy = idxWord == null ? 0 : idxWord.getWordIDs().size();
		// Adverb
		idxWord = wndict.getIndexWord(word.getLemma(), POS.ADVERB);
		this.adverbPolysemy = idxWord == null ? 0 : idxWord.getWordIDs().size();
	}

	/** add part of speech */
	public void addPOS(String pos) {
		POSs.add(pos);
	}

	/** @return the part of speech of such word in {@link String} format */
	public String POS2String() { return String.join(", ", POSs); }

	/** add new synset-word pair */
	public void addMap(IWord word) {
		map.put(word.getSynset(), word);
	}

	public String synsetsIDString() {
		List<String> list = map.keySet().stream().map(e -> e.getID().toString()).collect(Collectors.toList());
		return String.join("\t", list);
	}

	public String synsetsString() {
		return String.join("\t", map.keySet().stream().map(synset -> WordNetUtils.synset2String(synset, false))
				.collect(Collectors.toList()));
	}

	/** Getters and Setters */
	public String getWordStr() {
		return wordStr;
	}

	public int getNounPolysemy() {
		return nounPolysemy;
	}

	public int getVerbPolysemy() {
		return verbPolysemy;
	}

	public int getAdjectivePolysemy() {
		return adjectivePolysemy;
	}

	public int getAdverbPolysemy() {
		return adverbPolysemy;
	}

	public Set<String> getPOSs() { return POSs; }

	public Map<ISynset, IWord> getMap() {
		return map;
	}
	public void setMap(Map<ISynset, IWord> map) {
		this.map = map;
	}

}
