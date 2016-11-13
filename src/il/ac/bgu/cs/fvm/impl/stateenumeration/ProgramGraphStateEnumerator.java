package il.ac.bgu.cs.fvm.impl.stateenumeration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import il.ac.bgu.cs.fvm.programgraph.ActionDef;
import il.ac.bgu.cs.fvm.programgraph.ConditionDef;
import il.ac.bgu.cs.fvm.programgraph.PGTransition;
import il.ac.bgu.cs.fvm.programgraph.ProgramGraph;
import il.ac.bgu.cs.fvm.util.Pair;

/**
 * A class to facilitate the generation of logical circuit models.
 * <p>
 * @param <L>
 * @param <A>
 */
public class ProgramGraphStateEnumerator<L, A> implements StateEnumerator {

    protected ProgramGraph<L, A> pg;
    protected Set<ActionDef> actionDefs;
    protected Set<ConditionDef> conditionDefs;

    /**
     * @param pg
     * @param actionDefs
     * @param conditionDefs
     */
    public ProgramGraphStateEnumerator(ProgramGraph pg,
                                       Set<ActionDef> actionDefs,
                                       Set<ConditionDef> conditionDefs) {
        this.pg = pg;
        this.actionDefs = actionDefs;
        this.conditionDefs = conditionDefs;
    }

    /**
     * An object that represents a state of the program. Used for state
     * enumeration.
     * <p>
     */
    class ProgramState implements EnumeratedState {

        /**
         * An evaluation of all variables.
         */
        Map<String, Object> eval;

        /**
         * A location in the program graph.
         */
        L location;

        /**
         * Construct a s program state.
         *
         * @param location A location in the program graph.
         * @param eval     An evaluation of all variables.
         */
        public ProgramState(L location, Map<String, Object> eval) {
            super();
            this.location = location;
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
            for (Map.Entry<String, Object> entry : eval.entrySet()) {
                set.add("" + entry.getKey() + " = " + entry.getValue());
            }
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

            for (PGTransition<L, A> t : pg.getTransitions()) {
                if (t.getFrom().equals(location)) {
                    boolean condSat = ConditionDef.evaluate(conditionDefs, eval, t.getCondition());
                    boolean actSat = ActionDef.effect(actionDefs, eval, t.getAction()) != null;

                    if (condSat && actSat) {
                        set.add(new MyTransitionInput(t));
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
            PGTransition<L, A> tran = ((MyTransitionInput) input).tran;
            return new ProgramState(tran.getTo(), ActionDef.effect(actionDefs, eval, tran.getAction()));
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            Map<String, Object> e = new HashMap<String, Object>(eval);
            return "[location=" + location + ", eval=" + e + "]";
        }

        @Override
        public Object toState() {
            Map<String, Object> e = new HashMap<String, Object>(eval);
            return new Pair(location, e);
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((eval == null) ? 0 : eval.hashCode());
            result = prime * result + ((location == null) ? 0 : location.hashCode());
            return result;
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
         */
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
            ProgramState other = (ProgramState) obj;
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
            if (location == null) {
                if (other.location != null) {
                    return false;
                }
            } else if (!location.equals(other.location)) {
                return false;
            }
            return true;
        }

        private ProgramGraphStateEnumerator getOuterType() {
            return ProgramGraphStateEnumerator.this;
        }

    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see fvm.StateEnumerator#getInitialStates()
     */
 /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * il.ac.bgu.cs.fvm.programgraph.ProgramGraphInterface#getInitialStates()
     */
    @Override
    public Set<EnumeratedState> getInitialStates() {

        Set<EnumeratedState> set = new HashSet<>();

        for (L loc : pg.getInitialLocations()) {
            Map<String, Object> eval = new HashMap<String, Object>();

            if (pg.getInitalizations().size() == 0) {
                set.add(new ProgramState(loc, eval));
            } else {
                for (List<String> init : pg.getInitalizations()) {

                    for (String s : init) {
                        eval = ActionDef.effect(actionDefs, eval, s);
                    }

                    set.add(new ProgramState(loc, eval));
                }
            }

        }

        return set;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pg == null) ? 0 : pg.hashCode());
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
        if (!(obj instanceof ProgramGraphStateEnumerator)) {
            return false;
        }
        ProgramGraphStateEnumerator other = (ProgramGraphStateEnumerator) obj;
        if (pg == null) {
            if (other.pg != null) {
                return false;
            }
        } else if (!pg.equals(other.pg)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProgramGraphStateEnumerator [pg=" + pg + "]";
    }

}

class MyTransitionInput implements TransitionInput {

    PGTransition tran;

    public MyTransitionInput(PGTransition tran) {
        this.tran = tran;
    }

    @Override
    public Object getAction() {
        return tran.getAction();
    }

}
