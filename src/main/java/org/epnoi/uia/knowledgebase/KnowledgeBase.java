package org.epnoi.uia.knowledgebase;

import java.util.HashSet;
import java.util.Set;

import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.uia.core.Core;
import org.epnoi.uia.core.CoreUtility;
import org.epnoi.uia.knowledgebase.wikidata.WikidataHandler;
import org.epnoi.uia.knowledgebase.wordnet.WordNetHandler;

import com.google.common.collect.Sets;

import edu.stanford.nlp.ling.CoreAnnotations.StemAnnotation;

public class KnowledgeBase {

	WordNetHandler wordNetHandler;

	WikidataHandler wikidataHandler;
	private boolean considerWordNet = true;
	private boolean considerWikidata = true;
	KnowledgeBaseParameters parameters;

	// -----------------------------------------------------------------------------------------------

	public KnowledgeBase(WordNetHandler wordNetHandler,
			WikidataHandler wikidataHandler) {

		this.wordNetHandler = wordNetHandler;
		this.wikidataHandler = wikidataHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public void init(KnowledgeBaseParameters parameters) {
		this.parameters = parameters;
		this.considerWikidata = (boolean) this.parameters
				.getParameterValue(KnowledgeBaseParameters.CONSIDER_WIKIDATA);
		this.considerWordNet = (boolean) this.parameters
				.getParameterValue(KnowledgeBaseParameters.CONSIDER_WORDNET);
	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelated(String source, String target, String type) {
		if (RelationHelper.HYPERNYM.equals(type) && (source.length() > 0)
				&& (target.length() > 0)) {

			if (this.considerWikidata) {
				return (areRelatedInWikidata(source, target));
			}

			if (this.considerWordNet) {
				return (areRelatedInWordNet(source, target));
			}
		}
		return false;
	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelatedInWordNet(String source, String target) {

		String stemmedSource = this.wordNetHandler.stemNoun(source);
		stemmedSource = (stemmedSource == null) ? stemmedSource = source
				: stemmedSource;
		String stemmedTarget = this.wordNetHandler.stemNoun(target);
		stemmedTarget = (stemmedTarget == null) ? stemmedTarget = source
				: stemmedTarget;

		Set<String> sourceHypernyms = this.wordNetHandler
				.getNounFirstMeaningHypernyms(stemmedSource);
		return (sourceHypernyms != null && sourceHypernyms
				.contains(stemmedTarget));

	}

	// -----------------------------------------------------------------------------------------------

	public boolean areRelatedInWikidata(String source, String target) {
		System.out.println("> " + source + " " + target);
		source = source.toLowerCase();
		target = target.toLowerCase();

		String stemmedSource = this.wikidataHandler.stem(source);
		System.out.println(">> stemmedSource " + stemmedSource);
		String stemmedTarget = this.wikidataHandler.stem(target);
		System.out.println(">> stemmedTarget " + stemmedTarget);
		Set<String> stemmedSourceHypernyms = this.wikidataHandler.getRelated(
				stemmedSource, RelationHelper.HYPERNYM);
		Set<String> sourceHypernyms = this.wikidataHandler.getRelated(source,
				RelationHelper.HYPERNYM);
		System.out.println(">> stemmedSourceHypernyms "
				+ stemmedSourceHypernyms);

		System.out.println(">> sourceHypernyms " + sourceHypernyms);

		sourceHypernyms.addAll(stemmedSourceHypernyms);

		return (sourceHypernyms != null && (sourceHypernyms
				.contains(stemmedTarget) || sourceHypernyms.contains(target)));

	}

	// -----------------------------------------------------------------------------------------------
	/**
	 * Method that returns the hypermyms of a given term
	 * 
	 * @param source
	 *            We assume that the source has been stemmed using the stemmer
	 *            associated with the handler
	 * @return
	 */
	public Set<String> getHypernyms(String source) {
		
		
		Set<String> hypernyms = new HashSet<String>();
		if (this.considerWikidata) {
			
			Set<String> wikidataHypernyms = this.wikidataHandler.getRelated(source,
					RelationHelper.HYPERNYM);
			
			hypernyms.addAll(wikidataHypernyms);
		}
		if (this.considerWordNet) {
			Set<String> wordNetHypernyms = this.wordNetHandler
					.getNounFirstMeaningHypernyms(source);
			hypernyms.addAll(wordNetHypernyms);
		}
		return hypernyms;
	}

	// -----------------------------------------------------------------------------------------------

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

	// -----------------------------------------------------------------------------------------------

	public WordNetHandler getWordNetHandler() {
		return wordNetHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public void setWordNetHandler(WordNetHandler wordNetHandler) {
		this.wordNetHandler = wordNetHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public WikidataHandler getWikidataHandler() {
		return wikidataHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public void setWikidataHandler(WikidataHandler wikidataHandler) {
		this.wikidataHandler = wikidataHandler;
	}

	// -----------------------------------------------------------------------------------------------

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();
		try {
			KnowledgeBase knowledgeBase = core.getKnowledgeBaseHandler()
					.getKnowledgeBase();
		} catch (EpnoiInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
