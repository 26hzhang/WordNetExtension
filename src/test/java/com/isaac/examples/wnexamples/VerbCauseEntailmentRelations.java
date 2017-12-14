package com.isaac.examples.wnexamples;

import com.isaac.wordnet.WordNet;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VerbCauseEntailmentRelations {
    public static void main (String[] args) {
        Iterator<ISynset> verbIter = WordNet.getSynsetIterator(POS.VERB);
        Map<ISynset, List<ISynset>> causes = new HashMap<>();
        Map<ISynset, List<ISynset>> entailments = new HashMap<>();
        while (verbIter.hasNext()) {
            ISynset synset = verbIter.next();
            List<ISynsetID> causeIds = synset.getRelatedSynsets(Pointer.CAUSE);
            if (causeIds != null && !causeIds.isEmpty()) {
                List<ISynset> synsets = causeIds.stream().map(WordNet::getSynsetBySynsetId).collect(Collectors.toList());
                causes.put(synset, synsets);
            }
            List<ISynsetID> entailmentIds = synset.getRelatedSynsets(Pointer.ENTAILMENT);
            if (entailmentIds != null && !entailmentIds.isEmpty()) {
                List<ISynset> synsets = entailmentIds.stream().map(WordNet::getSynsetBySynsetId).collect(Collectors.toList());
                entailments.put(synset, synsets);
            }
        }
        String directory = "/Users/zhanghao/Desktop/";
        writeToFile(directory.concat("causes.txt"), causes);
        writeToFile(directory.concat("entailments.txt"), entailments);
    }

    private static void writeToFile(String path, Map<ISynset, List<ISynset>> map) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (Map.Entry<ISynset, List<ISynset>> entry : map.entrySet()) {
                String line = WordNet.synset2String(entry.getKey(), true).concat("\tCAUSES\t");
                List<String> synsetStr = entry.getValue().stream().map(synset -> WordNet.synset2String(synset, true)).collect(Collectors.toList());
                line = line.concat(String.join("\t", synsetStr));
                writer.write(line.concat("\n"));
            }
            writer.close();
            System.out.println("Write to " + path + ". Done...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
