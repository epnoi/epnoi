package org.epnoi.knowledgebase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epnoi.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.knowledgebase.wordnet.WordNetHandler;
import org.epnoi.model.KnowledgeBase;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.modules.KnowledgeBaseParameters;

public class KnolwedgeBaseImpl implements KnowledgeBase {

	WordNetHandler wordNetHandler;

	WikidataHandler wikidataHandler;
	private boolean considerWordNet = true;
	private boolean considerWikidata = true;
	KnowledgeBaseParameters parameters;

	// -----------------------------------------------------------------------------------------------

	public KnolwedgeBaseImpl(WordNetHandler wordNetHandler, WikidataHandler wikidataHandler) {

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
	 *@see
	 */

	@Override
	public boolean areRelated(String source, String target, String type) {
		System.out.println("Wordnet "+this.considerWordNet +" Wikidata "+this.considerWikidata);
		if (RelationHelper.HYPERNYM.equals(type) && (source.length() > 0) && (target.length() > 0)) {
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
		Set<String> stemmedSourceHypernyms = this.wikidataHandler.getRelated(stemmedSource, RelationHelper.HYPERNYM);
		Set<String> sourceHypernyms = this.wikidataHandler.getRelated(source, RelationHelper.HYPERNYM);
		System.out.println(">> stemmedSourceHypernyms " + stemmedSourceHypernyms);

		System.out.println(">> sourceHypernyms " + sourceHypernyms);

		sourceHypernyms.addAll(stemmedSourceHypernyms);

		return (sourceHypernyms != null
				&& (sourceHypernyms.contains(stemmedTarget) || sourceHypernyms.contains(target)));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.epnoi.uia.knowledgebase.KnowledgeBaseInterface#getHypernyms(java.lang.String)
	 */
	@Override
	public Set<String> getHypernyms(String source) {

		Set<String> hypernyms = new HashSet<String>();
		if (this.considerWikidata) {

			Set<String> wikidataHypernyms = this.wikidataHandler.getRelated(source, RelationHelper.HYPERNYM);
			hypernyms.addAll(wikidataHypernyms);

			String stemmedSource = this.wikidataHandler.stem(source);
			wikidataHypernyms = this.wikidataHandler.getRelated(stemmedSource, RelationHelper.HYPERNYM);

			hypernyms.addAll(wikidataHypernyms);

			}
		if (this.considerWordNet) {
			String stemmedSource = this.wordNetHandler.stemNoun(source);
			System.out.println("stemmedsource >"+stemmedSource);
			Set<String> wordNetHypernyms = this.wordNetHandler.getNounFirstMeaningHypernyms(stemmedSource);
			hypernyms.addAll(wordNetHypernyms);
		}
		return hypernyms;
	}

	// -----------------------------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.epnoi.uia.knowledgebase.KnowledgeBaseInterface#stem(java.lang.String)
	 */
	@Override
	public Set<String> stem(String term) {

		Set<String> stemmedTerm = new HashSet<String>();
		if (this.considerWordNet) {
			String wordNetStemmedTerm = this.wordNetHandler.stemNoun(term);
			if (wordNetStemmedTerm != null) {
				stemmedTerm.add(wordNetStemmedTerm);
			}
		}
		if (this.considerWikidata) {
			stemmedTerm.add(this.wikidataHandler.stem(term));
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
	 * RelationHelper.HYPERNYM));
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
	 * "http://www.epnoi.org/wikidataView/relations/"+RelationHelper. HYPERNYM,
	 * "WikidataViewCorpus", "Q2807");
	 */
	/*
	 * CassandraInformationStore cis = ((CassandraInformationStore) core
	 * .getInformationStoresByType(InformationStoreHelper.
	 * CASSANDRA_INFORMATION_STORE).get(0));
	 * 
	 * System.out.println(cis.getQueryResolver().getValues(
	 * "http://www.epnoi.org/wikidataView/relations/" + RelationHelper.HYPERNYM,
	 * "Q2807", WikidataViewCassandraHelper.COLUMN_FAMILY));
	 * System.out.println(cis.getQueryResolver().getValues(
	 * "http://www.epnoi.org/wikidataView/reverseDictionary", "Q2807",
	 * WikidataViewCassandraHelper.COLUMN_FAMILY));
	 * 
	 */
	// }
	
	public static void main(String[] args) {
		Set<String> aset = new HashSet<String>();
		aset.add("algo");
		System.out.println(aset);
		aset.add("algo");
		System.out.println(aset);
		List<String> alist = Arrays.asList("algo","otro");
		aset.addAll(alist);
		
		System.out.println(aset);
		
	}

}
