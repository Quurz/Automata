---
title: Example: Turnstile Models
description: A practical guide to implementing simple finite state machines using the Automata library.
---

A classic example of a finite state machine is a **Turnstile**. It has several states and reacts to inputs like coins, pushing, or even aggressive kicking.

In this guide, we will implement both a **Moore machine** and a **Mealy machine** to demonstrate the differences.

## 1. Moore Machine Example (Simple Turnstile)

In a Moore machine, the output depends only on the current state.

### The Model
- **States**: `LOCKED`, `UNLOCKED`.
- **Inputs**: `COIN`, `PUSH`.
- **Transitions**:
    - `LOCKED` + `COIN` -> `UNLOCKED`
    - `UNLOCKED` + `PUSH` -> `LOCKED`
- **Output**: The machine outputs `THANK_YOU` whenever it enters the `UNLOCKED` state.

```java
import org.quurz.automata.FiniteStateMachine;
import java.util.Set;

public class MooreTurnstile {
    enum State { LOCKED, UNLOCKED }
    enum Input { COIN, PUSH }
    enum Output { THANK_YOU, NOTHING }

    public static void main(String[] args) {
        var states = Set.of(State.LOCKED, State.UNLOCKED);
        var inputs = Set.of(Input.COIN, Input.PUSH);
        var outputs = Set.of(Output.THANK_YOU, Output.NOTHING);

        var transitionFunction = (State state, Input input) -> {
            return switch (state) {
                case LOCKED -> (input == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                case UNLOCKED -> (input == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
            };
        };

        // Output depends ONLY on the state
        var outputFunction = (State state) -> (state == State.UNLOCKED) ? Output.THANK_YOU : Output.NOTHING;

        var turnstile = FiniteStateMachine.mooreMachine(
            states, inputs, outputs,
            transitionFunction, outputFunction,
            Set.of(), State.LOCKED
        );

        System.out.println(turnstile.read(Input.COIN)); // Output: THANK_YOU
        System.out.println(turnstile.read(Input.PUSH)); // Output: NOTHING
    }
}
```

## 2. Mealy Machine Example (Advanced Turnstile)

In a Mealy machine, the output depends on both the current state and the input. This is useful for reacting to specific actions like "kicking" the machine.

### The Model
- **States**: `LOCKED`, `UNLOCKED`, `BROKEN`.
- **Inputs**: `COIN`, `PUSH`, `KICK`, `REPAIR`.
- **Outputs**: `THANK_YOU`, `ENJOY_YOUR_WALK`, `OUCH`, `BOOHOO`, `NOTHING`.

### Transitions & Outputs
1. **LOCKED**:
   - `COIN` -> `UNLOCKED` (Output: `THANK_YOU`)
   - `KICK` -> `BROKEN` (Output: `OUCH`)
   - `PUSH` -> `LOCKED` (Output: `NOTHING`)
2. **UNLOCKED**:
   - `PUSH` -> `LOCKED` (Output: `ENJOY_YOUR_WALK`)
   - `KICK` -> `BROKEN` (Output: `OUCH`)
   - `COIN` -> `UNLOCKED` (Output: `THANK_YOU`)
3. **BROKEN**:
   - `REPAIR` -> `LOCKED` (Output: `NOTHING`)
   - Any other input -> `BROKEN` (Output: `BOOHOO`)

```java
import org.quurz.automata.FiniteStateMachine;
import java.util.Set;

public class MealyTurnstile {
    enum State { LOCKED, UNLOCKED, BROKEN }
    enum Input { COIN, PUSH, KICK, REPAIR }
    enum Output { THANK_YOU, ENJOY_YOUR_WALK, OUCH, BOOHOO, NOTHING }

    public static void main(String[] args) {
        var states = Set.of(State.LOCKED, State.UNLOCKED, State.BROKEN);
        var inputs = Set.of(Input.COIN, Input.PUSH, Input.KICK, Input.REPAIR);
        var outputs = Set.of(Output.THANK_YOU, Output.ENJOY_YOUR_WALK, Output.OUCH, Output.BOOHOO, Output.NOTHING);

        var transitionFunction = (State state, Input input) -> {
            if (input == Input.KICK) return State.BROKEN;
            if (state == State.BROKEN) return (input == Input.REPAIR) ? State.LOCKED : State.BROKEN;

            return switch (state) {
                case LOCKED -> (input == Input.COIN) ? State.UNLOCKED : State.LOCKED;
                case UNLOCKED -> (input == Input.PUSH) ? State.LOCKED : State.UNLOCKED;
                default -> state;
            };
        };

        // Output depends on state AND input
        var outputFunction = (State state, Input input) -> {
            if (input == Input.KICK) return Output.OUCH;
            if (state == State.BROKEN && input != Input.REPAIR) return Output.BOOHOO;
            
            if (state == State.LOCKED && input == Input.COIN) return Output.THANK_YOU;
            if (state == State.UNLOCKED && input == Input.PUSH) return Output.ENJOY_YOUR_WALK;
            
            return Output.NOTHING;
        };

        var turnstile = FiniteStateMachine.mealyMachine(
            states, inputs, outputs,
            transitionFunction, outputFunction,
            Set.of(), State.LOCKED
        );

        System.out.println(turnstile.read(Input.COIN));  // Output: THANK_YOU
        System.out.println(turnstile.read(Input.KICK));  // Output: OUCH
        System.out.println(turnstile.read(Input.PUSH));  // Output: BOOHOO
    }
}
```

## Summary
- Use **Moore machines** when the output is a property of the state itself.
- Use **Mealy machines** when you need immediate feedback based on the interaction (input) and the current state.
