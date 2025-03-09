package com.lonetrail.util;

import jdk.internal.misc.Unsafe;
import jdk.internal.reflect.ReflectionFactory;

import java.lang.reflect.Field;

public final class UnsafeReflect {
	static final Unsafe unsafe;
	static final ReflectionFactory reflectionFactory;

	static {
		try {
			Demodulator.makeModuleOpen(Object.class.getModule(), "jdk.internal.misc", UnsafeReflect.class.getModule());
			Demodulator.makeModuleOpen(Object.class.getModule(), "jdk.internal.reflect", UnsafeReflect.class.getModule());

			var un = Unsafe.class.getDeclaredField("theUnsafe");
			un.setAccessible(true);
			unsafe = (Unsafe) un.get(null);

			Field rf = ReflectionFactory.class.getDeclaredField("soleInstance");
			rf.setAccessible(true);
			reflectionFactory = (ReflectionFactory) rf.get(null);
		} catch (Throwable e) {
			throw new RuntimeException();
		}
	}

	private UnsafeReflect() {}

	public static void set(Class<?> type, Object object, String name, Object value) {
		try {
			Field field = type.getDeclaredField(name);
			unsafe.putReference(object, unsafe.objectFieldOffset(field), value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
