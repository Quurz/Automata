---
title: Declarative Outputs with OutputMapping
description: Learn how to construct clean, declarative output functions for Moore state machines using the OutputMapping fluent DSL.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

In deterministic finite state machines, mapping states to their designated output symbols often leads to repetitive boilerplate code. `OutputMapping<I, O>` provides a type-safe, declarative Domain-Specific Language (DSL) that defines single-input output functions ($\lambda: S \to OA$) cleanly using fluent `from(state).goTo(output)` expressions.

---

## 1. Single-Input Mappings

`OutputMapping<I, O>` maps a single input `I` (such as a state in a Moore machine) to an output `O`.

### Basic Syntax

Create individual mappings using the fluent static entry point `from(input).goTo(output)` and combine them into a functional mapping (`Fun<I, O>`) with `combine(...)`:

```java title="OutputMappingExample.java"
import org.quurz.automata.OutputMapping;
import org.quurz.foomp.base.core.Fun;

import static org.quurz.automata.OutputMapping.from;
import static org.quurz.automata.OutputMapping.combine;

Fun<State, Display> outputFunction = combine(
    from(State.RED).goTo(Display.STOP),
    from(State.RED_AMBER).goTo(Display.PREPARE_GO),
    from(State.GREEN).goTo(Display.GO),
    from(State.AMBER).goTo(Display.PREPARE_STOP)
);

Display display = outputFunction.apply(State.GREEN); // Display.GO
```

---

## 2. Moore Machine Integration

In a **Moore machine**, the output depends solely on the current state ($\lambda: S \to OA$). You can supply declarative `OutputMapping` instances directly to `MooreMachineBuilder`:

```java title="MooreTrafficLight.java"
import org.quurz.automata.FiniteStateMachine;
import org.quurz.automata.OutputMapping;
import org.quurz.automata.BiOutputMapping;

import static org.quurz.automata.OutputMapping.from;

public class MooreTrafficLight {

    public enum State { RED, RED_AMBER, GREEN, AMBER }
    public enum Signal { TICK }
    public enum Display { STOP, PREPARE_GO, GO, PREPARE_STOP }

    public static FiniteStateMachine<State, Signal, Display> create() {
        return FiniteStateMachine.<State, Signal, Display>mooreMachineBuilder()
            .states(State.RED, State.RED_AMBER, State.GREEN, State.AMBER)
            .inputAlphabet(Signal.TICK)
            .outputAlphabet(Display.STOP, Display.PREPARE_GO, Display.GO, Display.PREPARE_STOP)
            .transitionFunction(
                BiOutputMapping.from(State.RED,       Signal.TICK).goTo(State.RED_AMBER),
                BiOutputMapping.from(State.RED_AMBER, Signal.TICK).goTo(State.GREEN),
                BiOutputMapping.from(State.GREEN,     Signal.TICK).goTo(State.AMBER),
                BiOutputMapping.from(State.AMBER,     Signal.TICK).goTo(State.RED)
            )
            .outputFunction(
                from(State.RED).goTo(Display.STOP),
                from(State.RED_AMBER).goTo(Display.PREPARE_GO),
                from(State.GREEN).goTo(Display.GO),
                from(State.AMBER).goTo(Display.PREPARE_STOP)
            )
            .initialState(State.RED)
            .build();
    }
}
```

---

## 3. Fail-Fast Validation & Invariants

`OutputMapping` enforces robust validation rules:

### Duplicate Key Detection

Declaring the same source input state more than once throws an `IllegalStateException`:

```java
// Throws IllegalStateException: Duplicate key State.RED
Fun<State, String> faulty = OutputMapping.combine(
    OutputMapping.from(State.RED).goTo("Stop"),
    OutputMapping.from(State.RED).goTo("Halt")
);
```

### Strict Null Defense

Passing `null` for input, output, or mapping arrays immediately throws a descriptive `NullPointerException`:

```java
// Throws NullPointerException
OutputMapping.from(null);

// Throws NullPointerException
OutputMapping.from(State.RED).goTo(null);
```

---

## 4. Comparison: DSL vs. Switch Expressions

| Feature | `OutputMapping` DSL | Java `switch` Expressions |
| :--- | :--- | :--- |
| **Readability** | High tabular clarity | Compact for small enums |
| **Builder Varargs** | Directly supported in builders | Wrapped in lambda |
| **Duplicate Prevention** | Checked at runtime | Checked at compile-time (exhaustiveness/case duplicate) |
| **Dynamic Configuration** | Can be loaded dynamically | Static compile-time only |

---

## See Also

* [`OutputMapping` Reference](/reference/automata/outputmapping/) – Detailed API reference for single-input mappings.
* [Guide: Defining Transitions & Mealy Outputs with `BiOutputMapping`](/guides/automata/bioutputmapping/) – Two-input mapping guide.
* [`BiOutputMapping` Reference](/reference/automata/bioutputmapping/) – Detailed API reference for two-input mappings.
* [Guide: Finite State Machines in Practice](/guides/automata/finitestatemachine/) – Complete guide to building and running state machines.
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/) – State machine builder API details.
