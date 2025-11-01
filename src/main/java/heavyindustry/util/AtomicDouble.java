package heavyindustry.util;

import jdk.internal.misc.Unsafe;

import java.io.Serializable;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

public class AtomicDouble extends Number implements Serializable {
	private static final long serialVersionUID = 572795237818305608l;

	private static final Unsafe U = Unsafe.getUnsafe();
	private static final long VALUE = U.objectFieldOffset(AtomicDouble.class, "value");

	private volatile double value;

	public AtomicDouble(double initialValue) {
		value = initialValue;
	}

	public AtomicDouble() {}

	public final double get() {
		return value;
	}

	public final void set(double newValue) {
		U.putDoubleVolatile(this, VALUE, newValue);
	}

	public final void lazySet(double newValue) {
		U.putDoubleRelease(this, VALUE, newValue);
	}

	public final double getAndSet(double newValue) {
		return U.getAndSetDouble(this, VALUE, newValue);
	}

	public final boolean compareAndSet(double expectedValue, double newValue) {
		return U.compareAndSetDouble(this, VALUE, expectedValue, newValue);
	}

	@Deprecated(since = "9")
	public final boolean weakCompareAndSet(double expectedValue, double newValue) {
		return U.weakCompareAndSetDoublePlain(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetPlain(double expectedValue, double newValue) {
		return U.weakCompareAndSetDoublePlain(this, VALUE, expectedValue, newValue);
	}

	public final double getAndIncrement() {
		return U.getAndAddDouble(this, VALUE, 1d);
	}

	public final double getAndDecrement() {
		return U.getAndAddDouble(this, VALUE, -1d);
	}

	public final double getAndAdd(double delta) {
		return U.getAndAddDouble(this, VALUE, delta);
	}

	public final double incrementAndGet() {
		return U.getAndAddDouble(this, VALUE, 1d) + 1d;
	}

	public final double decrementAndGet() {
		return U.getAndAddDouble(this, VALUE, -1d) - 1d;
	}

	public final double addAndGet(double delta) {
		return U.getAndAddDouble(this, VALUE, delta) + delta;
	}

	public final double getAndUpdate(DoubleUnaryOperator updateFunction) {
		double prev = get(), next = 0d;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsDouble(prev);
			if (weakCompareAndSetVolatile(prev, next))
				return prev;
			haveNext = (prev == (prev = get()));
		}
	}

	public final double updateAndGet(DoubleUnaryOperator updateFunction) {
		double prev = get(), next = 0d;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsDouble(prev);
			if (weakCompareAndSetVolatile(prev, next))
				return next;
			haveNext = (prev == (prev = get()));
		}
	}

	public final double getAndAccumulate(double value, DoubleBinaryOperator accumulatorFunction) {
		double prev = get(), next = 0d;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsDouble(prev, value);
			if (weakCompareAndSetVolatile(prev, next))
				return prev;
			haveNext = (prev == (prev = get()));
		}
	}

	public final double accumulateAndGet(double value, DoubleBinaryOperator accumulatorFunction) {
		double prev = get(), next = 0d;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsDouble(prev, value);
			if (weakCompareAndSetVolatile(prev, next))
				return next;
			haveNext = (prev == (prev = get()));
		}
	}

	@Override
	public String toString() {
		return Double.toString(get());
	}

	@Override
	public byte byteValue() {
		return (byte) get();
	}

	@Override
	public short shortValue() {
		return (short) get();
	}

	@Override
	public int intValue() {
		return (int) get();
	}

	@Override
	public long longValue() {
		return (long) get();
	}

	@Override
	public float floatValue() {
		return (float) get();
	}

	@Override
	public double doubleValue() {
		return get();
	}

	public final double getPlain() {
		return U.getDouble(this, VALUE);
	}

	public final void setPlain(double newValue) {
		U.putDouble(this, VALUE, newValue);
	}

	public final double getOpaque() {
		return U.getDoubleOpaque(this, VALUE);
	}

	public final void setOpaque(double newValue) {
		U.putDoubleOpaque(this, VALUE, newValue);
	}

	public final double getAcquire() {
		return U.getDoubleAcquire(this, VALUE);
	}

	public final void setRelease(double newValue) {
		U.putDoubleRelease(this, VALUE, newValue);
	}

	public final double compareAndExchange(double expectedValue, double newValue) {
		return U.compareAndExchangeDouble(this, VALUE, expectedValue, newValue);
	}

	public final double compareAndExchangeAcquire(double expectedValue, double newValue) {
		return U.compareAndExchangeDoubleAcquire(this, VALUE, expectedValue, newValue);
	}

	public final double compareAndExchangeRelease(double expectedValue, double newValue) {
		return U.compareAndExchangeDoubleRelease(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetVolatile(double expectedValue, double newValue) {
		return U.weakCompareAndSetDouble(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetAcquire(double expectedValue, double newValue) {
		return U.weakCompareAndSetDoubleAcquire(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetRelease(double expectedValue, double newValue) {
		return U.weakCompareAndSetDoubleRelease(this, VALUE, expectedValue, newValue);
	}
}
