# WordNet and VerbNet Extension in Java
It's an extension of [WordNet API](https://projects.csail.mit.edu/jwi/) and [VerbNet API](http://projects.csail.mit.edu/jverbnet/) to analyze the structures of WordNet and VerbNet as well as extract information from both hierarchical structured commonsense knowledge database.

For WordNet, the [JWI (the MIT Java Wordnet Interface)](https://projects.csail.mit.edu/jwi/) tool is used, which is a Java library for interfacing with Wordnet. JWI supports access to Wordnet versions `1.6` through `3.0`, among other related Wordnet extensions. Currently, JWI is released at version `2.4.0`, all the binaries, manuals, development kit, API reference and etc. are available [here](https://projects.csail.mit.edu/jwi/). Besides, it's also allowed to be import into project through maven snippet: [[link]](https://mvnrepository.com/artifact/edu.mit/jwi) (latest version `2.2.3`).

For VerbNet, the [JVerbnet](http://projects.csail.mit.edu/jverbnet/) tool is used, which is a Java library for interfacing with [the University of Colorado's VerbNet data](https://verbs.colorado.edu/verbnet/). It features API calls to retrieve verb classes by their id numbers, associated Wordnet keys or Propbank groupings. Currently, JVerbnet is released at version `1.2.0`, samely, all the related resources are available at its project website, [here](http://projects.csail.mit.edu/jverbnet/). And its maven snippet is accessible through this [link](https://mvnrepository.com/artifact/edu.mit/jverbnet) (latest vernsion `1.2.0.1`).

Thus, this repository also contains all the methods and functions in the above tools, and, in addition, in order to make the WordNet and VerbNet are easily to be accessed, 

### WordNet
WordNet (WN) is a lexical database for the English language. It groups English words into sets of [synonyms](https://en.wikipedia.org/wiki/Synonyms) called [synsets](https://en.wikipedia.org/wiki/Synsets), provides short definitions and usage examples, and records a number of relations among these synonym sets or their members. WordNet can thus be seen as a combination of dictionary and thesaurus. For more details about **WordNet**, please see its official webpage: [[link]](https://wordnet.princeton.edu).

[**WordNet Online Demo!**](http://wordnetweb.princeton.edu/perl/webwn)


### VerbNet
VerbNet (VN) is the largest online verb lexicon currently available for English. It is a hierarchical domain-independent, broad-coverage verb lexicon with mappings to other lexical resources such as WordNet, Xtag, and FrameNet. VerbNet is organized into verb classes extending Levin classes through refinement and addition of subclasses to achieve syntactic and semantic coherence among members of a class. Each verb class in VN is completely described by [thematic roles](http://verbs.colorado.edu/~mpalmer/projects/verbnet.html#thetaroles), [selectional restrictions on the arguments](http://verbs.colorado.edu/~mpalmer/projects/verbnet.html#selrestr), and [frames](http://verbs.colorado.edu/~mpalmer/projects/verbnet.html#frames) consisting of a syntactic description and semantic predicates with a temporal function. For more details about **VerbNet**, please see its official webpage: [[link]](http://verbs.colorado.edu/~mpalmer/projects/verbnet.html).

[**VerbNet Online Demo!**](https://verbs.colorado.edu/verb-index/)

### Usage
**WordNet** (sample usages):
```java
public class GeneralWordNetExample {
    public static void main (String[] args) {
        // get Verb Synset Iterator
        Iterator<ISynset> synsetIterator = WordNet.getSynsetIterator(POS.VERB);
        // validate and parse Sense Key String
        String senseKeyString = "make_clean%2:35:00::";
        // if senseKeyString is valid, convert it to ISenseKey, otherwise null
        ISenseKey key = WordNet.senseKeyPattern(senseKeyString) ? WordNet.parseSenseKeyString(senseKeyString) : null;
        assert key != null;
        IWord iWord = WordNet.getWordBySenseKey(key); // obtain IWord
        System.out.println(iWord.getLemma() + "\t" + iWord.getSynset() + "\t" + iWord.getLexicalID() + "...");
        // validate and parse Word ID
        String wordIdString = "WID-01535377-V-02-make_clean";
        System.out.println("WordID Pattern Result: " + WordNet.wordIdPattern(wordIdString));
        IWordID wordId = WordNet.wordIdPattern(wordIdString) ? WordNet.parseIWordIDString(wordIdString) : null;
        iWord =  WordNet.getWordByWordId(wordId);
        // validate and parse Synset ID
        ISynsetID synsetId = WordNet.parseSynsetIDString("SID-02081903-V");
        System.out.println("SynsetID Pattern Result: " + WordNet.synsetIdPattern("SID-02081903-V"));
        // others
        String lemma = "take";
        List<IWordID> ids = WordNet.getIndexWord(lemma, POS.VERB).getWordIDs();
        int index = 0;
        for (IWordID id : ids) {
            index++;
            iWord = WordNet.getWordByWordId(id);
            System.out.println(iWord.getLemma() + "\t" + WordNet.getTagCount4IWord(iWord) + "\t" + WordNet.getSenseNumber4IWord(iWord));
        }
        System.out.println(index);
        System.out.println(WordNet.getNumberOfSense4Word(lemma, POS.VERB));
        // ......
    }
}
```

**VerbNet**:
```java
public class GeneralVerbNetExample {
    public static void main (String[] args) {
        System.out.println("All Classes: " + VerbNet.countAllVerbClasses());
        System.out.println("Root Classes: " + VerbNet.countRootVerbClasses());
        System.out.println("Sub Classes: " + VerbNet.countSubVerbClasses());
        Iterator<IVerbClass> iterator = VerbNet.rootVerbClassIterator(); // get IVerbClass iterator
        List<IVerbClass> rootVerbs = VerbNet.getRootVerbClasses();
        IVerbClass iVerbClass = VerbNet.getRootVerb("hit-18.1");
        List<IMember> members = iVerbClass.getMembers();
        List<String> wordNetKeys = VerbNet.iMemberToWordNetKeys(members.get(0)); 
        // ...
    }
}
```