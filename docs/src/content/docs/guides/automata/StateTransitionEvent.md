---
title: Working with StateTransitionEvent
description: Guide to capturing, analyzing, pattern-matching, and storing state transition events in Automata.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`StateTransitionEvent<S>` is an immutable record that encapsulates the transition payload (`oldState` and `newState`) emitted whenever a [`FiniteStateMachine`](/reference/automata/finitestatemachine/) performs a transition.

---

## 1. Anatomy of a Transition Event

The event contains two non-null state references:

```java
public record StateTransitionEvent<S>(@NonNull S oldState, @NonNull S newState) {}
```

* **`oldState()`**: The state of the machine before the transition occurred.
* **`newState()`**: The new state of the machine after the transition completed.

---

## 2. Transition History & Audit Trails

Because `StateTransitionEvent` is an immutable record, instances can be safely collected in thread-safe histories or published to message brokers without copying:

```java title="HistoryRecorder.java"
import org.quurz.automata.FiniteStateMachine;
import org.quurz.automata.StateTransitionEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HistoryRecorder<S> {

    private final List<StateTransitionEvent<S>> history = new CopyOnWriteArrayList<>();

    public void attach(FiniteStateMachine<S, ?, ?> fsm) {
        fsm.registerStateTransitionListener(history::add);
    }

    public List<StateTransitionEvent<S>> getHistory() {
        return List.copyOf(history);
    }
}
```

---

## 3. Pattern Matching with Record Patterns

Modern Java allows decomposing `StateTransitionEvent` using record patterns and `switch` expressions:

<Tabs>
  <TabItem label="Record Pattern Matching">
    ```java
    public void analyzeTransition(StateTransitionEvent<TurnstileState> event) {
        switch (event) {
            case StateTransitionEvent(var oldState, var newState) when oldState == newState ->
                System.out.println("Self-transition on state: " + oldState);

            case StateTransitionEvent(TurnstileState.LOCKED, TurnstileState.UNLOCKED) ->
                System.out.println("Turnstile unlocked!");

            case StateTransitionEvent(TurnstileState.UNLOCKED, TurnstileState.LOCKED) ->
                System.out.println("Turnstile locked after entry.");
        }
    }
    ```
  </TabItem>
  <TabItem label="Direct Accessors">
    ```java
    public void logTransition(StateTransitionEvent<?> event) {
        System.out.printf("[%s] -> [%s]%n", event.oldState(), event.newState());
    }
    ```
  </TabItem>
</Tabs>

---

## 4. Value Equality & Diagnostics

`StateTransitionEvent` provides value-based `equals()` and `hashCode()`, making it ideal for assertions in unit tests:

```java title="EventTest.java"
import static org.assertj.core.api.Assertions.assertThat;

StateTransitionEvent<State> expected = new StateTransitionEvent<>(State.LOCKED, State.UNLOCKED);
StateTransitionEvent<State> actual = capturedEvents.get(0);

assertThat(actual).isEqualTo(expected);
```

---

## See Also

* [`StateTransitionEvent` Reference](/reference/automata/statetransitionevent/)
* [Guide: Reactive Event Monitoring with Listeners](/guides/automata/statetransitioneventlistener/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
