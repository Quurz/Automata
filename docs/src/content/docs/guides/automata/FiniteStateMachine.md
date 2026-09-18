---
title: Finite State Machines in Practice
description: Comprehensive guide to creating, executing, and monitoring Moore and Mealy finite state machines with FiniteStateMachine.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`FiniteStateMachine` provides a thread-safe, lock-protected implementation of deterministic finite automata with functional transition and output logic. This guide covers how to model systems, choose between Moore and Mealy models, attach lifecycle listeners, and handle edge cases.

---

## 1. Moore vs. Mealy Automata

Before building an FSM, choose the model that fits your domain:

* **Moore Machine:** The output depends **only on the current state** ($\lambda: S \to OA$). Ideal for state-centric models like traffic lights, elevators, or UI screens.
* **Mealy Machine:** The output depends **on both state and input token** ($\lambda: S \times IA \to OA$). Ideal for event-driven protocols, tokenizers, or vending machines where inputs trigger immediate distinct actions.

---

## 2. Quick Start: Traffic Light (Moore Machine)

Here is a classic traffic light controller modeled as a Moore machine:

```java title="TrafficLightExample.java"
import org.quurz.automata.FiniteStateMachine;

import java.util.Set;

public class TrafficLightExample {

    public enum State { RED, RED_AMBER, GREEN, AMBER }
    public enum Signal { TICK }
    public enum Display { STOP, PREPARE_GO, GO, PREPARE_STOP }

    public static void main(String[] args) {
        final var fsm = FiniteStateMachine.mooreMachine(
            Set.of(State.RED, State.RED_AMBER, State.GREEN, State.AMBER),
            Set.of(Signal.TICK),
            Set.of(Display.STOP, Display.PREPARE_GO, Display.GO, Display.PREPARE_STOP),
            (state, signal) -> switch (state) {
                case RED -> State.RED_AMBER;
                case RED_AMBER -> State.GREEN;
                case GREEN -> State.AMBER;
                case AMBER -> State.RED;
            },
            state -> switch (state) {
                case RED -> Display.STOP;
                case RED_AMBER -> Display.PREPARE_GO;
                case GREEN -> Display.GO;
                case AMBER -> Display.PREPARE_STOP;
            },
            Set.of(State.RED),
            State.RED
        );

        System.out.println(fsm.read(Signal.TICK)); // Output: STOP (transitioning to RED_AMBER)
        System.out.println(fsm.read(Signal.TICK)); // Output: PREPARE_GO (transitioning to GREEN)
        System.out.println(fsm.read(Signal.TICK)); // Output: GO (transitioning to AMBER)
    }
}
```

---

## 3. Modeling a Turnstile (Mealy Machine)

A turnstile's response depends both on whether it is locked and what the user does (`COIN` vs. `PUSH`):

<Tabs>
  <TabItem label="Mealy Setup">
    ```java title="TurnstileMealy.java"
    import org.quurz.automata.FiniteStateMachine;
    import java.util.Set;

    public class TurnstileMealy {
        public enum State { LOCKED, UNLOCKED }
        public enum Action { INSERT_COIN, PUSH_BAR }
        public enum Response { UNLOCK_GATE, PASS_ALLOWED, ALARM, ALREADY_UNLOCKED }

        public static FiniteStateMachine<State, Action, Response> createTurnstile() {
            return FiniteStateMachine.mealyMachine(
                Set.of(State.LOCKED, State.UNLOCKED),
                Set.of(Action.INSERT_COIN, Action.PUSH_BAR),
                Set.of(Response.UNLOCK_GATE, Response.PASS_ALLOWED, Response.ALARM, Response.ALREADY_UNLOCKED),
                (state, action) -> switch (state) {
                    case LOCKED -> (action == Action.INSERT_COIN) ? State.UNLOCKED : State.LOCKED;
                    case UNLOCKED -> (action == Action.PUSH_BAR) ? State.LOCKED : State.UNLOCKED;
                },
                (state, action) -> switch (state) {
                    case LOCKED -> (action == Action.INSERT_COIN) ? Response.UNLOCK_GATE : Response.ALARM;
                    case UNLOCKED -> (action == Action.INSERT_COIN) ? Response.ALREADY_UNLOCKED : Response.PASS_ALLOWED;
                },
                Set.of(State.LOCKED),
                State.LOCKED
            );
        }
    }
    ```
  </TabItem>
  <TabItem label="Usage & Chaining">
    ```java
    var turnstile = TurnstileMealy.createTurnstile();

    // Normal passage
    Response r1 = turnstile.read(Action.INSERT_COIN); // Response.UNLOCK_GATE
    Response r2 = turnstile.read(Action.PUSH_BAR);    // Response.PASS_ALLOWED

    // Illegal push without coin
    Response r3 = turnstile.read(Action.PUSH_BAR);    // Response.ALARM

    // Resetting state
    turnstile.reset();
    boolean inEndState = turnstile.isInEndState();    // true (LOCKED is configured end state)
    ```
  </TabItem>
</Tabs>

---

## 4. Fluent Construction with Builders

In addition to static factory methods, you can construct finite state machines using fluent builders. Thanks to *Self-Types (CRTP)* and *varargs convenience overloads*, methods can be chained in any order without boilerplate `Set.of(...)`:

