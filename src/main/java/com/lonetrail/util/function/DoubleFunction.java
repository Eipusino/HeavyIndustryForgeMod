package com.lonetrail.util.function;

@FunctionalInterface
public interface DoubleFunction<T> {
	double apply(T t);
}
