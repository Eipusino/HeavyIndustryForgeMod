package heavyindustry.util;

import jdk.internal.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

import static heavyindustry.util.ObjectUtils.requireAssignableFrom;
import static heavyindustry.util.ObjectUtils.requireNonNullInstance;

public final class UnsafeUtils {
	static Unsafe unsafe = Unsafe.getUnsafe();

	private UnsafeUtils() {}

	public static <T> T getObject(Class<?> type, String name, Object object) {
		try {
			return getObject(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieve the value of a field through {@code Unsafe}. If the field is {@code static}, object can be {@code null}.
	 * Otherwise, {@code object} must not be {@code null} and be an instance of {@code field.getDeclaringClass()}.
	 *
	 * @throws IllegalArgumentException If any of the following  is true:
	 *                                  <ul><li>If the field type is a primitive type.
	 *                                  <li>If the field is not {@code static} and the {@code object} is not an
	 *                                                                   instance of {@code field.getDeclaringClass()} or {@code null}.</ul>
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObject(Field field, Object object) {
		if (field.getType().isPrimitive()) throw new IllegalArgumentException("Method 'getObject' does not support field of primitive types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);

		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return (T) (Modifier.isVolatile(modifiers) ?
				unsafe.getReferenceVolatile(o, offset) :
				unsafe.getReference(o, offset));
	}

	public static boolean getBool(Class<?> type, String name, Object object) {
		try {
			return getBool(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Retrieve the value of a field through {@code Unsafe}. If the field is {@code static}, object can be {@code null}.
	 * Otherwise, {@code object} must not be {@code null} and be an instance of {@code field.getDeclaringClass()}.
	 *
	 * @throws IllegalArgumentException If any of the following  is true:
	 *                                  <ul><li>If the field type is not boolean.
	 *                                  <li>If the field is not {@code static} and the {@code object} is not an
	 *                                  instance of {@code field.getDeclaringClass()} or {@code null}.</ul>
	 */
	public static boolean getBool(Field field, Object object) {
		if (field.getType() != boolean.class) throw new IllegalArgumentException("Method 'getBool' does not support field other than boolean types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getBooleanVolatile(o, offset) :
				unsafe.getBoolean(o, offset);
	}

	public static byte getByte(Class<?> type, String name, Object object) {
		try {
			return getByte(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte getByte(Field field, Object object) {
		if (field.getType() != byte.class) throw new IllegalArgumentException("Method 'getByte' does not support field other than byte types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getByteVolatile(o, offset) :
				unsafe.getByte(o, offset);
	}

	public static short getShort(Class<?> type, String name, Object object) {
		try {
			return getShort(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static short getShort(Field field, Object object) {
		if (field.getType() != short.class) throw new IllegalArgumentException("Method 'getShort' does not support field other than short types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getShortVolatile(o, offset) :
				unsafe.getShort(o, offset);
	}

	public static int getInt(Class<?> type, String name, Object object) {
		try {
			return getInt(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static int getInt(Field field, Object object) {
		if (field.getType() != int.class) throw new IllegalArgumentException("Method 'getInt' does not support field other than int types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getIntVolatile(o, offset) :
				unsafe.getInt(o, offset);
	}

	public static long getLong(Class<?> type, String name, Object object) {
		try {
			return getLong(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static long getLong(Field field, Object object) {
		if (field.getType() != long.class) throw new IllegalArgumentException("Method 'getLong' does not support field other than long types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getLongVolatile(o, offset) :
				unsafe.getLong(o, offset);
	}

	public static float getFloat(Class<?> type, String name, Object object) {
		try {
			return getFloat(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static float getFloat(Field field, Object object) {
		if (field.getType() != float.class) throw new IllegalArgumentException("Method 'getFloat' does not support field other than float types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Class<?> type = field.getDeclaringClass();

		return Modifier.isVolatile(modifiers) ?
				unsafe.getFloatVolatile(isStatic ? type : requireNonNullInstance(type, object), offset) :
				unsafe.getFloat(isStatic ? type : requireNonNullInstance(type, object), offset);
	}

	public static double getDouble(Class<?> type, String name, Object object) {
		try {
			return getDouble(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static double getDouble(Field field, Object object) {
		if (field.getType() != double.class) throw new IllegalArgumentException("Method 'getDouble' does not support field other than double types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getDoubleVolatile(o, offset) :
				unsafe.getDouble(o, offset);
	}

	public static char getChar(Class<?> type, String name, Object object) {
		try {
			return getChar(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static char getChar(Field field, Object object) {
		if (field.getType() != char.class) throw new IllegalArgumentException("Method 'getChar' does not support field other than char types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		return Modifier.isVolatile(modifiers) ?
				unsafe.getCharVolatile(o, offset) :
				unsafe.getChar(o, offset);
	}

	public static void setObject(Class<?> type, String name, Object object, Object value) {
		try {
			setObject(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Set the value of a field through {@code Unsafe}. If the field is {@code static}, object can be {@code null}.
	 * Otherwise, {@code object} must not be {@code null} and be an instance of {@code field.getDeclaringClass()}.
	 *
	 * @throws IllegalArgumentException If the field type is a primitive type.
	 * @throws ClassCastException If the field is not {@code static} and the {@code object} is not an
	 *                                  instance of {@code field.getDeclaringClass()} or {@code null}.
	 */
	public static void setObject(Field field, Object object, Object value) {
		if (field.getType().isPrimitive()) throw new IllegalArgumentException("Method 'getObject' does not support field of primitive types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);

		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putReferenceVolatile(o, offset, requireAssignableFrom(field.getType(), value));
		} else {
			unsafe.putReference(o, offset, requireAssignableFrom(field.getType(), value));
		}
	}

	public static void setBool(Class<?> type, String name, Object object, boolean value) {
		try {
			setBool(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setBool(Field field, Object object, boolean value) {
		if (field.getType() != boolean.class) throw new IllegalArgumentException("Method 'setBool' does not support field other than boolean types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putBooleanVolatile(o, offset, value);
		} else {
			unsafe.putBoolean(o, offset, value);
		}
	}

	public static void setByte(Class<?> type, String name, Object object, byte value) {
		try {
			setByte(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setByte(Field field, Object object, byte value) {
		if (field.getType() != byte.class) throw new IllegalArgumentException("Method 'setByte' does not support field other than byte types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putByteVolatile(o, offset, value);
		} else {
			unsafe.putByte(o, offset, value);
		}
	}

	public static void setShort(Class<?> type, String name, Object object, short value) {
		try {
			setShort(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setShort(Field field, Object object, short value) {
		if (field.getType() != short.class) throw new IllegalArgumentException("Method 'setShort' does not support field other than short types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putShortVolatile(o, offset, value);
		} else {
			unsafe.putShort(o, offset, value);
		}
	}

	public static void setInt(Class<?> type, String name, Object object, int value) {
		try {
			setInt(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setInt(Field field, Object object, int value) {
		if (field.getType() != int.class) throw new IllegalArgumentException("Method 'setInt' does not support field other than int types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putIntVolatile(o, offset, value);
		} else {
			unsafe.putInt(o, offset, value);
		}
	}

	public static void setLong(Class<?> type, String name, Object object, long value) {
		try {
			setLong(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setLong(Field field, Object object, long value) {
		if (field.getType() != long.class) throw new IllegalArgumentException("Method 'setLong' does not support field other than long types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putLongVolatile(o, offset, value);
		} else {
			unsafe.putLong(o, offset, value);
		}
	}

	public static void setFloat(Class<?> type, String name, Object object, float value) {
		try {
			setFloat(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setFloat(Field field, Object object, float value) {
		if (field.getType() != float.class) throw new IllegalArgumentException("Method 'setFloat' does not support field other than float types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putFloatVolatile(o, offset, value);
		} else {
			unsafe.putFloat(o, offset, value);
		}
	}

	public static void setDouble(Class<?> type, String name, Object object, double value) {
		try {
			setDouble(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setDouble(Field field, Object object, double value) {
		if (field.getType() != double.class) throw new IllegalArgumentException("Method 'setDouble' does not support field other than double types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putDoubleVolatile(o, offset, value);
		} else {
			unsafe.putDouble(o, offset, value);
		}
	}

	public static void setChar(Class<?> type, String name, Object object, char value) {
		try {
			setChar(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setChar(Field field, Object object, char value) {
		if (field.getType() != char.class) throw new IllegalArgumentException("Method 'setChar' does not support field other than char types");

		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);

		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			unsafe.putCharVolatile(o, offset, value);
		} else {
			unsafe.putChar(o, offset, value);
		}
	}

	public static Object get(Class<?> type, String name, Object object) {
		try {
			return get(type.getDeclaredField(name), object);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object get(Field field, Object object) {
		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);
		Class<?> type = field.getType();
		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			if (type.isPrimitive()) {
				if (type == int.class) return unsafe.getIntVolatile(o, offset);
				else if (type == float.class) return unsafe.getFloatVolatile(o, offset);
				else if (type == boolean.class) return unsafe.getBooleanVolatile(o, offset);
				else if (type == byte.class) return unsafe.getByteVolatile(o, offset);
				else if (type == long.class) return unsafe.getLongVolatile(o, offset);
				else if (type == double.class) return unsafe.getDoubleVolatile(o, offset);
				else if (type == char.class) return unsafe.getCharVolatile(o, offset);
				else if (type == short.class) return unsafe.getShortVolatile(o, offset);
				else throw new IllegalArgumentException("unknown type of field " + field);
			} else {
				return unsafe.getReferenceVolatile(o, offset);
			}
		} else {
			if (type.isPrimitive()) {
				if (type == int.class) return unsafe.getInt(o, offset);
				else if (type == float.class) return unsafe.getFloat(o, offset);
				else if (type == boolean.class) return unsafe.getBoolean(o, offset);
				else if (type == byte.class) return unsafe.getByte(o, offset);
				else if (type == long.class) return unsafe.getDouble(o, offset);
				else if (type == double.class) return unsafe.getLong(o, offset);
				else if (type == char.class) return unsafe.getChar(o, offset);
				else if (type == short.class) return unsafe.getShort(o, offset);
				else throw new IllegalArgumentException("unknown type of field " + type);
			} else {
				return unsafe.getReference(o, offset);
			}
		}
	}

	public static void set(Class<?> type, String name, Object object, Object value) {
		try {
			set(type.getDeclaredField(name), object, value);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static void set(Field field, Object object, Object value) {
		int modifiers = field.getModifiers();
		boolean isStatic = Modifier.isStatic(modifiers);
		long offset = isStatic ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);
		Class<?> type = field.getType();
		Object o = isStatic ? unsafe.staticFieldBase(field) : requireNonNullInstance(field.getDeclaringClass(), object);

		if (Modifier.isVolatile(modifiers)) {
			if (type.isPrimitive()) {
				if (type == int.class) unsafe.putIntVolatile(o, offset, (int) value);
				else if (type == float.class) unsafe.putFloatVolatile(o, offset, (float) value);
				else if (type == boolean.class) unsafe.putBooleanVolatile(o, offset, (boolean) value);
				else if (type == byte.class) unsafe.putByteVolatile(o, offset, (byte) value);
				else if (type == long.class) unsafe.putLongVolatile(o, offset, (long) value);
				else if (type == double.class) unsafe.putDoubleVolatile(o, offset, (double) value);
				else if (type == char.class) unsafe.putCharVolatile(o, offset, (char) value);
				else if (type == short.class) unsafe.putShortVolatile(o, offset, (short) value);
				else throw new IllegalArgumentException("unknown type of field " + field);
			} else {
				unsafe.putReferenceVolatile(o, offset, requireAssignableFrom(type, value));
			}
		} else {
			if (type.isPrimitive()) {
				if (type == int.class) unsafe.putInt(o, offset, (int) value);
				else if (type == float.class) unsafe.putFloat(o, offset, (float) value);
				else if (type == boolean.class) unsafe.putBoolean(o, offset, (boolean) value);
				else if (type == byte.class) unsafe.putByte(o, offset, (byte) value);
				else if (type == double.class) unsafe.putDouble(o, offset, (double) value);
				else if (type == long.class) unsafe.putLong(o, offset, (long) value);
				else if (type == char.class) unsafe.putChar(o, offset, (char) value);
				else if (type == short.class) unsafe.putShort(o, offset, (short) value);
				else throw new IllegalArgumentException("unknown type of field " + field);
			} else {
				unsafe.putReference(o, offset, requireAssignableFrom(type, value));
			}
		}
	}

	public static Class<?> defineClass(String name, byte[] bytes, ClassLoader loader) {
		return defineClass(name, bytes, loader, null);
	}

	public static Class<?> defineClass(String name, byte[] bytes, ClassLoader loader, ProtectionDomain protectionDomain) {
		return unsafe.defineClass(name, bytes, 0, bytes.length, loader, protectionDomain);
	}
}
