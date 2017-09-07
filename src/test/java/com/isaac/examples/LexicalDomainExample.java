package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.POS;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LexicalDomainExample {

	public static void main(String[] args) {
		Map<String, LinkedHashSet<String>> map = WordNetUtils.splitWordNetWithLexicalDomain(POS.ADVERB);
		System.out.println(map.size());
		List<String> names = map.entrySet().stream().map(Map.Entry::getKey).sorted().collect(Collectors.toList());
		for (String s : names) {
			System.out.println(s);
		}
	}

}
