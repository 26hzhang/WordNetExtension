package com.isaac.examples.wordnet;

import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.POS;

public class VerbSynsetsCount {
    public static void main (String[] args) {
        System.out.println(WordNet.countSynsetsByPOSTag(POS.VERB));
    }
}
