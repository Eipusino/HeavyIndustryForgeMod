package heavyindustry.util;

import net.minecraft.util.ToFloatFunction;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public final class Structs {
	private Structs() {}

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
					Object value = Unsafes.get(field, object);

					if (value == null) {
						builder.append("null");

						continue;
					}

					Class<?> valueType = value.getClass();

					if (valueType.isArray()) {
						// I think using instanceof would be better.
						switch (value) {
							case float[] array -> append(builder, array);
							case int[] array -> append(builder, array);
							case boolean[] array -> append(builder, array);
							case byte[] array -> append(builder, array);
							case char[] array -> append(builder, array);
							case double[] array -> append(builder, array);
							case long[] array -> append(builder, array);
							case short[] array -> append(builder, array);
							case Object[] array -> append(builder, array);
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

		if (type.isArray()) return "[" + toDescriptor(type.componentType());

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
			idx = selects(items, comp, kthLowest, size);
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

	public static <T> int selects(T[] items, Comparator<T> comp, int kthLowest, int size) {
		return selectIndex(items, comp, 0, size - 1, kthLowest);
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

	public static <T> boolean any(T[] array, Predicate<T> pred) {
		for (T e : array) if (pred.test(e)) return true;
		return false;
	}

	public static <T> boolean all(T[] array, Predicate<T> pred) {
		for (T e : array) if (!pred.test(e)) return false;
		return true;
	}

	public static <T> void each(T[] array, Consumer<? super T> cons) {
		each(array, 0, array.length, cons);
	}

	public static <T> void each(T[] array, int offset, int length, Consumer<? super T> cons) {
		for (int i = offset, len = i + length; i < len; i++) cons.accept(array[i]);
	}

	public static <T> Single<T> iter(T item) {
		return new Single<>(item);
	}

	@SafeVarargs
	public static <T> Iter<T> iter(T... array) {
		return iter(array, 0, array.length);
	}

	public static <T> Iter<T> iter(T[] array, int offset, int length) {
		return new Iter<>(array, offset, length);
	}

	public static <T> Chain<T> chain(Iterator<T> first, Iterator<T> second) {
		return new Chain<>(first, second);
	}

	public static <T, R> R reduce(T[] array, R initial, BiFunction<T, R, R> reduce) {
		for (T item : array) initial = reduce.apply(item, initial);
		return initial;
	}

	public static <T> int reduceInt(T[] array, int initial, ReduceInt<T> reduce) {
		for (T item : array) initial = reduce.get(item, initial);
		return initial;
	}

	public static <T> int sumInt(T[] array, ToIntFunction<T> extract) {
		return reduceInt(array, 0, (item, accum) -> accum + extract.applyAsInt(item));
	}

	public static <T> float reduceFloat(T[] array, float initial, ReduceFloat<T> reduce) {
		for (T item : array) initial = reduce.get(item, initial);
		return initial;
	}

	public static <T> float average(T[] array, ToFloatFunction<T> extract) {
		return reduceFloat(array, 0f, (item, accum) -> accum + extract.applyAsFloat(item)) / array.length;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] resize(T[] array, int newSize, T fill) {
		return resize(array, size -> (T[]) Array.newInstance(array.getClass().componentType(), newSize), newSize, fill);
	}

	public static <T> T[] resize(T[] array, ArrayCreator<T> create, int newSize, T fill) {
		if (array.length == newSize) return array;

		T[] out = create.get(newSize);
		System.arraycopy(array, 0, out, 0, Math.min(array.length, newSize));

		if (fill != null && newSize > array.length) Arrays.fill(out, array.length, newSize, fill);
		return out;
	}

	public static <T> boolean arrayEquals(T[] first, T[] second, BiPredicate<T, T> equals) {
		if (first.length != second.length) return false;
		for (int i = 0; i < first.length; i++) {
			if (!equals.test(first[i], second[i])) return false;
		}
		return true;
	}

	public static class Single<T> implements Iterable<T>, Iterator<T> {
		protected final T item;
		protected boolean done;

		public Single(T t) {
			item = t;
		}

		@Override
		public Single<T> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return !done;
		}

		@Override
		public T next() {
			if (done) return null;
			done = true;
			return item;
		}

		public void each(Consumer<? super T> cons) {
			if (!done) cons.accept(item);
		}
	}

	public static class Iter<T> implements Iterable<T>, Iterator<T> {
		protected final T[] array;
		protected final int offset, length;
		protected int index = 0;

		public Iter(T[] arr, int off, int len) {
			array = arr;
			offset = off;
			length = len;
		}

		public int length() {
			return length;
		}

		public void reset() {
			index = 0;
		}

		@Override
		public Iter<T> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return index < length - offset;
		}

		@Override
		public T next() {
			return hasNext() ? array[offset + index++] : null;
		}

		public void each(Consumer<? super T> cons) {
			while (hasNext()) cons.accept(array[offset + index++]);
		}
	}

	public static class Chain<T> implements Iterable<T>, Iterator<T> {
		protected final Iterator<T> first, second;

		public Chain(Iterator<T> fir, Iterator<T> sec) {
			first = fir;
			second = sec;
		}

		@Override
		public Chain<T> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return first.hasNext() || second.hasNext();
		}

		@Override
		public T next() {
			return first.hasNext() ? first.next() : second.next();
		}

		public void each(Consumer<? super T> cons) {
			while (first.hasNext()) cons.accept(first.next());
			while (second.hasNext()) cons.accept(second.next());
		}
	}

	public interface ReduceInt<T> {
		int get(T item, int accum);
	}

	public interface ReduceFloat<T> {
		float get(T item, float accum);
	}

	public interface ArrayCreator<T> {
		T[] get(int size);
	}
}
