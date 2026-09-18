---
title: FiniteStateMachine
description: Comprehensive API reference, factory methods, concurrency guarantees, and inspection methods of FiniteStateMachine in Automata.
---

`org.quurz.automata.FiniteStateMachine<S, IA, OA>`

`FiniteStateMachine` is the core thread-safe implementation of [`StateMachine<S, IA, OA>`](/reference/automata/statemachine/). It provides deterministic finite state machine execution with support for both **Moore** and **Mealy** machine semantics.

```java
public class FiniteStateMachine<S, IA, OA> implements StateMachine<S, IA, OA>
```

---

## Generic Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<S>` | The type of states maintained by the state machine. |
| `<IA>` | The type of valid input symbols (**Input Alphabet**). |
| `<OA>` | The type of valid output symbols (**Output Alphabet**). |

---

## Factory Methods

Instances must be created using the static factory methods `mooreMachine` or `mealyMachine`.

### `mooreMachine`

Creates a Moore machine where the output depends **strictly on the current state** ($\lambda: S \to OA$).

```java
public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mooreMachine(
    final @NonNull Set<S> states,
    final @NonNull Set<IA> inputAlphabet,
    final @NonNull Set<OA> outputAlphabet,
    final @NonNull Fun2<S, IA, S> transitionFunction,
    final @NonNull Fun<S, OA> outputFunction,
    final @NonNull Set<S> endStates,
    final @NonNull S initialState
)
```

* **Parameters:**
  * `states` – Non-empty set of all possible states; must not contain `null`.
  * `inputAlphabet` – Non-empty set of valid input symbols; must not contain `null`.
  * `outputAlphabet` – Non-empty set of valid output symbols; must not contain `null`.
  * `transitionFunction` – Function mapping `(currentState, input) -> nextState`; must not return `null`.
  * `outputFunction` – Function mapping `currentState -> output`; must not return `null`.
  * `endStates` – Subset of states designated as accepting/final states; must be a subset of `states`.
  * `initialState` – The starting state (must be contained in `states`).
* **Throws:**
  * `NullPointerException` – if any required parameter is `null` or any collection contains `null` elements.
  * `IllegalArgumentException` – if `states`, `inputAlphabet`, or `outputAlphabet` is empty, `initialState` is not in `states`, or `endStates` is not a subset of `states`.

---

### `mealyMachine`

Creates a Mealy machine where the output depends **on both current state and input** ($\lambda: S \times IA \to OA$).

```java
public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mealyMachine(
    final @NonNull Set<S> states,
    final @NonNull Set<IA> inputAlphabet,
    final @NonNull Set<OA> outputAlphabet,
    final @NonNull Fun2<S, IA, S> transitionFunction,
    final @NonNull Fun2<S, IA, OA> outputFunction,
    final @NonNull Set<S> endStates,
    final @NonNull S initialState
)
```

* **Parameters:**
  * `states` – Non-empty set of all possible states; must not contain `null`.
  * `inputAlphabet` – Non-empty set of valid input symbols; must not contain `null`.
  * `outputAlphabet` – Non-empty set of valid output symbols; must not contain `null`.
  * `transitionFunction` – Function mapping `(currentState, input) -> nextState`; must not return `null`.
  * `outputFunction` – Function mapping `(currentState, input) -> output`; must not return `null`.
  * `endStates` – Subset of states designated as accepting/final states; must be a subset of `states`.
  * `initialState` – The starting state (must be contained in `states`).
* **Throws:**
  * `NullPointerException` – if any required parameter is `null` or any collection contains `null` elements.
  * `IllegalArgumentException` – if `states`, `inputAlphabet`, or `outputAlphabet` is empty, `initialState` is not in `states`, or `endStates` is not a subset of `states`.

---

## Instance Methods

### `read`

```java
@Override
public @NonNull OA read(final @NonNull IA input)
```

Atomically processes an incoming input symbol, fires listener events, transitions to the next state, and returns the computed output symbol.

* **Guarantees:** Thread-safe execution under `ReentrantLock`.
* **Throws:**
  * `NullPointerException` – if `input` is `null`, or if transition/output function returns `null`.
  * `IllegalArgumentException` – if `input` is not in the input alphabet, if the next state is not in the state set, or if the output is not in the output alphabet.

---

### `reset`

```java
public @NonNull FiniteStateMachine<S, IA, OA> reset()
```

Atomically resets the machine's current state back to its configured `initialState`. Returns `this` for fluent method chaining.

---

### `isInEndState`

```java
public boolean isInEndState()
```

Thread-safe check returning `true` if the machine's current state is contained in `endStates`, `false` otherwise.

---

### Inspection Methods

The following methods return defensive copies or values of the machine's configured components:

```java
public @NonNull Set<S> getStates()
public @NonNull Set<IA> getInputAlphabet()
public @NonNull Set<OA> getOutputAlphabet()
public @NonNull Set<S> getEndStates()
public @NonNull S getInitialState()
```

* `getStates()` – Returns a defensive copy of all valid states.
* `getInputAlphabet()` – Returns a defensive copy of all accepted input symbols.
* `getOutputAlphabet()` – Returns a defensive copy of all valid output symbols.
* `getEndStates()` – Returns a defensive copy of accepting/final states.
* `getInitialState()` – Returns the initial start state.

---

### State Transition Listeners

```java
public void registerStateTransitionListener(final @NonNull StateTransitionEventListener<S> listener)
public void unregisterStateTransitionListener(final @NonNull StateTransitionEventListener<S> listener)
```

Registers or unregisters a [`StateTransitionEventListener<S>`](/reference/automata/statetransitioneventlistener/) for receiving [`StateTransitionEvent<S>`](/reference/automata/statetransitionevent/) notifications upon each state transition.

* **Thread-Safety:** Protected under `ReentrantLock`.
* **Throws:** `NullPointerException` if `listener` is `null`.

---

## Concurrency Guarantees

* All mutable state transitions and lifecycle operations (`read`, `reset`, `isInEndState`, listener registration) are guarded by an internal `ReentrantLock`.
* Multi-threaded concurrent calls to `read(...)` or `reset()` execute atomically without race conditions or inconsistent states.

---

## See Also

* [Guide: Finite State Machine In Depth](/guides/automata/finitestatemachine/)
* [`StateMachine` Reference](/reference/automata/statemachine/)
* [`StateTransitionEventListener` Reference](/reference/automata/statetransitioneventlistener/)
* [`StateTransitionEvent` Reference](/reference/automata/statetransitionevent/)
* [`AutomataMessages` Reference](/reference/automata/localisation/automatamessages/)
* [Full JavaDoc: `FiniteStateMachine`](/api/automata/foomp.automata/org/quurz/automata/FiniteStateMachine.html)
