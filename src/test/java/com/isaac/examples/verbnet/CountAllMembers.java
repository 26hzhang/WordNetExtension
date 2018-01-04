package com.isaac.examples.verbnet;

import com.isaac.verbnet.VerbNet;
import edu.mit.jverbnet.data.IVerbClass;

import java.util.Iterator;

public class CountAllMembers {
    public static void main (String[] args) {
        Iterator<IVerbClass> verbIter = VerbNet.allVerbClassIterator();
        int count = 0;
        while (verbIter.hasNext()) {
            IVerbClass verb = verbIter.next();
            count += verb.getMembers().size();
        }
        System.out.println(count);
    }
}
