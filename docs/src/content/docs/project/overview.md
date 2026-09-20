---
title: Project Overview & Architecture
description: Comprehensive overview of design goals, mathematical definitions, concurrency model, and integration of the Automata library.
---

import { Card, CardGrid } from '@astrojs/starlight/components';

**Automata** is a lightweight, type-safe, and thread-safe Java library for building and executing deterministic Finite State Machines (FSM). It natively supports both **Moore machines** and **Mealy machines** with functional transition abstractions.

---

## 1. Core Mathematical Model

In theoretical computer science, a deterministic finite state machine with output is formalized as a 6-tuple $(S, \Sigma, \Lambda, \delta, \lambda, s_0)$:

1. **$S$ (States):** A non-empty, finite set of states.
2. **$\Sigma$ / $IA$ (Input Alphabet):** A non-empty, finite set of input symbols.
3. **$\Lambda$ / $OA$ (Output Alphabet):** A non-empty, finite set of output symbols.
4. **$\delta$ (Transition Function):** A mapping from current state and input to next state:
   $$\delta: S \times IA \to S$$
5. **$\lambda$ (Output Function):**
   - **Moore Machine:** Output depends strictly on the current state:
     $$\lambda: S \to OA$$
   - **Mealy Machine:** Output depends on both the current state and input:
     $$\lambda: S \times IA \to OA$$
6. **$s_0$ (Initial State):** The starting state $s_0 \in S$.
7. **$F \subseteq S$ (End States):** Optional set of terminal/accepting states.

---

## 2. Architecture & Design Principles

<CardGrid>
  <Card title="Type Safety with Generics" icon="puzzle">
    State machines are fully parameterized over `<S, IA, OA>`, allowing custom Enums, Records, or domain classes to represent states, inputs, and outputs without type casting.
  </Card>
  <Card title="Thread-Safe Concurrency" icon="rocket">
    Internal state changes and output evaluation are guarded by `java.util.concurrent.locks.ReentrantLock`, ensuring safe concurrent access across worker threads.
  </Card>
  <Card title="Functional Composition" icon="forward-slash">
    Transition and output functions use `org.quurz.foomp.base.core.Fun` and `Fun2` from Foomp, allowing seamless lambda expressions and method references.
  </Card>
  <Card title="Fail-Fast Validation" icon="open-book">
    Constructors and runtime transition methods validate all invariants up-front using localized messages from `AutomataMessages`.
  </Card>
</CardGrid>

---

## 3. Concurrency & State Atomicity

`FiniteStateMachine` ensures atomic execution of transitions through an internal `ReentrantLock`:

```java
lock.lock();
try {
    // 1. Verify input token is in the input alphabet
    // 2. Compute output symbol via outputFunction
    // 3. Compute next state via transitionFunction
    // 4. Update currentState
    // 5. Return output
} finally {
    lock.unlock();
}
```

This ensures that intermediate state transitions are never observed in an inconsistent state, and thread race conditions during `read(...)` or `reset()` calls are prevented.

---

## 4. Integration with the Foomp Ecosystem

`Automata` integrates smoothly with the `Foomp-Base` module:
- Uses `Fun<A, R>` and `Fun2<A, B, R>` for higher-order functional mappings.
- Leverages Checker Framework annotations (`@NonNull`) for compile-time nullness verification.
- Conforms to standard Foomp packaging, error localization, and Java module system (`foomp.automata`) conventions.
