package com.isaac.examples;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.isaac.representation.SynsetElement;
import com.isaac.wordnet.BaseExtraction;
import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.POS;

public class TopNLevelSynsets {

	/**
	 * This code is used for extracting top n level synsets of nouns in WordNet
	 * */
	public static void main(String[] args) throws IOException {
		String word = "entity";
		int level = 4;
		List<LinkedList<SynsetElement>> hypo = BaseExtraction.hyponymList(word, POS.NOUN);
		
		List<String> hypoStrings = hypo.stream().map(l -> {
			Collections.reverse(l);
			if (l.size() < level) return WordNetUtils.hierarchyPath2String(l, false);
			else return WordNetUtils.hierarchyPath2String(new LinkedList<>(l.subList(0, level)), false);
		}).distinct().collect(Collectors.toList());
		
		// Write to file
		BufferedWriter writer = new BufferedWriter(new FileWriter("/home/zhanghao/Desktop/top_4_level_synsets.txt"));
		writer.write(String.join("\t", String.valueOf(1), String.valueOf(2), String.valueOf(3), String.valueOf(4)).concat("\n"));
		writer.write(String.join("\t", String.valueOf(uniqueSynNum(hypoStrings, 0)), String.valueOf(uniqueSynNum(hypoStrings, 1)), 
				String.valueOf(uniqueSynNum(hypoStrings, 2)), String.valueOf(uniqueSynNum(hypoStrings, 3))).concat("\n"));
		writer.write(String.join("\t", String.valueOf(uniqueWordNum(hypoStrings, 0)), String.valueOf(uniqueWordNum(hypoStrings, 1)), 
				String.valueOf(uniqueWordNum(hypoStrings, 2)), String.valueOf(uniqueWordNum(hypoStrings, 3))).concat("\n"));
		for (String s : hypoStrings)
			writer.write(s.replaceAll("-->", "\t").concat("\n"));
		writer.close();
		System.out.println("Done...");
	}
	
	private static int uniqueSynNum (List<String> hypoStrings, int level) {
		Set<String> count = new HashSet<>();
		for (String str : hypoStrings) {
			String[] arr = str.split("-->");
			if (arr.length - 1 < level) continue;
			count.add(arr[level]);
		}
		return count.size();
	}
	
	private static int uniqueWordNum (List<String> hypoStrings, int level) {
		Set<String> count = new HashSet<>();
		for (String str : hypoStrings) {
			String[] arr = str.split("-->");
			if (arr.length - 1 < level) continue;
			Matcher matcher = Pattern.compile("[\\w_-]+").matcher(arr[level]);
			while (matcher.find()) count.add(matcher.group());
		}
		return count.size();
	}

}
