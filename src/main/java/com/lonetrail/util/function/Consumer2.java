package com.lonetrail.util.function;

@FunctionalInterface
public interface Consumer2<A, B> {
	void accept(A a, B b);
}
