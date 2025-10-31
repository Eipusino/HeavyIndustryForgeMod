package heavyindustry.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * 一个基于{@link TreeSet}实现的有序可重集，向这个集合中加入元素会将其插入到合适其大小的位置，根据比较器，这个集合中的元素一定是有序的
 * <p>区别于{@link TreeSet}，这个集合允许多个比较器认为是相等的对象。
 * <p>插入复杂度通常为o(logn)，但如果比较器比较的值很集中，这个集合可能会退化到o(n)，遍历这个集合时，遍历获得的元素时有序的
 */
public class TreeSequence<T> implements Iterable<T> {
	private final LinkedList<T> tmp = new LinkedList<>();

	Comparator<T> comparator;

	int size;

	TreeSet<LinkedList<T>> set;

	public TreeSequence(Comparator<T> cmp) {
		comparator = cmp;
		set = new TreeSet<>((a, b) -> cmp.compare(a.getFirst(), b.getFirst()));
	}

	public TreeSequence() {
		set = new TreeSet<>();
	}

	public void add(T item) {
		tmp.clear();
		tmp.addFirst(item);
		LinkedList<T> t = set.ceiling(tmp);
		if (t == null || set.floor(tmp) != t) {
			t = new LinkedList<>();
			t.addFirst(item);
			set.add(t);
		} else {
			t.addFirst(item);
		}
		size++;
	}

	public boolean remove(T item) {
		tmp.clear();
		tmp.addFirst(item);

		LinkedList<T> t = set.ceiling(tmp);
		if (t != null && set.floor(tmp) == t) {
			if (t.size() == 1 && t.getFirst().equals(item)) set.remove(t);
			t.remove(item);
			size--;
			return true;
		}
		return false;
	}

	public int size() {
		return size;
	}

	public boolean removeIf(Function<T, Boolean> boolf) {
		boolean test = false;
		TreeItr itr = iterator();
		T item;
		while (itr.hasNext()) {
			item = itr.next();
			if (boolf.apply(item)) {
				itr.remove();
				size--;
				test = true;
			}
		}

		return test;
	}

	public void clear() {
		set.clear();
		size = 0;
	}

	public boolean isEmpty() {
		return set.isEmpty();
	}

	public T[] toArray(T[] arr) {
		T[] list = Arrays.copyOf(arr, size);
		int index = 0;
		for (T item : this) {
			list[index++] = item;
		}
		return list;
	}

	@Override
	public TreeItr iterator() {
		return new TreeItr();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		for (LinkedList<T> list : set) {
			builder.append(list).append(", ");
		}
		return builder.substring(0, builder.length() - 2) + "}";
	}

	public class TreeItr implements Iterator<T> {
		Iterator<LinkedList<T>> itr = set.iterator();
		Iterator<T> listItr;
		LinkedList<T> curr;

		@Override
		public boolean hasNext() {
			return (listItr != null && listItr.hasNext()) || (itr.hasNext() && (listItr = (curr = itr.next()).iterator()).hasNext());
		}

		@Override
		public T next() {
			return listItr.next();
		}

		@Override
		public void remove() {
			listItr.remove();
			if (curr.isEmpty()) itr.remove();
		}
	}
}
