package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TopNWordsExample {

	public static void main(String[] args) throws IOException {
		String filePath = "data/top_n_words.txt";
		String pos = "v"; // POS.VERB: v, POS.NOUN: n, POS.ADJECTIVE & POS.ADVERB: a
		long maxLength = 1000;
		List<String> topNWords = Files.readAllLines(Paths.get(WordNetUtils.GLOBALPATH.concat("data/upd_corewn_with_sense.txt")))
				.stream()
				.filter(line -> !line.isEmpty() && line.split("\t")[0].equals(pos)) // remove empty lines and keep line with target pos tag only
				.sorted((l1, l2) -> calcCount(l2).compareTo(calcCount(l1))) // sort via counting result
				.limit(maxLength) // set max size of list
				.map(TopNWordsExample::mapToTarget) // map to target format
				.collect(Collectors.toList());
		/* write to file */
		BufferedWriter writer = new BufferedWriter(new FileWriter(WordNetUtils.GLOBALPATH.concat(filePath)));
		writer.write(String.join("\t", "POS", "Word", "Sense_Number", "Total_Sense", "Count", "Synset", "Gloss")
				.concat("\n"));
		for (String s : topNWords)
			writer.write(s);
		writer.close();
		System.out.println("Done...");
	}

	private static Double calcCount (String line) {
		double count = 0.0;
		String[] arr = line.split("\t");
		if (Integer.parseInt(arr[7]) != 0)
			count = Double.parseDouble(arr[5]) * Double.parseDouble(arr[6]) / Double.parseDouble(arr[7]);
		return count;
	}
	
	private static String mapToTarget (String line) {
		String[] arr = line.split("\t");
		Double count = calcCount(line);
		// 0: POS, 1: Word, 3: Sense Number, 4: Total Sense, count: normalize counting, 8: Synset, 9: Gloss
		return String.join("\t", arr[0], arr[1], arr[3], arr[4],
				String.format("%.2f", count), arr[8].substring(arr[8].indexOf("{") + 1, arr[8].indexOf("}")), arr[9])
				.concat("\n");
	}

}
