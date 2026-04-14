---
title: API Overview
description: Technical reference for the Automata library.
---

The `Automata` library provides a thread-safe implementation of Finite State Machines (FSM) in Java. It supports both **Moore** and **Mealy** machine semantics.

## Core Concepts

### StateMachine Interface
The base interface for all state machines. It defines the core interaction point.

- **`S`**: The type of states (e.g., an `Enum`).
- **`IA`**: The type of input symbols (Input Alphabet).
- **`OA`**: The type of output symbols (Output Alphabet).

#### Methods
- `read(IA input)`: Performs a transition for the given input symbol and returns the associated output.

---

## FiniteStateMachine Class
The primary implementation of the `StateMachine` interface.

### Factory Methods

Instead of using the constructor directly, you should use the static factory methods:

#### `mooreMachine(...)`
Creates a Moore machine where the output depends **only on the current state**.

```java
public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mooreMachine(
    Set<S> states,
    Set<IA> inputAlphabet,
    Set<OA> outputAlphabet,
    Fun2<S, IA, S> transitionFunction,
    Fun<S, OA> outputFunction,
    Set<S> endStates,
    S initialState
)
```

#### `mealyMachine(...)`
Creates a Mealy machine where the output depends on **both the current state and the input**.

```java
public static <S, IA, OA> FiniteStateMachine<S, IA, OA> mealyMachine(
    Set<S> states,
    Set<IA> inputAlphabet,
    Set<OA> outputAlphabet,
    Fun2<S, IA, S> transitionFunction,
    Fun2<S, IA, OA> outputFunction,
    Set<S> endStates,
    S initialState
)
```

### Additional Methods

- `reset()`: Resets the state machine to its initial state. Returns the instance for method chaining.
- `isInEndState()`: Returns `true` if the machine is currently in one of the defined `endStates`.

---

## Technical Details

### Thread Safety
The `FiniteStateMachine` implementation is **thread-safe**. All state transitions and output computations are protected by a `ReentrantLock`. The entire read-transition-output cycle is executed atomically.

### Validation
The implementation performs several checks during construction and at runtime:
- All sets (states, alphabets, end states) must not be null.
- The `states`, `inputAlphabet`, and `outputAlphabet` sets must not be empty.
- None of the sets must contain `null` elements.
- The `initialState` must be part of the `states` set.
- `endStates` must be a subset of `states`.
- At runtime, if a transition leads to an unknown state or an unknown input is provided, an `IllegalArgumentException` is thrown.
