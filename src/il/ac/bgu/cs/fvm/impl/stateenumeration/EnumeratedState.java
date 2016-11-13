package il.ac.bgu.cs.fvm.impl.stateenumeration;

import java.util.Set;

/**
 * A general interface for states of logical circuits.
 *
 */
public interface EnumeratedState {

	/**
	 * Get the set of all possible inputs to the circuit.
	 *
	 * @return A set of objects, each representing one possible input to the
	 *         circuit.
	 */
	Set<TransitionInput> getPossibleInputs();

	/**
	 * A method that computes the next state of the circuit when presented with
	 * a given input.
	 *
	 * @param input
	 *            One of the possible inputs to the system (as given by the
	 *            getPossibleInputs method)
	 * @return The next state.
	 */
	EnumeratedState nextState(TransitionInput input);

	/**
	 * Compute a list of labels for the state.
	 *
	 * @return A set of strings that may act as atomic propositions when
	 *         referring to sequences of states of the circuit.
	 */
	Set<String> getLabels();

        Object toState();
}
