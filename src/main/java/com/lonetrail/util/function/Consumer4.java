package com.lonetrail.util.function;

@FunctionalInterface
public interface Consumer4<T, N, I, P> {
	void accept(T param1, N param2, I param3, P param4);
}
