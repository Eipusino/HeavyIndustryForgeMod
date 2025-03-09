package com.lonetrail.util;

import com.lonetrail.util.function.ToBooleanFunction;
import com.lonetrail.util.function.ToByteFunction;
import com.lonetrail.util.function.ToFloatFunction;
import com.lonetrail.util.function.ToShortFunction;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public final class Structs {
	private Structs() {}

	@SafeVarargs
	public static <T> T[] arrayOf(T... arg) {
		return arg;
	}

	public static boolean[] boolOf(boolean... arg) {
		return arg;
	}

	public static byte[] byteOf(byte... arg) {
		return arg;
	}

	public static short[] shortOf(short... arg) {
		return arg;
	}

	public static int[] intOf(int... arg) {
		return arg;
	}

	public static long[] longOf(long... arg) {
		return arg;
	}

	public static float[] floatOf(float... arg) {
		return arg;
	}

	public static double[] doubleOf(double... arg) {
		return arg;
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
		return (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
	}

	public static <T> Comparator<T> comparingBoolean(ToBooleanFunction<? super T> keyExtractor) {
		return (c1, c2) -> Boolean.compare(keyExtractor.applyAsBoolean(c1), keyExtractor.applyAsBoolean(c2));
	}

	public static <T> Comparator<T> comparingByte(ToByteFunction<? super T> keyExtractor) {
		return (c1, c2) -> Byte.compare(keyExtractor.applyAsByte(c1), keyExtractor.applyAsByte(c2));
	}

	public static <T> Comparator<T> comparingShort(ToShortFunction<? super T> keyExtractor) {
		return (c1, c2) -> Short.compare(keyExtractor.applyAsShort(c1), keyExtractor.applyAsShort(c2));
	}

	public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
		return (c1, c2) -> Integer.compare(keyExtractor.applyAsInt(c1), keyExtractor.applyAsInt(c2));
	}

	public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor) {
		return (c1, c2) -> Long.compare(keyExtractor.applyAsLong(c1), keyExtractor.applyAsLong(c2));
	}

	public static <T> Comparator<T> comparingFloat(ToFloatFunction<? super T> keyExtractor) {
		return (c1, c2) -> Float.compare(keyExtractor.applyAsFloat(c1), keyExtractor.applyAsFloat(c2));
	}

	public static <T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor) {
		return (c1, c2) -> Double.compare(keyExtractor.applyAsDouble(c1), keyExtractor.applyAsDouble(c2));
	}

	@SuppressWarnings("unchecked")
	public static <E extends Throwable, R> R sneak(Throwable e) throws E {
		throw (E) e;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cast(Object inst) {
		return (T) inst;
	}
}
