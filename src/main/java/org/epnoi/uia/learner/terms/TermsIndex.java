package org.epnoi.uia.learner.terms;

import gate.Annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermsIndex {
	
	static final Comparator<AnnotatedWord<TermCandidateMetadata>> TERMS_ORDER = new Comparator<AnnotatedWord<TermCandidateMetadata>>() {
		public int compare(AnnotatedWord<TermCandidateMetadata> term1, AnnotatedWord<TermCandidateMetadata> term2) {
			if(term1.getAnnotation().getCValue()>term2.getAnnotation().getCValue())
				return 1;
			else if(term1.getAnnotation().getCValue()==term2.getAnnotation().getCValue()){
				return 0;
			}else{
				return -1;
			}
				
		}
	};
	private Map<String, AnnotatedWord<TermCandidateMetadata>> terms;

	
	
	// -------------------------------------------------------------------------------------------------------

	public void init() {
		this.terms = new HashMap<String, AnnotatedWord<TermCandidateMetadata>>();
	}

	// -------------------------------------------------------------------------------------------------------

	public AnnotatedWord<TermCandidateMetadata> lookUp(String word) {
		return terms.get(word);
	}

	// -------------------------------------------------------------------------------------------------------

	public void updateTerm(AnnotatedWord<TermCandidateMetadata> term) {
		AnnotatedWord<TermCandidateMetadata> indexedTerm = this.terms.get(term
				.getWord());
		if (indexedTerm == null) {
			this.terms.put(term.getWord(), term);
		} else {
			// System.out.println("indexed> "+indexedTerm);
			indexedTerm.getAnnotation().setOcurrences(
					indexedTerm.getAnnotation().getOcurrences() + 1);

		}

	}

	// -------------------------------------------------------------------------------------------------------

	public void updateSubTerm(AnnotatedWord<TermCandidateMetadata> term,
			AnnotatedWord<TermCandidateMetadata> subTerm) {
		
		if (subTerm.getWord().equals("simple"))
		System.out.println("Entra Term> "+term+"  SubTerm> "+subTerm);
		
		
		AnnotatedWord<TermCandidateMetadata> indexedTerm = this.terms
				.get(subTerm.getWord());
		if (indexedTerm == null) {
			this.terms.put(subTerm.getWord(), subTerm);
			subTerm.getAnnotation().setOcurrences(
					term.getAnnotation().getOcurrences());
			subTerm.getAnnotation().setOcurrencesAsSubterm(
					term.getAnnotation().getOcurrences());
			subTerm.getAnnotation().setNumberOfSuperterns(1L);
		} else {

			indexedTerm.getAnnotation().setOcurrences(
					indexedTerm.getAnnotation().getOcurrences()
							+ term.getAnnotation().getOcurrences());

			indexedTerm.getAnnotation().setOcurrencesAsSubterm(
					indexedTerm.getAnnotation().getOcurrencesAsSubterm()
							+ term.getAnnotation().getOcurrences());
			indexedTerm.getAnnotation().setNumberOfSuperterns(
					indexedTerm.getAnnotation().getNumberOfSuperterns() + 1);

		}
		if (subTerm.getWord().equals("simple"))
		System.out.println("Sale Term> "+term+"  SubTerm> "+subTerm);

	}

	// -------------------------------------------------------------------------------------------------------

	public List<AnnotatedWord<TermCandidateMetadata>> getTermCandidates() {
		List<AnnotatedWord<TermCandidateMetadata>> termCandidates = new ArrayList<AnnotatedWord<TermCandidateMetadata>>(
				this.terms.values());
		Collections.sort(termCandidates);
		return termCandidates;
	}
	
	// -------------------------------------------------------------------------------------------------------
	
	public List<AnnotatedWord<TermCandidateMetadata>> getTerms() {
		List<AnnotatedWord<TermCandidateMetadata>> termCandidates = new ArrayList<AnnotatedWord<TermCandidateMetadata>>(
				this.terms.values());
		Collections.sort(termCandidates, TERMS_ORDER);
		return termCandidates;
	}

}
