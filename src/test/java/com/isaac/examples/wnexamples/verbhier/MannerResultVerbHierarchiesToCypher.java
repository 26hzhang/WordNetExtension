package com.isaac.examples.wnexamples.verbhier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.isaac.phrases.SynsetElement;
import com.isaac.wordnet.WordNet;
import com.isaac.wordnet.BaseExtraction;
import edu.mit.jwi.item.ISynset;
import javafx.util.Pair;

public class MannerResultVerbHierarchiesToCypher {

	public static void main(String[] args) throws IOException {
		// load verb synset strings to list
        List<String> resultSynsetsString = Files.readAllLines(Paths.get(WordNet.GLOBALPATH.concat("data/104-result-verbs.txt")));
        // convert those synset string to ISynset (only if the those synsets are at the top level )
        List<ISynset> synsets = convert2Synsets(resultSynsetsString);
        List<LinkedList<Pair<SynsetElement, String>>> hierarchies = new ArrayList<>();
        synsets.forEach(iSynset -> hierarchies.addAll(ResultMannerVerbHierarchies(iSynset)));
		// create cypher commands
        List<String> cypher = generateCypher(hierarchies);
		// write to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(WordNet.GLOBALPATH.concat("data/manner_result_verbs_cypher.txt")));
        for (String str : cypher) writer.write(str.concat("\n"));
        writer.close();
        System.out.println("Done...");
	}
	
	private static List<String> generateCypher (List<LinkedList<Pair<SynsetElement, String>>> hierarchies) {
		List<String> nodes = new ArrayList<>();
		List<String> relations = new ArrayList<>();
		LinkedHashSet<String> uniqueNodes = new LinkedHashSet<>();
		LinkedHashSet<String> uniqueRelations = new LinkedHashSet<>();
		int index = 0;
		for (LinkedList<Pair<SynsetElement, String>> list : hierarchies) {
			// Create nodes
			for (Pair<SynsetElement, String> pair : list) {
				SynsetElement key = pair.getKey();
				String value = pair.getValue();
				if (!uniqueNodes.add(key.getSynsetId())) continue; // node is already created
				index++;
				String nodeCypher = "";
				if (value.equals("Result")) {
					nodeCypher = "CREATE (Result".concat(String.valueOf(index)).concat(":Result{id:'").concat(key.getSynsetId()).concat("', name:'")
							.concat(key.getSynsetStr().substring(1, key.getSynsetStr().length() - 1)).concat("', gloss:'").concat(key.getGloss().split(";")[0])
							.concat("', type:'").concat(value).concat("'});");
				} else if (value.equals("Manner")) {
					nodeCypher = "CREATE (Manner".concat(String.valueOf(index)).concat(":Manner{id:'").concat(key.getSynsetId()).concat("', name:'")
							.concat(key.getSynsetStr().substring(1, key.getSynsetStr().length() - 1)).concat("', gloss:'").concat(key.getGloss().split(";")[0])
							.concat("', type:'").concat(value).concat("'});");
				}
				nodes.add(nodeCypher);
			}
			// Create relations
			LinkedList<Pair<SynsetElement, String>> resultVerbs = list.stream()
					.filter(pair -> pair.getValue().equals("Result")).collect(Collectors.toCollection(LinkedList::new));
			List<Pair<SynsetElement, String>> mannerVerbs = list.stream()
					.filter(pair -> pair.getValue().equals("Manner")).collect(Collectors.toList());
			// result verbs relations
			for (int i = 0; i < resultVerbs.size() - 1; i++) {
				if (!uniqueRelations.add(resultVerbs.get(i).getKey().getSynsetId().concat(";").concat(resultVerbs.get(i + 1).getKey().getSynsetId())))
					continue;// rel is added
				String nodeMatcher = "MATCH (a:Synset {id:'".concat(resultVerbs.get(i).getKey().getSynsetId()).concat("'}), (b:Synset {id:'")
						.concat(resultVerbs.get(i + 1).getKey().getSynsetId()).concat("'}) ");
				String hypeCypher = nodeMatcher.concat("WITH a,b CREATE (a)<-[:HYPE]-(b);");
				String hypoCypher = nodeMatcher.concat("WITH a,b CREATE (a)-[:TROP]->(b);");
				relations.add(hypeCypher);
				relations.add(hypoCypher);
			}
			// manner verbs relations
			for (int i = 0; i < mannerVerbs.size() - 1; i++) {
				if (!uniqueRelations.add(mannerVerbs.get(i).getKey().getSynsetId().concat(";").concat(mannerVerbs.get(i + 1).getKey().getSynsetId()))) continue; // rel is added
				String nodeMatcher = "MATCH (a:Manner {id:'".concat(mannerVerbs.get(i).getKey().getSynsetId()).concat("'}), (b:Manner {id:'")
						.concat(mannerVerbs.get(i + 1).getKey().getSynsetId()).concat("'}) ");
				String hypeCypher = nodeMatcher.concat("WITH a,b CREATE (a)<-[:HYPE]-(b);");
				String tropCypher = nodeMatcher.concat("WITH a,b CREATE (a)-[:TROP]->(b);");
				relations.add(hypeCypher);
				relations.add(tropCypher);
			}
			// manner verbs link to result verbs
			for (Pair<SynsetElement, String> manner : mannerVerbs) {
				for (Pair<SynsetElement, String> result : resultVerbs) {
					if (!uniqueRelations.add(result.getKey().getSynsetId().concat(";").concat(manner.getKey().getSynsetId()))) continue; // rel is added
					String nodeMatcher = "MATCH (a:Synset {id:'".concat(result.getKey().getSynsetId()).concat("'}), (b:Synset {id:'")
							.concat(manner.getKey().getSynsetId()).concat("'}) ");
					String m2rCypher = nodeMatcher.concat("WITH a,b CREATE (a)<-[:CAUSES]-(b);");
					//String r2mCypher = nodeMatcher.concat("WITH a,b CREATE (a)-[:CAUSEDBY]->(b);");
					relations.add(m2rCypher);
					//relations.add(r2mCypher);
				}
			}
		}
		nodes.addAll(relations);
		return nodes;
	}
    
