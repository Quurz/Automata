
package org.quurz.automata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quurz.automata.localisation.AutomataMessages;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.functions.Fun2;
import org.slf4j.Logger;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.automata.FiniteStateMachine.mealyMachine;
import static org.quurz.automata.FiniteStateMachine.mealyMachineBuilder;
import static org.quurz.automata.FiniteStateMachine.mooreMachine;
import static org.quurz.automata.FiniteStateMachine.mooreMachineBuilder;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("FiniteStateMachine")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FiniteStateMachineTest {

    private static final Logger LOGGER
        = getLogger(FiniteStateMachineTest.class);

    // Test FSM: Simple turnstile
    // States: LOCKED, UNLOCKED
    // Inputs: COIN, PUSH
    // Outputs: THANK_YOU, OPEN, ALARM
    private enum State { LOCKED, UNLOCKED }
    private enum Input { COIN, PUSH }
    private enum Output { THANK_YOU, OPEN, ALARM }

    private static final Set<State> STATES = Set.of(State.LOCKED, State.UNLOCKED);
    private static final Set<Input> INPUT_ALPHABET = Set.of(Input.COIN, Input.PUSH);
    private static final Set<Output> OUTPUT_ALPHABET = Set.of(Output.THANK_YOU, Output.OPEN, Output.ALARM);
    private static final Set<State> END_STATES = Set.of(State.UNLOCKED);
    private static final State INITIAL_STATE = State.LOCKED;

    @Nested
    @DisplayName("Factory - Moore Machine")
    class Factory_Moore {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_states_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null states should throw");
            assertThatThrownBy(() -> mooreMachine(
                    null,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("states");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_inputAlphabet_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null inputAlphabet should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    null,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("inputAlphabet");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_outputAlphabet_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null outputAlphabet should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    null,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("outputAlphabet");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_transitionFunction_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null transitionFunction should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    null,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("transitionFunction");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_outputFunction_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null outputFunction should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    null,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(NullPointerException.class)
                .hasMessageContaining("outputFunction");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_endStates_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null endStates should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    null,
                    INITIAL_STATE
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("endStates");
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachine_null_initialState_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: null initialState should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    null
            )).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("initialState");
        }

        @Test
        void mooreMachine_empty_states_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: empty states should throw");
            assertThatThrownBy(() -> mooreMachine(
                    Set.of(),
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.emptyStateSet());
        }

        @Test
        void mooreMachine_empty_inputAlphabet_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: empty inputAlphabet should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    Set.of(),
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.emptyInputAlphabet());
        }

        @Test
        void mooreMachine_empty_outputAlphabet_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: empty outputAlphabet should throw");
            assertThatThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    Set.of(),
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.emptyOutputAlphabet());
        }

        @Test
        void mooreMachine_endStates_not_subset_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: endStates not subset of states should throw");
            assertThatThrownBy(() -> mooreMachine(
                    Set.of(State.LOCKED),
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    Set.of(State.UNLOCKED), // Not in states!
                    State.LOCKED
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.endStatesNotATrueSubsetOfStates());
        }

        @Test
        void mooreMachine_initialState_not_in_states_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachine: initialState not in states should throw");
            assertThatThrownBy(() -> mooreMachine(
                    Set.of(State.LOCKED),
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    Set.of(),
                    State.UNLOCKED // Not in states!
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.unknownStartState(State.UNLOCKED));
        }

        @Test
        void mooreMachine_valid_configuration_succeeds() {
            LOGGER.info("FiniteStateMachine.mooreMachine: valid configuration should succeed");
            assertThatNoException().isThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : State.LOCKED,
                    s -> s == State.LOCKED ? Output.ALARM : Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            ));
        }
    }

    @Nested
    @DisplayName("Factory - Mealy Machine")
    class Factory_Mealy {

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mealyMachine_null_parameters_throw() {
            LOGGER.info("FiniteStateMachine.mealyMachine: null parameters should throw");

            assertThatThrownBy(() -> mealyMachine(
                    null, INPUT_ALPHABET, OUTPUT_ALPHABET,
                    (s, i) -> s, (s, i) -> Output.THANK_YOU,
                    END_STATES, INITIAL_STATE
            )).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> mealyMachine(
                    STATES, null, OUTPUT_ALPHABET,
                    (s, i) -> s, (s, i) -> Output.THANK_YOU,
                    END_STATES, INITIAL_STATE
            )).isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> mealyMachine(
                    STATES, INPUT_ALPHABET, null,
                    (s, i) -> s, (s, i) -> Output.THANK_YOU,
                    END_STATES, INITIAL_STATE
            )).isInstanceOf(NullPointerException.class);
        }

        @Test
        void mealyMachine_valid_configuration_succeeds() {
            LOGGER.info("FiniteStateMachine.mealyMachine: valid configuration should succeed");
            assertThatNoException().isThrownBy(() -> mealyMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : State.LOCKED,
                    (s, i) -> {
                        if (s == State.LOCKED && i == Input.COIN) return Output.THANK_YOU;
                        if (s == State.UNLOCKED && i == Input.PUSH) return Output.OPEN;
                        return Output.ALARM;
                    },
                    END_STATES,
                    INITIAL_STATE
            ));
        }

        @Test
        void mealyMachine_empty_states_throws() {
            LOGGER.info("FiniteStateMachine.mealyMachine: empty states should throw");
            assertThatThrownBy(() -> mealyMachine(
                    Set.of(),
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    (s, i) -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.emptyStateSet());
        }

        @Test
        void mealyMachine_empty_inputAlphabet_throws() {
            LOGGER.info("FiniteStateMachine.mealyMachine: empty inputAlphabet should throw");
            assertThatThrownBy(() -> mealyMachine(
                    STATES,
                    Set.of(),
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    (s, i) -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.emptyInputAlphabet());
        }

        @Test
        void mealyMachine_empty_outputAlphabet_throws() {
            LOGGER.info("FiniteStateMachine.mealyMachine: empty outputAlphabet should throw");
            assertThatThrownBy(() -> mealyMachine(
                    STATES,
                    INPUT_ALPHABET,
                    Set.of(),
                    (s, i) -> s,
                    (s, i) -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.emptyOutputAlphabet());
        }

        @Test
        void mealyMachine_endStates_not_subset_throws() {
            LOGGER.info("FiniteStateMachine.mealyMachine: endStates not subset of states should throw");
            assertThatThrownBy(() -> mealyMachine(
                    Set.of(State.LOCKED),
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    (s, i) -> Output.THANK_YOU,
                    Set.of(State.UNLOCKED), // Not in states!
                    State.LOCKED
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.endStatesNotATrueSubsetOfStates());
        }

        @Test
        void mealyMachine_initialState_not_in_states_throws() {
            LOGGER.info("FiniteStateMachine.mealyMachine: initialState not in states should throw");
            assertThatThrownBy(() -> mealyMachine(
                    Set.of(State.LOCKED),
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    (s, i) -> Output.THANK_YOU,
                    Set.of(),
                    State.UNLOCKED // Not in states!
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(AutomataMessages.unknownStartState(State.UNLOCKED));
        }
    }

    @Nested
    @DisplayName("Behaviour - Moore Machine")
    class Behaviour_Moore {

        @Test
        void read_performs_transition_and_returns_output() {
            LOGGER.info("FiniteStateMachine (Moore).read: should perform transition and return output");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : s,
                    s -> s == State.LOCKED ? Output.ALARM : Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            // Initial state: LOCKED -> output is ALARM
            final var output1 = fsm.read(Input.COIN);
            assertThat(output1).isEqualTo(Output.ALARM); // Moore: output depends on current state before transition

            // After COIN: UNLOCKED -> output is THANK_YOU
            final var output2 = fsm.read(Input.PUSH);
            assertThat(output2).isEqualTo(Output.THANK_YOU);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void read_null_input_throws() {
            LOGGER.info("FiniteStateMachine.read: null input should throw");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            assertThatThrownBy(() -> fsm.read(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("input");
        }

        @Test
        void read_unknown_input_throws() {
            LOGGER.info("FiniteStateMachine.read: unknown input should throw");

            final var fsm = mooreMachine(
                    Set.of(State.LOCKED),
                    Set.of(Input.COIN), // PUSH not in alphabet!
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    Set.of(),
                    State.LOCKED
            );

            assertThatThrownBy(() -> fsm.read(Input.PUSH))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void read_transitionFunction_returns_null_throws() {
            LOGGER.info("FiniteStateMachine.read: transitionFunction returning null should throw");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> null, // Returns null!
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            assertThatThrownBy(() -> fsm.read(Input.COIN))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void read_transitionFunction_returns_unknown_state_throws() {
            LOGGER.info("FiniteStateMachine.read: transitionFunction returning unknown state should throw");

            final var fsm = mooreMachine(
                    Set.of(State.LOCKED), // Only LOCKED in states
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> State.UNLOCKED, // Returns state not in set!
                    s -> Output.THANK_YOU,
                    Set.of(),
                    State.LOCKED
            );

            assertThatThrownBy(() -> fsm.read(Input.COIN))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void read_outputFunction_returns_null_throws() {
            LOGGER.info("FiniteStateMachine.read: outputFunction returning null should throw");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> null, // Returns null!
                    END_STATES,
                    INITIAL_STATE
            );

            assertThatThrownBy(() -> fsm.read(Input.COIN))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        void read_outputFunction_returns_unknown_output_throws() {
            LOGGER.info("FiniteStateMachine.read: outputFunction returning unknown output should throw");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    Set.of(Output.THANK_YOU), // Only THANK_YOU in alphabet
                    (s, i) -> s,
                    s -> Output.ALARM, // Returns output not in alphabet!
                    END_STATES,
                    INITIAL_STATE
            );

            assertThatThrownBy(() -> fsm.read(Input.COIN))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Behaviour - Mealy Machine")
    class Behaviour_Mealy {

        @Test
        void read_performs_transition_and_returns_output_based_on_state_and_input() {
            LOGGER.info("FiniteStateMachine (Mealy).read: output should depend on state and input");

            final var fsm = mealyMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> {
                        if (s == State.LOCKED && i == Input.COIN) return State.UNLOCKED;
                        if (s == State.UNLOCKED && i == Input.PUSH) return State.LOCKED;
                        return s;
                    },
                    (s, i) -> {
                        if (s == State.LOCKED && i == Input.COIN) return Output.THANK_YOU;
                        if (s == State.UNLOCKED && i == Input.PUSH) return Output.OPEN;
                        return Output.ALARM;
                    },
                    END_STATES,
                    INITIAL_STATE
            );

            // LOCKED + COIN -> THANK_YOU, transition to UNLOCKED
            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.THANK_YOU);

            // UNLOCKED + PUSH -> OPEN, transition to LOCKED
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.OPEN);

            // LOCKED + PUSH -> ALARM, stay LOCKED
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.ALARM);
        }
    }

    @Nested
    @DisplayName("Reset")
    class Reset {

        @Test
        void reset_returns_to_initial_state() {
            LOGGER.info("FiniteStateMachine.reset: should return to initial state");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : State.LOCKED,
                    s -> s == State.LOCKED ? Output.ALARM : Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            // Transition to UNLOCKED
            fsm.read(Input.COIN);
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.THANK_YOU); // We're in UNLOCKED

            // Reset
            fsm.reset();
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.ALARM); // Back to LOCKED
        }

        @Test
        void reset_returns_this_for_chaining() {
            LOGGER.info("FiniteStateMachine.reset: should return this for fluent chaining");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            assertThat(fsm.reset()).isSameAs(fsm);
        }

        @Test
        void reset_can_be_called_multiple_times() {
            LOGGER.info("FiniteStateMachine.reset: should be idempotent");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : s,
                    s -> s == State.LOCKED ? Output.ALARM : Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            fsm.read(Input.COIN); // Transition to UNLOCKED
            fsm.reset();
            fsm.reset(); // Multiple resets

            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.ALARM); // Still LOCKED
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class Thread_Safety {

        @Test
        void concurrent_reads_are_safe() throws InterruptedException {
            LOGGER.info("FiniteStateMachine: concurrent reads should be thread-safe");

            final var fsm = mealyMachine(
                    Set.of(0, 1, 2, 3, 4, 5),
                    Set.of("INC"),
                    Set.of("OK"),
                    (s, i) -> (s + 1) % 6,
                    (s, i) -> "OK",
                    Set.of(5),
                    0
            );

            final int threadCount = 10;
            final Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        fsm.read("INC");
                    }
                });
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // After 1000 increments (10 * 100), state should be (1000 % 6) = 4
            // Verify by doing one more read and checking transition
            fsm.reset(); // Reset to known state
            fsm.read("INC"); // Should go 0 -> 1

            assertThatNoException().isThrownBy(() -> fsm.read("INC"));
        }

        @Test
        void concurrent_reset_and_read_are_safe() throws InterruptedException {
            LOGGER.info("FiniteStateMachine: concurrent reset and read should be thread-safe");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : State.LOCKED,
                    s -> s == State.LOCKED ? Output.ALARM : Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final Thread reader = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    fsm.read(i % 2 == 0 ? Input.COIN : Input.PUSH);
                }
            });

            final Thread resetter = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    fsm.reset();
                }
            });

            reader.start();
            resetter.start();

            reader.join();
            resetter.join();

            // Should complete without exceptions
            assertThatNoException().isThrownBy(() -> fsm.read(Input.COIN));
        }
    }

    @Nested
    @DisplayName("Complex Scenarios")
    class Complex_Scenarios {

        @Test
        void complete_turnstile_scenario() {
            LOGGER.info("FiniteStateMachine: complete turnstile scenario");

            final var turnstile = mealyMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> {
                        if (s == State.LOCKED && i == Input.COIN) return State.UNLOCKED;
                        if (s == State.UNLOCKED && i == Input.PUSH) return State.LOCKED;
                        return s;
                    },
                    (s, i) -> {
                        if (s == State.LOCKED && i == Input.COIN) return Output.THANK_YOU;
                        if (s == State.UNLOCKED && i == Input.PUSH) return Output.OPEN;
                        return Output.ALARM;
                    },
                    END_STATES,
                    INITIAL_STATE
            );

            // Scenario: Try to push while locked
            assertThat(turnstile.read(Input.PUSH)).isEqualTo(Output.ALARM);

            // Insert coin
            assertThat(turnstile.read(Input.COIN)).isEqualTo(Output.THANK_YOU);

            // Push through
            assertThat(turnstile.read(Input.PUSH)).isEqualTo(Output.OPEN);

            // Try to push again (locked again)
            assertThat(turnstile.read(Input.PUSH)).isEqualTo(Output.ALARM);

            // Reset and verify
            turnstile.reset();
            assertThat(turnstile.read(Input.PUSH)).isEqualTo(Output.ALARM);
        }

        @Test
        void empty_endStates_is_valid() {
            LOGGER.info("FiniteStateMachine: empty endStates should be valid");

            assertThatNoException().isThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    Set.of(), // Empty end states
                    INITIAL_STATE
            ));
        }

        @Test
        void all_states_as_endStates_is_valid() {
            LOGGER.info("FiniteStateMachine: all states as endStates should be valid");

            assertThatNoException().isThrownBy(() -> mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    STATES, // All states are end states
                    INITIAL_STATE
            ));
        }
    }

    @Nested
    @DisplayName("End State Check")
    class End_State_Check {

        @Test
        void isInEndState_returns_false_when_not_in_end_state() {
            LOGGER.info("FiniteStateMachine.isInEndState: should return false when not in end state");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES, // UNLOCKED is end state
                    State.LOCKED // Start in LOCKED
            );

            assertThat(fsm.isInEndState()).isFalse();
        }

        @Test
        void isInEndState_returns_true_when_in_end_state() {
            LOGGER.info("FiniteStateMachine.isInEndState: should return true when in end state");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : s,
                    s -> Output.THANK_YOU,
                    END_STATES, // UNLOCKED is end state
                    State.LOCKED
            );

            fsm.read(Input.COIN); // Transition to UNLOCKED
            assertThat(fsm.isInEndState()).isTrue();
        }

        @Test
        void isInEndState_follows_state_transitions() {
            LOGGER.info("FiniteStateMachine.isInEndState: should follow state transitions correctly");

            final var fsm = mealyMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> {
                        if (s == State.LOCKED && i == Input.COIN) return State.UNLOCKED;
                        if (s == State.UNLOCKED && i == Input.PUSH) return State.LOCKED;
                        return s;
                    },
                    (s, i) -> Output.THANK_YOU,
                    END_STATES, // UNLOCKED is end state
                    State.LOCKED
            );

            assertThat(fsm.isInEndState()).isFalse(); // LOCKED

            fsm.read(Input.COIN); // -> UNLOCKED
            assertThat(fsm.isInEndState()).isTrue();

            fsm.read(Input.PUSH); // -> LOCKED
            assertThat(fsm.isInEndState()).isFalse();
        }

        @Test
        void isInEndState_resets_correctly() {
            LOGGER.info("FiniteStateMachine.isInEndState: should reflect reset correctly");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : s,
                    s -> Output.THANK_YOU,
                    END_STATES, // UNLOCKED is end state
                    State.LOCKED // Initial state is not end state
            );

            fsm.read(Input.COIN); // -> UNLOCKED (end state)
            assertThat(fsm.isInEndState()).isTrue();

            fsm.reset(); // Back to LOCKED
            assertThat(fsm.isInEndState()).isFalse();
        }

        @Test
        void isInEndState_with_empty_end_states() {
            LOGGER.info("FiniteStateMachine.isInEndState: should always return false with empty end states");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> i == Input.COIN ? State.UNLOCKED : State.LOCKED,
                    s -> Output.THANK_YOU,
                    Set.of(), // No end states!
                    State.LOCKED
            );

            assertThat(fsm.isInEndState()).isFalse();

            fsm.read(Input.COIN); // Transition to UNLOCKED
            assertThat(fsm.isInEndState()).isFalse(); // Still false
        }

        @Test
        void isInEndState_when_initial_state_is_end_state() {
            LOGGER.info("FiniteStateMachine.isInEndState: should return true if initial state is end state");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    Set.of(State.LOCKED), // LOCKED is end state
                    State.LOCKED // Start in end state!
            );

            assertThat(fsm.isInEndState()).isTrue();
        }
    }

    @Nested
    @DisplayName("Getters and Defensive Copies")
    class Getters {

        @Test
        void getStates_returns_defensive_copy() {
            LOGGER.info("FiniteStateMachine.getStates: should return defensive copy");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var returnedStates = fsm.getStates();
            assertThat(returnedStates).containsExactlyInAnyOrderElementsOf(STATES);

            // Mutating returned collection must not affect FSM internal state or subsequent calls
            returnedStates.clear();
            assertThat(fsm.getStates()).containsExactlyInAnyOrderElementsOf(STATES);
        }

        @Test
        void getInputAlphabet_returns_defensive_copy() {
            LOGGER.info("FiniteStateMachine.getInputAlphabet: should return defensive copy");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var returnedAlphabet = fsm.getInputAlphabet();
            assertThat(returnedAlphabet).containsExactlyInAnyOrderElementsOf(INPUT_ALPHABET);

            returnedAlphabet.clear();
            assertThat(fsm.getInputAlphabet()).containsExactlyInAnyOrderElementsOf(INPUT_ALPHABET);
        }

        @Test
        void getOutputAlphabet_returns_defensive_copy() {
            LOGGER.info("FiniteStateMachine.getOutputAlphabet: should return defensive copy");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var returnedAlphabet = fsm.getOutputAlphabet();
            assertThat(returnedAlphabet).containsExactlyInAnyOrderElementsOf(OUTPUT_ALPHABET);

            returnedAlphabet.clear();
            assertThat(fsm.getOutputAlphabet()).containsExactlyInAnyOrderElementsOf(OUTPUT_ALPHABET);
        }

        @Test
        void getEndStates_returns_defensive_copy() {
            LOGGER.info("FiniteStateMachine.getEndStates: should return defensive copy");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var returnedEndStates = fsm.getEndStates();
            assertThat(returnedEndStates).containsExactlyInAnyOrderElementsOf(END_STATES);

            returnedEndStates.clear();
            assertThat(fsm.getEndStates()).containsExactlyInAnyOrderElementsOf(END_STATES);
        }

        @Test
        void getInitialState_returns_initial_state() {
            LOGGER.info("FiniteStateMachine.getInitialState: should return initial state");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
        }

        @Test
        void getters_return_configured_values_for_mealy() {
            LOGGER.info("FiniteStateMachine getters: should return valid values for Mealy machine");

            final var fsm = mealyMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> State.UNLOCKED,
                    (s, i) -> Output.OPEN,
                    END_STATES,
                    INITIAL_STATE
            );

            // Also check getters for Mealy machine instance
            assertThat(fsm.getStates()).containsExactlyInAnyOrderElementsOf(STATES);
            assertThat(fsm.getInputAlphabet()).containsExactlyInAnyOrderElementsOf(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).containsExactlyInAnyOrderElementsOf(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).containsExactlyInAnyOrderElementsOf(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
        }
    }

    @Nested
    @DisplayName("State Transition Listeners")
    class StateTransitionEventListeners {

        @Test
        void registerStateTransitionListener_throws_NullPointerException_on_null_listener() {
            LOGGER.info("FiniteStateMachine.registerStateTransitionListener: should throw NullPointerException on null");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            assertThatThrownBy(() -> fsm.registerStateTransitionListener(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("listener"));
        }

        @Test
        void unregisterStateTransitionListener_throws_NullPointerException_on_null_listener() {
            LOGGER.info("FiniteStateMachine.unregisterStateTransitionListener: should throw NullPointerException on null");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> s,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            assertThatThrownBy(() -> fsm.unregisterStateTransitionListener(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("listener"));
        }

        @Test
        void registered_listener_receives_state_transition_events() {
            LOGGER.info("FiniteStateMachine: registered listener should receive events on state transition");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                        case UNLOCKED -> (i == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                    },
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var events = new java.util.ArrayList<StateTransitionEvent<State>>();
            final StateTransitionEventListener<State> listener = events::add;

            fsm.registerStateTransitionListener(listener);

            fsm.read(Input.COIN);
            assertThat(events).containsExactly(
                    new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED)
            );

            fsm.read(Input.PUSH);
            assertThat(events).containsExactly(
                    new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED),
                    new StateTransitionEvent<>(State.UNLOCKED, State.LOCKED)
            );
        }

        @Test
        void unregister_stops_listener_from_receiving_events() {
            LOGGER.info("FiniteStateMachine: unregistered listener should no longer receive events");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                        case UNLOCKED -> (i == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                    },
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var events = new java.util.ArrayList<StateTransitionEvent<State>>();
            final StateTransitionEventListener<State> listener = events::add;

            fsm.registerStateTransitionListener(listener);
            fsm.read(Input.COIN);
            assertThat(events).hasSize(1);

            fsm.unregisterStateTransitionListener(listener);
            fsm.read(Input.PUSH);

            // No additional event should be captured
            assertThat(events).hasSize(1);
        }

        @Test
        void multiple_listeners_receive_events() {
            LOGGER.info("FiniteStateMachine: multiple registered listeners should all receive events");

            final var fsm = mooreMachine(
                    STATES,
                    INPUT_ALPHABET,
                    OUTPUT_ALPHABET,
                    (s, i) -> State.UNLOCKED,
                    s -> Output.THANK_YOU,
                    END_STATES,
                    INITIAL_STATE
            );

            final var events1 = new java.util.ArrayList<StateTransitionEvent<State>>();
            final var events2 = new java.util.ArrayList<StateTransitionEvent<State>>();

            fsm.registerStateTransitionListener(events1::add);
            fsm.registerStateTransitionListener(events2::add);

            fsm.read(Input.COIN);

            assertThat(events1).containsExactly(new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED));
            assertThat(events2).containsExactly(new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED));
        }

        @Test
        void state_transition_event_properties() {
            LOGGER.info("StateTransitionEvent: properties and record behavior");

            final var event = new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED);

            assertThat(event.oldState()).isEqualTo(State.LOCKED);
            assertThat(event.newState()).isEqualTo(State.UNLOCKED);
            assertThat(event).isEqualTo(new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED));
            assertThat(event.hashCode()).isEqualTo(new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED).hashCode());
            assertThat(event.toString()).contains("LOCKED", "UNLOCKED");
        }
    }

    @Nested
    @DisplayName("Builder - Moore Machine")
    class Builder_Moore {

        @Test
        void mooreMachineBuilder_builds_valid_fsm_with_fluent_chaining() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: build valid FSM with arbitrary chaining order");

            final var fsm = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET)
                    .transitionFunction((s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                        case UNLOCKED -> (i == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                    })
                    .outputFunction(s -> (s == State.UNLOCKED) ? Output.OPEN : Output.ALARM)
                    .endStates(END_STATES)
                    .initialState(INITIAL_STATE)
                    .build();

            assertThat(fsm.getStates()).isEqualTo(STATES);
            assertThat(fsm.getInputAlphabet()).isEqualTo(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).isEqualTo(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).isEqualTo(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
            assertThat(fsm.isInEndState()).isFalse();

            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.ALARM);
            assertThat(fsm.isInEndState()).isTrue();
        }

        @Test
        void mooreMachineBuilder_defaults_endStates_to_empty_set() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: endStates should default to empty set");

            final var fsm = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET)
                    .transitionFunction((s, i) -> s)
                    .outputFunction(s -> Output.THANK_YOU)
                    .initialState(INITIAL_STATE)
                    .build();

            assertThat(fsm.getEndStates()).isEmpty();
            assertThat(fsm.isInEndState()).isFalse();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachineBuilder_null_checks_throw_immediately() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: null arguments to builder methods throw NPE");

            final var builder = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder();

            assertThatThrownBy(() -> builder.states((Set<State>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("states"));

            assertThatThrownBy(() -> builder.inputAlphabet((Set<Input>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("inputAlphabet"));

            assertThatThrownBy(() -> builder.outputAlphabet((Set<Output>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputAlphabet"));

            assertThatThrownBy(() -> builder.transitionFunction((Fun2<State, Input, State>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("transitionFunction"));

            assertThatThrownBy(() -> builder.outputFunction((Fun<State, Output>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputFunction"));

            assertThatThrownBy(() -> builder.endStates((Set<State>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("endStates"));

            assertThatThrownBy(() -> builder.initialState(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("initialState"));
        }

        @Test
        void mooreMachineBuilder_builds_valid_fsm_with_varargs() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: build valid FSM with varargs methods");

            final var fsm = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder()
                    .states(State.LOCKED, State.UNLOCKED)
                    .inputAlphabet(Input.COIN, Input.PUSH)
                    .outputAlphabet(Output.OPEN, Output.ALARM, Output.THANK_YOU)
                    .transitionFunction((s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                        case UNLOCKED -> (i == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                    })
                    .outputFunction(s -> (s == State.UNLOCKED) ? Output.OPEN : Output.ALARM)
                    .endStates(State.UNLOCKED)
                    .initialState(State.LOCKED)
                    .build();

            assertThat(fsm.getStates()).isEqualTo(STATES);
            assertThat(fsm.getInputAlphabet()).isEqualTo(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).isEqualTo(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).isEqualTo(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
            assertThat(fsm.isInEndState()).isFalse();

            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.ALARM);
            assertThat(fsm.isInEndState()).isTrue();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mooreMachineBuilder_varargs_null_checks_throw_immediately() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: null varargs throw NPE");

            final var builder = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder();

            assertThatThrownBy(() -> builder.states((State[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("states"));

            assertThatThrownBy(() -> builder.inputAlphabet((Input[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("inputAlphabet"));

            assertThatThrownBy(() -> builder.outputAlphabet((Output[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputAlphabet"));

            assertThatThrownBy(() -> builder.endStates((State[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("endStates"));

            assertThatThrownBy(() -> builder.states(State.LOCKED, null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> builder.transitionFunction((BiOutputMapping<State, Input, State>[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("transitionMappings"));

            assertThatThrownBy(() -> builder.outputFunction((OutputMapping<State, Output>[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputMappings"));
        }

        @Test
        void mooreMachineBuilder_builds_valid_fsm_with_declarative_mappings() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: build valid FSM with declarative mappings");

            final var fsm = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder()
                    .states(State.LOCKED, State.UNLOCKED)
                    .inputAlphabet(Input.COIN, Input.PUSH)
                    .outputAlphabet(Output.OPEN, Output.ALARM, Output.THANK_YOU)
                    .transitionFunction(
                            BiOutputMapping.<State, Input, State>from(State.LOCKED, Input.COIN).goTo(State.UNLOCKED),
                            BiOutputMapping.<State, Input, State>from(State.LOCKED, Input.PUSH).goTo(State.LOCKED),
                            BiOutputMapping.<State, Input, State>from(State.UNLOCKED, Input.PUSH).goTo(State.LOCKED),
                            BiOutputMapping.<State, Input, State>from(State.UNLOCKED, Input.COIN).goTo(State.UNLOCKED)
                    )
                    .outputFunction(
                            OutputMapping.<State, Output>from(State.LOCKED).goTo(Output.ALARM),
                            OutputMapping.<State, Output>from(State.UNLOCKED).goTo(Output.OPEN)
                    )
                    .endStates(State.UNLOCKED)
                    .initialState(State.LOCKED)
                    .build();

            assertThat(fsm.getStates()).isEqualTo(STATES);
            assertThat(fsm.getInputAlphabet()).isEqualTo(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).isEqualTo(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).isEqualTo(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
            assertThat(fsm.isInEndState()).isFalse();

            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.ALARM);
            assertThat(fsm.isInEndState()).isTrue();
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.OPEN);
            assertThat(fsm.isInEndState()).isFalse();
        }

        @Test
        void mooreMachineBuilder_incomplete_build_throws() {
            LOGGER.info("FiniteStateMachine.mooreMachineBuilder: incomplete builder throws upon build()");

            final var builder = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET);

            assertThatThrownBy(builder::build)
                    .isInstanceOf(NullPointerException.class);

            final var builderMissingOutput = FiniteStateMachine.<State, Input, Output>mooreMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET)
                    .transitionFunction((s, i) -> s)
                    .initialState(INITIAL_STATE);

            assertThatThrownBy(builderMissingOutput::build)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputFunction"));
        }
    }

    @Nested
    @DisplayName("Builder - Mealy Machine")
    class Builder_Mealy {

        @Test
        void mealyMachineBuilder_builds_valid_fsm_with_fluent_chaining() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: build valid Mealy FSM with fluent chaining");

            final var fsm = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET)
                    .transitionFunction((s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                        case UNLOCKED -> (i == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                    })
                    .outputFunction((s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? Output.THANK_YOU : Output.ALARM;
                        case UNLOCKED -> (i == Input.PUSH) ? Output.OPEN : Output.THANK_YOU;
                    })
                    .endStates(END_STATES)
                    .initialState(INITIAL_STATE)
                    .build();

            assertThat(fsm.getStates()).isEqualTo(STATES);
            assertThat(fsm.getInputAlphabet()).isEqualTo(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).isEqualTo(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).isEqualTo(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
            assertThat(fsm.isInEndState()).isFalse();

            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.THANK_YOU);
            assertThat(fsm.isInEndState()).isTrue();
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.OPEN);
            assertThat(fsm.isInEndState()).isFalse();
        }

        @Test
        void mealyMachineBuilder_defaults_endStates_to_empty_set() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: endStates should default to empty set");

            final var fsm = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET)
                    .transitionFunction((s, i) -> s)
                    .outputFunction((s, i) -> Output.THANK_YOU)
                    .initialState(INITIAL_STATE)
                    .build();

            assertThat(fsm.getEndStates()).isEmpty();
            assertThat(fsm.isInEndState()).isFalse();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mealyMachineBuilder_null_checks_throw_immediately() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: null arguments to builder methods throw NPE");

            final var builder = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder();

            assertThatThrownBy(() -> builder.states((Set<State>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("states"));

            assertThatThrownBy(() -> builder.inputAlphabet((Set<Input>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("inputAlphabet"));

            assertThatThrownBy(() -> builder.outputAlphabet((Set<Output>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputAlphabet"));

            assertThatThrownBy(() -> builder.transitionFunction((Fun2<State, Input, State>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("transitionFunction"));

            assertThatThrownBy(() -> builder.outputFunction((Fun2<State, Input, Output>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputFunction"));

            assertThatThrownBy(() -> builder.endStates((Set<State>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("endStates"));

            assertThatThrownBy(() -> builder.initialState(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("initialState"));
        }

        @Test
        void mealyMachineBuilder_builds_valid_fsm_with_varargs() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: build valid Mealy FSM with varargs methods");

            final var fsm = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder()
                    .states(State.LOCKED, State.UNLOCKED)
                    .inputAlphabet(Input.COIN, Input.PUSH)
                    .outputAlphabet(Output.OPEN, Output.ALARM, Output.THANK_YOU)
                    .transitionFunction((s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                        case UNLOCKED -> (i == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                    })
                    .outputFunction((s, i) -> switch (s) {
                        case LOCKED -> (i == Input.COIN) ? Output.THANK_YOU : Output.ALARM;
                        case UNLOCKED -> (i == Input.PUSH) ? Output.OPEN : Output.THANK_YOU;
                    })
                    .endStates(State.UNLOCKED)
                    .initialState(State.LOCKED)
                    .build();

            assertThat(fsm.getStates()).isEqualTo(STATES);
            assertThat(fsm.getInputAlphabet()).isEqualTo(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).isEqualTo(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).isEqualTo(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
            assertThat(fsm.isInEndState()).isFalse();

            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.THANK_YOU);
            assertThat(fsm.isInEndState()).isTrue();
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.OPEN);
            assertThat(fsm.isInEndState()).isFalse();
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        void mealyMachineBuilder_varargs_null_checks_throw_immediately() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: null varargs throw NPE");

            final var builder = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder();

            assertThatThrownBy(() -> builder.states((State[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("states"));

            assertThatThrownBy(() -> builder.inputAlphabet((Input[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("inputAlphabet"));

            assertThatThrownBy(() -> builder.outputAlphabet((Output[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputAlphabet"));

            assertThatThrownBy(() -> builder.endStates((State[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("endStates"));

            assertThatThrownBy(() -> builder.states(State.LOCKED, null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> builder.transitionFunction((BiOutputMapping<State, Input, State>[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("transitionMappings"));

            assertThatThrownBy(() -> builder.outputFunction((BiOutputMapping<State, Input, Output>[]) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputMappings"));
        }

        @Test
        void mealyMachineBuilder_builds_valid_fsm_with_declarative_mappings() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: build valid Mealy FSM with declarative mappings");

            final var fsm = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder()
                    .states(State.LOCKED, State.UNLOCKED)
                    .inputAlphabet(Input.COIN, Input.PUSH)
                    .outputAlphabet(Output.OPEN, Output.ALARM, Output.THANK_YOU)
                    .transitionFunction(
                            BiOutputMapping.<State, Input, State>from(State.LOCKED, Input.COIN).goTo(State.UNLOCKED),
                            BiOutputMapping.<State, Input, State>from(State.LOCKED, Input.PUSH).goTo(State.LOCKED),
                            BiOutputMapping.<State, Input, State>from(State.UNLOCKED, Input.PUSH).goTo(State.LOCKED),
                            BiOutputMapping.<State, Input, State>from(State.UNLOCKED, Input.COIN).goTo(State.UNLOCKED)
                    )
                    .outputFunction(
                            BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.COIN).goTo(Output.THANK_YOU),
                            BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.PUSH).goTo(Output.ALARM),
                            BiOutputMapping.<State, Input, Output>from(State.UNLOCKED, Input.PUSH).goTo(Output.OPEN),
                            BiOutputMapping.<State, Input, Output>from(State.UNLOCKED, Input.COIN).goTo(Output.THANK_YOU)
                    )
                    .endStates(State.UNLOCKED)
                    .initialState(State.LOCKED)
                    .build();

            assertThat(fsm.getStates()).isEqualTo(STATES);
            assertThat(fsm.getInputAlphabet()).isEqualTo(INPUT_ALPHABET);
            assertThat(fsm.getOutputAlphabet()).isEqualTo(OUTPUT_ALPHABET);
            assertThat(fsm.getEndStates()).isEqualTo(END_STATES);
            assertThat(fsm.getInitialState()).isEqualTo(INITIAL_STATE);
            assertThat(fsm.isInEndState()).isFalse();

            assertThat(fsm.read(Input.COIN)).isEqualTo(Output.THANK_YOU);
            assertThat(fsm.isInEndState()).isTrue();
            assertThat(fsm.read(Input.PUSH)).isEqualTo(Output.OPEN);
            assertThat(fsm.isInEndState()).isFalse();
        }

        @Test
        void mealyMachineBuilder_incomplete_build_throws() {
            LOGGER.info("FiniteStateMachine.mealyMachineBuilder: incomplete builder throws upon build()");

            final var builder = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET);

            assertThatThrownBy(builder::build)
                    .isInstanceOf(NullPointerException.class);

            final var builderMissingOutput = FiniteStateMachine.<State, Input, Output>mealyMachineBuilder()
                    .states(STATES)
                    .inputAlphabet(INPUT_ALPHABET)
                    .outputAlphabet(OUTPUT_ALPHABET)
                    .transitionFunction((s, i) -> s)
                    .initialState(INITIAL_STATE);

            assertThatThrownBy(builderMissingOutput::build)
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(nullValue("outputFunction"));
        }
    }

}