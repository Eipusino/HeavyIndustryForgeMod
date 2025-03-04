package com.lonetrail.util.struct;

import com.lonetrail.util.Threads;

import java.util.Comparator;

/**
 * Provides methods to sort arrays of objects. Sorting requires working memory and this class allows that memory to be reused to
 * avoid allocation. The sorting is otherwise identical to the Arrays.sort methods (uses timsort).<br>
 * <br>
 * Note that sorting primitive arrays with the Arrays.sort methods does not allocate memory (unless sorting large arrays of char,
 * short, or byte).
 *
 * @author Nathan Sweet
 */
public class Sort {
	private static final ThreadLocal<Sort> instance = Threads.local(Sort::new);

	@SuppressWarnings("rawtypes")
	private TimSort timSort;
	private ComparableTimSort comparableTimSort;

	/** Returns a Sort instance for convenience. Multiple threads must not use this instance at the same time. */
	public static Sort instance() {
		return instance.get();
	}

	public <T> void sort(Seq<T> a) {
		if (comparableTimSort == null) comparableTimSort = new ComparableTimSort();
		comparableTimSort.doSort(a.items, 0, a.size);
	}

	public <T> void sort(T[] a) {
		if (comparableTimSort == null) comparableTimSort = new ComparableTimSort();
		comparableTimSort.doSort(a, 0, a.length);
	}

	public <T> void sort(T[] a, int fromIndex, int toIndex) {
		if (comparableTimSort == null) comparableTimSort = new ComparableTimSort();
		comparableTimSort.doSort(a, fromIndex, toIndex);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> void sort(Seq<T> a, Comparator<? super T> c) {
		if (timSort == null) timSort = new TimSort();
		timSort.doSort(a.items, c, 0, a.size);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> void sort(T[] a, Comparator<? super T> c) {
		if (timSort == null) timSort = new TimSort();
		timSort.doSort(a, c, 0, a.length);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public <T> void sort(T[] a, Comparator<? super T> c, int fromIndex, int toIndex) {
		if (timSort == null) timSort = new TimSort();
		timSort.doSort(a, c, fromIndex, toIndex);
	}
}