    private static List<LinkedList<Pair<SynsetElement, String>>> ResultMannerVerbHierarchies (ISynset iSynset) {
    	List<LinkedList<Pair<SynsetElement, String>>> hierarchies = new ArrayList<>();
    	Set<String> unique = new HashSet<>();
    	BaseExtraction.hyponymList(iSynset).forEach(list -> {
    		LinkedList<String> temp = new LinkedList<>();
    		LinkedList<Pair<SynsetElement, String>> res = new LinkedList<>();
    		int count = 0;
    		for (int i = list.size() - 1; i >= 0; i--) {
                if (isManner(list.get(i))) { 
                	temp.addLast(list.get(i).getSynsetStr().concat("-").concat(list.get(i).getGloss().split(";")[0]));
                	if (i == list.size() -1)
                		res.addLast(new Pair<>(list.get(i), "Result"));
                	else res.addLast(new Pair<>(list.get(i), "Manner"));
                	count += 1;
                }
                else {
                	temp.addLast(list.get(i).getSynsetStr().concat("-NONE"));
                	res.addLast(new Pair<>(list.get(i), "Result"));
                }
            }
    		// if this line only contain NONE, remove this line, if tails is NONE, remove this line, if unique contains this line, remove
    		if (count != 0 && !temp.getLast().contains("NONE") && unique.add(temp.toString())) hierarchies.add(res);
    	});
    	return hierarchies;
    }
    
    // rules to determine whether a synset is result verbs or manner verbs (not sufficient, a lot of work needs to do)
    private static boolean isManner (SynsetElement synsetElement) {
        List<String> gloss = Arrays.asList(synsetElement.getGloss().split(";")[0].split(" "));
        int idx = gloss.indexOf("by") + gloss.indexOf("with");
        //if (gloss.indexOf("with") == gloss.size() - 1 || gloss.indexOf("by") == gloss.size() - 1) return false;
        return idx != -2;
    }
    
    // convert the top level synset string to ISynset
    private static List<ISynset> convert2Synsets (List<String> resultSynsetsString) {
		List<ISynset> synsets = new ArrayList<>();
		resultSynsetsString.forEach(str -> {
			String id = str.split("\t")[0].trim();
			ISynset synset = WordNet.getSynsetBySynsetId(WordNet.parseSynsetIDString(id));
			synsets.add(synset);
		});
		return synsets;
    }

}
