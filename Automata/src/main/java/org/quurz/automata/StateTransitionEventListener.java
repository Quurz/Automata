package org.quurz.automata;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Listener interface for receiving state transition events from a {@link FiniteStateMachine}.
 *     </p>
 *     <p>
 *         Classes interested in monitoring state transitions can implement this interface and register
 *         themselves with {@link FiniteStateMachine#registerStateTransitionListener(StateTransitionEventListener)}.
 *     </p>
 * </div>
 *
 * @param <S> the state type
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface StateTransitionEventListener<S> {

    /**
     * <div>
     *     <p>
     *         Invoked when a state transition has occurred in the monitored state machine.
     *     </p>
     * </div>
     *
     * @param stateTransitionEvent the event object containing the old and new state; must not be {@code null}
     *
     * @since 1.0.0
     */
    void stateTransition(final @NonNull StateTransitionEvent<S> stateTransitionEvent);

}
