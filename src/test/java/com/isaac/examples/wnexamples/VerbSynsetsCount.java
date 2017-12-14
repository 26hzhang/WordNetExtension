package com.isaac.examples.wnexamples;

import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.POS;

public class VerbSynsetsCount {
    public static void main (String[] args) {
        System.out.println(WordNet.countAllSynsets(POS.VERB));
    }
}
