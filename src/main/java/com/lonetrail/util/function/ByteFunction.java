package com.lonetrail.util.function;

@FunctionalInterface
public interface ByteFunction<T> {
	byte apply(T t);
}
