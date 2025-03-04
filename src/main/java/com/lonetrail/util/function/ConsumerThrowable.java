package com.lonetrail.util.function;

/** A cons that throws something. */
@FunctionalInterface
public interface ConsumerThrowable<T, E extends Throwable> {
	void accept(T t) throws E;
}
