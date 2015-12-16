package org.epnoi.knowledgebase;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.modules.KnowledgeBaseParameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnowledgeBaseImpl implements KnowledgeBase {
private static Set<String> ambiguousTerms;
    static{
        ambiguousTerms= new HashSet<String>(Arrays.asList("film", "taxon", "band", "song", "album","episode","magazine","human"));
    }
    WordNetHandler wordNetHandler;

    WikidataHandler wikidataHandler;
    private boolean considerWordNet = true;
    private boolean considerWikidata = true;
    KnowledgeBaseParameters parameters;

    // -----------------------------------------------------------------------------------------------

    public KnowledgeBaseImpl(WordNetHandler wordNetHandler, WikidataHandler wikidataHandler) {

        this.wordNetHandler = wordNetHandler;
        this.wikidataHandler = wikidataHandler;
    }

    // -----------------------------------------------------------------------------------------------


    @Override
    public void init(KnowledgeBaseParameters parameters) {
        this.parameters = parameters;
        this.considerWikidata = (boolean) this.parameters.getParameterValue(KnowledgeBaseParameters.CONSIDER_WIKIDATA);
        this.considerWordNet = (boolean) this.parameters.getParameterValue(KnowledgeBaseParameters.CONSIDER_WORDNET);
    }

    // -----------------------------------------------------------------------------------------------

    /**
     * @see
     */

    @Override
    public boolean areRelated(String source, String target, String type) {
        System.out.println("Wordnet " + this.considerWordNet + " Wikidata " + this.considerWikidata);
        if (RelationHelper.HYPERNYMY.equals(type) && (source.length() > 0) && (target.length() > 0)) {
            boolean areRelatedInWikidata = false;
            if (this.considerWikidata) {
                areRelatedInWikidata = (areRelatedInWikidata(source, target));
            }
            boolean areRelatedInWordNet = false;
            if (this.considerWordNet) {
                areRelatedInWordNet = (areRelatedInWordNet(source, target));
            }
            return (areRelatedInWikidata || areRelatedInWordNet);
        }
        return false;
    }

    // -----------------------------------------------------------------------------------------------


    @Override
    public boolean areRelatedInWordNet(String source, String target) {

        String stemmedSource = this.wordNetHandler.stemNoun(source);
        stemmedSource = (stemmedSource == null) ? stemmedSource = source : stemmedSource;
        String stemmedTarget = this.wordNetHandler.stemNoun(target);
        stemmedTarget = (stemmedTarget == null) ? stemmedTarget = source : stemmedTarget;
        System.out.println(">> stemmedSource " + stemmedSource);
        Set<String> sourceHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(stemmedSource);
        System.out.println(">> stemmedSourceHypernyms " + sourceHypernyms);

        return (sourceHypernyms != null && sourceHypernyms.contains(stemmedTarget));

    }

    // -----------------------------------------------------------------------------------------------


    @Override
    public boolean areRelatedInWikidata(String source, String target) {
        System.out.println("> " + source + " " + target);
        source = source.toLowerCase();
        target = target.toLowerCase();

        String stemmedSource = this.wikidataHandler.stem(source);
        System.out.println(">> stemmedSource " + stemmedSource);
        String stemmedTarget = this.wikidataHandler.stem(target);
        System.out.println(">> stemmedTarget " + stemmedTarget);
        Set<String> stemmedSourceHypernyms = this.wikidataHandler.getRelated(stemmedSource, RelationHelper.HYPERNYMY);
        Set<String> sourceHypernyms = this.wikidataHandler.getRelated(source, RelationHelper.HYPERNYMY);
        System.out.println(">> stemmedSourceHypernyms " + stemmedSourceHypernyms);

        System.out.println(">> sourceHypernyms " + sourceHypernyms);

        sourceHypernyms.addAll(stemmedSourceHypernyms);

        return (sourceHypernyms != null
                && (sourceHypernyms.contains(stemmedTarget) || sourceHypernyms.contains(target)));

    }


    @Override
    public Set<String> getHypernyms(String source) {

        Set<String> hypernyms = new HashSet<String>();
        if (this.considerWikidata) {

            Set<String> wikidataHypernyms = this.wikidataHandler.getRelated(source, RelationHelper.HYPERNYMY);
            hypernyms.addAll(wikidataHypernyms);

            String stemmedSource = this.wikidataHandler.stem(source);
            wikidataHypernyms = this.wikidataHandler.getRelated(stemmedSource, RelationHelper.HYPERNYMY);

            hypernyms.addAll(wikidataHypernyms);

        }
        if (this.considerWordNet) {
            String stemmedSource = this.wordNetHandler.stemNoun(source);

            Set<String> wordNetHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(stemmedSource);
            hypernyms.addAll(wordNetHypernyms);
        }
        hypernyms.remove(source);
        hypernyms.removeAll(ambiguousTerms);
        return hypernyms;
    }


    @Override
    public Set<String> stem(String term) {


        Set<String> stemmedTerm = new HashSet<String>();
        if (this.considerWordNet) {
            String wordNetStemmedTerm = this.wordNetHandler.stemNoun(term);
            if (_test(wordNetStemmedTerm)) {
                stemmedTerm.add(wordNetStemmedTerm);
            }
        }
        if (this.considerWikidata) {
            String wikidataStemmedTerm = this.wikidataHandler.stem(term);
            if (_test(wikidataStemmedTerm)) {
                stemmedTerm.add(wikidataStemmedTerm);
            }
        }
        return stemmedTerm;
    }

	/*
     *
	 * //
	 * -------------------------------------------------------------------------
	 * ----------------------
	 * 
	 * @Override public WordNetHandler getWordNetHandler() { return
	 * wordNetHandler; }
	 * 
	 * //
	 * -------------------------------------------------------------------------
	 * ----------------------
	 * 
	 * @Override public void setWordNetHandler(WordNetHandler wordNetHandler) {
	 * this.wordNetHandler = wordNetHandler; }
	 * 
	 * //
	 * -------------------------------------------------------------------------
	 * ----------------------
	 * 
	 * @Override public WikidataHandler getWikidataHandler() { return
	 * wikidataHandler; }
	 * 
	 * //
	 * -------------------------------------------------------------------------
	 * ----------------------
	 * 
	 * @Override public void setWikidataHandler(WikidataHandler wikidataHandler)
	 * { this.wikidataHandler = wikidataHandler; }
	 */
    // -----------------------------------------------------------------------------------------------
    /*
	 * FOR_TEST public static void main(String[] args) {
	 * 
	 * Core core = CoreUtility.getUIACore();
	 * 
	 * try { KnowledgeBase knowledgeBase=null; try { knowledgeBase =
	 * core.getKnowledgeBaseHandler().getKnowledgeBase(); } catch
	 * (EpnoiResourceAccessException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * System.out.println("100> " + knowledgeBase.getWikidataHandler()
	 * .getRelated(knowledgeBase.getWikidataHandler().stem("madrid"),
	 * RelationHelper.HYPERNYMY));
	 * 
	 * } catch (EpnoiInitializationException e) {
	 * 
	 * e.printStackTrace(); }
	 */
	/*
	 * 
	 * CassandraInformationStore cis = ((CassandraInformationStore) core
	 * .getInformationStoresByType(InformationStoreHelper.
	 * CASSANDRA_INFORMATION_STORE).get(0)); cis.getQueryResolver().getWith(
	 * "http://www.epnoi.org/wikidataView/relations/"+RelationHelper. HYPERNYMY,
	 * "WikidataViewCorpus", "Q2807");
	 */
	/*
	 * CassandraInformationStore cis = ((CassandraInformationStore) core
	 * .getInformationStoresByType(InformationStoreHelper.
	 * CASSANDRA_INFORMATION_STORE).get(0));
	 * 
	 * System.out.println(cis.getQueryResolver().getValues(
	 * "http://www.epnoi.org/wikidataView/relations/" + RelationHelper.HYPERNYMY,
	 * "Q2807", WikidataViewCassandraHelper.COLUMN_FAMILY));
	 * System.out.println(cis.getQueryResolver().getValues(
	 * "http://www.epnoi.org/wikidataView/reverseDictionary", "Q2807",
	 * WikidataViewCassandraHelper.COLUMN_FAMILY));
	 * 
	 */
    // }

    private boolean _test(String term) {
        if (term != null && term.length() > 1) {
            String result = term.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "");
            return result.length() > 2;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Valid? ");
    }
}
