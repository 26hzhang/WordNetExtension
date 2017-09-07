package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import com.isaac.utils.IOUtils;
import edu.mit.jwi.item.POS;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetTopNWordsExample {

	public static void main(String[] args) throws IOException {
		String filename = "results/top1000verbs-sense.txt";
		List<WNWord> list = getTopNWordsFromCoreWN(POS.VERB, 1000, true, filename);
		System.out.println(list.size());
	}

	private static List<WNWord> getTopNWordsFromCoreWN(POS tag, int maxLength, boolean write2file, String filename) throws IOException {
		String tagStr = POSMap.get(tag);
		List<String> corewn = IOUtils.loadFile2List("data/upd_corewn_with_sense.txt");
		List<WNWord> words = new ArrayList<>();
		for (String line : corewn) {
			if (line.isEmpty()) continue;
			String[] arr = line.split("\t");
			if (!arr[0].equals(tagStr)) continue;
			int count = 0;
			if (Integer.parseInt(arr[7]) != 0)
				count = Integer.parseInt(arr[5]) * Integer.parseInt(arr[6]) / Integer.parseInt(arr[7]);
			WNWord word = new WNWord(arr[0], arr[1], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), count,
					arr[8].substring(arr[8].indexOf("{") + 1, arr[8].indexOf("}")), arr[9]);
			words.add(word);
		}
		if (words.size() > maxLength)
			words = words.stream().sorted((e1, e2) -> e2.getCount() - e1.getCount()).limit(maxLength).collect(Collectors.toList());
		else
			words = words.stream().sorted((e1, e2) -> e2.getCount() - e1.getCount()).collect(Collectors.toList());
		if (write2file) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(WordNetUtils.GLOBALPATH.concat(filename)));
			writer.write("POS\tWord\tSense Number\tTotal Sense\tCount\tSynset\tGloss\n");
			for (WNWord word : words) {
				writer.write(word.getPos() + "\t" + word.getWord() + "\t" + word.getSenseNum() + "\t" + word.getTotalSense()
						+ "\t" + word.getCount() + "\t" + word.getSynset() + "\t" + word.getGloss() + "\n");
			}
			writer.close();
		}
		System.out.println("Finished!!!");
		return words;
	}

	static class WNWord {
		/** String of POS tag */
		private String pos;
		/** word lemma */
		private String word;
		/** sense number */
		private int senseNum;
		/** total sense */
		private int totalSense;
		/** count */
		private int count;
		/** synset string */
		private String synset;
		/** gloss string */
		private String gloss;
		WNWord(String pos, String word, int senseNum, int totalSense, int count, String synset, String gloss) {
			this.pos = pos;
			this.word = word;
			this.senseNum = senseNum;
			this.totalSense = totalSense;
			this.count = count;
			this.synset = synset;
			this.gloss = gloss;
		}
		/** Getters */
		String getPos() {
			return pos;
		}
		String getWord() { return word; }
		int getSenseNum() {
			return senseNum;
		}
		int getTotalSense() {
			return totalSense;
		}
		int getCount() {
			return count;
		}
		String getSynset() {
			return synset;
		}
		String getGloss() {
			return gloss;
		}
	}

	/** POS tag pairs */
	private static final Map<POS, String> POSMap = new HashMap<POS, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(POS.VERB, "v");
			put(POS.NOUN, "n");
			put(POS.ADJECTIVE, "a");
			put(POS.ADVERB, "a");
		}
	};

}
