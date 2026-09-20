---
title: Defining Transitions and Mealy Outputs with BiOutputMapping
description: Learn how to construct declarative, type-safe transition functions and Mealy machine output mappings using BiOutputMapping.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`BiOutputMapping<I1, I2, O>` provides a declarative Domain-Specific Language (DSL) for constructing two-argument functions (`Fun2<I1, I2, O>`). In `Automata`, it is primarily used for defining state machine transition tables ($\delta: S \times IA \to S$) and Mealy machine output functions ($\lambda: S \times IA \to OA$).

---

## 1. Declarative Transition Tables

Defining transition logic using switch expressions or nested if-statements can become unwieldy as state spaces grow. `BiOutputMapping` allows you to declare transition rules like a tabular state table using fluent `from(state, input).goTo(nextState)` expressions.

### Syntax & Construction

```java title="TransitionExample.java"
import org.quurz.automata.BiOutputMapping;
import org.quurz.foomp.base.core.Fun2;

import static org.quurz.automata.BiOutputMapping.from;
import static org.quurz.automata.BiOutputMapping.biCombine;

Fun2<State, Action, State> transitionFunction = biCombine(
    from(State.LOCKED,   Action.INSERT_COIN).goTo(State.UNLOCKED),
    from(State.LOCKED,   Action.PUSH_BAR).goTo(State.LOCKED),
    from(State.UNLOCKED, Action.INSERT_COIN).goTo(State.UNLOCKED),
    from(State.UNLOCKED, Action.PUSH_BAR).goTo(State.LOCKED)
);

State next = transitionFunction.apply(State.LOCKED, Action.INSERT_COIN); // State.UNLOCKED
```

---

## 2. Mealy Machine Output Mappings

In a **Mealy machine**, output symbols depend on both the current state and the incoming input symbol ($\lambda: S \times IA \to OA$). You can declare Mealy outputs using the same intuitive syntax:

```java title="MealyOutputExample.java"
import org.quurz.automata.BiOutputMapping;
import org.quurz.foomp.base.core.Fun2;

import static org.quurz.automata.BiOutputMapping.from;
import static org.quurz.automata.BiOutputMapping.biCombine;

Fun2<State, Action, Response> outputFunction = biCombine(
    from(State.LOCKED,   Action.INSERT_COIN).goTo(Response.UNLOCK_GATE),
    from(State.LOCKED,   Action.PUSH_BAR).goTo(Response.ALARM),
    from(State.UNLOCKED, Action.INSERT_COIN).goTo(Response.ALREADY_UNLOCKED),
    from(State.UNLOCKED, Action.PUSH_BAR).goTo(Response.PASS_ALLOWED)
);

Response response = outputFunction.apply(State.LOCKED, Action.INSERT_COIN); // Response.UNLOCK_GATE
```

---

## 3. Direct Builder Integration

`FiniteStateMachineBuilder` and `MealyMachineBuilder` provide varargs overloads accepting `BiOutputMapping` instances directly, eliminating intermediate collections or manual `biCombine` calls:

<Tabs>
  <TabItem label="Mealy Machine Builder">
    ```java title="TurnstileMachine.java"
    import org.quurz.automata.BiOutputMapping;
    import org.quurz.automata.FiniteStateMachine;

    public class TurnstileMachine {

        public enum State { LOCKED, UNLOCKED }
        public enum Action { INSERT_COIN, PUSH_BAR }
        public enum Response { UNLOCK_GATE, PASS_ALLOWED, ALARM, ALREADY_UNLOCKED }

        public static FiniteStateMachine<State, Action, Response> create() {
            return FiniteStateMachine.<State, Action, Response>mealyMachineBuilder()
                .states(State.LOCKED, State.UNLOCKED)
                .inputAlphabet(Action.INSERT_COIN, Action.PUSH_BAR)
                .outputAlphabet(Response.UNLOCK_GATE, Response.PASS_ALLOWED, Response.ALARM, Response.ALREADY_UNLOCKED)
                .transitionFunction(
                    BiOutputMapping.from(State.LOCKED,   Action.INSERT_COIN).goTo(State.UNLOCKED),
                    BiOutputMapping.from(State.LOCKED,   Action.PUSH_BAR).goTo(State.LOCKED),
                    BiOutputMapping.from(State.UNLOCKED, Action.INSERT_COIN).goTo(State.UNLOCKED),
                    BiOutputMapping.from(State.UNLOCKED, Action.PUSH_BAR).goTo(State.LOCKED)
                )
                .outputFunction(
                    BiOutputMapping.from(State.LOCKED,   Action.INSERT_COIN).goTo(Response.UNLOCK_GATE),
                    BiOutputMapping.from(State.LOCKED,   Action.PUSH_BAR).goTo(Response.ALARM),
                    BiOutputMapping.from(State.UNLOCKED, Action.INSERT_COIN).goTo(Response.ALREADY_UNLOCKED),
                    BiOutputMapping.from(State.UNLOCKED, Action.PUSH_BAR).goTo(Response.PASS_ALLOWED)
                )
                .initialState(State.LOCKED)
                .endStates(State.LOCKED)
                .build();
        }
    }
    ```
  </TabItem>
  <TabItem label="Moore Machine Transitions">
    ```java title="MooreTransitions.java"
    import org.quurz.automata.BiOutputMapping;
    import org.quurz.automata.FiniteStateMachine;

    // Transition functions in Moore machines also take (State, Input) -> next State
    var fsm = FiniteStateMachine.<State, Signal, Display>mooreMachineBuilder()
        .states(State.RED, State.GREEN)
        .inputAlphabet(Signal.TICK)
        .outputAlphabet(Display.STOP, Display.GO)
        .transitionFunction(
            BiOutputMapping.from(State.RED,   Signal.TICK).goTo(State.GREEN),
            BiOutputMapping.from(State.GREEN, Signal.TICK).goTo(State.RED)
        )
        // ...
        .build();
    ```
  </TabItem>
</Tabs>

---

## 4. Fail-Fast Validation & Invariants

`BiOutputMapping` enforces strict safety invariants:

* **Duplicate Input Pairs:** Defining duplicate `(input1, input2)` combinations throws `IllegalStateException` immediately when combined.
* **Strict Null Defense:** Passing `null` for `input1`, `input2`, `output`, or the mapping array raises descriptive `NullPointerException` errors.

```java
// Throws IllegalStateException: Duplicate key (State.LOCKED, Action.INSERT_COIN)
BiOutputMapping.biCombine(
    BiOutputMapping.from(State.LOCKED, Action.INSERT_COIN).goTo(Response.UNLOCK_GATE),
    BiOutputMapping.from(State.LOCKED, Action.INSERT_COIN).goTo(Response.ALARM)
);
```

---

## See Also

* [`BiOutputMapping` Reference](/reference/automata/bioutputmapping/) – Formal API specification for two-input mappings.
* [Guide: Single-Input OutputMapping](/guides/automata/outputmapping/) – Learn about single-input mappings for Moore machines.
* [`OutputMapping` Reference](/reference/automata/outputmapping/) – Single-input mapping API reference.
* [Guide: Finite State Machines in Practice](/guides/automata/finitestatemachine/) – Learn how to build and execute state machines.
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/) – State machine builder API details.
