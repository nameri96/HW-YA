package il.ac.bgu.cs.fvm.impl;

import il.ac.bgu.cs.fvm.FvmFacade;
import il.ac.bgu.cs.fvm.automata.Automaton;
import il.ac.bgu.cs.fvm.channelsystem.ChannelSystem;
import il.ac.bgu.cs.fvm.circuits.Circuit;
import il.ac.bgu.cs.fvm.programgraph.ActionDef;
import il.ac.bgu.cs.fvm.programgraph.ConditionDef;
import il.ac.bgu.cs.fvm.programgraph.ProgramGraph;
import il.ac.bgu.cs.fvm.transitionsystem.AlternatingSequence;
import il.ac.bgu.cs.fvm.transitionsystem.TransitionSystem;
import il.ac.bgu.cs.fvm.util.Pair;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implement the methods in this class. You may add additional classes as you
 * want, as long as they live in the {@code impl} package, or one of its 
 * sub-packages.
 */
public class FvmFacadeImpl implements FvmFacade {

    @Override
    public <S, A, P> TransitionSystem<S, A, P> createTransitionSystem() {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement createTransitionSystem
    }

    @Override
    public <S, A, P> boolean isActionDeterministic(TransitionSystem<S, A, P> ts) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isActionDeterministic
    }

    @Override
    public <S, A, P> boolean isAPDeterministic(TransitionSystem<S, A, P> ts) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isAPDeterministic
    }

    @Override
    public <S, A, P> boolean isExecution(TransitionSystem<S, A, P> ts, AlternatingSequence<S, A> e) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isExecution
    }

    @Override
    public <S, A, P> boolean isExecutionFragment(TransitionSystem<S, A, P> ts, AlternatingSequence<S, A> e) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isExecutionFragment
    }

    @Override
    public <S, A, P> boolean isInitialExecutionFragment(TransitionSystem<S, A, P> ts, AlternatingSequence<S, A> e) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isInitialExecutionFragment
    }

    @Override
    public <S, A, P> boolean isMaximalExecutionFragment(TransitionSystem<S, A, P> ts, AlternatingSequence<S, A> e) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isMaximalExecutionFragment
    }

    @Override
    public <S, A> boolean isStateTerminal(TransitionSystem<S, A, ?> ts, S s) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement isStateTerminal
    }

    @Override
    public <S> Set<S> post(TransitionSystem<S, ?, ?> ts, S s) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement post
    }

    @Override
    public <S> Set<S> post(TransitionSystem<S, ?, ?> ts, Set<S> c) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement post
    }

    @Override
    public <S, A> Set<S> post(TransitionSystem<S, A, ?> ts, S s, A a) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement post
    }

    @Override
    public <S, A> Set<S> post(TransitionSystem<S, A, ?> ts, Set<S> c, A a) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement post
    }

    @Override
    public <S> Set<S> pre(TransitionSystem<S, ?, ?> ts, S s) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement pre
    }

    @Override
    public <S> Set<S> pre(TransitionSystem<S, ?, ?> ts, Set<S> c) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement pre
    }

    @Override
    public <S, A> Set<S> pre(TransitionSystem<S, A, ?> ts, Set<S> c, A a) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement pre
    }

    @Override
    public <S, A> Set<S> pre(TransitionSystem<S, A, ?> ts, S s, A a) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement pre
    }

    @Override
    public <S, A> Set<S> reach(TransitionSystem<S, A, ?> ts) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement reach
    }

    @Override
    public <S1, S2, A, P> TransitionSystem<Pair<S1, S2>, A, P> interleave(TransitionSystem<S1, A, P> ts1, TransitionSystem<S2, A, P> ts2) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement interleave
    }

    @Override
    public <S1, S2, A, P> TransitionSystem<Pair<S1, S2>, A, P> interleave(TransitionSystem<S1, A, P> ts1, TransitionSystem<S2, A, P> ts2, Set<A> handShakingActions) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement interleave
    }

    @Override
    public <L, A> ProgramGraph<L, A> createProgramGraph() {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement createProgramGraph
    }

    @Override
    public <L1, L2, A> ProgramGraph<Pair<L1, L2>, A> interleave(ProgramGraph<L1, A> pg1, ProgramGraph<L2, A> pg2) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement interleave
    }

    @Override
    public TransitionSystem<Pair<List<Boolean>, List<Boolean>>, List<Boolean>, Object> transitionSystemFromCircuit(Circuit c) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement transitionSystemFromCircuit
    }

    @Override
    public <L, A> TransitionSystem<Pair<L, Map<String, Object>>, A, String> transitionSystemFromProgramGraph(ProgramGraph<L, A> pg, Set<ActionDef> actionDefs, Set<ConditionDef> conditionDefs) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement transitionSystemFromProgramGraph
    }

    @Override
    public <Sts, Saut, A, P> TransitionSystem<Pair<Sts, Saut>, A, P> product(TransitionSystem<Sts, A, P> ts, Automaton<Saut> aut) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement product
    }

    @Override
    public TransitionSystem transitionSystemFromChannelSystem(ChannelSystem cs) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement transitionSystemFromChannelSystem
    }

    @Override
    public ProgramGraph programGraphFromNanoPromela(String filename) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement programGraphFromNanoPromela
    }

    @Override
    public ProgramGraph programGraphFromNanoPromelaString(String nanopromela) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement programGraphFromNanoPromelaString
    }

    @Override
    public ProgramGraph programGraphFromNanoPromela(InputStream inputStream) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // TODO: Implement programGraphFromNanoPromela
    }

   

}
