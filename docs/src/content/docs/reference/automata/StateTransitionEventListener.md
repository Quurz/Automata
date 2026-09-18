---
title: StateTransitionEventListener
description: Functional interface specification, callback method, and registration contract of StateTransitionEventListener in Automata.
---

`org.quurz.automata.StateTransitionEventListener<S>`

`StateTransitionEventListener` is a functional interface for receiving notifications whenever a state transition occurs in a [`FiniteStateMachine`](/reference/automata/finitestatemachine/).

```java
@FunctionalInterface
public interface StateTransitionEventListener<S>
```

---

## Generic Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<S>` | The type of states maintained by the state machine. |

---

## Interface Methods

### `stateTransition`

```java
void stateTransition(final @NonNull StateTransitionEvent<S> stateTransitionEvent)
```

Invoked synchronously under lock protection when a state transition has completed in the monitored state machine.

* **Parameters:**
  * `stateTransitionEvent` – The event object containing `oldState` and `newState`; guaranteed to be non-null.

---

## Registration and Thread-Safety

Listeners are registered and unregistered via `FiniteStateMachine`:

* `fsm.registerStateTransitionListener(listener)`
* `fsm.unregisterStateTransitionListener(listener)`

### Execution Contract

* **Synchronous Invocation:** Callbacks are triggered immediately during the `fsm.read(...)` execution before returning the output.
* **Lock Protection:** Listeners run within the machine's internal lock scope. Callbacks should be fast and non-blocking to prevent holding the machine lock unnecessarily.
* **Idempotent Registration:** Registering the same listener instance multiple times has no duplicate effect (backed by a set).

---

## See Also

* [Guide: Reactive Monitoring with Listeners](/guides/automata/statetransitioneventlistener/)
* [`StateTransitionEvent` Reference](/reference/automata/statetransitionevent/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
* [Full JavaDoc: `StateTransitionEventListener`](/api/automata/foomp.automata/org/quurz/automata/StateTransitionEventListener.html)
