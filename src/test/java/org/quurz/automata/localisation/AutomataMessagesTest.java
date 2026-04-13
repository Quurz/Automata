package org.quurz.automata.localisation;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.slf4j.LoggerFactory.getLogger;

@DisplayName("AutomataMessages")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AutomataMessagesTest {

    private static final Logger LOGGER = getLogger(AutomataMessagesTest.class);

    private Locale previous;

    @BeforeEach
    void remember_and_set_deterministic_locale() {
        previous
            = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterEach
    void restore_locale() {
        Locale.setDefault(previous);
    }

    @Test
    void unknownStartState_formats_state() {
        LOGGER.info("AutomataMessages unknownStartState() should correctly format state in message");
        final var msg
            = AutomataMessages.unknownStartState("S42");
        assertThat(msg)
            .isEqualTo("Unknown start state (startState: 'S42')");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void unknownStartState_throws_NPE_on_null() {
        LOGGER.info("AutomataMessages unknownStartState() should throw NullPointerException for null state");
        assertThatThrownBy(() -> AutomataMessages.unknownStartState(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void emptyStateSet_returns_non_empty_message() {
        LOGGER.info("AutomataMessages emptyStateSet() should return non-empty message containing 'empty' and 'state'");
        final var msg
            = AutomataMessages.emptyStateSet();
        assertThat(msg)
            .isNotNull()
            .isNotBlank()
            .containsIgnoringCase("empty") // grobe Plausibilität
            .contains("state");
    }

    @Test
    void emptyInputAlphabet_returns_non_empty_message() {
        LOGGER.info("AutomataMessages emptyInputAlphabet() should return non-empty message containing 'empty' and 'input'");
        final var msg
            = AutomataMessages.emptyInputAlphabet();
        assertThat(msg)
            .isNotNull()
            .isNotBlank()
            .containsIgnoringCase("empty")
            .contains("input");
    }

    @Test
    void emptyOutputAlphabet_returns_non_empty_message() {
        LOGGER.info("AutomataMessages emptyOutputAlphabet() should return non-empty message containing 'empty' and 'output'");
        final var msg
            = AutomataMessages.emptyOutputAlphabet();
        assertThat(msg)
            .isNotNull()
            .isNotBlank()
            .containsIgnoringCase("empty")
            .contains("output");
    }

    @Test
    void endStatesNotATrueSubsetOfStates_returns_non_empty_message() {
        LOGGER.info("AutomataMessages endStatesNotATrueSubsetOfStates() should return message about subset relationship");
        final var msg
            = AutomataMessages.endStatesNotATrueSubsetOfStates();
        assertThat(msg)
            .isNotNull()
            .isNotBlank()
            .containsIgnoringCase("subset")
            .contains("end");
    }

    @Test
    void unknownInputToken_formats_token() {
        LOGGER.info("AutomataMessages unknownInputToken() should correctly format input token in message");
        final var msg
            = AutomataMessages.unknownInputToken("X");
        assertThat(msg)
            .isEqualTo("Unknown input token (input: 'X')");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void unknownInputToken_throws_NPE_on_null() {
        LOGGER.info("AutomataMessages unknownInputToken() should throw NullPointerException for null input");
        assertThatThrownBy(() -> AutomataMessages.unknownInputToken(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void unknownState_formats_state() {
        LOGGER.info("AutomataMessages unknownState() should correctly format state in message");
        final var msg
            = AutomataMessages.unknownState("S42");
        assertThat(msg)
            .isEqualTo("Unknown state (state: S42)");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void unknownState_throws_NPE_on_null() {
        LOGGER.info("AutomataMessages unknownState() should throw NullPointerException for null state");
        assertThatThrownBy(() -> AutomataMessages.unknownState(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    void unknownOutputToken_formats_token() {
        LOGGER.info("AutomataMessages unknownOutputToken() should correctly format output token in message");
        final var msg
            = AutomataMessages.unknownOutputToken("Y");
        assertThat(msg)
            .isEqualTo("Unknown output token (output: Y)");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void unknownOutputToken_throws_NPE_on_null() {
        LOGGER.info("AutomataMessages unknownOutputToken() should throw NullPointerException for null output");
        assertThatThrownBy(() -> AutomataMessages.unknownOutputToken(null))
            .isInstanceOf(NullPointerException.class);
    }

}