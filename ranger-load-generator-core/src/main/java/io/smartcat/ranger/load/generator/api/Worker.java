package io.smartcat.ranger.load.generator.api;

import java.util.function.Consumer;

/**
 * Worker interface providing API for {@link io.smartcat.ranger.load.generator.LoadGenerator} task execution.
 *
 * @param <T> Type of data worker accepts.
 */
public interface Worker<T> extends Consumer<T> {

}