package org.quurz.automata;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Represents a generic state machine with states, inputs and outputs.
 *     </p>
 * </div>
 *
 * @param <S>  the type of states
 * @param <IA> the type of inputs (Input Alphabet)
 * @param <OA> the type of outputs (Output Alphabet)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public interface StateMachine<S, IA, OA> {

    /**
     * <div>
     *     <p>
     *         Performs a transition for the given input symbol and returns the associated output.
     *     </p>
     * </div>
     *
     * @param input the input symbol
     * @return the output associated with the input
     * @throws NullPointerException     if {@code input} is null
     * @throws IllegalArgumentException if the input symbol is unknown
     *
     * @since 1.0.0
     */
    @NonNull
    OA read(final @NonNull IA input);

}
