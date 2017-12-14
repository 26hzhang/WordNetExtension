package com.isaac.examples.vnexamples;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.isaac.verbnet.VerbNet;
import edu.mit.jverbnet.data.*;
import edu.mit.jverbnet.data.selection.SemRestrType;
import edu.mit.jverbnet.data.semantics.ArgType;
import edu.mit.jverbnet.data.semantics.IPredicateDesc;
import edu.mit.jverbnet.data.semantics.ISemanticArgType;
import edu.mit.jverbnet.data.syntax.ISyntaxArgDesc;
import edu.mit.jverbnet.data.syntax.ISyntaxDesc;

public class VerbNetInitExample {

	public static void main(String[] args) {
		IVerbClass iVerbClass = VerbNet.getRootVerb("hit-18.1");
		IMember iMember = iVerbClass.getMembers().get(0);
		Map<IWordnetKey, Boolean> keys = iMember.getWordnetTypes();
		IFrame iFrame = iVerbClass.getFrames().get(0);
		FrameType type = iFrame.getPrimaryType();
		String example = iFrame.getExamples().get(0);
		System.out.println("id: " + iVerbClass.getID());
		System.out.println("Member: " + iMember.getName());
		System.out.println("first wordnet keys: ");
		keys.forEach((key, value) -> System.out.println(key.toString() + "\t" + value));
		System.out.println("first frame type: " + type.getID());
		System.out.println("first example: " + example);
		/*=======================================================================================================================*/
		IVerbClass verb = VerbNet.getRootVerb("run-51.3.2").getSubclasses().get(1).getSubclasses().get(0);
		List<IMember> members = verb.getMembers();
		for (IMember member : members) {
			System.out.println(member.getName() + "\t\t");
			List<String> groupings = member.getGroupings();
			System.out.println(groupings.toString() + "\n");
			Map<IWordnetKey, Boolean> map = member.getWordnetTypes();
			for (Map.Entry<IWordnetKey, Boolean> entry : map.entrySet()) {
				System.out.println("{" + entry.getKey().getLemma() + ", " + entry.getKey().getLexicalFileNumber() + ", " +
						entry.getKey().getLexicalID() + ", " + entry.getKey().getSynsetType() + ", " + entry.getKey().toString() +
						", " + entry.getValue() + "\n");
			}
			System.out.println("\n");
		}
		System.out.println("\n----------------------FRAMES----------------------------------\n\n");
		List<IFrame> frames = verb.getFrames();
		for (IFrame frame : frames) {
			System.out.println(frame.getPrimaryType().getID() + "\n");
			System.out.println(frame.getSecondaryType().getID() + "\n");
			System.out.println(frame.getExamples().toString() + "\n");
			ISyntaxDesc syntax = frame.getSyntax();
			List<ISyntaxArgDesc> preList = syntax.getPreVerbDescriptors();
			for (ISyntaxArgDesc p : preList) {
				System.out.println("{" + p.getValue() + ", " + p.getType().getName() + ", " + p.getType().getID() + ", " + p.getType().getValueRule().name() + "}\t");
			}
			System.out.println("\n");
			List<ISyntaxArgDesc> postList = syntax.getPostVerbDescriptors();
			for (ISyntaxArgDesc p : postList) {
				//System.out.println(p.getNounPhraseType().getID());
				System.out.println("{" + p.getValue() + ", " + p.getType().getName() + ", " + p.getType().getID() + ", " + p.getType().getValueRule().name() + "}\t");
			}
			System.out.println("\n");
			//System.out.println(frame.getSyntax().getPostVerbDescriptors().toString() + "\n");
			//System.out.println(frame.getSyntax().getPreVerbDescriptors().toString() + "\n");
			//System.out.println(frame.getSemantics().toString() + "\n");
			List<IPredicateDesc> predicates = frame.getSemantics().getPredicates();
			for (IPredicateDesc pre : predicates) {
				System.out.println(pre.getValue().getID() + "\t");
				List<ISemanticArgType> lst = pre.getArgumentTypes();
				for (ISemanticArgType l : lst) {
					ArgType at = l.getArgType();
					System.out.println(at.name() + "\t");
				}
			}
			System.out.println("\n");
			System.out.println(frame.getSemantics().getPredicates().toString() + "\n");
			System.out.println(frame.getXTag() + "\n");
			System.out.println(frame.getDescriptionNumber() + "\n\n");
		}
		System.out.println("---------------------ROLES-----------------------------------\n\n");
		List<IThematicRole> roles = verb.getThematicRoles();
		for (IThematicRole role : roles) {
			System.out.println(role.getType().getID() + "\t");
			//List<ISelRestrictions<SemRestrType>> rest = role.getSelRestrictions().getSubSelRestrictions();
			//System.out.println(rest.size());
			//for (ISelRestrictions<SemRestrType> s : rest) {
			//	System.out.println(s.toString());
			//}
			//System.out.println(role.getSelRestrictions().getSubSelRestrictions().toString() + "\n");
			StringBuilder str = new StringBuilder("{");
			Map<SemRestrType, Boolean> map = role.getSelRestrictions().getTypeRestrictions();
			for (Map.Entry<SemRestrType, Boolean> entry : map.entrySet()) {
				str.append("<").append(entry.getKey().getID()).append(", ").append(entry.getValue()).append(">\t");
			}
			System.out.println(str.toString().trim() + "}\n");
		}
		System.out.println("Finished!!!");
	}

}
