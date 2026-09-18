package org.quurz.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Represents a single-input declarative output mapping for state machines (such as Moore machines).
 *     </p>
 *     <p>
 *         Provides a fluent DSL entry point via {@link #from(Object)} to construct mappings of the form
 *         {@code from(state).goTo(output)} and combine them into a functional mapping via {@link #combine(OutputMapping[])}.
 *     </p>
 * </div>
 *
 * @param <I> the input or state type
 * @param <O> the output type
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class OutputMapping<I, O> {

    /**
     * <div>
     *     <p>
     *         Intermediate builder step representing the source value of a mapping before defining its target output.
     *     </p>
     * </div>
     *
     * @param <I> the input or state type
     * @param <O> the output type
     *
     * @since 1.0.0
     *
     * @author Alexander Schell
     */
    public static final class GoTo<I, O> {

        private final I input;

        private GoTo(final I input) {
            this.input = input;
        }

        /**
         * <div>
         *     <p>
         *         Specifies the output value associated with the source input.
         *     </p>
         * </div>
         *
         * @param output the mapped output value; must not be {@code null}
         * @return the constructed {@link OutputMapping}
         * @throws NullPointerException if {@code output} is {@code null}
         *
         * @since 1.0.0
         */
        public OutputMapping<I, O> goTo(final @NonNull O output) {
            Objects.requireNonNull(output, nullValue("output"));

            return new OutputMapping<>(this.input, output);
        }

    }

    /**
     * <div>
     *     <p>
     *         Initiates the creation of an {@link OutputMapping} starting from a given source input.
     *     </p>
     * </div>
     *
     * @param input the input value; must not be {@code null}
     * @param <I>   the input type
     * @param <O>   the output type
     * @return a {@link GoTo} step to complete the mapping with an output
     * @throws NullPointerException if {@code input} is {@code null}
     *
     * @since 1.0.0
     */
    public static <I, O> GoTo<I, O> from(final @NonNull I input) {
        Objects.requireNonNull(input, nullValue("input"));

        return new GoTo<>(input);
    }

    private final I input;
    private final O output;

    private OutputMapping(final I input,
                          final O output) {
        this.input
            = input;
        this.output
            = output;
    }

    /**
     * <div>
     *     <p>
     *         Combines multiple {@link OutputMapping} instances into a single {@link Fun} functional mapping.
     *     </p>
     * </div>
     *
     * @param mappings the mappings to combine; must not be {@code null}
     * @param <I>      the input type
     * @param <O>      the output type
     * @return a function mapping inputs to outputs based on the provided mappings
     * @throws NullPointerException if {@code mappings} is {@code null} or contains {@code null} elements
     * @throws IllegalStateException if duplicate input keys are provided
     *
     * @since 1.0.0
     */
    @SafeVarargs
    public static <I, O> Fun<I, O> combine(final @NonNull OutputMapping<I, O>... mappings) {
        Objects.requireNonNull(mappings, nullValue("mappings"));

        final Map<I, O> map
            = Arrays.stream(mappings)
                .collect(Collectors.toMap(
                    mapping -> Objects.requireNonNull(mapping, nullValue("mapping")).input,
                    mapping -> mapping.output
                ));

        return map::get;
    }

}
