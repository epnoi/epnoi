package org.epnoi.learner.automata;

import org.epnoi.model.AnnotatedWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomatonImpl implements Automaton<AnnotatedWord<String>> {

	private State initialState = new State("init", false, true);
	private State currentState;
	private List<Input<AnnotatedWord<String>>> regonizedInputString;

	private List<Input<AnnotatedWord<String>>> inputString;

	Map<State, Map<String, State>> transitions = new HashMap<State, Map<String, State>>();

	// Map<State, Map<Input<AnnotatedWord<String>>, Action>> actions = new
	// HashMap<State, Map<Input<AnnotatedWord<String>>, Action>>();

	public AutomatonImpl() {
		this.inputString = new ArrayList<>();
		this.regonizedInputString = new ArrayList<>();

		// States definition
		State stateOneAdjective = new State("stateOneAdjective", false, false);
		
		State finalState = new State("finalState", true, false);

		// Transistion tables definitions
		Map<String, State> initTransitions = new HashMap<String, State>();
		initTransitions.put("JJ", stateOneAdjective);
		initTransitions.put("NN", finalState);
		initTransitions.put("NNP", finalState);

		Map<String, State> stateOneAdjectiveTransitions = new HashMap<String, State>();
		stateOneAdjectiveTransitions.put("NN", finalState);
		stateOneAdjectiveTransitions.put("NNP", finalState);

		Map<String, State> finalStateTransitions  = new HashMap<String, State>();
		finalStateTransitions.put("NN", finalState);
		finalStateTransitions.put("NNP", finalState);
		
		transitions.put(this.initialState, initTransitions);
		transitions.put(stateOneAdjective, stateOneAdjectiveTransitions);
		transitions.put(finalState, finalStateTransitions);
	}

	// ------------------------------------------------------------------------

	@Override
	public void transit(Input<AnnotatedWord<String>> input) {
		System.out
				.println(">transit ===================================================================================");

		System.out.println(" input>"+input);
		System.out.println(" currentState>"+this.currentState);
		
		
		System.out.println("	InputString> " + this.inputString
				+ " +RecognizedString> " + this.regonizedInputString);
		
		String inputAnnotation = input.getContent().getAnnotation();
		State nextState = null;
		this.inputString.add(input);
		Map<String, State> stateTransitions = this.transitions
				.get(currentState);
		if (stateTransitions != null) {
			nextState = stateTransitions.get(inputAnnotation);
		}
		System.out.println(" nextState>"
				+ nextState);

		/*
		 * Action<AnnotatedWord<String>> transitionAction = this.actions.get(
		 * currentState).get(inputAnnotation);
		 */

		/*
		 * if (transitionAction != null) { transitionAction.act(this); }
		 */
		if (nextState != null) {

			this.currentState = nextState;

		} else {
			this.currentState = initialState;
		}

		if (currentState.isFinal()) {
			this.regonizedInputString.addAll(inputString);
			this.inputString.clear();
		}
		System.out
				.println("===================================================================================");
	}

	// ------------------------------------------------------------------------

	@Override
	public State getCurrentState() {
		return this.currentState;
	}

	// ------------------------------------------------------------------------

	public void init() {
		this.currentState = this.initialState;
		this.inputString.clear();
		this.regonizedInputString.clear();

	}

	// ------------------------------------------------------------------------

	@Override
	public List<Input<AnnotatedWord<String>>> getRecognizedInputString() {

		return this.regonizedInputString;
	}

	// ------------------------------------------------------------------------

	@Override
	public List<Input<AnnotatedWord<String>>> getInputString() {

		return this.inputString;
	}

	// ------------------------------------------------------------------------
}
