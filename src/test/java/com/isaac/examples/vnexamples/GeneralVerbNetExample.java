package com.isaac.examples.vnexamples;

import com.isaac.verbnet.VerbNet;
import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;

import java.util.Iterator;
import java.util.List;

public class GeneralVerbNetExample {
    public static void main (String[] args) {
        System.out.println("All Classes: " + VerbNet.countAllVerbClasses());
        System.out.println("Root Classes: " + VerbNet.countRootVerbClasses());
        System.out.println("Sub Classes: " + VerbNet.countSubVerbClasses());
        Iterator<IVerbClass> iterator = VerbNet.rootVerbClassIterator(); // get IVerbClass iterator
        List<IVerbClass> rootVerbs = VerbNet.getRootVerbClasses();
        IVerbClass iVerbClass = VerbNet.getRootVerb("hit-18.1");
        List<IMember> members = iVerbClass.getMembers();
        List<String> wordNetKeys = VerbNet.iMemberToWordNetKeys(members.get(0));
        // ...
    }
}
