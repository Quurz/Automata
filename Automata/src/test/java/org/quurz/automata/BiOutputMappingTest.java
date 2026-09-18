package org.quurz.automata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;

@DisplayName("BiOutputMapping Tests")
class BiOutputMappingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiOutputMappingTest.class);

    private enum State { LOCKED, UNLOCKED }
    private enum Input { COIN, PUSH }
    private enum Output { ALARM, OPEN, THANK_YOU }

    @Test
    void from_null_input_throws() {
        LOGGER.info("BiOutputMapping.from: null input should throw NullPointerException");

        assertThatThrownBy(() -> BiOutputMapping.from(null, Input.COIN))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("input1"));

        assertThatThrownBy(() -> BiOutputMapping.from(State.LOCKED, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("input2"));
    }

    @Test
    void goTo_null_output_throws() {
        LOGGER.info("BiOutputMapping.goTo: null output should throw NullPointerException");

        final var step = BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.COIN);

        assertThatThrownBy(() -> step.goTo(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("output"));
    }

    @Test
    void biCombine_null_mappings_array_throws() {
        LOGGER.info("BiOutputMapping.biCombine: null mappings array should throw NullPointerException");

        assertThatThrownBy(() -> BiOutputMapping.biCombine((BiOutputMapping<State, Input, Output>[]) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("mappings"));
    }

    @Test
    void biCombine_null_mapping_element_throws() {
        LOGGER.info("BiOutputMapping.biCombine: array containing null elements should throw NullPointerException");

        final var mapping = BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.COIN).goTo(Output.THANK_YOU);

        assertThatThrownBy(() -> BiOutputMapping.biCombine(mapping, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(nullValue("mapping"));
    }

    @Test
    void biCombine_duplicate_keys_throws() {
        LOGGER.info("BiOutputMapping.biCombine: duplicate keys should throw IllegalStateException");

        final var m1 = BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.COIN).goTo(Output.THANK_YOU);
        final var m2 = BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.COIN).goTo(Output.ALARM);

        assertThatThrownBy(() -> BiOutputMapping.biCombine(m1, m2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void biCombine_produces_functional_mapping() {
        LOGGER.info("BiOutputMapping.biCombine: produces working functional mapping");

        final var fun = BiOutputMapping.biCombine(
                BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.COIN).goTo(Output.THANK_YOU),
                BiOutputMapping.<State, Input, Output>from(State.LOCKED, Input.PUSH).goTo(Output.ALARM),
                BiOutputMapping.<State, Input, Output>from(State.UNLOCKED, Input.PUSH).goTo(Output.OPEN)
        );

        assertThat(fun.apply(State.LOCKED, Input.COIN)).isEqualTo(Output.THANK_YOU);
        assertThat(fun.apply(State.LOCKED, Input.PUSH)).isEqualTo(Output.ALARM);
        assertThat(fun.apply(State.UNLOCKED, Input.PUSH)).isEqualTo(Output.OPEN);
    }
}
