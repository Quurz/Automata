
package org.quurz.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.quurz.foomp.base.types.Mutable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;
import static org.quurz.automata.localisation.AutomataMessages.*;
import static org.quurz.foomp.base.localisation.BaseMessages.nullResultFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.*;

/**
 * <div>
 *     <p>
 *         A thread-safe implementation of a finite state machine (FSM) that supports both
 *         Moore and Mealy machine semantics.
 *     </p>
 *     <p>
 *         This implementation maintains a current state and transitions between states based on
 *         input symbols, producing output symbols according to the configured output function.
 *         All state transitions are protected by a reentrant lock, making this class safe for
 *         concurrent use.
 *     </p>
 *     <p>
 *         Instances should be created using the static factory methods {@link #mooreMachine}
 *         or {@link #mealyMachine} rather than the constructor directly.
 *     </p>
 * </div>
 *
 * @param <S>  the type of states
 * @param <IA> the type of input symbols (Input Alphabet)
 * @param <OA> the type of output symbols (Output Alphabet)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
@Mutable
public class FiniteStateMachine<S, IA, OA>
        implements StateMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Creates a Moore machine, where outputs depend only on the current state.
     *     </p>
     *     <p>
     *         In a Moore machine, the output is determined solely by the current state,
     *         independent of the input that triggered the transition. The provided
     *         {@code outputFunction} receives only the state as input.
     *     </p>
     * </div>
     *
     * @param states             the set of all possible states; must not be {@code null} or empty,
     *                          and must not contain {@code null} elements
     * @param inputAlphabet      the set of all valid input symbols; must not be {@code null} or empty,
     *                          and must not contain {@code null} elements
     * @param outputAlphabet     the set of all valid output symbols; must not be {@code null} or empty,
     *                          and must not contain {@code null} elements
     * @param transitionFunction the function mapping (state, input) to next state; must not be {@code null}
     *                          and must not return {@code null}
     * @param outputFunction     the function mapping state to output; must not be {@code null}
     *                          and must not return {@code null}
     * @param endStates          the set of accepting/final states; must be a subset of {@code states};
     *                          must not be {@code null} (but may be empty)
     * @param initialState       the initial state; must not be {@code null} and must be contained in {@code states}
     * @param <S>                the type of states
     * @param <IA>               the type of input symbols
     * @param <OA>               the type of output symbols
     * @return a new Moore machine instance
     * @throws NullPointerException     if any parameter is {@code null}, or if any set contains {@code null} elements
     * @throws IllegalArgumentException if any set is empty, if {@code endStates} is not a subset of {@code states},
     *                                 or if {@code initialState} is not in {@code states}
     *
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mooreMachine(final @NonNull Set<S> states,
                                                                         final @NonNull Set<IA> inputAlphabet,
                                                                         final @NonNull Set<OA> outputAlphabet,
                                                                         final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                         final @NonNull Fun<S, OA> outputFunction,
                                                                         final @NonNull Set<S> endStates,
                                                                         final @NonNull S initialState) {
        requireNonNull(outputFunction, nullValue("outputFunction"));

        return new FiniteStateMachine<>(
            states,
            inputAlphabet,
            outputAlphabet,
            transitionFunction,
            s -> $_ -> outputFunction.apply(s),
            endStates,
            initialState
        );
    }

    /**
     * <div>
     *     <p>
     *         Creates a Mealy machine, where outputs depend on both the current state and the input.
     *     </p>
     *     <p>
     *         In a Mealy machine, the output is determined by both the current state and the input
     *         that triggers the transition. The provided {@code outputFunction} receives both
     *         the state and the input symbol.
     *     </p>
     * </div>
     *
     * @param states             the set of all possible states; must not be {@code null} or empty,
     *                          and must not contain {@code null} elements
     * @param inputAlphabet      the set of all valid input symbols; must not be {@code null} or empty,
     *                          and must not contain {@code null} elements
     * @param outputAlphabet     the set of all valid output symbols; must not be {@code null} or empty,
     *                          and must not contain {@code null} elements
     * @param transitionFunction the function mapping (state, input) to next state; must not be {@code null}
     *                          and must not return {@code null}
     * @param outputFunction     the function mapping (state, input) to output; must not be {@code null}
     *                          and must not return {@code null}
     * @param endStates          the set of accepting/final states; must be a subset of {@code states};
     *                          must not be {@code null} (but may be empty)
     * @param initialState       the initial state; must not be {@code null} and must be contained in {@code states}
     * @param <S>                the type of states
     * @param <IA>               the type of input symbols
     * @param <OA>               the type of output symbols
     * @return a new Mealy machine instance
     * @throws NullPointerException     if any parameter is {@code null}, or if any set contains {@code null} elements
     * @throws IllegalArgumentException if any set is empty, if {@code endStates} is not a subset of {@code states},
     *                                 or if {@code initialState} is not in {@code states}
     *
     * @since 1.0.0
     */
    public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mealyMachine(final @NonNull Set<S> states,
                                                                         final @NonNull Set<IA> inputAlphabet,
                                                                         final @NonNull Set<OA> outputAlphabet,
                                                                         final @NonNull Fun2<S, IA, S> transitionFunction,
                                                                         final @NonNull Fun2<S, IA, OA> outputFunction,
                                                                         final @NonNull Set<S> endStates,
                                                                         final @NonNull S initialState) {
        requireNonNull(outputFunction, nullValue("outputFunction"));

        return new FiniteStateMachine<>(
            states,
            inputAlphabet,
            outputAlphabet,
            transitionFunction,
            s -> ia -> outputFunction.apply(s, ia),
            endStates,
            initialState
        );
    }

    private final Set<S> states;
    private final Set<IA> inputAlphabet;
    private final Set<OA> outputAlphabet;
    private final Fun2<S, IA, S> transitionFunction;
    private final Fun<S, Fun<IA, OA>> outputFunction;
    private final Set<S> endStates;

    private final S initialState;
    private S currentState;

    protected final ReentrantLock executionLock;

    private FiniteStateMachine(final @NonNull Set<S> states,
                               final @NonNull Set<IA> inputAlphabet,
                               final @NonNull Set<OA> outputAlphabet,
                               final @NonNull Fun2<S, IA, S> transitionFunction,
                               final @NonNull Fun<S, Fun<IA, OA>> outputFunction,
                               final @NonNull Set<S> endStates,
                               final @NonNull S initialState) {
        // TODO: Ein Validator wäre nicht schlecht für all diese Prüfungen. Könnte man dann bestimmt auch an anderen stellen gebrauchen

        this.states
            = new HashSet<>(
                requireNonNullElements(
                    requireNonEmpty(
                        requireNonNull(states, nullValue("states")),
                        emptyStateSet()
                    ),
                    "states",
                    IllegalArgumentException::new
                )
        );

        this.inputAlphabet
            = new HashSet<>(
                requireNonNullElements(
                    requireNonEmpty(
                        requireNonNull(inputAlphabet, nullValue("inputAlphabet")),
                        emptyInputAlphabet()
                    ),
                    "inputAlphabet",
                    IllegalArgumentException::new
                )
        );

        this.outputAlphabet
            = new HashSet<>(
                requireNonNullElements(
                    requireNonEmpty(
                        requireNonNull(outputAlphabet, nullValue("outputAlphabet")),
                        emptyOutputAlphabet()
                    ),
                    "outputAlphabet",
                    IllegalArgumentException::new
                )
        );

        this.transitionFunction
            = requireNonNull(transitionFunction, nullValue("transitionFunction"));

        this.outputFunction
            = outputFunction;

        requireNonNull(endStates, nullValue("endStates"));
        this.endStates
            = new HashSet<>(mustBeSubSet(this.states, endStates, endStatesNotATrueSubsetOfStates()));

        this.executionLock
            = new ReentrantLock(true);

        this.initialState
            = initialState;
        this.currentState
            = requireNonNull(initialState, nullValue("initialState"));
        if (!this.states.contains(this.currentState)) {
            throw new IllegalArgumentException(unknownState(this.currentState));
        }
    }

    /**
     * <div>
     *     <p>
     *         Performs a state transition for the given input symbol and returns the associated output.
     *     </p>
     *     <p>
     *         This method is thread-safe. It computes the output based on the current state
     *         (and optionally the input, depending on machine type), then transitions to the
     *         next state as determined by the transition function.
     *     </p>
     *     <p>
     *         The entire read-transition-output cycle is executed atomically under a lock.
     *     </p>
     * </div>
     *
     * @param input the input symbol to process; must not be {@code null}
     * @return the output symbol associated with this transition
     * @throws NullPointerException     if {@code input} is {@code null}, or if the transition or output
     *                                 function returns {@code null}
     * @throws IllegalArgumentException if {@code input} is not in the input alphabet, if the transition
     *                                 function returns a state not in the state set, or if the output
     *                                 function returns an output not in the output alphabet
     *
     * @since 1.0.0
     */
    @Override
    public @NonNull OA read(final @NonNull IA input) {
        Objects.requireNonNull(input, nullValue("input"));
        if (this.inputAlphabet.contains(input)) {
            this.executionLock.lock();
            try {
                final var newState
                    = requireNonNull(this.transitionFunction.apply(currentState, input), nullResultFrom("transitionFunction"));
                if (this.states.contains(newState)) {
                    final var output
                        = Objects.requireNonNull(
                            this.outputFunction.apply(this.currentState).apply(input),
                            nullResultFrom("outputFunction")
                        );
                    this.currentState
                        = newState;
                    if (this.outputAlphabet.contains(output)) {
                        return output;
                    } else {
                        throw new IllegalArgumentException(unknownOutputToken(output));
                    }
                } else {
                    throw new IllegalArgumentException(unknownState(newState));
                }
            } finally {
                this.executionLock.unlock();
            }
        } else {
            throw new IllegalArgumentException(unknownInputToken(input));
        }
    }

    /**
     * <div>
     *     <p>
     *         Resets the state machine to its initial state.
     *     </p>
     *     <p>
     *         This method is thread-safe and can be called at any time. After calling this method,
     *         the machine behaves as if it was just constructed, with all state history cleared.
     *     </p>
     *     <p>
     *         This method supports fluent-style method chaining.
     *     </p>
     * </div>
     *
     * @return this instance for method chaining
     *
     * @since 1.0.0
     */
    public @NonNull FiniteStateMachine<S, IA, OA> reset() {
        this.executionLock.lock();
        try {
            this.currentState
                = this.initialState;
            return this;
        } finally {
            this.executionLock.unlock();
        }
    }

    /**
     * <div>
     *     <p>
     *         Checks whether the state machine is currently in an end state (accepting state).
     *     </p>
     *     <p>
     *         This method is thread-safe and returns {@code true} if the current state
     *         is contained in the set of end states, {@code false} otherwise.
     *     </p>
     * </div>
     *
     * @return {@code true} if the current state is an end state, {@code false} otherwise
     *
     * @since 1.0.0
     */
    public boolean isInEndState() {
        this.executionLock.lock();
        try {
            return this.endStates.contains(this.currentState);
        } finally {
            this.executionLock.unlock();
        }
    }

}