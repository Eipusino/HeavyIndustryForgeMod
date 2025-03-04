package com.lonetrail.util.function;

@FunctionalInterface
public interface BoolFunction<T> {
	boolean apply(T t);
}
