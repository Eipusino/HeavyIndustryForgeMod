package com.lonetrail.util.function;

/** A cons that throws something. */
@FunctionalInterface
public interface ConsumerThrows<T, E extends Throwable> {
	void accept(T t) throws E;
}
