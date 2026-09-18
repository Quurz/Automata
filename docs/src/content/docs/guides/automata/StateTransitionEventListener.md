---
title: Reactive Monitoring with StateTransitionEventListener
description: Guide to building event-driven architectures, observer patterns, and reactive pipelines using StateTransitionEventListener.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`StateTransitionEventListener<S>` is a functional interface that enables decoupled observation of state transitions occurring in a [`FiniteStateMachine`](/reference/automata/finitestatemachine/).

---

## 1. Implementing Listeners

Since `StateTransitionEventListener` is a `@FunctionalInterface`, you can implement it using lambda expressions, method references, or dedicated classes:

<Tabs>
  <TabItem label="Lambda & Method Reference">
    ```java
    // Simple inline lambda
    fsm.registerStateTransitionListener(event ->
        log.debug("State changed from {} to {}", event.oldState(), event.newState())
    );

    // Method reference
    fsm.registerStateTransitionListener(this::onTransition);
    ```
  </TabItem>
  <TabItem label="Class Implementation">
    ```java title="MetricListener.java"
    import org.quurz.automata.StateTransitionEvent;
    import org.quurz.automata.StateTransitionEventListener;
    import org.checkerframework.checker.nullness.qual.NonNull;

    public class MetricListener<S> implements StateTransitionEventListener<S> {

        @Override
        public void stateTransition(final @NonNull StateTransitionEvent<S> event) {
            MetricsRegistry.counter("fsm.transitions",
                "oldState", event.oldState().toString(),
                "newState", event.newState().toString()
            ).increment();
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 2. Managing Listener Lifecycles

Listeners can be registered and unregistered dynamically at runtime:

```java title="LifecycleManagement.java"
StateTransitionEventListener<State> listener = event -> notifySubscribers(event);

// Register listener
fsm.registerStateTransitionListener(listener);

// Trigger transitions
fsm.read(Input.TRIGGER);

// Unregister when no longer needed (e.g., during teardown)
fsm.unregisterStateTransitionListener(listener);
```

* **Idempotency:** Registering the same listener instance multiple times does not result in duplicate events.
* **Thread-Safety:** Both `registerStateTransitionListener` and `unregisterStateTransitionListener` are thread-safe and lock-protected.

---

## 3. Asynchronous Offloading

Because listener callbacks execute **synchronously under the machine's internal lock**, long-running tasks (such as network I/O or heavy database writes) should be dispatched asynchronously:

```java title="AsyncEventDispatcher.java"
import org.quurz.automata.StateTransitionEventListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncEventDispatcher {

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public <S> StateTransitionEventListener<S> async(StateTransitionEventListener<S> target) {
        return event -> executor.submit(() -> target.stateTransition(event));
    }
}

// Usage:
fsm.registerStateTransitionListener(dispatcher.async(event -> sendToKafka(event)));
```

---

## 4. Best Practices

1. **Keep callbacks fast:** Avoid blocking operations in direct synchronous listeners.
2. **Handle errors locally:** Exceptions thrown inside a listener propagate out of `read(...)` and will abort the remaining listeners.
3. **Use weak references or explicit unregistration:** Prevent memory leaks in long-lived state machines by unregistering temporary UI or request-scoped listeners.

---

## See Also

* [`StateTransitionEventListener` Reference](/reference/automata/statetransitioneventlistener/)
* [`StateTransitionEvent` Reference](/reference/automata/statetransitionevent/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
