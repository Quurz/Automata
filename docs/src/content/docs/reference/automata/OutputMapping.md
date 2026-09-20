---
title: OutputMapping
description: API reference and usage documentation for single-input declarative output mappings.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`OutputMapping<I, O>` represents a single-input declarative output mapping for state machines (such as Moore machines).

It provides a fluent DSL entry point via `from(input).goTo(output)` to construct mappings and combine them into executable functions (`Fun<I, O>`) via `combine(...)`.

## 1. Class Overview

* **Package:** `org.quurz.automata`
* **Type:** Class
* **Generic Parameters:**
  * `I` – The source input or state type.
  * `O` – The output type.
* **Since:** `1.0.0`

## 2. Static Methods

### `from(I input)`

Initiates the creation of an `OutputMapping` starting from a given source input.

```java
public static <I, O> OutputMapping.GoTo<I, O> from(final @NonNull I input)
```

* **Parameters:** `input` – The source input value; must not be `null`.
* **Returns:** An intermediate `GoTo` builder step.
* **Throws:** `NullPointerException` if `input` is `null`.

### `combine(OutputMapping<I, O>... mappings)`

Combines multiple `OutputMapping` instances into a single `Fun<I, O>` functional mapping.

```java
@SafeVarargs
public static <I, O> Fun<I, O> combine(final @NonNull OutputMapping<I, O>... mappings)
```

* **Parameters:** `mappings` – The mappings to aggregate; must not be `null`.
* **Returns:** A functional mapping `input -> output`.
* **Throws:**
  * `NullPointerException` if `mappings` or any element is `null`.
  * `IllegalStateException` if duplicate source input keys are provided.

## 3. Nested Classes

### `OutputMapping.GoTo<I, O>`

Intermediate builder step representing the source value before defining its target output.

* **Method:**
  * `public OutputMapping<I, O> goTo(final @NonNull O output)` – Completes the mapping with the specified output value. Throws `NullPointerException` if `output` is `null`.

## 4. Usage Example

```java
import static org.quurz.automata.OutputMapping.from;
import static org.quurz.automata.OutputMapping.combine;

Fun<State, Display> outputFunction = combine(
    from(State.RED).goTo(Display.STOP),
    from(State.GREEN).goTo(Display.GO)
);

Display display = outputFunction.apply(State.RED); // Display.STOP
```

---

## See Also

* [Guide: Declarative Outputs with `OutputMapping`](/guides/automata/outputmapping/)
* [Guide: Defining Transitions & Mealy Outputs with `BiOutputMapping`](/guides/automata/bioutputmapping/)
* [`BiOutputMapping` Reference](/reference/automata/bioutputmapping/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
