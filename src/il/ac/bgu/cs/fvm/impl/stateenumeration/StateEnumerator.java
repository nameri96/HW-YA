package il.ac.bgu.cs.fvm.impl.stateenumeration;

import java.util.Set;

public interface StateEnumerator {

	/**
	 * Get the set of all initial states.
	 * 
	 * @return A set of objects, each representing one possible initial state of
	 *         the circuit.
	 */
	Set<EnumeratedState> getInitialStates();
}