<Tabs>
  <TabItem label="Moore Builder (Varargs)">
    ```java
    var trafficLight = FiniteStateMachine.<State, Signal, Display>mooreMachineBuilder()
        .states(State.RED, State.RED_AMBER, State.GREEN, State.AMBER)
        .inputAlphabet(Signal.TICK)
        .outputAlphabet(Display.STOP, Display.PREPARE_GO, Display.GO, Display.PREPARE_STOP)
        .transitionFunction((state, signal) -> switch (state) {
            case RED -> State.RED_AMBER;
            case RED_AMBER -> State.GREEN;
            case GREEN -> State.AMBER;
            case AMBER -> State.RED;
        })
        .outputFunction(state -> switch (state) {
            case RED -> Display.STOP;
            case RED_AMBER -> Display.PREPARE_GO;
            case GREEN -> Display.GO;
            case AMBER -> Display.PREPARE_STOP;
        })
        .initialState(State.RED)
        // .endStates(...) is optional and defaults to empty set
        .build();
    ```
  </TabItem>
  <TabItem label="Mealy Builder (Set / Varargs)">
    ```java
    var turnstile = FiniteStateMachine.<State, Action, Response>mealyMachineBuilder()
        .states(State.LOCKED, State.UNLOCKED)
        .inputAlphabet(Action.INSERT_COIN, Action.PUSH_BAR)
        .outputAlphabet(Response.UNLOCK_GATE, Response.PASS_ALLOWED, Response.ALARM, Response.ALREADY_UNLOCKED)
        .transitionFunction((state, action) -> switch (state) {
            case LOCKED -> (action == Action.INSERT_COIN) ? State.UNLOCKED : State.LOCKED;
            case UNLOCKED -> (action == Action.PUSH_BAR) ? State.LOCKED : State.UNLOCKED;
        })
        .outputFunction((state, action) -> switch (state) {
            case LOCKED -> (action == Action.INSERT_COIN) ? Response.UNLOCK_GATE : Response.ALARM;
            case UNLOCKED -> (action == Action.INSERT_COIN) ? Response.ALREADY_UNLOCKED : Response.PASS_ALLOWED;
        })
        .endStates(State.LOCKED)
        .initialState(State.LOCKED)
        .build();
    ```
  </TabItem>
  <TabItem label="Declarative Mappings (DSL)">
    ```java
    import static org.quurz.automata.OutputMapping.from;
    import static org.quurz.automata.BiOutputMapping.from;

    var turnstile = FiniteStateMachine.<State, Action, Response>mealyMachineBuilder()
        .states(State.LOCKED, State.UNLOCKED)
        .inputAlphabet(Action.INSERT_COIN, Action.PUSH_BAR)
        .outputAlphabet(Response.UNLOCK_GATE, Response.PASS_ALLOWED, Response.ALARM, Response.ALREADY_UNLOCKED)
        .transitionFunction(
            BiOutputMapping.from(State.LOCKED,   Action.INSERT_COIN).goTo(State.UNLOCKED),
            BiOutputMapping.from(State.LOCKED,   Action.PUSH_BAR).goTo(State.LOCKED),
            BiOutputMapping.from(State.UNLOCKED, Action.PUSH_BAR).goTo(State.LOCKED),
            BiOutputMapping.from(State.UNLOCKED, Action.INSERT_COIN).goTo(State.UNLOCKED)
        )
        .outputFunction(
            BiOutputMapping.from(State.LOCKED,   Action.INSERT_COIN).goTo(Response.UNLOCK_GATE),
            BiOutputMapping.from(State.LOCKED,   Action.PUSH_BAR).goTo(Response.ALARM),
            BiOutputMapping.from(State.UNLOCKED, Action.PUSH_BAR).goTo(Response.PASS_ALLOWED),
            BiOutputMapping.from(State.UNLOCKED, Action.INSERT_COIN).goTo(Response.ALREADY_UNLOCKED)
        )
        .endStates(State.LOCKED)
        .initialState(State.LOCKED)
        .build();
    ```
  </TabItem>
</Tabs>

---

## 5. Registering State Transition Listeners

You can attach event listeners to monitor all state changes reactively:

```java title="AuditLoggingExample.java"
fsm.registerStateTransitionListener(event -> {
    System.out.printf("[AUDIT] Transitioned from %s to %s%n",
        event.oldState(),
        event.newState()
    );
});
```

* Callbacks execute synchronously within the atomic transition step.
* Handlers should be fast and non-blocking to keep throughput high.

---

## 6. Thread Safety & Concurrency

`FiniteStateMachine` is thread-safe and designed for concurrent multi-threaded environments:

```java
// Thread A and Thread B can concurrently process tokens safely:
executor.submit(() -> fsm.read(Input.COIN));
executor.submit(() -> fsm.read(Input.PUSH));
```

All state transitions, output evaluations, and listener dispatches happen atomically inside a `ReentrantLock`.

---

## 7. Defensive Copies & Introspection

All getter methods provide safe defensive copies to prevent outside mutation:

```java
Set<State> states = fsm.getStates();
states.clear(); // Does NOT affect internal FSM state

Set<Input> inputs = fsm.getInputAlphabet();
Set<Output> outputs = fsm.getOutputAlphabet();
Set<State> ends = fsm.getEndStates();
State initial = fsm.getInitialState();
```

---

## See Also

* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
* [Guide: Generic StateMachine Interface](/guides/automata/statemachine/)
* [Guide: Reactive Event Monitoring](/guides/automata/statetransitioneventlistener/)
* [Guide: Standardized Messaging](/guides/automata/localisation/automatamessages/)
