---
title: StateTransitionEvent
description: Record specification, state payload properties, immutability, and usage of StateTransitionEvent in Automata.
---

`org.quurz.automata.StateTransitionEvent<S>`

`StateTransitionEvent` is an immutable record representing an atomic state transition event published by a [`FiniteStateMachine`](/reference/automata/finitestatemachine/).

```java
public record StateTransitionEvent<S>(@NonNull S oldState, @NonNull S newState)
```

---

## Generic Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<S>` | The type of states maintained by the state machine. |

---

## Record Components

### `oldState`

```java
public @NonNull S oldState()
```

Returns the state of the machine immediately before the transition took place. Guaranteed to be non-null.

---

### `newState`

```java
public @NonNull S newState()
```

Returns the new state of the machine immediately after the transition completed. Guaranteed to be non-null.

---

## Characteristics & Guarantees

* **Immutability:** As a standard Java record, `StateTransitionEvent` is immutable, thread-safe, and value-based.
* **Equality:** Implements canonical `equals()`, `hashCode()`, and `toString()` methods based on `oldState` and `newState`.
* **Non-Null Contract:** Both `oldState` and `newState` are guaranteed to be non-null.

---

## See Also

* [Guide: State Transition Events & Monitoring](/guides/automata/statetransitionevent/)
* [`StateTransitionEventListener` Reference](/reference/automata/statetransitioneventlistener/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
* [Full JavaDoc: `StateTransitionEvent`](/api/automata/foomp.automata/org/quurz/automata/StateTransitionEvent.html)
