package com.lonetrail.util;

import com.lonetrail.util.function.BoolFunction;
import com.lonetrail.util.function.FloatFunction;
import com.lonetrail.util.function.IntFunction;
import com.lonetrail.util.function.LongFunction;

import java.util.Comparator;
import java.util.function.Function;

public final class Structs {
	private Structs() {
	}

	@SafeVarargs
	public static <T> T[] arrayOf(T... arg) {
		return arg;
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
		return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
	}

	public static <T> Comparator<T> comparingFloat(FloatFunction<? super T> keyExtractor) {
		return (c1, c2) -> Float.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
	}

	public static <T> Comparator<T> comparingInt(IntFunction<? super T> keyExtractor) {
		return (c1, c2) -> Integer.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
	}

	public static <T> Comparator<T> comparingLong(LongFunction<? super T> keyExtractor) {
		return (c1, c2) -> Long.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
	}

	public static <T> Comparator<T> comparingBool(BoolFunction<? super T> keyExtractor) {
		return (c1, c2) -> Boolean.compare(keyExtractor.apply(c1), keyExtractor.apply(c2));
	}
}
