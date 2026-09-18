# Automata

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](license.txt)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Gradle](https://img.shields.io/badge/Gradle-Multi--Module-02303A.svg)](settings.gradle.kts)

> **Deterministic Finite State Machines (FSM), Moore and Mealy automata for modern Java.**

Automata is a lightweight, type-safe, and thread-safe Java library for building and executing deterministic Finite State Machines. It provides first-class support for both **Moore machines** and **Mealy machines**, backed by mathematical contracts, generic state/token types, and seamless functional lambda integration.

---

## ✨ Features

- **🧩 Moore & Mealy Automata:** Full support for Moore machines (output determined by state) and Mealy machines (output determined by state and input transition).
- **🛡️ Thread-Safe & Lock-Protected:** Built with `ReentrantLock` guarantees ensuring atomic state transitions and output calculations across concurrent threads.
- **⚡ Generic & Type-Safe:** Fully parameterized over `<S, IA, OA>` (States, Input Alphabet, Output Alphabet) to work seamlessly with Enums, Records, or custom domain models.
- **λ Functional Transition Lambdas:** Native integration with functional interfaces (`Fun`, `Fun2`) from Foomp for expressive transition tables and output mappings.
- **🔍 Fail-Fast Validation & Diagnostics:** Comprehensive runtime and precondition checks with localized error messages via `AutomataMessages`.
- **🎯 Accepting / Terminal States:** Support for defined end states (`isInEndState()`) and chainable state resets (`reset()`).

---

## 📦 Project Modules

| Module | Description |
| :--- | :--- |
| **`foomp.automata`** (`:Automata`) | Core finite state machine abstractions (`StateMachine`, `FiniteStateMachine`) and diagnostics (`AutomataMessages`). |
| **`docs`** | Interactive documentation, guides, and JavaDoc API site built with Astro & Starlight. |

---

## 🚀 Quick Start

### 1. Moore Machine Example (Traffic Light)

In a **Moore machine**, the output is solely a function of the current state:

```java
import org.quurz.automata.FiniteStateMachine;
import java.util.Set;

enum State { RED, RED_YELLOW, GREEN, YELLOW }
enum Input { TICK }
enum Output { STOP, GET_READY, GO, CAUTION }

public class TrafficLight {
    public static void main(String[] args) {
        var states = Set.of(State.RED, State.RED_YELLOW, State.GREEN, State.YELLOW);
        var inputs = Set.of(Input.TICK);
        var outputs = Set.of(Output.STOP, Output.GET_READY, Output.GO, Output.CAUTION);

        // State transition: (State, Input) -> NextState
        var transition = (State state, Input input) -> switch (state) {
            case RED -> State.RED_YELLOW;
            case RED_YELLOW -> State.GREEN;
            case GREEN -> State.YELLOW;
            case YELLOW -> State.RED;
        };

        // Moore output: State -> Output
        var outputFn = (State state) -> switch (state) {
            case RED -> Output.STOP;
            case RED_YELLOW -> Output.GET_READY;
            case GREEN -> Output.GO;
            case YELLOW -> Output.CAUTION;
        };

        var trafficLight = FiniteStateMachine.mooreMachine(
            states, inputs, outputs, transition, outputFn, Set.of(), State.RED
        );

        System.out.println(trafficLight.read(Input.TICK)); // Output.GET_READY
        System.out.println(trafficLight.read(Input.TICK)); // Output.GO
        System.out.println(trafficLight.read(Input.TICK)); // Output.CAUTION
        System.out.println(trafficLight.read(Input.TICK)); // Output.STOP
    }
}
```

### 2. Mealy Machine Example (Turnstile)

In a **Mealy machine**, the output depends on both the current state and the incoming input:

```java
import org.quurz.automata.FiniteStateMachine;
import java.util.Set;

enum TurnstileState { LOCKED, UNLOCKED }
enum TurnstileInput { COIN, PUSH }
enum TurnstileOutput { UNLOCK_BARRIER, LOCK_BARRIER, THANKS_ALREADY_OPEN, PLEASE_INSERT_COIN }

public class Turnstile {
    public static void main(String[] args) {
        var turnstile = FiniteStateMachine.mealyMachine(
            Set.of(TurnstileState.LOCKED, TurnstileState.UNLOCKED),
            Set.of(TurnstileInput.COIN, TurnstileInput.PUSH),
            Set.of(
                TurnstileOutput.UNLOCK_BARRIER,
                TurnstileOutput.LOCK_BARRIER,
                TurnstileOutput.THANKS_ALREADY_OPEN,
                TurnstileOutput.PLEASE_INSERT_COIN
            ),
            // Transition: (State, Input) -> NextState
            (state, input) -> switch (state) {
                case LOCKED -> (input == TurnstileInput.COIN) ? TurnstileState.UNLOCKED : TurnstileState.LOCKED;
                case UNLOCKED -> (input == TurnstileInput.PUSH) ? TurnstileState.LOCKED : TurnstileState.UNLOCKED;
            },
            // Mealy Output: (State, Input) -> Output
            (state, input) -> switch (state) {
                case LOCKED -> (input == TurnstileInput.COIN)
                    ? TurnstileOutput.UNLOCK_BARRIER
                    : TurnstileOutput.PLEASE_INSERT_COIN;
                case UNLOCKED -> (input == TurnstileInput.PUSH)
                    ? TurnstileOutput.LOCK_BARRIER
                    : TurnstileOutput.THANKS_ALREADY_OPEN;
            },
            Set.of(),
            TurnstileState.LOCKED
        );

        System.out.println(turnstile.read(TurnstileInput.COIN)); // UNLOCK_BARRIER
        System.out.println(turnstile.read(TurnstileInput.PUSH)); // LOCK_BARRIER
    }
}
```

---

## 🛠️ Building & Testing

### Prerequisites
* **JDK 25** (or compatible modern JDK)
* Gradle (or use the included `./gradlew` wrapper)

### Build Commands

```bash
# Run unit tests
./gradlew test

# Full build (classes, jars, javadocs)
./gradlew build

# Assemble JavaDocs for the Astro/Starlight documentation site
./gradlew assembleDocsForStarlight
```

---

## 📚 Documentation

Detailed guides, interactive examples, architecture notes, and JavaDocs are available in the `docs` directory.

To run the documentation site locally:

```bash
cd docs
npm install
npm run dev
```

Visit `http://localhost:4321` to explore the docs.

---

## 📄 License

This project is licensed under the [MIT License](license.txt) - see the `license.txt` file for details.
