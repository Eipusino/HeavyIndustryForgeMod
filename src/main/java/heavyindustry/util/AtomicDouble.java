package heavyindustry.util;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * A double type atomic operation class implemented using VarHandle.
 *
 * @see VarHandle
 * @see AtomicFloat
 */
public class AtomicDouble extends Number implements Serializable {
	private static final long serialVersionUID = 572795237818305608L;

	private static final VarHandle VALUE;

	static {
		try {
			Lookup lookup = MethodHandles.lookup();
			VALUE = lookup.findVarHandle(AtomicDouble.class, "value", double.class);
		} catch (ReflectiveOperationException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private volatile double value;

	/**
	 * Creates a new AtomicInteger with the given initial value.
	 *
	 * @param initialValue the initial value
	 */
	public AtomicDouble(double initialValue) {
		value = initialValue;
	}

	/**
	 * Creates a new AtomicInteger with initial value {@code 0}.
	 */
	public AtomicDouble() {}

	/**
	 * Returns the current value,
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 *
	 * @return the current value
	 */
	public final double get() {
		return value;
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setVolatile}.
	 *
	 * @param newValue the new value
	 */
	public final void set(double newValue) {
		value = newValue;
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setRelease}.
	 *
	 * @param newValue the new value
	 * @since 1.6
	 */
	public final void lazySet(double newValue) {
		VALUE.setRelease(this, newValue);
	}

	/**
	 * Atomically sets the value to {@code newValue} and returns the old value,
	 * with memory effects as specified by {@link VarHandle#getAndSet}.
	 *
	 * @param newValue the new value
	 * @return the previous value
	 */
	public final double getAndSet(double newValue) {
		return (double) VALUE.getAndSet(this, newValue);
	}

	/**
	 * Atomically sets the value to {@code newValue}
	 * if the current value {@code == expectedValue},
	 * with memory effects as specified by {@link VarHandle#compareAndSet}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return {@code true} if successful. False return indicates that
	 * the actual value was not equal to the expected value.
	 */
	public final boolean compareAndSet(double expectedValue, double newValue) {
		return VALUE.compareAndSet(this, expectedValue, newValue);
	}

	/**
	 * Possibly atomically sets the value to {@code newValue}
	 * if the current value {@code == expectedValue},
	 * with memory effects as specified by {@link VarHandle#weakCompareAndSetPlain}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return {@code true} if successful
	 * @see #weakCompareAndSetPlain
	 * @deprecated This method has plain memory effects but the method
	 * name implies volatile memory effects (see methods such as
	 * {@link #compareAndExchange} and {@link #compareAndSet}).  To avoid
	 * confusion over plain or volatile memory effects it is recommended that
	 * the method {@link #weakCompareAndSetPlain} be used instead.
	 */
	@Deprecated(since = "9")
	public final boolean weakCompareAndSet(double expectedValue, double newValue) {
		return VALUE.weakCompareAndSetPlain(this, expectedValue, newValue);
	}

	/**
	 * Possibly atomically sets the value to {@code newValue}
	 * if the current value {@code == expectedValue},
	 * with memory effects as specified by {@link VarHandle#weakCompareAndSetPlain}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return {@code true} if successful
	 * @since 9
	 */
	public final boolean weakCompareAndSetPlain(double expectedValue, double newValue) {
		return VALUE.weakCompareAndSetPlain(this, expectedValue, newValue);
	}

	/**
	 * Atomically increments the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code getAndAdd(1)}.
	 *
	 * @return the previous value
	 */
	public final double getAndIncrement() {
		return (double) VALUE.getAndAdd(this, 1d);
	}

	/**
	 * Atomically decrements the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code getAndAdd(-1)}.
	 *
	 * @return the previous value
	 */
	public final double getAndDecrement() {
		return (double) VALUE.getAndAdd(this, -1d);
	}

	/**
	 * Atomically adds the given value to the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * @param delta the value to add
	 * @return the previous value
	 */
	public final double getAndAdd(double delta) {
		return (double) VALUE.getAndAdd(this, delta);
	}

	/**
	 * Atomically increments the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code addAndGet(1)}.
	 *
	 * @return the updated value
	 */
	public final double incrementAndGet() {
		return (double) VALUE.getAndAdd(this, 1d) + 1d;
	}

	/**
	 * Atomically decrements the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code addAndGet(-1)}.
	 *
	 * @return the updated value
	 */
	public final double decrementAndGet() {
		return (double) VALUE.getAndAdd(this, -1d) - 1d;
	}

	/**
	 * Atomically adds the given value to the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * @param delta the value to add
	 * @return the updated value
	 */
	public final double addAndGet(double delta) {
		return (double) VALUE.getAndAdd(this, delta) + delta;
	}

	/**
	 * Atomically updates (with memory effects as specified by {@link
	 * VarHandle#compareAndSet}) the current value with the results of
	 * applying the given function, returning the previous value. The
	 * function should be side-effect-free, since it may be re-applied
	 * when attempted updates fail due to contention among threads.
	 *
	 * @param updateFunction a side-effect-free function
	 * @return the previous value
	 * @since 1.8
	 */
	public final double getAndUpdate(DoubleUnaryOperator updateFunction) {
		double prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsDouble(prev);
			if (weakCompareAndSetVolatile(prev, next))
				return prev;
			haveNext = (prev == (prev = get()));
		}
	}

	/**
	 * Atomically updates (with memory effects as specified by {@link
	 * VarHandle#compareAndSet}) the current value with the results of
	 * applying the given function, returning the updated value. The
	 * function should be side-effect-free, since it may be re-applied
	 * when attempted updates fail due to contention among threads.
	 *
	 * @param updateFunction a side-effect-free function
	 * @return the updated value
	 * @since 1.8
	 */
	public final double updateAndGet(DoubleUnaryOperator updateFunction) {
		double prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsDouble(prev);
			if (weakCompareAndSetVolatile(prev, next))
				return next;
			haveNext = (prev == (prev = get()));
		}
	}

