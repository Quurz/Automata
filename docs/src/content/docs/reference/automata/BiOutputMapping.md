---
title: BiOutputMapping
description: API reference and usage documentation for two-input declarative output mappings.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`BiOutputMapping<I1, I2, O>` represents a two-input declarative output mapping for state machines (such as Mealy machines or transition definitions).

It provides a fluent DSL entry point via `from(input1, input2).goTo(output)` to construct mappings and combine them into executable two-argument functions (`Fun2<I1, I2, O>`) via `biCombine(...)`.

## 1. Class Overview

* **Package:** `org.quurz.automata`
* **Type:** Class
* **Generic Parameters:**
  * `I1` – The first input type (e.g., current state).
  * `I2` – The second input type (e.g., input alphabet symbol).
  * `O` – The output type (e.g., output symbol or next state).
* **Since:** `1.0.0`

## 2. Static Methods

### `from(I1 input1, I2 input2)`

Initiates the creation of a `BiOutputMapping` starting from a pair of source inputs.

```java
public static <I1, I2, O> BiOutputMapping.BiGoTo<I1, I2, O> from(final @NonNull I1 input1,
                                                                 final @NonNull I2 input2)
```

* **Parameters:**
  * `input1` – The first input value; must not be `null`.
  * `input2` – The second input value; must not be `null`.
* **Returns:** An intermediate `BiGoTo` builder step.
* **Throws:** `NullPointerException` if `input1` or `input2` is `null`.

### `biCombine(BiOutputMapping<I1, I2, O>... mappings)`

Combines multiple `BiOutputMapping` instances into a single `Fun2<I1, I2, O>` functional mapping.

```java
@SafeVarargs
public static <I1, I2, O> Fun2<I1, I2, O> biCombine(final @NonNull BiOutputMapping<I1, I2, O>... mappings)
```

* **Parameters:** `mappings` – The mappings to aggregate; must not be `null`.
* **Returns:** A two-argument functional mapping `(input1, input2) -> output`.
* **Throws:**
  * `NullPointerException` if `mappings` or any element is `null`.
  * `IllegalStateException` if duplicate source input pairs are provided.

## 3. Nested Classes

### `BiOutputMapping.BiGoTo<I1, I2, O>`

Intermediate builder step representing the pair of source inputs before defining its target output.

* **Method:**
  * `public BiOutputMapping<I1, I2, O> goTo(final @NonNull O output)` – Completes the mapping with the specified output value. Throws `NullPointerException` if `output` is `null`.

## 4. Usage Example

```java
import static org.quurz.automata.BiOutputMapping.from;
import static org.quurz.automata.BiOutputMapping.biCombine;

Fun2<State, Action, Response> mealyOutputFunction = biCombine(
    from(State.LOCKED, Action.INSERT_COIN).goTo(Response.UNLOCK_GATE),
    from(State.LOCKED, Action.PUSH_BAR).goTo(Response.ALARM),
    from(State.UNLOCKED, Action.PUSH_BAR).goTo(Response.PASS_ALLOWED)
);

Response response = mealyOutputFunction.apply(State.LOCKED, Action.INSERT_COIN); // Response.UNLOCK_GATE
```

---

## See Also

* [Guide: Defining Transitions & Mealy Outputs with `BiOutputMapping`](/guides/automata/bioutputmapping/)
* [Guide: Declarative Outputs with `OutputMapping`](/guides/automata/outputmapping/)
* [`OutputMapping` Reference](/reference/automata/outputmapping/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
