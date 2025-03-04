package com.lonetrail.util.function;

@FunctionalInterface
public interface Function2<A, B, R> {
	R get(A a, B b);
}
