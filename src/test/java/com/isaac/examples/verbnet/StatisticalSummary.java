package com.isaac.examples.verbnet;

import com.isaac.verbnet.VerbNet;
import edu.mit.jverbnet.data.IThematicRole;
import edu.mit.jverbnet.data.IVerbClass;
import edu.mit.jverbnet.data.ThematicRoleType;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticalSummary {
    public static void main (String[] args) {
        Integer allVerbCount = 0;
        Map<Integer, int[]> frameDist = new HashMap<>();
        Map<String, int[]> oneRoleDist = new HashMap<>();
        Map<String, int[]> conFrameDist = new HashMap<>();
        Iterator<IVerbClass> iterator = VerbNet.rootVerbClassIterator();
        while (iterator.hasNext()) {
            IVerbClass verbClass = iterator.next();
            Integer members = VerbNet.countMembersOfVerbClass(verbClass);
            allVerbCount += members; // count number of members in current verb class
            List<String> roles = verbClass.getThematicRoles().stream().map(IThematicRole::getType).map(ThematicRoleType::getID)
                    .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
            Integer roleNum = roles.size();
            String roleStr = String.join(", ", roles);
            // update frame distribution
            if (frameDist.containsKey(roleNum)) frameDist.get(roleNum)[0] += members;
            else frameDist.put(roleNum, new int[] { members });
            // update one role distribution
            if (roleNum == 1) {
                if (oneRoleDist.containsKey(roleStr)) oneRoleDist.get(roleStr)[0] += members;
                else oneRoleDist.put(roleStr, new int[] { members });
            }
            // update concrete frame distribution
            if (conFrameDist.containsKey(roleStr)) conFrameDist.get(roleStr)[0] += members;
            else conFrameDist.put(roleStr, new int[] { members });
            //System.out.println(verbClass.getID() + "\t" + members + "\t" + roleStr + "\t" + roleNum);
        }
        System.out.println();
        // print total number of verbs
        System.out.print("Total number of verbs in VerbNet: ");
        System.out.println(allVerbCount + "\n");

        // print frame distribution
        System.out.println("Frame Distribution:");
        frameDist.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value[0]));
        System.out.println();
        // print one role distribution
        System.out.println("One Role Distribution:");
        oneRoleDist.entrySet().stream().sorted((e1, e2) -> e2.getValue()[0] - e1.getValue()[0])
                .forEach(e -> System.out.println("Role: " + e.getKey() + ", Value: " + e.getValue()[0]));
        System.out.println();
        // print concrete frame distribution
        System.out.println("Concrete Frame Distribution:");
        conFrameDist.entrySet().stream().sorted((e1, e2) -> e2.getValue()[0] - e1.getValue()[0])
                .forEach(e -> System.out.println("Role: " + e.getKey() + ", Value: " + e.getValue()[0]));
        System.out.println();
    }
}
