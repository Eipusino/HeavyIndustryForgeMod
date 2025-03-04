package com.lonetrail.util.function;

@FunctionalInterface
public interface LongFunction<T> {
	long apply(T t);
}
