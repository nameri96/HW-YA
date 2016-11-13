package il.ac.bgu.cs.fvm.impl.stateenumeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import il.ac.bgu.cs.fvm.channelsystem.ChannelSystem;
import il.ac.bgu.cs.fvm.channelsystem.ParserBasedInterleavingActDef;
import il.ac.bgu.cs.fvm.programgraph.ActionDef;
import il.ac.bgu.cs.fvm.programgraph.ConditionDef;
import il.ac.bgu.cs.fvm.programgraph.PGTransition;
import il.ac.bgu.cs.fvm.programgraph.ProgramGraph;
import il.ac.bgu.cs.fvm.util.Pair;

/**
 * A class to facilitate the generation of logical circuit models.
 */
public class ChannelSystemStateEnumerator<L, A> implements StateEnumerator {

    protected ChannelSystem<L,A> cs;

    protected Set<ActionDef> actionDefs;
    protected Set<ConditionDef> conditionDefs;

    Map<ProgramGraph, Map<Object, Set<PGTransition<L, A>>>> outgoing = new HashMap<>();

    ParserBasedInterleavingActDef parserBasedInterleavingActDef = new ParserBasedInterleavingActDef();

    /**
     * @param cs
     * @param actionDefs
     * @param conditionDefs
     *
     */
    public ChannelSystemStateEnumerator(ChannelSystem<L, A> cs,
                                        Set<ActionDef> actionDefs,
                                        Set<ConditionDef> conditionDefs) {
        this.cs = cs;
        this.actionDefs = actionDefs;
        this.conditionDefs = conditionDefs;

        // Construct the outgoing map, for efficiency
        cs.getProgramGraphs().forEach((pg) -> {
            Map<Object, Set<PGTransition<L, A>>> _outgoing = new HashMap<>();
            outgoing.put(pg, _outgoing);

            pg.getTransitions().forEach((tr) -> {
                Set<PGTransition<L, A>> set = _outgoing.get(tr.getFrom());

                if (set == null) {
                    set = new HashSet<>();
                    _outgoing.put(tr.getFrom(), set);
                }

                set.add(tr);
            });
        });

    }

    /**
     * An object that represents a state of the program. Used for state
     * enumeration.
     * <p>
     */
    class ChannelSystemState implements EnumeratedState {

        /**
         * An evaluation of all variables.
         */
        Map<String, Object> eval;

        /**
         * A location in the program graph.
         */
        List<L> locations;

