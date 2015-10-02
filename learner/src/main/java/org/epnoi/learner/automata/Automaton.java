package org.epnoi.learner.automata;

import java.util.List;

public interface Automaton<T> {

	public void transit(Input<T> input);
	public State getCurrentState();
	public void init();
	public List<Input<T>> getRecognizedInputString();
	public List<Input<T>> getInputString();
}
