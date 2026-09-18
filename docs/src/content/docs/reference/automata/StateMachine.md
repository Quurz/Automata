---
title: StateMachine
description: Core interface specification, generic type parameters, and contract guarantees of StateMachine in Automata.
---

`org.quurz.automata.StateMachine<S, IA, OA>`

`StateMachine` is the fundamental root interface representing a deterministic state machine that consumes input symbols and produces output symbols while transitioning through states.

```java
public interface StateMachine<S, IA, OA>
```

---

## Generic Type Parameters

| Parameter | Description |
| :--- | :--- |
| `<S>` | The type of states maintained by the state machine. |
| `<IA>` | The type of valid input symbols (**Input Alphabet**). |
| `<OA>` | The type of valid output symbols (**Output Alphabet**). |

---

## Interface Methods

### `read`

```java
@NonNull OA read(final @NonNull IA input)
```

Performs a transition for the given input symbol and returns the computed output symbol associated with that transition.

* **Parameters:**
  * `input` – The incoming input symbol to process; must not be `null`.
* **Returns:**
  * The non-null output symbol associated with this transition.
* **Throws:**
  * `NullPointerException` – if `input` is `null`.
  * `IllegalArgumentException` – if `input` is not recognized or not part of the input alphabet.

---

## Direct Implementations

* [`FiniteStateMachine<S, IA, OA>`](/reference/automata/finitestatemachine/) – Thread-safe, lock-protected implementation supporting both Moore and Mealy machine semantics.

---

## See Also

* [Guide: Working with StateMachine](/guides/automata/statemachine/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
* [Full JavaDoc: `StateMachine`](/api/automata/foomp.automata/org/quurz/automata/StateMachine.html)