        /**
         * Construct a s program state.
         *
         * @param location A location in the program graph.
         * @param eval     An evaluation of all variables.
         */
        public ChannelSystemState(List<L> locations,
                                  Map<String, Object> eval) {
            super();
            this.locations = locations;
            this.eval = eval;

        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see fvm.StateInterface#getLabels()
         */
        @Override
        public Set<String> getLabels() {
            Set<String> set = new HashSet<>();
            eval.entrySet().forEach((entry) -> {
                set.add("" + entry.getKey() + " = " + entry.getValue());
            });
            return set;
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see fvm.StateInterface#getPossibleInputs()
         */
        @Override
        public Set<TransitionInput> getPossibleInputs() {
            Set<TransitionInput> set = new HashSet<>();

            for (int i = 0; i < locations.size(); i++) {
                L lc = locations.get(i);
                for (PGTransition<L, A> t : outgoing.get(cs.getProgramGraphs().get(i)).get(lc)) {
                    if (parserBasedInterleavingActDef.isOneSidedAction((String) t.getAction())) {
                        for (int j = i + 1; j < locations.size(); j++) {
                            L lc2 = locations.get(j);
                            for (PGTransition<L, A> t2 : outgoing.get(cs.getProgramGraphs().get(j)).get(lc2)) {
                                if (parserBasedInterleavingActDef.isOneSidedAction((String) t.getAction())) {

                                    String cond1 = t.getCondition().isEmpty() ? "true" : "(" + t.getCondition() + ")";
                                    String cond2 = t2.getCondition().isEmpty() ? "true" : "(" + t2.getCondition() + ")";
                                    String condition = cond1 + "&&" + cond2;
                                    String action = t.getAction() + "|" + t2.getAction();

                                    if (parserBasedInterleavingActDef.isMatchingAction(action)) {

                                        boolean condSat = ConditionDef.evaluate(conditionDefs, eval, condition);

                                        Map<String, Object> nexteval = parserBasedInterleavingActDef.effect(eval, action);

                                        if (condSat && nexteval != null) {
                                            List<L> nextlocations = new ArrayList<>(locations);
                                            nextlocations.set(i, t.getTo());
                                            nextlocations.set(j, t2.getTo());
                                            set.add(new CsTransitionInput(nextlocations, nexteval, action));
                                        }
                                    }
                                }
                            }

                        }

                    } else {

                        boolean condSat = ConditionDef.evaluate(conditionDefs, eval, t.getCondition());
                        Map<String, Object> nexteval = ActionDef.effect(actionDefs, eval, t.getAction());

                        if (condSat && nexteval != null) {
                            List<L> nextlocations = new ArrayList<>(locations);
                            nextlocations.set(i, t.getTo());
                            set.add(new CsTransitionInput(nextlocations, nexteval, t.getAction()));
                        }
                    }
                }
            }

            return set;
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see fvm.StateInterface#nextState(java.lang.Object)
         */
        @Override
        public EnumeratedState nextState(TransitionInput input) {
            CsTransitionInput in = (CsTransitionInput) input;
            return new ChannelSystemState(in.nextlocations, in.nexteval);
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            Map<String, Object> e = new HashMap<>(eval);

            String locs = null;
            for (L lc : locations) {
                if (locs == null) {
                    locs = lc.toString();
                } else {
                    locs += "," + lc.toString();
                }
            }

            return "[location=" + locs + ", eval=" + e + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((eval == null) ? 0 : eval.hashCode());
            result = prime * result + ((locations == null) ? 0 : locations.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ChannelSystemState other = (ChannelSystemState) obj;
            if (!getOuterType().equals(other.getOuterType())) {
                return false;
            }
            if (eval == null) {
                if (other.eval != null) {
                    return false;
                }
            } else if (!eval.equals(other.eval)) {
                return false;
            }
            if (locations == null) {
                if (other.locations != null) {
                    return false;
                }
            } else if (!locations.equals(other.locations)) {
                return false;
            }
            return true;
        }

        private ChannelSystemStateEnumerator getOuterType() {
            return ChannelSystemStateEnumerator.this;
        }

        @Override
        public Object toState() {
            List<L> l = new LinkedList<>(locations);
            return new Pair(l,eval);
        }

    }

    @Override
    public Set<EnumeratedState> getInitialStates() {

        Set<EnumeratedState> set = new HashSet<>();

        Map<String, Object> eval = new HashMap<>();

        for (ProgramGraph<L,A> pg : cs.getProgramGraphs()) {
            for (List<String> init : pg.getInitalizations()) {

                for (String s : init) {
                    eval = ActionDef.effect(actionDefs, eval, s);
                }
            }
        }

        for (List<L> locs : getInitLocs()) {
            set.add(new ChannelSystemState(locs, eval));
        }

        return set;
    }

    private Set<List<L>> getInitLocs() {
        Set<List<L>> ret = getInitLocs(cs.getProgramGraphs().size());
        return ret;
    }

    private Set<List<L>> getInitLocs(int recursion) {
        if (recursion == 0) {
            Set<List<L>> ret = new HashSet<>();
            ret.add(Arrays.asList());
            return ret;
        } else {
            Set<List<L>> set = new HashSet<>();
            for (List<L> locations : getInitLocs(recursion - 1)) {
                List<ProgramGraph<L, A>> programGraphs = cs.getProgramGraphs();
                for (L lc : programGraphs.get(recursion - 1).getInitialLocations()) {
                    List<L> extendedLocations = new LinkedList<>(locations);
                    extendedLocations.add(lc);
                    set.add(extendedLocations);
                }
            }
            return set;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cs == null) ? 0 : cs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ChannelSystemStateEnumerator)) {
            return false;
        }
        ChannelSystemStateEnumerator other = (ChannelSystemStateEnumerator) obj;
        if (cs == null) {
            if (other.cs != null) {
                return false;
            }
        } else if (!cs.equals(other.cs)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProgramGraphStateEnumerator [pg=" + cs + "]";
    }

}

class CsTransitionInput<L, A> implements TransitionInput {

    List<L> nextlocations;
    Map<String, Object> nexteval;
    A action;

    public CsTransitionInput(List<L> nextlocations,
                             Map<String, Object> nexteval, A action) {
        this.nextlocations = nextlocations;
        this.nexteval = nexteval;
        this.action = action;
    }

    @Override
    public A getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "CsTransitionInput [nextlocations=" + nextlocations + "]";
    }

}
