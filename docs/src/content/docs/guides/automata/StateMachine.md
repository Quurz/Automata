---
title: Designing with StateMachine
description: Best practices for leveraging the StateMachine interface for abstraction, dependency injection, and testability.
---

import { Tabs, TabItem } from '@astrojs/starlight/components';

`StateMachine<S, IA, OA>` is the core abstraction interface in Automata. By depending on `StateMachine` rather than concrete classes, your business logic remains decoupled, highly testable, and flexible.

---

## 1. Programming to the Interface

When building services, controllers, or processors, always accept `StateMachine<S, IA, OA>` as a dependency:

```java title="OrderProcessingService.java"
import org.quurz.automata.StateMachine;

public class OrderProcessingService {

    private final StateMachine<OrderState, OrderEvent, OrderAction> stateMachine;

    public OrderProcessingService(StateMachine<OrderState, OrderEvent, OrderAction> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public OrderAction processEvent(OrderEvent event) {
        return stateMachine.read(event);
    }
}
```

---

## 2. Implementing Custom Decorators & Wrappers

Because `StateMachine` exposes a single, clean method (`read`), decorating or instrumenting state machines is straightforward:

<Tabs>
  <TabItem label="Logging Decorator">
    ```java title="LoggingStateMachine.java"
    import org.quurz.automata.StateMachine;
    import org.checkerframework.checker.nullness.qual.NonNull;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class LoggingStateMachine<S, IA, OA> implements StateMachine<S, IA, OA> {
        private static final Logger log = LoggerFactory.getLogger(LoggingStateMachine.class);
        private final StateMachine<S, IA, OA> delegate;

        public LoggingStateMachine(StateMachine<S, IA, OA> delegate) {
            this.delegate = delegate;
        }

        @Override
        public @NonNull OA read(final @NonNull IA input) {
            log.info("Processing input: {}", input);
            OA output = delegate.read(input);
            log.info("Generated output: {}", output);
            return output;
        }
    }
    ```
  </TabItem>
  <TabItem label="Metrics Decorator">
    ```java title="MetricsStateMachine.java"
    import org.quurz.automata.StateMachine;
    import org.checkerframework.checker.nullness.qual.NonNull;

    public class MetricsStateMachine<S, IA, OA> implements StateMachine<S, IA, OA> {
        private final StateMachine<S, IA, OA> delegate;
        private long transitionCount = 0;

        public MetricsStateMachine(StateMachine<S, IA, OA> delegate) {
            this.delegate = delegate;
        }

        @Override
        public @NonNull OA read(final @NonNull IA input) {
            transitionCount++;
            return delegate.read(input);
        }

        public long getTransitionCount() {
            return transitionCount;
        }
    }
    ```
  </TabItem>
</Tabs>

---

## 3. Unit Testing & Mocking

Using the interface makes unit testing downstream components trivial without needing a full finite state machine setup:

```java title="OrderServiceTest.java"
import org.junit.jupiter.api.Test;
import org.quurz.automata.StateMachine;
import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest {

    @Test
    void serviceDelegatesCorrectlyToStateMachine() {
        // Simple stub lambda or mock
        StateMachine<OrderState, OrderEvent, OrderAction> stub =
            event -> OrderAction.SEND_CONFIRMATION_EMAIL;

        OrderProcessingService service = new OrderProcessingService(stub);
        OrderAction action = service.processEvent(OrderEvent.PAYMENT_RECEIVED);

        assertThat(action).isEqualTo(OrderAction.SEND_CONFIRMATION_EMAIL);
    }
}
```

---

## See Also

* [`StateMachine` Reference](/reference/automata/statemachine/)
* [Guide: Finite State Machines in Practice](/guides/automata/finitestatemachine/)
* [`FiniteStateMachine` Reference](/reference/automata/finitestatemachine/)
