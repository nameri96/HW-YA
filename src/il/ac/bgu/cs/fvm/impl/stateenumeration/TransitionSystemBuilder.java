package il.ac.bgu.cs.fvm.impl.stateenumeration;

import il.ac.bgu.cs.fvm.FvmFacade;
import java.util.HashSet;
import java.util.Set;

import il.ac.bgu.cs.fvm.transitionsystem.Transition;
import il.ac.bgu.cs.fvm.transitionsystem.TransitionSystem;

public class TransitionSystemBuilder {

    static public TransitionSystem transitionSystemFromStateEnumerator(
            StateEnumerator se) {
        TransitionSystem ts = FvmFacade.createInstance().createTransitionSystem();

        Set<EnumeratedState> statesSet = new HashSet<>(se.getInitialStates());

        // Add the initial states
        for (EnumeratedState s : statesSet) {
            Object state = s.toState();
            ts.addState(state);
            ts.addInitialState(state);
        }

        // Traverse the state space and create the states and transitions
        Set<EnumeratedState> toAdd = new HashSet<>(statesSet);

        do {

            Set<EnumeratedState> lastAdded = new HashSet<>(toAdd);
            toAdd.clear();

            for (EnumeratedState s : lastAdded) {
                for (TransitionInput input : s.getPossibleInputs()) {

                    EnumeratedState ns = s.nextState(input);
                    if (!statesSet.contains(ns)) {
                        ts.addState(ns.toState());
                        toAdd.add(ns);
                    }

                    ts.addAction(input.getAction());
                    ts.addTransition(new Transition(s.toState(), input.getAction(), ns.toState()));
                }
            }

            statesSet.addAll(toAdd);
        } while (!toAdd.isEmpty());

        // Create the labeling function.
        for (EnumeratedState s : statesSet) {
            for (String lbl : s.getLabels()) {
                ts.addAtomicProposition(lbl);
                ts.addToLabel(s.toState(), lbl);
            }
        }

        return ts;

    }

}
