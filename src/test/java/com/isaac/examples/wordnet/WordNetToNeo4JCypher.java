package com.isaac.examples.wordnet;

import com.isaac.phrases.SynsetElement;
import com.isaac.phrases.WordElement;
import com.isaac.wordnet.WordNet;
import com.isaac.wordnet.WordNetExtraction;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WordNetToNeo4JCypher {
    public static void main (String[] args) throws IOException {
        String infilename = "data/upd_corewn_with_sense.txt";
        String outfilename = "data/cypher-result.txt";
        POS tag = POS.NOUN;
        String tagStr = "n"; // POS.VERB: v, POS.NOUN: n, POS.ADJECTIVE & POS.ADVERB: a
        List<String> corewn = Files.readAllLines(Paths.get(WordNet.GLOBALPATH.concat(infilename)));
        List<Pair<String, String>> wordlst = new ArrayList<>();
        for (String line : corewn) {
            String[] arr = line.split("\t");
            if (!arr[0].equals(tagStr)) continue;
            wordlst.add(new Pair<>(arr[2].split("%")[0], arr[8].split("----")[0]));
        }
        List<LinkedList<SynsetElement>> list = WordNetExtraction.hierarchyPaths4Pairs(wordlst, tag);

        List<String> cypher = createCypher(list, tag);
        BufferedWriter writer = new BufferedWriter(new FileWriter(WordNet.GLOBALPATH.concat(outfilename)));
        for (String s : cypher) writer.write(s.concat("\n"));
        writer.close();
        System.out.println("Finished!!!");
    }

    private static List<String> createCypher(List<LinkedList<SynsetElement>> list, POS tag) {
        List<String> node = new ArrayList<>();
        List<String> relation = new ArrayList<>();
        Set<String> set = new LinkedHashSet<>();
        Set<String> relSet = new LinkedHashSet<>();
        // Add synset and create the relation between synsets
        int index = 1;
        for (LinkedList<SynsetElement> lst : list) {
            for (int i = 0; i < lst.size(); i++) {
                SynsetElement ele = lst.get(i);
                if (set.add(ele.getSynsetId())) {
                    String synsetStr = ele.getSynsetStr().replaceAll("\\{", "").replaceAll("}", "");
                    String gloss = ele.getGloss();
                    String replace = "\"";
                    String result = "";
                    for (int j = 0; j < gloss.length(); j++) {
                        if (replace.contains(String.valueOf(gloss.charAt(j))))
                            result = result.concat("\\" + gloss.charAt(j));
                        else
                            result = result.concat(String.valueOf(gloss.charAt(j)));
                    }
                    String s = "CREATE (Synset" + (index++) + ":Synset { id: \"" + ele.getSynsetId() + "\", name : \"" + synsetStr
                            + "\", MaxPolysemy : \"" + ele.getPolysemy("max") + "\", gloss : \"" + result + "\", POS : \""
                            + ele.getPOS() + "\", LexicalDomain : \"" + ele.getLexicalDomain() + "\" });";
                    node.add(s);
                }
                if (i < lst.size() - 1 && relSet.add(lst.get(i).getSynsetId() + ";" + lst.get(i + 1).getSynsetId())) {
                    String ss = "MATCH (a:Synset {id:\"" + lst.get(i).getSynsetId() + "\"}), (b:Synset {id:\""
                            + lst.get(i + 1).getSynsetId() + "\"}) WITH a,b CREATE (a)-[:HYPE]->(b);";
                    relation.add(ss);
                }
            }
        }
        // Add word and create the relation between word and synset
        Map<String, WordElement> map = extractWordElements(list);
        index = 1;
        for (Map.Entry<String, WordElement> entry : map.entrySet()) {
            WordElement ele = entry.getValue();
            String s = "CREATE (Word" + (index++) + ":Word { name: \"" + ele.getWordStr() + "\", POS : \"" + ele.POS2String() + "\", PolysemyN : \""
                    + ele.getNounPolysemy() + "\", PolysemyV : \"" + ele.getVerbPolysemy() + "\", PolysemyA : \"" + ele.getAdjectivePolysemy() + "\", PolysemyR : \""
                    + ele.getAdverbPolysemy() + "\" });";
            node.add(s);
            Map<ISynset, IWord> eleMap = ele.getMap();
            for (Map.Entry<ISynset, IWord> en : eleMap.entrySet()) {
                int senseNumber = WordNet.getSenseNumber4IWord(en.getValue());
                int tagCount = WordNet.getTagCount4IWord(en.getValue());
                String str = "MATCH (a:Word {name:\"" + ele.getWordStr() + "\", POS:\"" + tag.toString() + "\"}), (b:Synset {id:\""
                        + en.getKey().getID().toString() + "\"}) WITH a,b CREATE (a)-[:MEMBER_OF{SenseNumber:\"" + senseNumber + "\", TagCount:\"" + tagCount
                        + "\"}]->(b);";
                relation.add(str);
            }
        }
        node.addAll(relation);
        return node;
    }

    /** Extract word element, each pair contains a word lemma and a list of its different types in WordNet */
    private static Map<String, WordElement> extractWordElements (List<LinkedList<SynsetElement>> list) {
        Map<String, WordElement> map = new HashMap<>();
        list.forEach(lst -> lst.forEach(synsetElement -> synsetElement.getSynset().getWords().forEach( iword ->
                {
                    String word = iword.getLemma();
                    if (map.containsKey(word)) {
                        map.get(word).addPOS(iword.getPOS().toString());
                        map.get(word).addMap(iword);
                    } else map.put(word, new WordElement(iword));
                }
        )));
        return map;
    }
}
