package com.isaac.examples;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.POS;

import java.util.Iterator;

public class VerbSynsetsCount {
    public static void main (String[] args) {
        System.out.println(WordNetUtils.totalSynsetsInWordNet(POS.VERB));
    }
}
