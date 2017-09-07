package com.isaac.representation;

import com.isaac.wordnet.WordNetUtils;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import static com.isaac.wordnet.WordNetUtils.wndict;

public class SynsetElement {
    /** synset */
    private ISynset synset;

    /** Constructor */
    public SynsetElement(ISynset synset) { this.synset = synset; }

    /** @return the polysemy count (sense number) of the synset */
    public double getPolysemy (String method) {
        OptionalDouble value;
        switch (method) {
            case "max": value = synset2WordSenseCount(synset).stream().mapToDouble(Integer::doubleValue).max(); break;
            case "min": value = synset2WordSenseCount(synset).stream().mapToDouble(Integer::doubleValue).min(); break;
            case "mean":
            case "average":
            default: value = synset2WordSenseCount(synset).stream().mapToDouble(Integer::doubleValue).average();
        }
        return value.isPresent() ? value.getAsDouble() : 0.0;
    }

    /** @return the lexical domain of synset */
    public String getLexicalDomain () { return synset.getLexicalFile().toString(); }

    /** @return synset id */
    public String getSynsetId() {
        return synset.getID().toString();
    }

    /** @return synset string */
    public String getSynsetStr() { return WordNetUtils.synset2String(synset, false); }

    /** @return number of words in the synset */
    public int getWordsSize () { return synset.getWords().size(); }

    /** @return part of speech of the synset */
    public String getPOS () { return synset.getPOS().toString(); }

    /** @return the gloss of the synset, which is an explanation of such synset */
    public String getGloss() {
        return synset.getGloss();
    }

    /** @return the synset */
    public ISynset getSynset() {
        return synset;
    }

    /** @return {@link List} of {@link Integer}, polysemy count (sense number) of each {@link IWord} in the {@link ISynset} */
    private List<Integer> synset2WordSenseCount(ISynset synset) {
        return synset.getWords().stream().map(w -> wndict.getIndexWord(w.getLemma(), w.getPOS()).getWordIDs().size())
                .mapToInt(Integer::intValue).boxed().collect(Collectors.toList());
    }

}
