package org.quurz.automata.localisation;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * <div>
 *     <p>
 *         Provides localized messages for error and information handling in the Automata system.
 *         This class manages resource bundle access for internationalized strings,
 *         centralizing all message retrieval operations.
 *     </p>
 * </div>
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class AutomataMessages {

    private static final ResourceBundle RESOURCE_BUNDLE
        = ResourceBundle.getBundle("AutomataMessages", Locale.getDefault());

    private AutomataMessages() {}

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an unknown start state error.
     *     </p>
     * </div>
     *
     * @param startState the invalid start state that was encountered
     * @return a formatted error message indicating the unknown start state
     * @throws NullPointerException if {@code startState} is {@code null}
     * @since 1.0.0
     */
    public static String unknownStartState(final @NonNull Object startState) {
        Objects.requireNonNull(startState);
        return RESOURCE_BUNDLE.getString("UNKNOWN_START_STATE").formatted(startState);
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an empty state set error.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating that the state set is empty
     * @since 1.0.0
     */
    public static String emptyStateSet() {
        return RESOURCE_BUNDLE.getString("EMPTY_STATE_SET");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for {@code null} elements in the state set.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating null elements in state set
     * @since 1.0.0
     */
    public static String nullElementsInStateSet() {
        return RESOURCE_BUNDLE.getString("NULL_ELEMENTS_IN_STATE_SET");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an empty input alphabet error.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating that the input alphabet is empty
     * @since 1.0.0
     */
    public static String emptyInputAlphabet() {
        return RESOURCE_BUNDLE.getString("EMPTY_INPUT_ALPHABET");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for {@code null} elements in the input alphabet.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating null elements in input alphabet
     * @since 1.0.0
     */
    public static String nullElementsInInputAlphabet() {
        return RESOURCE_BUNDLE.getString("NULL_ELEMENTS_IN_INPUT_ALPHABET");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an empty output alphabet error.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating that the output alphabet is empty
     * @since 1.0.0
     */
    public static String emptyOutputAlphabet() {
        return RESOURCE_BUNDLE.getString("EMPTY_OUTPUT_ALPHABET");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for {@code null} elements in the output alphabet.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating null elements in output alphabet
     * @since 1.0.0
     */
    public static String nullElementsInOutputAlphabet() {
        return RESOURCE_BUNDLE.getString("NULL_ELEMENTS_IN_OUTPUT_ALPHABET");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for invalid end states configuration.
     *     </p>
     * </div>
     *
     * @return a formatted error message indicating that end states are not a true subset of states
     * @since 1.0.0
     */
    public static String endStatesNotATrueSubsetOfStates() {
        return RESOURCE_BUNDLE.getString("END_STATES_TRUE_NO_SUBSET_OF_STATES");
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an unknown input token error.
     *     </p>
     * </div>
     *
     * @param inputToken the invalid input token that was encountered
     * @return a formatted error message indicating the unknown input token
     * @throws NullPointerException if {@code inputToken} is {@code null}
     * @since 1.0.0
     */
    public static String unknownInputToken(final @NonNull Object inputToken) {
        Objects.requireNonNull(inputToken);
        return String.format(
            RESOURCE_BUNDLE.getString("UNKNOWN_INPUT_TOKEN"),
            inputToken
        );
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an unknown state error.
     *     </p>
     * </div>
     *
     * @param state the invalid state that was encountered
     * @return a formatted error message indicating the unknown state
     * @throws NullPointerException if {@code state} is {@code null}
     * @since 1.0.0
     */
    public static String unknownState(final @NonNull Object state) {
        Objects.requireNonNull(state);
        return String.format(
            RESOURCE_BUNDLE.getString("UNKNOWN_STATE"),
            state
        );
    }

    /**
     * <div>
     *     <p>
     *         Creates a localized message for an unknown output token error.
     *     </p>
     * </div>
     *
     * @param outputToken the invalid output token that was encountered
     * @return a formatted error message indicating the unknown output token
     * @throws NullPointerException if {@code outputToken} is {@code null}
     * @since 1.0.0
     */
    public static String unknownOutputToken(final @NonNull Object outputToken) {
        Objects.requireNonNull(outputToken);
        return String.format(
            RESOURCE_BUNDLE.getString("UNKNOWN_OUTPUT_TOKEN"),
            outputToken
        );
    }

}
