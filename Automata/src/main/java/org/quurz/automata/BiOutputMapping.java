package org.quurz.automata;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun2;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

/**
 * <div>
 *     <p>
 *         Represents a two-input declarative output mapping for state machines (such as Mealy machines or transitions).
 *     </p>
 *     <p>
 *         Provides a fluent DSL entry point via {@link #from(Object, Object)} to construct mappings of the form
 *         {@code from(state, input).goTo(output)} and combine them into a functional mapping via {@link #biCombine(BiOutputMapping[])}.
 *     </p>
 * </div>
 *
 * @param <I1> the first input type (e.g., state)
 * @param <I2> the second input type (e.g., input symbol)
 * @param <O>  the output type (e.g., output symbol or next state)
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class BiOutputMapping<I1, I2, O> {

    /**
     * <div>
     *     <p>
     *         Intermediate builder step representing the pair of source inputs before defining its target output.
     *     </p>
     * </div>
     *
     * @param <I1> the first input type
     * @param <I2> the second input type
     * @param <O>  the output type
     *
     * @since 1.0.0
     *
     * @author Alexander Schell
     */
    public static final class BiGoTo<I1, I2, O> {

        private final I1 input1;
        private final I2 input2;

        private BiGoTo(final I1 input1,
                       final I2 input2) {
            this.input1
                = input1;
            this.input2
                = input2;
        }

        /**
         * <div>
         *     <p>
         *         Specifies the output value associated with the pair of source inputs.
         *     </p>
         * </div>
         *
         * @param output the mapped output value; must not be {@code null}
         * @return the constructed {@link BiOutputMapping}
         * @throws NullPointerException if {@code output} is {@code null}
         *
         * @since 1.0.0
         */
        public BiOutputMapping<I1, I2, O> goTo(final @NonNull O output) {
            Objects.requireNonNull(output, nullValue("output"));

            return new BiOutputMapping<>(this.input1, this.input2, output);
        }

    }

    /**
     * <div>
     *     <p>
     *         Initiates the creation of a {@link BiOutputMapping} starting from a pair of source inputs.
     *     </p>
     * </div>
     *
     * @param input1 the first input value; must not be {@code null}
     * @param input2 the second input value; must not be {@code null}
     * @param <I1>   the first input type
     * @param <I2>   the second input type
     * @param <O>    the output type
     * @return a {@link BiGoTo} step to complete the mapping with an output
     * @throws NullPointerException if {@code input1} or {@code input2} is {@code null}
     *
     * @since 1.0.0
     */
    public static <I1, I2, O> BiGoTo<I1, I2, O> from(final @NonNull I1 input1,
                                                     final @NonNull I2 input2) {
        Objects.requireNonNull(input1, nullValue("input1"));
        Objects.requireNonNull(input2, nullValue("input2"));

        return new BiGoTo<>(input1, input2);
    }

    private final I1 input1;
    private final I2 input2;
    private final O output;

    private BiOutputMapping(final I1 input1,
                            final I2 input2,
                            final O output) {
        this.input1
            = input1;
        this.input2
            = input2;
        this.output
            = output;
    }

    private record Input<I1, I2>(I1 input1,
                                 I2 input2) {}

    /**
     * <div>
     *     <p>
     *         Combines multiple {@link BiOutputMapping} instances into a single {@link Fun2} two-argument functional mapping.
     *     </p>
     * </div>
     *
     * @param mappings the mappings to combine; must not be {@code null}
     * @param <I1>     the first input type
     * @param <I2>     the second input type
     * @param <O>      the output type
     * @return a function mapping pairs of inputs to outputs based on the provided mappings
     * @throws NullPointerException if {@code mappings} is {@code null} or contains {@code null} elements
     * @throws IllegalStateException if duplicate input pairs are provided
     *
     * @since 1.0.0
     */
    @SafeVarargs
    public static <I1, I2, O> Fun2<I1, I2, O> biCombine(final @NonNull BiOutputMapping<I1, I2, O>... mappings) {
        Objects.requireNonNull(mappings, nullValue("mappings"));

        final Map<Input<I1, I2>, O> map
            = Arrays.stream(mappings)
                .collect(Collectors.toMap(
                    mapping -> {
                        Objects.requireNonNull(mapping, nullValue("mapping"));
                        return new Input<>(mapping.input1, mapping.input2);
                    },
                    mapping -> mapping.output
                ));

        return (i1, i2) -> map.get(new Input<>(i1, i2));
    }

}
