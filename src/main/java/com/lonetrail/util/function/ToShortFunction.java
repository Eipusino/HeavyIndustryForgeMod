package com.lonetrail.util.function;

@FunctionalInterface
public interface ToShortFunction<T> {
	short applyAsShort(T t);
}
