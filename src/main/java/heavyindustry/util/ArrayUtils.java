package heavyindustry.util;

import net.minecraft.util.ToFloatFunction;

import java.util.Comparator;

public final class ArrayUtils {
	private ArrayUtils() {}

	/**
	 * Convert vararg to an array.
	 * Returns an array containing the specified elements.
	 * <p><strong>Never use for generic array fields.</strong>
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

	public static <T> Comparator<T> comparingFloat(ToFloatFunction<? super T> keyExtractor) {
		return (c1, c2) -> Float.compare(keyExtractor.applyAsFloat(c1), keyExtractor.applyAsFloat(c2));
	}

	public static <T> T select(T[] items, Comparator<T> comp, int kthLowest, int size) {
		int idx = selectIndex(items, comp, kthLowest, size);
		return items[idx];
	}

	public static <T> int selectIndex(T[] items, Comparator<T> comp, int kthLowest, int size) {
		if (size < 1) {
			throw new IllegalArgumentException("cannot select from empty array (size < 1)");
		} else if (kthLowest > size) {
			throw new IllegalArgumentException("Kth rank is larger than size. k: " + kthLowest + ", size: " + size);
		}
		int idx;
		// naive partial selection sort almost certain to outperform quickselect where n is min or max
		if (kthLowest == 1) {
			// find min
			idx = fastMin(items, comp, size);
		} else if (kthLowest == size) {
			// find max
			idx = fastMax(items, comp, size);
		} else {
			// quickselect a better choice for cases of k between min and max
			idx = selectIndex0(items, comp, kthLowest, size);
		}
		return idx;
	}

	/** Faster than quickselect for n = min */
	private static <T> int fastMin(T[] items, Comparator<T> comp, int size) {
		int lowestIdx = 0;
		for (int i = 1; i < size; i++) {
			int comparison = comp.compare(items[i], items[lowestIdx]);
			if (comparison < 0) {
				lowestIdx = i;
			}
		}
		return lowestIdx;
	}

	/** Faster than quickselect for n = max */
	private static <T> int fastMax(T[] items, Comparator<T> comp, int size) {
		int highestIdx = 0;
		for (int i = 1; i < size; i++) {
			int comparison = comp.compare(items[i], items[highestIdx]);
			if (comparison > 0) {
				highestIdx = i;
			}
		}
		return highestIdx;
	}

	private static <T> int selectIndex0(T[] items, Comparator<T> comp, int n, int size) {
		return selectIndex(items, comp, 0, size - 1, n);
	}

	private static <T> int partition(T[] array, Comparator<? super T> comp, int left, int right, int pivot) {
		T pivotValue = array[pivot];
		swap(array, right, pivot);
		int storage = left;
		for (int i = left; i < right; i++) {
			if (comp.compare(array[i], pivotValue) < 0) {
				swap(array, storage, i);
				storage++;
			}
		}
		swap(array, right, storage);
		return storage;
	}

	private static <T> int selectIndex(T[] array, Comparator<? super T> comp, int left, int right, int kthLowest) {
		if (left == right) return left;
		int pivotIndex = medianOfThreePivot(array, comp, left, right);
		int pivotNewIndex = partition(array, comp, left, right, pivotIndex);
		int pivotDist = (pivotNewIndex - left) + 1;
		int result;
		if (pivotDist == kthLowest) {
			result = pivotNewIndex;
		} else if (kthLowest < pivotDist) {
			result = selectIndex(array, comp, left, pivotNewIndex - 1, kthLowest);
		} else {
			result = selectIndex(array, comp, pivotNewIndex + 1, right, kthLowest - pivotDist);
		}
		return result;
	}

	/** Median of Three has the potential to outperform a random pivot, especially for partially sorted arrays */
	private static <T> int medianOfThreePivot(T[] array, Comparator<? super T> comp, int leftIdx, int rightIdx) {
		T left = array[leftIdx];
		int midIdx = (leftIdx + rightIdx) / 2;
		T mid = array[midIdx];
		T right = array[rightIdx];

		// spaghetti median of three algorithm
		// does at most 3 comparisons
		if (comp.compare(left, mid) > 0) {
			if (comp.compare(mid, right) > 0) {
				return midIdx;
			} else if (comp.compare(left, right) > 0) {
				return rightIdx;
			} else {
				return leftIdx;
			}
		} else {
			if (comp.compare(left, right) > 0) {
				return leftIdx;
			} else if (comp.compare(mid, right) > 0) {
				return rightIdx;
			} else {
				return midIdx;
			}
		}
	}

	private static <T> void swap(T[] array, int left, int right) {
		T tmp = array[left];
		array[left] = array[right];
		array[right] = tmp;
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
