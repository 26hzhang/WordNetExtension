package com.isaac.examples;


import com.isaac.wordnet.BaseExtraction;
import com.isaac.representation.SynsetElement;
import edu.mit.jwi.item.POS;

import java.util.*;

public class WordExample {
    public static void main (String[] args) {
        String word = "entity";
        List<LinkedList<SynsetElement>> hypo = BaseExtraction.hyponymList(word, POS.NOUN);
        Map<Integer, Set<String>> level = new HashMap<>();

        int depth = 4;
        hypo.stream().filter(l -> l.size() > depth).map(l -> l.subList(l.size() - depth, l.size())).forEach(l -> {
            Collections.reverse(l);
            for (int i = 0; i < l.size(); i++) {
                if (level.containsKey(i)) level.get(i).add(l.get(i).getSynsetStr());
                else level.put(i, new HashSet<>(Collections.singletonList(l.get(i).getSynsetStr())));
            }
        });
        level.forEach((key, set) -> {
            System.out.println(key + ": ");
            System.out.println("Number of Synsets: " + set.size());
            String[] arrays = Arrays.toString(set.toArray())
                    .replaceAll("\\{", "")
                    .replaceAll("}", "")
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .split(", ");
            Set<String> newSet = new HashSet<>(Arrays.asList(arrays));
            System.out.println("Number of Words: " + newSet.size());
            set.forEach(System.out::println);
        });
    }

}
