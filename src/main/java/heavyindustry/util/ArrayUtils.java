package heavyindustry.util;

public final class ArrayUtils {
	private ArrayUtils() {}

	/**
	 * Convert vararg to an array.
	 * Returns an array containing the specified elements.
	 *
	 * @apiNote Never use for generic array fields.
	 */
	@SafeVarargs
	public static <T> T[] arrayOf(T... elements) {
		return elements;
	}

	public static boolean[] boolOf(boolean... bools) {
		return bools;
	}

	public static byte[] byteOf(byte... bytes) {
		return bytes;
	}

	public static short[] shortOf(short... shorts) {
		return shorts;
	}

	public static int[] intOf(int... ints) {
		return ints;
	}

	public static long[] longOf(long... longs) {
		return longs;
	}

	public static float[] floatOf(float... floats) {
		return floats;
	}

	public static double[] doubleOf(double... doubles) {
		return doubles;
	}

	public static int[] sortInt(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int tmp = arr[i];

			int j = i;
			while (j > 0 && tmp < arr[j - 1]) {
				arr[j] = arr[j - 1];
				j--;
			}

			if (j != i) {
				arr[j] = tmp;
			}
		}
		return arr;
	}

	public static void shellSortInt(int[] arr) {
		int temp;
		for (int step = arr.length / 2; step >= 1; step /= 2) {
			for (int i = step; i < arr.length; i++) {
				temp = arr[i];
				int j = i - step;
				while (j >= 0 && arr[j] > temp) {
					arr[j + step] = arr[j];
					j -= step;
				}
				arr[j + step] = temp;
			}
		}
	}

	/**
	 * Used to avoid performance overhead caused by creating an instance of {@link StringBuilder}.
	 *
	 * @throws NullPointerException If {@code builder} is null.
	 * //@see Arrays#toString(Object[])
	 */
	public static void append(StringBuilder builder, Object[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}

		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, boolean[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, byte[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, short[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, int[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, long[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, char[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');

		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, float[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}

	public static void append(StringBuilder builder, double[] array) {
		if (array == null) {
			builder.append("null");
			return;
		}
		int max = array.length - 1;
		if (max == -1) {
			builder.append("[]");
			return;
		}

		builder.append('[');
		for (int i = 0; i < array.length; i++) {
			builder.append(array[i]);
			if (i == max) {
				builder.append(']');
				break;
			}
			builder.append(", ");
		}
	}
}