	/**
	 * Atomically updates (with memory effects as specified by {@link
	 * VarHandle#compareAndSet}) the current value with the results of
	 * applying the given function to the current and given values,
	 * returning the previous value. The function should be
	 * side-effect-free, since it may be re-applied when attempted
	 * updates fail due to contention among threads.  The function is
	 * applied with the current value as its first argument, and the
	 * given update as the second argument.
	 *
	 * @param value               the update value
	 * @param accumulatorFunction a side-effect-free function of two arguments
	 * @return the previous value
	 * @since 1.8
	 */
	public final double getAndAccumulate(int value, DoubleBinaryOperator accumulatorFunction) {
		double prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsDouble(prev, value);
			if (weakCompareAndSetVolatile(prev, next))
				return prev;
			haveNext = (prev == (prev = get()));
		}
	}

	/**
	 * Atomically updates (with memory effects as specified by {@link
	 * VarHandle#compareAndSet}) the current value with the results of
	 * applying the given function to the current and given values,
	 * returning the updated value. The function should be
	 * side-effect-free, since it may be re-applied when attempted
	 * updates fail due to contention among threads.  The function is
	 * applied with the current value as its first argument, and the
	 * given update as the second argument.
	 *
	 * @param value               the update value
	 * @param accumulatorFunction a side-effect-free function of two arguments
	 * @return the updated value
	 * @since 1.8
	 */
	public final double accumulateAndGet(double value, DoubleBinaryOperator accumulatorFunction) {
		double prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsDouble(prev, value);
			if (weakCompareAndSetVolatile(prev, next))
				return next;
			haveNext = (prev == (prev = get()));
		}
	}

	/**
	 * Returns the String representation of the current value.
	 *
	 * @return the String representation of the current value
	 */
	@Override
	public String toString() {
		return Double.toString(get());
	}

	/**
	 * Returns the current value of this {@code AtomicInteger} as an
	 * {@code int},
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 * <p>
	 * Equivalent to {@link #get()}.
	 */
	@Override
	public int intValue() {
		return (int) get();
	}

	/**
	 * Returns the current value of this {@code AtomicInteger} as a
	 * {@code long} after a widening primitive conversion,
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 */
	@Override
	public long longValue() {
		return (long) get();
	}

	/**
	 * Returns the current value of this {@code AtomicInteger} as a
	 * {@code float} after a widening primitive conversion,
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 */
	@Override
	public float floatValue() {
		return (float) get();
	}

	/**
	 * Returns the current value of this {@code AtomicInteger} as a
	 * {@code double} after a widening primitive conversion,
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 */
	@Override
	public double doubleValue() {
		return get();
	}

	// jdk9

	/**
	 * Returns the current value, with memory semantics of reading as
	 * if the variable was declared non-{@code volatile}.
	 *
	 * @return the value
	 * @since 9
	 */
	public final double getPlain() {
		return (double) VALUE.get(this);
	}

	/**
	 * Sets the value to {@code newValue}, with memory semantics
	 * of setting as if the variable was declared non-{@code volatile}
	 * and non-{@code final}.
	 *
	 * @param newValue the new value
	 * @since 9
	 */
	public final void setPlain(double newValue) {
		VALUE.set(this, newValue);
	}

	/**
	 * Returns the current value,
	 * with memory effects as specified by {@link VarHandle#getOpaque}.
	 *
	 * @return the value
	 * @since 9
	 */
	public final double getOpaque() {
		return (double) VALUE.getOpaque(this);
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setOpaque}.
	 *
	 * @param newValue the new value
	 * @since 9
	 */
	public final void setOpaque(double newValue) {
		VALUE.setOpaque(this, newValue);
	}

	/**
	 * Returns the current value,
	 * with memory effects as specified by {@link VarHandle#getAcquire}.
	 *
	 * @return the value
	 * @since 9
	 */
	public final double getAcquire() {
		return (double) VALUE.getAcquire(this);
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setRelease}.
	 *
	 * @param newValue the new value
	 * @since 9
	 */
	public final void setRelease(double newValue) {
		VALUE.setRelease(this, newValue);
	}

	/**
	 * Atomically sets the value to {@code newValue} if the current value,
	 * referred to as the <em>witness value</em>, {@code == expectedValue},
	 * with memory effects as specified by
	 * {@link VarHandle#compareAndExchange}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return the <em>witness value</em>, which will be the same as the
	 * expected value if successful
	 * @since 9
	 */
	public final double compareAndExchange(double expectedValue, double newValue) {
		return (double) VALUE.compareAndExchange(this, expectedValue, newValue);
	}

	/**
	 * Atomically sets the value to {@code newValue} if the current value,
	 * referred to as the <em>witness value</em>, {@code == expectedValue},
	 * with memory effects as specified by
	 * {@link VarHandle#compareAndExchangeAcquire}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return the <em>witness value</em>, which will be the same as the
	 * expected value if successful
	 * @since 9
	 */
	public final double compareAndExchangeAcquire(double expectedValue, double newValue) {
		return (double) VALUE.compareAndExchangeAcquire(this, expectedValue, newValue);
	}

	/**
	 * Atomically sets the value to {@code newValue} if the current value,
	 * referred to as the <em>witness value</em>, {@code == expectedValue},
	 * with memory effects as specified by
	 * {@link VarHandle#compareAndExchangeRelease}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return the <em>witness value</em>, which will be the same as the
	 * expected value if successful
	 * @since 9
	 */
	public final double compareAndExchangeRelease(double expectedValue, double newValue) {
		return (double) VALUE.compareAndExchangeRelease(this, expectedValue, newValue);
	}

	/**
	 * Possibly atomically sets the value to {@code newValue} if
	 * the current value {@code == expectedValue},
	 * with memory effects as specified by
	 * {@link VarHandle#weakCompareAndSet}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return {@code true} if successful
	 * @since 9
	 */
	public final boolean weakCompareAndSetVolatile(double expectedValue, double newValue) {
		return VALUE.weakCompareAndSet(this, expectedValue, newValue);
	}

	/**
	 * Possibly atomically sets the value to {@code newValue} if
	 * the current value {@code == expectedValue},
	 * with memory effects as specified by
	 * {@link VarHandle#weakCompareAndSetAcquire}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return {@code true} if successful
	 * @since 9
	 */
	public final boolean weakCompareAndSetAcquire(double expectedValue, double newValue) {
		return VALUE.weakCompareAndSetAcquire(this, expectedValue, newValue);
	}

	/**
	 * Possibly atomically sets the value to {@code newValue} if
	 * the current value {@code == expectedValue},
	 * with memory effects as specified by
	 * {@link VarHandle#weakCompareAndSetRelease}.
	 *
	 * @param expectedValue the expected value
	 * @param newValue      the new value
	 * @return {@code true} if successful
	 * @since 9
	 */
	public final boolean weakCompareAndSetRelease(double expectedValue, double newValue) {
		return VALUE.weakCompareAndSetRelease(this, expectedValue, newValue);
	}
}
