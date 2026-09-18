---
title: Standardized Messaging with AutomataMessages
description: Best practices, practical validation recipes, static import idioms, and consistent exception formatting using AutomataMessages in Automata.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`AutomataMessages` provides standardized, internationalized error and validation messages across the `Automata` library. By leveraging `AutomataMessages`, your application maintains uniform exception diagnostics when configuring state machines, validating input tokens, or handling invalid transitions.

---

## 1. The Static Import Idiom

The most idiomatic way to use `AutomataMessages` is via static imports in validation preconditions or exception handlers:

```java title="ValidationExample.java"
import static java.util.Objects.requireNonNull;
import static org.quurz.automata.localisation.AutomataMessages.unknownInputToken;
import static org.quurz.automata.localisation.AutomataMessages.unknownState;

public class CustomDispatcher {
    public void dispatch(State state, InputEvent event) {
        if (!validStates.contains(state)) {
            throw new IllegalArgumentException(unknownState(state));
        }
        if (!validEvents.contains(event)) {
            throw new IllegalArgumentException(unknownInputToken(event));
        }
    }
}
```

---

## 2. Common Validation Recipes

<Tabs>
  <TabItem label="Alphabet Preconditions">
    ```java
    import static org.quurz.automata.localisation.AutomataMessages.*;

    public void validateAlphabets(Set<?> inputs, Set<?> outputs) {
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException(emptyInputAlphabet());
        }
        if (inputs.contains(null)) {
            throw new NullPointerException(nullElementsInInputAlphabet());
        }
        if (outputs.isEmpty()) {
            throw new IllegalArgumentException(emptyOutputAlphabet());
        }
        if (outputs.contains(null)) {
            throw new NullPointerException(nullElementsInOutputAlphabet());
        }
    }
    ```
  </TabItem>
  <TabItem label="State & Subset Guards">
    ```java
    import static org.quurz.automata.localisation.AutomataMessages.*;

    public <S> void validateStates(Set<S> states, Set<S> endStates, S initialState) {
        if (states.isEmpty()) {
            throw new IllegalArgumentException(emptyStateSet());
        }
        if (states.contains(null)) {
            throw new NullPointerException(nullElementsInStateSet());
        }
        if (!states.contains(initialState)) {
            throw new IllegalArgumentException(unknownStartState(initialState));
        }
        if (!states.containsAll(endStates)) {
            throw new IllegalArgumentException(endStatesNotATrueSubsetOfStates());
        }
    }
    ```
  </TabItem>
  <TabItem label="Runtime Token Verification">
    ```java
    import static org.quurz.automata.localisation.AutomataMessages.*;

    public <IA, OA> OA process(IA token, Set<IA> alphabet) {
        if (!alphabet.contains(token)) {
            throw new IllegalArgumentException(unknownInputToken(token));
        }
        // Processing...
        return null;
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Summary Table of Key Message Patterns

| Category | Typical Exception | AutomataMessages Method | Resulting Message |
| :--- | :--- | :--- | :--- |
| **Start State** | `IllegalArgumentException` | `unknownStartState(s)` | `Start-State '%s' is not in state set` |
| **Empty State Set** | `IllegalArgumentException` | `emptyStateSet()` | `State set must not be empty` |
| **Null in State Set** | `NullPointerException` | `nullElementsInStateSet()` | `State set must not contain null elements` |
| **Empty Input Alphabet** | `IllegalArgumentException` | `emptyInputAlphabet()` | `Input alphabet must not be empty` |
| **Null in Input Alphabet** | `NullPointerException` | `nullElementsInInputAlphabet()` | `Input alphabet must not contain null elements` |
| **Empty Output Alphabet** | `IllegalArgumentException` | `emptyOutputAlphabet()` | `Output alphabet must not be empty` |
| **Null in Output Alphabet** | `NullPointerException` | `nullElementsInOutputAlphabet()` | `Output alphabet must not contain null elements` |
| **End States Subset** | `IllegalArgumentException` | `endStatesNotATrueSubsetOfStates()` | `End states must be a true subset of states` |
| **Unknown Input Token** | `IllegalArgumentException` | `unknownInputToken(token)` | `Input-Token '%s' is not in input alphabet` |
| **Unknown State** | `IllegalArgumentException` | `unknownState(state)` | `State '%s' is not in state set` |
| **Unknown Output Token** | `IllegalArgumentException` | `unknownOutputToken(token)` | `Output-Token '%s' is not in output alphabet` |

---

## See Also

* [`AutomataMessages` Reference](/reference/automata/localisation/automatamessages/) – Complete formal method reference and resource bundle keys.
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/) – Learn how `FiniteStateMachine` uses these messages internally.
