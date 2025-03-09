package com.lonetrail.util.function;

@FunctionalInterface
public interface ToByteFunction<T> {
	byte applyAsByte(T t);
}
