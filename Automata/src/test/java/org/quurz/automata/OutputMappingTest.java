package org.quurz.automata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@DisplayName("OutputMapping Tests")
class OutputMappingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputMappingTest.class);

    private enum State { LOCKED, UNLOCKED }
    private enum Output { ALARM, OPEN }

    @Test
    void from_null_input_throws() {
        LOGGER.info("OutputMapping.from: null input should throw NullPointerException");

        assertThatThrownBy(() -> OutputMapping.from(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("input"));
    }

    @Test
    void goTo_null_output_throws() {
        LOGGER.info("OutputMapping.goTo: null output should throw NullPointerException");

        final var step = OutputMapping.<State, Output>from(State.LOCKED);

        assertThatThrownBy(() -> step.goTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("output"));
    }

    @Test
    void combine_null_mappings_array_throws() {
        LOGGER.info("OutputMapping.combine: null mappings array should throw NullPointerException");

        assertThatThrownBy(() -> OutputMapping.combine((OutputMapping<State, Output>[]) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("mappings"));
    }

    @Test
    void combine_null_mapping_element_throws() {
        LOGGER.info("OutputMapping.combine: array containing null elements should throw NullPointerException");

        final var mapping = OutputMapping.<State, Output>from(State.LOCKED).goTo(Output.ALARM);

        assertThatThrownBy(() -> OutputMapping.combine(mapping, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("mapping"));
    }

    @Test
    void combine_duplicate_keys_throws() {
        LOGGER.info("OutputMapping.combine: duplicate keys should throw IllegalStateException");

        final var m1 = OutputMapping.<State, Output>from(State.LOCKED).goTo(Output.ALARM);
        final var m2 = OutputMapping.<State, Output>from(State.LOCKED).goTo(Output.OPEN);

        assertThatThrownBy(() -> OutputMapping.combine(m1, m2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void combine_produces_functional_mapping() {
        LOGGER.info("OutputMapping.combine: produces working functional mapping");

        final var fun = OutputMapping.combine(
                OutputMapping.<State, Output>from(State.LOCKED).goTo(Output.ALARM),
                OutputMapping.<State, Output>from(State.UNLOCKED).goTo(Output.OPEN)
        );

        assertThat(fun.apply(State.LOCKED)).isEqualTo(Output.ALARM);
        assertThat(fun.apply(State.UNLOCKED)).isEqualTo(Output.OPEN);
    }
}
