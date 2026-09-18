---
title: AutomataMessages
description: Formal API reference, resource bundle keys, formatting contracts, and exception patterns of AutomataMessages in Automata.
---

`org.quurz.automata.localisation.AutomataMessages`

`AutomataMessages` is the centralized localization and diagnostic message provider for the `Automata` library. It acts as a strongly-typed façade over a Java [`ResourceBundle`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ResourceBundle.html) (`AutomataMessages.properties`), ensuring consistent, localized, and well-formatted exception and validation messages across state machines.

```java
public class AutomataMessages
```

All methods in `AutomataMessages` are `public static`, thread-safe, and enforce strict non-null contracts.

---

## Architectural Overview

`AutomataMessages` loads its bundle via:

```java
ResourceBundle.getBundle("AutomataMessages", Locale.getDefault())
```

When validating state machine configurations, input tokens, or transition results, referencing `AutomataMessages` guarantees that exception messages adhere to the standardized formatting patterns used throughout the Foomp ecosystem.

---

## Method Catalog by Category

### 1. State Set & Initial State Validation

#### `unknownStartState`
```java
public static String unknownStartState(final @NonNull Object startState)
```
Generates an error message indicating that the specified initial start state is not contained in the valid states set.
* **Bundle Key:** `UNKNOWN_START_STATE`
* **Pattern:** `Start-State '%s' is not in state set`
* **Throws:** `NullPointerException` if `startState` is `null`.

#### `emptyStateSet`
```java
public static String emptyStateSet()
```
Generates an error message indicating that the state set is empty.
* **Bundle Key:** `EMPTY_STATE_SET`
* **Pattern:** `State set must not be empty`

#### `nullElementsInStateSet`
```java
public static String nullElementsInStateSet()
```
Generates an error message indicating that the state set contains a forbidden `null` element.
* **Bundle Key:** `NULL_ELEMENTS_IN_STATE_SET`
* **Pattern:** `State set must not contain null elements`

#### `unknownState`
```java
public static String unknownState(final @NonNull Object state)
```
Generates an error message indicating that an encountered state is not in the configured state set.
* **Bundle Key:** `UNKNOWN_STATE`
* **Pattern:** `State '%s' is not in state set`
* **Throws:** `NullPointerException` if `state` is `null`.

#### `endStatesNotATrueSubsetOfStates`
```java
public static String endStatesNotATrueSubsetOfStates()
```
Generates an error message indicating that the configured end states set is not a proper subset of the state set.
* **Bundle Key:** `END_STATES_TRUE_NO_SUBSET_OF_STATES`
* **Pattern:** `End states must be a true subset of states`

---

### 2. Input Alphabet Validation

#### `emptyInputAlphabet`
```java
public static String emptyInputAlphabet()
```
Generates an error message indicating that the input alphabet is empty.
* **Bundle Key:** `EMPTY_INPUT_ALPHABET`
* **Pattern:** `Input alphabet must not be empty`

#### `nullElementsInInputAlphabet`
```java
public static String nullElementsInInputAlphabet()
```
Generates an error message indicating that the input alphabet contains a forbidden `null` element.
* **Bundle Key:** `NULL_ELEMENTS_IN_INPUT_ALPHABET`
* **Pattern:** `Input alphabet must not contain null elements`

#### `unknownInputToken`
```java
public static String unknownInputToken(final @NonNull Object inputToken)
```
Generates an error message indicating that an input token is not contained in the configured input alphabet.
* **Bundle Key:** `UNKNOWN_INPUT_TOKEN`
* **Pattern:** `Input-Token '%s' is not in input alphabet`
* **Throws:** `NullPointerException` if `inputToken` is `null`.

---

### 3. Output Alphabet Validation

#### `emptyOutputAlphabet`
```java
public static String emptyOutputAlphabet()
```
Generates an error message indicating that the output alphabet is empty.
* **Bundle Key:** `EMPTY_OUTPUT_ALPHABET`
* **Pattern:** `Output alphabet must not be empty`

#### `nullElementsInOutputAlphabet`
```java
public static String nullElementsInOutputAlphabet()
```
Generates an error message indicating that the output alphabet contains a forbidden `null` element.
* **Bundle Key:** `NULL_ELEMENTS_IN_OUTPUT_ALPHABET`
* **Pattern:** `Output alphabet must not contain null elements`

#### `unknownOutputToken`
```java
public static String unknownOutputToken(final @NonNull Object outputToken)
```
Generates an error message indicating that an evaluated output symbol is not contained in the configured output alphabet.
* **Bundle Key:** `UNKNOWN_OUTPUT_TOKEN`
* **Pattern:** `Output-Token '%s' is not in output alphabet`
* **Throws:** `NullPointerException` if `outputToken` is `null`.

---

## Contract & Guarantees

1. **Non-Null Return:** All methods in `AutomataMessages` are guaranteed to return a non-null, fully formatted `String`.
2. **Immediate Fail-Fast:** Parameter validation throws `NullPointerException` immediately if required arguments or template parameters are `null`.
3. **Locale Adaptability:** Reads message definitions dynamically from `AutomataMessages.properties` using the JVM's default `Locale`.

---

## See Also

* [Guide: Standardized Messaging with AutomataMessages](/guides/automata/localisation/automatamessages/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
* [Full JavaDoc: `AutomataMessages`](/api/automata/foomp.automata/org/quurz/automata/localisation/AutomataMessages.html)
