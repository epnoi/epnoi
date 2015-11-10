package org.epnoi.learner.terms;

import org.epnoi.model.AnnotatedWord;
import org.epnoi.model.TermMetadata;

import java.util.*;

public class TermsIndex {

	// -------------------------------------------------------------------------------------------------------

	static final Comparator<AnnotatedWord<TermMetadata>> TERMS_ORDER = new Comparator<AnnotatedWord<TermMetadata>>() {
		public int compare(AnnotatedWord<TermMetadata> term1,
				AnnotatedWord<TermMetadata> term2) {
			if (term1.getAnnotation().getTermhood() < term2.getAnnotation()
					.getTermhood())
				return 1;
			else if (term1.getAnnotation().getTermhood() == term2
					.getAnnotation().getTermhood()) {
				return 0;
			} else {
				return -1;
			}

		}
	};

	// Terms are indexed per domain, thus this table is domain_uri->term_word ->
	// term
	private Map<String, Map<String, AnnotatedWord<TermMetadata>>> terms;

	// -------------------------------------------------------------------------------------------------------

	public void init() {
		this.terms = new HashMap<String, Map<String, AnnotatedWord<TermMetadata>>>();
	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<TermMetadata> lookUp(String domain, String word) {
		return terms.get(domain).get(word);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateTerm(String domain, AnnotatedWord<TermMetadata> term) {

		Map<String, AnnotatedWord<TermMetadata>> domainTerms = this.terms
				.get(domain);

		if (domainTerms == null) {
			domainTerms = new HashMap<>();
			this.terms.put(domain, domainTerms);

		}

		AnnotatedWord<TermMetadata> indexedTerm = domainTerms.get(term
				.getWord());
		if (indexedTerm == null) {
			domainTerms.put(term.getWord(), term);
		} else {
			// System.out.println("indexed> "+indexedTerm);
			indexedTerm.getAnnotation().setOcurrences(
					indexedTerm.getAnnotation().getOcurrences() + 1);

		}
		// System.out.println("this.terms " + this.terms);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateSubTerm(String domain, AnnotatedWord<TermMetadata> term,
			AnnotatedWord<TermMetadata> subTerm) {

		Map<String, AnnotatedWord<TermMetadata>> domainTerms = this.terms
				.get(domain);

		if (domainTerms == null) {
			domainTerms = new HashMap<>();
			this.terms.put(domain, domainTerms);

		}

		AnnotatedWord<TermMetadata> indexedTerm = domainTerms.get(subTerm
				.getWord());
		if (indexedTerm == null) {
			domainTerms.put(subTerm.getWord(), subTerm);
			subTerm.getAnnotation().setOcurrences(
					term.getAnnotation().getOcurrences()
							- term.getAnnotation().getOcurrencesAsSubterm());
			subTerm.getAnnotation().setOcurrencesAsSubterm(
					term.getAnnotation().getOcurrences()
							- term.getAnnotation().getOcurrencesAsSubterm());
			subTerm.getAnnotation().setNumberOfSuperterns(1L);
		} else {

			indexedTerm.getAnnotation().setOcurrences(
					indexedTerm.getAnnotation().getOcurrences()
							+ term.getAnnotation().getOcurrences()
							- term.getAnnotation().getOcurrencesAsSubterm());

			indexedTerm.getAnnotation().setOcurrencesAsSubterm(
					indexedTerm.getAnnotation().getOcurrencesAsSubterm()
							+ term.getAnnotation().getOcurrences()
							- term.getAnnotation().getOcurrencesAsSubterm());
			indexedTerm.getAnnotation().setNumberOfSuperterns(
					indexedTerm.getAnnotation().getNumberOfSuperterns() + 1);

		}

	}

	// -------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermMetadata>> getTermCandidates(String domain) {
		if (this.terms.get(domain) != null) {
		List<AnnotatedWord<TermMetadata>> termCandidates = new ArrayList<AnnotatedWord<TermMetadata>>(
				this.terms.get(domain).values());
		Collections.sort(termCandidates);
		
		return termCandidates;
		}
		return new ArrayList<AnnotatedWord<TermMetadata>>();
	}

	// -------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermMetadata>> getTerms(String domain) {
		if (this.terms.get(domain) != null) {
			List<AnnotatedWord<TermMetadata>> termCandidates = new ArrayList<AnnotatedWord<TermMetadata>>(
					this.terms.get(domain).values());
			Collections.sort(termCandidates, TERMS_ORDER);
			return termCandidates;
		}
		return new ArrayList<AnnotatedWord<TermMetadata>>();

	}

	// -------------------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "TermsIndex [terms=" + terms + "]";
	}

	// -------------------------------------------------------------------------------------------------------

}
