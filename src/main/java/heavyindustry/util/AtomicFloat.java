package heavyindustry.util;

import heavyindustry.util.function.FloatBinaryOperator;
import heavyindustry.util.function.FloatUnaryOperator;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;

/**
 * A float type atomic operation class implemented using VarHandle.
 *
 * @see VarHandle
 * @see AtomicDouble
 */
public class AtomicFloat extends Number implements Serializable {
	private static final long serialVersionUID = 8826071785285943139L;

	private static final VarHandle VALUE;

	static {
		try {
			Lookup lookup = MethodHandles.lookup();
			VALUE = lookup.findVarHandle(AtomicFloat.class, "value", float.class);
		} catch (ReflectiveOperationException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private volatile float value;

	/**
	 * Creates a new AtomicInteger with the given initial value.
	 *
	 * @param initialValue the initial value
	 */
	public AtomicFloat(float initialValue) {
		value = initialValue;
	}

	/**
	 * Creates a new AtomicInteger with initial value {@code 0}.
	 */
	public AtomicFloat() {}

	/**
	 * Returns the current value,
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 *
	 * @return the current value
	 */
	public final float get() {
		return value;
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setVolatile}.
	 *
	 * @param newValue the new value
	 */
	public final void set(float newValue) {
		value = newValue;
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setRelease}.
	 *
	 * @param newValue the new value
	 * @since 1.6
	 */
	public final void lazySet(float newValue) {
		VALUE.setRelease(this, newValue);
	}

	/**
	 * Atomically sets the value to {@code newValue} and returns the old value,
	 * with memory effects as specified by {@link VarHandle#getAndSet}.
	 *
	 * @param newValue the new value
	 * @return the previous value
	 */
	public final float getAndSet(float newValue) {
		return (float) VALUE.getAndSet(this, newValue);
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
	public final boolean compareAndSet(float expectedValue, float newValue) {
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
	public final boolean weakCompareAndSet(float expectedValue, float newValue) {
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
	public final boolean weakCompareAndSetPlain(float expectedValue, float newValue) {
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
	public final float getAndIncrement() {
		return (float) VALUE.getAndAdd(this, 1f);
	}

	/**
	 * Atomically decrements the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code getAndAdd(-1)}.
	 *
	 * @return the previous value
	 */
	public final float getAndDecrement() {
		return (float) VALUE.getAndAdd(this, -1f);
	}

	/**
	 * Atomically adds the given value to the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * @param delta the value to add
	 * @return the previous value
	 */
	public final float getAndAdd(float delta) {
		return (float) VALUE.getAndAdd(this, delta);
	}

	/**
	 * Atomically increments the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code addAndGet(1)}.
	 *
	 * @return the updated value
	 */
	public final float incrementAndGet() {
		return (float) VALUE.getAndAdd(this, 1f) + 1f;
	}

	/**
	 * Atomically decrements the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * <p>Equivalent to {@code addAndGet(-1)}.
	 *
	 * @return the updated value
	 */
	public final float decrementAndGet() {
		return (float) VALUE.getAndAdd(this, -1f) - 1f;
	}

	/**
	 * Atomically adds the given value to the current value,
	 * with memory effects as specified by {@link VarHandle#getAndAdd}.
	 *
	 * @param delta the value to add
	 * @return the updated value
	 */
	public final float addAndGet(float delta) {
		return (float) VALUE.getAndAdd(this, delta) + delta;
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
	public final float getAndUpdate(FloatUnaryOperator updateFunction) {
		float prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsFloat(prev);
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
	public final float updateAndGet(FloatUnaryOperator updateFunction) {
		float prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsFloat(prev);
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
	public final float getAndAccumulate(float value, FloatBinaryOperator accumulatorFunction) {
		float prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsFloat(prev, value);
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
	public final float accumulateAndGet(float value, FloatBinaryOperator accumulatorFunction) {
		float prev = get(), next = 0;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsFloat(prev, value);
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
		return Float.toString(get());
	}

	/**
	 * Returns the current value of this {@code AtomicInteger} as an
	 * {@code int},
	 * with memory effects as specified by {@link VarHandle#getVolatile}.
	 * <p>Equivalent to {@link #get()}.
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
		return get();
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
	public final float getPlain() {
		return (float) VALUE.get(this);
	}

	/**
	 * Sets the value to {@code newValue}, with memory semantics
	 * of setting as if the variable was declared non-{@code volatile}
	 * and non-{@code final}.
	 *
	 * @param newValue the new value
	 * @since 9
	 */
	public final void setPlain(float newValue) {
		VALUE.set(this, newValue);
	}

	/**
	 * Returns the current value,
	 * with memory effects as specified by {@link VarHandle#getOpaque}.
	 *
	 * @return the value
	 * @since 9
	 */
	public final float getOpaque() {
		return (int) VALUE.getOpaque(this);
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setOpaque}.
	 *
	 * @param newValue the new value
	 * @since 9
	 */
	public final void setOpaque(float newValue) {
		VALUE.setOpaque(this, newValue);
	}

	/**
	 * Returns the current value,
	 * with memory effects as specified by {@link VarHandle#getAcquire}.
	 *
	 * @return the value
	 * @since 9
	 */
	public final float getAcquire() {
		return (float) VALUE.getAcquire(this);
	}

	/**
	 * Sets the value to {@code newValue},
	 * with memory effects as specified by {@link VarHandle#setRelease}.
	 *
	 * @param newValue the new value
	 * @since 9
	 */
	public final void setRelease(float newValue) {
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
	public final float compareAndExchange(float expectedValue, float newValue) {
		return (int) VALUE.compareAndExchange(this, expectedValue, newValue);
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
	public final float compareAndExchangeAcquire(float expectedValue, float newValue) {
		return (float) VALUE.compareAndExchangeAcquire(this, expectedValue, newValue);
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
	public final float compareAndExchangeRelease(float expectedValue, float newValue) {
		return (float) VALUE.compareAndExchangeRelease(this, expectedValue, newValue);
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
	public final boolean weakCompareAndSetVolatile(float expectedValue, float newValue) {
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
	public final boolean weakCompareAndSetAcquire(float expectedValue, float newValue) {
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
	public final boolean weakCompareAndSetRelease(float expectedValue, float newValue) {
		return VALUE.weakCompareAndSetRelease(this, expectedValue, newValue);
	}
}
