package org.epnoi.uia.learner.automata;

public interface Action<T> {
	
	public void act(Automaton<T> automaton);

}
