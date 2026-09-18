package org.quurz.automata;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * <div>
 *     <p>
 *         Represents an event triggered when a state transition occurs in a finite state machine.
 *     </p>
 *     <p>
 *         Contains the previous state ({@link #oldState()}) before the transition and the new state
 *         ({@link #newState()}) after the transition.
 *     </p>
 * </div>
 *
 * @param oldState the state of the machine before the transition; must not be {@code null}
 * @param newState the state of the machine after the transition; must not be {@code null}
 * @param <S> the state type
 *
 * @since 1.0.0
 */
public record StateTransitionEvent<S>(@NonNull S oldState, @NonNull S newState) {}
