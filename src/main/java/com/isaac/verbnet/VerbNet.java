package com.isaac.verbnet;

import edu.mit.jverbnet.data.IMember;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.IWordnetKey;
import edu.mit.jverbnet.index.IVerbIndex;
import edu.mit.jverbnet.index.VerbIndex;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
public class VerbNet {

    /** Initialize IVerbIndex */
    private static final IVerbIndex vndict;
    static {
        URL url = null;
        try { url = new URL("file", null, "src/main/resources/dict/vn-dict3.2b"); }
        catch (IOException e) { e.printStackTrace(); }
        assert url != null;
        vndict = new VerbIndex(url);
        try { vndict.open(); }
        catch (IOException e) {e.printStackTrace(); }
    }

    /** @return {@link Iterator} of all the root verb classes {@link IVerbClass} */
    public static Iterator<IVerbClass> rootVerbClassIterator() { return vndict.iteratorRoots(); }

    /** @return {@link Iterator} of all the verb classes {@link IVerbClass} */
    public static Iterator<IVerbClass> allVerbClassIterator() { return vndict.iterator(); }

    /** @return {@link List} of all root verb classes {@link IVerbClass} */
    public static List<IVerbClass> getRootVerbClasses() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(rootVerbClassIterator(), Spliterator.CONCURRENT),
                false).collect(Collectors.toList());
    }

    /** @return {@link List} of all root verb classes as string */
    public static List<String> getRootVerbClassesAsString() {
        return getRootVerbClasses().stream().map(IVerbClass::getID).collect(Collectors.toList());
    }

    /** @return {@link List} of all verb classes {@link IVerbClass} */
    public static List<IVerbClass> getAllVerbClasses() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(allVerbClassIterator(), Spliterator.CONCURRENT),
                false).collect(Collectors.toList());
    }

    /** @return {@link List} of all verb classes as string */
    public static List<String> getAllVerbClassesAsString () {
        return getAllVerbClasses().stream().map(IVerbClass::getID).collect(Collectors.toList());
    }

    /** @return count of root verb classes in verbnet */
    public static int countRootVerbClasses () { return getRootVerbClasses().size(); }

    /** @return count of all verb classes in verbnet */
    public static int countAllVerbClasses () { return getAllVerbClasses().size(); }

    /** @return count of all subclasses in verbnet */
    public static int countSubVerbClasses () { return countAllVerbClasses() - countRootVerbClasses(); }

    /** @return {@link IVerbClass} for a given string (the string here should be the ID of {@link IVerbClass}) */
    public static IVerbClass getRootVerb (String name) { return vndict.getRootVerb(name); }

    /** @return the number of members and members in sub-classes for a given string */
    public static int countMembersOfVerbClass (String name) { return countMembersOfVerbClass(getRootVerb(name)); }

    /** @return the number of members and members in sub-classes for a given {@link IVerbClass} */
    public static int countMembersOfVerbClass (IVerbClass verb) {
        int[] count = new int[] { verb.getMembers().size() };
        memberCountRecursion(count, verb);
        return count[0];
    }

    /** recursion to count the number of members and members in sub-classes for a given {@link IVerbClass} */
    private static void memberCountRecursion (int[] count, IVerbClass verb) {
        if (verb.getSubclasses().isEmpty()) return;
        List<IVerbClass> subClasses = verb.getSubclasses();
        for (IVerbClass subClass : subClasses) {
            count[0] += subClass.getMembers().size();
            memberCountRecursion(count, subClass);
        }
    }

    /** @return {@link List} of WordNet Keys String for a given {@link IMember} in VerbBet */
    public static List<String> iMemberToWordNetKeys (IMember iMember) {
        return iMember.getWordnetTypes().keySet().stream().map(IWordnetKey::toString).collect(Collectors.toList());
    }

}
