package com.lonetrail.util.function;

@FunctionalInterface
public interface IntFunction<T> {
	int apply(T t);
}
