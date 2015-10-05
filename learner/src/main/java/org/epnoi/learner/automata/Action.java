package org.epnoi.learner.automata;

public interface Action<T> {
	
	public void act(Automaton<T> automaton);

}
