package org.epnoi.knowledgebase.wordnet;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.WordnetStemmer;
import org.epnoi.model.exceptions.EpnoiInitializationException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class WordNetHandler {
    private WordNetHandlerParameters parameters;
    private IRAMDictionary wordNetDictionary;
    private WordnetStemmer wordnetStemmer;
    private Map<String, Set<String>> hypernyms;
    private static final Logger logger = Logger
            .getLogger(WordNetHandler.class.getName());
    // ---------------------------------------------------------------------------------------------------------------

    public void init(WordNetHandlerParameters parameters)
            throws EpnoiInitializationException {
        logger.info("Initializing the WordNetHandler with the following parameters " + parameters);
        this.parameters = parameters;
        String filePath = (String) this.parameters
                .getParameterValue(WordNetHandlerParameters.DICTIONARY_LOCATION);
        try {
            File folder = new File(
                    (String) this.parameters
                            .getParameterValue(WordNetHandlerParameters.DICTIONARY_LOCATION));

            this.wordNetDictionary = new RAMDictionary(folder,
                    ILoadPolicy.IMMEDIATE_LOAD);

            this.wordNetDictionary.open();
        } catch (IOException e) {
            // e.printStackTrace();
            throw new EpnoiInitializationException(
                    "The WordNetHandler was not able to open the WordNet dictionary at "
                            + filePath);
        }

        this.wordnetStemmer = new WordnetStemmer(this.wordNetDictionary);
        this.hypernyms = new HashMap<String, Set<String>>();
        for (String noun : getNouns()) {
            /*
			 * Set<String> nounHypernyms = wordnetHandler
			 * .getNounFirstMeaningHypernyms(noun);
			 */
            addHypernym(noun,
                    getNounFirstMeaningHypernyms(noun));

        }

    }

    // -----------------------------------------------------------------------------------------------

    public void addHypernym(String word, Set<String> wordHypernyms) {

        if (wordHypernyms.size() > 0) {

            this.hypernyms.put(word, wordHypernyms);

        }
    }

    // ---------------------------------------------------------------------------------------------------------------

    public Set<String> getNounFirstMeaningHypernyms(String noun) {

        // We assume that the word has been stemmed
        Set<String> nounHypernyms = new HashSet<String>();

        if (noun != null) {


            IIndexWord idxWord = this.wordNetDictionary
                    .getIndexWord(noun, POS.NOUN);
            if (idxWord != null) {
                IWordID wordID = idxWord.getWordIDs().get(0); // We obtain the first
                // meaning (the fist
                // lemma that
                // belongs to
                // the more frequent
                // synset)
                IWord word = this.wordNetDictionary.getWord(wordID);
                ISynset synset = word.getSynset();
                List<ISynsetID> hypernyms = synset
                        .getRelatedSynsets(Pointer.HYPERNYM);

                List<IWord> words;
                for (ISynsetID sid : hypernyms) {
                    words = this.wordNetDictionary.getSynset(sid).getWords();
                    // System.out.print(sid + " {");
                    for (Iterator<IWord> i = words.iterator(); i.hasNext(); ) {

                        nounHypernyms.add(i.next().getLemma());
                    }

                }
            }
        }
        return nounHypernyms;

    }


    // ---------------------------------------------------------------------------------------------------------------

    public String stemNoun(String noun) {
        try {
            List<String> stemmerResult = this.wordnetStemmer.findStems(noun,
                    POS.NOUN);
            if (stemmerResult.size() > 0) {
                return this.wordnetStemmer.findStems(noun, POS.NOUN).get(0);
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------

    public List<String> getNouns() {
        List<String> nouns = new ArrayList<String>();
        for (Iterator<IIndexWord> i = this.wordNetDictionary
                .getIndexWordIterator(POS.NOUN); i.hasNext(); ) {
            IIndexWord wid = i.next();
            nouns.add(wid.getLemma());
        }
        return nouns;
    }

    // ---------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        System.out.println("Starting WordNetHandlerTest");
        // String filepath = "/epnoi/epnoideployment/wordnet/dictWN3.1/";

        String filepath = "/epnoi/epnoideployment/wordnet/dictWN40K/dict";
        WordNetHandlerParameters parameters = new WordNetHandlerParameters();
        parameters
                .setParameter(WordNetHandlerParameters.DICTIONARY_LOCATION, filepath);
        WordNetHandler handler = new WordNetHandler();

        try {
            handler.init(parameters);
        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out
                .println("Testing for dog--------------------------------------------------------");
        System.out.println(handler.getNounFirstMeaningHypernyms("dog"));

        System.out
                .println("Testing for lion--------------------------------------------------------");

        System.out.println(handler.getNounFirstMeaningHypernyms("lion"));

        System.out
                .println("Testing for lions--------------------------------------------------------");

        System.out.println(handler.getNounFirstMeaningHypernyms("lions"));

        System.out.println("Testing the get nouns function");
        long t = System.currentTimeMillis();

        System.out.println("These are the nouns ");
        int count = 0;
        int hypernymsTotal = 0;
        List<String> nouns = handler.getNouns();
        for (String noun : nouns) {
            Set<String> hypernyms = handler.getNounFirstMeaningHypernyms(noun);
            System.out.println(":::> " + noun + " -> " + hypernyms);
            if (hypernyms.size() > 0) {
                count = count + 1;
                hypernymsTotal += hypernyms.size();
            }
        }
        System.out.println("There are " + nouns.size() + " nouns");
        System.out.println("About " + ((double) count)
                / ((double) nouns.size()) + " have hypernyms defined");
        System.out.println("With an average of " + ((double) hypernymsTotal)
                / ((double) nouns.size()) + "hypernyms each");
        long time = System.currentTimeMillis() - t;
        System.out.printf(" done in " + String.valueOf(time));
        System.out.println("Ending WordNetHandlerTest");

    }

}
