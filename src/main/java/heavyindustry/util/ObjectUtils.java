package heavyindustry.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ObjectUtils {
	private ObjectUtils() {}

	/** Used to optimize code conciseness in specific situations. */
	public static <T> T requireInstance(Class<?> type, T obj) {
		if (obj == null || type.isInstance(obj))
			return obj;
		throw new IllegalArgumentException("obj cannot be casted to " + type.getName());
	}

	/** Used to optimize code conciseness in specific situations. */
	public static <T> T requireNonNullInstance(Class<?> type, T obj) {
		if (type.isInstance(obj))
			return obj;
		throw new IllegalArgumentException("obj is not an instance of " + type.getName());
	}

	public static <T> T requireAssignableFrom(Class<?> type, T value) {
		if (value == null || type.isAssignableFrom(value.getClass()))
			return value;
		throw new IllegalArgumentException(value.getClass() + " cannot be cast to " + type.getName());
	}

	/**
	 * Returns a string reporting the value of each declared field, via reflection.
	 * <p>Static fields are automatically skipped. Produces output like:
	 * <p>{@code "SimpleClassName[integer=1234, string=hello, character=c, intArray=[1, 2, 3], object=java.lang.Object@1234abcd, none=null]"}.
	 * <p>If there is an exception in obtaining the value of a certain field, it will result in:
	 * <p>{@code "SimpleClassName[unknown=???]"}.
	 *
	 * @param last Should the fields of the super class be retrieved.
	 */
	public static String toString(Object object, boolean last) {
		if (object == null) return "null";

		Class<?> type = object.getClass();

		StringBuilder builder = new StringBuilder();
		builder.append(type.getSimpleName()).append('[');
		int i = 0;
		while (type != null) {
			for (Field field : type.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}

				if (i++ > 0) {
					builder.append(", ");
				}

				builder.append(field.getName()).append('=');

				try {
					Object value = UnsafeUtils.get(field, object);

					if (value == null) {
						builder.append("null");

						continue;
					}

					Class<?> valueType = value.getClass();

					if (valueType.isArray()) {
						// I think using instanceof would be better.
						switch (value) {
							case float[] array -> ArrayUtils.append(builder, array);
							case int[] array -> ArrayUtils.append(builder, array);
							case boolean[] array -> ArrayUtils.append(builder, array);
							case byte[] array -> ArrayUtils.append(builder, array);
							case char[] array -> ArrayUtils.append(builder, array);
							case double[] array -> ArrayUtils.append(builder, array);
							case long[] array -> ArrayUtils.append(builder, array);
							case short[] array -> ArrayUtils.append(builder, array);
							case Object[] array -> ArrayUtils.append(builder, array);
							default -> builder.append("???");// It shouldn't have happened...
						}
					} else {
						builder.append(value);
					}
				} catch (Exception e) {
					builder.append("???");
				}
			}

			type = last ? type.getSuperclass() : null;
		}

		return builder.append(']').toString();
	}

	/**
	 * Convert Class object to JVM type descriptor.
	 * <p>Example:
	 * <ul>
	 *     <li>boolean -> Z
	 *     <li>int -> I
	 *     <li>long -> J
	 *     <li>float[] -> [F
	 *     <li>java.lang.Object -> Ljava/lang/Object;
	 *     <li>java.lang.invoke.MethodHandles.Lookup -> Ljava/lang/invoke/MethodHandles$Lookup;
	 *     <li>mindustry.world.Tile[][] -> [[Lmindustry/world/Tile;
	 * </ul>
	 *
	 * @param type The Class object to be converted
	 * @return JVM type descriptor string
	 * @throws NullPointerException If {@code type} is null.
	 */
	public static String toDescriptor(Class<?> type) {
		if (type == null) throw new NullPointerException("param 'type' is null");

		if (type.isArray()) return "[" + toDescriptor(type.getComponentType());

		if (type.isPrimitive()) {
			if (type == void.class) return "V";
			else if (type == boolean.class) return "Z";
			else if (type == byte.class) return "B";
			else if (type == short.class) return "S";
			else if (type == int.class) return "I";
			else if (type == long.class) return "J";
			else if (type == float.class) return "F";
			else if (type == double.class) return "D";
			else if (type == char.class) return "C";
			else throw new IllegalArgumentException("unknown type of " + type);// should not happen
		}

		return "L" + type.getName().replace('.', '/') + ";";
	}
}
