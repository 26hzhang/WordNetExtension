package com.isaac.examples.verbnet;

import com.isaac.verbnet.ThemanticRole;
import com.isaac.verbnet.VerbNet;
import edu.mit.jverbnet.data.IThematicRole;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.ThematicRoleType;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class AgentWithoutTheme {
    public static void main (String[] args) {
        Iterator<IVerbClass> iterator = VerbNet.rootVerbClassIterator();
        System.out.println(String.join("\t", "Classes", "Frames", "Members",
                String.join("\t", ThemanticRole.roles)));
        while (iterator.hasNext()) {
            IVerbClass verb = iterator.next();
            List<String> roles = verb.getThematicRoles().stream().map(IThematicRole::getType).map(ThematicRoleType::getID)
                    .collect(Collectors.toList());
            if (roles.contains("Agent") && !roles.contains("Theme")) {
                System.out.print(verb.getID() + "\t"); // print verb id
                System.out.print(String.join("+", verb.getThematicRoles().stream().map(IThematicRole::getType)
                        .map(ThematicRoleType::getID).collect(Collectors.toList())) + "\t"); // print frames
                System.out.print(VerbNet.countMembersOfVerbClass(verb) + "\t"); // print number of members
                List<Integer> roleIds = verb.getThematicRoles().stream().map(IThematicRole::getType).map(ThematicRoleType::getID)
                        .map(ThemanticRole::idOf).collect(Collectors.toList());
                int[] values = new int[30];
                for (int id : roleIds) values[id] = 1;
                for (int i = 0; i < values.length; i++) {
                    System.out.print(values[i]);
                    if (i < values.length - 1) System.out.print("\t");
                }
                System.out.println(); // a new line
            }
        }
    }
}
