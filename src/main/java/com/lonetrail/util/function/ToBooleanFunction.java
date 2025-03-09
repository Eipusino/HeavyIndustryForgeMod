package com.lonetrail.util.function;

@FunctionalInterface
public interface ToBooleanFunction<T> {
	boolean applyAsBoolean(T t);
}
