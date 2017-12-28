package com.isaac.examples.verbnet;

import com.isaac.verbnet.VerbNet;
import edu.mit.jverbnet.data.IThematicRole;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.ThematicRoleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SubClassWithRoles {
    public static void main (String[] args) {
        Iterator<IVerbClass> iterator = VerbNet.rootVerbClassIterator();
        while (iterator.hasNext()) {
            IVerbClass verb = iterator.next();
            List<String> rootRoles = verb.getThematicRoles().stream().map(IThematicRole::getType).map(ThematicRoleType::getID)
                    .sorted().collect(Collectors.toList());
            List<String> subRoles = new ArrayList<>();
            recursion(subRoles, verb);
            subRoles = subRoles.stream().distinct().sorted().collect(Collectors.toList());
            if (subRoles.size() != 0 && !contains(rootRoles, subRoles))
                System.out.println(verb.getID());
        }
    }

    private static void recursion (List<String> list, IVerbClass verb) {
        if (verb.getSubclasses().isEmpty()) return;
        List<IVerbClass> subs = verb.getSubclasses();
        for (IVerbClass sub : subs) {
            List<String> roles = sub.getThematicRoles().stream().map(IThematicRole::getType).map(ThematicRoleType::getID)
                    .collect(Collectors.toList());
            list.addAll(roles);
            recursion(list, sub);
        }
    }

    private static <T> boolean contains (List<T> list, List<T> subList) {
        return Collections.indexOfSubList(list, subList) != -1;
    }
}
