package com.isaac.verbnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public enum ThemanticRole {
    Agent("Agent", 0), Asset("Asset", 1), Attribute("Attribute", 2), Beneficiary("Beneficiary", 3), Cause("Cause", 4),
    CoAgent("Co-Agent", 5), CoPatient("Co-Patient", 6), CoTheme("Co-Theme", 7), Destination("Destination", 8),
    Experiencer("Experiencer", 9), Extent("Extent", 10), Goal("Goal", 11), Initial_Location("Initial_Location", 12),
    Instrument("Instrument", 13), Location("Location", 14), Material("Material", 15), Patient("Patient", 16),
    Pivot("Pivot", 17), Predicate("Predicate", 18), Product("Product", 19), Recipient("Recipient", 20),
    Reflexive("Reflexive", 21), Result("Result", 22), Source("Source", 23), Stimulus("Stimulus", 24), Theme("Theme", 25),
    Time("Time", 26), Topic("Topic", 27), Trajectory("Trajectory", 28), Value("Value", 29);

    /** Initialize list and maps */
    public static List<String> roles = new ArrayList<>(); // list of themantic roles values
    private static Map<ThemanticRole, Integer> roleMap = new HashMap<>(); // Themantic Role -- ID pairs
    private static Map<Integer, ThemanticRole> revRoleMap = new HashMap<>(); // reverse Themantic Role -- ID pairs
    private static Map<Integer, String> valueMap = new HashMap<>(); // ID -- Role Value pairs
    private static Map<String, Integer> revValueMap = new HashMap<>(); // reverse ID -- Role Value pairs
    static { // initialize
        for (ThemanticRole role : ThemanticRole.values()) {
            roleMap.put(role, role.index);
            revRoleMap.put(role.index, role);
            valueMap.put(role.index, role.value);
            revValueMap.put(role.value, role.index);
            roles.add(role.value);
        }
    }

    private String value;
    private int index;

    /** Constructor */
    ThemanticRole(final String value, final int index) {
        this.value = value;
        this.index = index;
    }

    /** @return {@link ThemanticRole} of given id */
    public static ThemanticRole valueOf (int index) { return revRoleMap.get(index); }

    /** @return the string value of Themantic Role of given id */
    public static String StringOf (int index) { return valueMap.get(index); }

    /** @return the id of given Themantic Role */
    public static Integer idOf (ThemanticRole role) { return roleMap.get(role); }

    /** @return the id of given Themantic Role String */
    public static Integer idOf (String value) { return revValueMap.get(value); }

}
