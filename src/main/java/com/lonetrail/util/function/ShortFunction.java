package com.lonetrail.util.function;

@FunctionalInterface
public interface ShortFunction<T> {
	boolean apply(T t);
}
