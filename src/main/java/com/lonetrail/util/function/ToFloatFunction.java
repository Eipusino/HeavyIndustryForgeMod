package com.lonetrail.util.function;

@FunctionalInterface
public interface ToFloatFunction<T> {
	float applyAsFloat(T t);
}
