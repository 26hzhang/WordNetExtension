package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class VerbHierarchy {
    public static void main (String[] args) {
        List<ISynset> synsets = new ArrayList<>();
        WordNetUtils.getSynsetIterator(POS.VERB).forEachRemaining(synsets::add);
        System.out.println(synsets.size());
        // get top synsets of verbs
        synsets = synsets.stream().filter(synset -> synset.getRelatedSynsets(Pointer.HYPERNYM).size() == 0).collect(Collectors.toList());
        System.out.println(synsets.size());
        synsets.forEach(synset -> System.out.println(WordNetUtils.synset2String(synset, false)));

        // get words in the top synsets
        Set<String> words = new HashSet<>();
        synsets.forEach(synset -> synset.getWords().forEach(word -> words.add(word.getLemma())));
        System.out.println(words.size());
        words.forEach(System.out::println);

        // get the word who only appear in the top level
        Set<String> unique = new HashSet<>();
        synsets.forEach(synset -> synset.getWords().forEach(word -> {
            if (WordNetUtils.getIndexWord(word.getLemma(), POS.VERB).getWordIDs().size() == 1)
                unique.add(word.getLemma());
        }));
        System.out.println(unique.size());
        unique.forEach(System.out::println);

    }
}
