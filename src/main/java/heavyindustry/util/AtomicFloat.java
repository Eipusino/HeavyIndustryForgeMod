package heavyindustry.util;

import heavyindustry.util.function.FloatBinaryOperator;
import heavyindustry.util.function.FloatUnaryOperator;
import jdk.internal.misc.Unsafe;

import java.io.Serializable;

public class AtomicFloat extends Number implements Serializable {
	private static final long serialVersionUID = 8826071785285943139l;

	private static final Unsafe U = Unsafe.getUnsafe();
	private static final long VALUE = U.objectFieldOffset(AtomicFloat.class, "value");

	private volatile float value;

	public AtomicFloat(float initialValue) {
		value = initialValue;
	}

	public AtomicFloat() {}

	public final float get() {
		return value;
	}

	public final void set(float newValue) {
		U.putFloatVolatile(this, VALUE, newValue);
	}

	public final void lazySet(float newValue) {
		U.putFloatRelease(this, VALUE, newValue);
	}

	public final float getAndSet(float newValue) {
		return U.getAndSetFloat(this, VALUE, newValue);
	}

	public final boolean compareAndSet(float expectedValue, float newValue) {
		return U.compareAndSetFloat(this, VALUE, expectedValue, newValue);
	}

	@Deprecated(since = "9")
	public final boolean weakCompareAndSet(float expectedValue, float newValue) {
		return U.weakCompareAndSetFloatPlain(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetPlain(float expectedValue, float newValue) {
		return U.weakCompareAndSetFloatPlain(this, VALUE, expectedValue, newValue);
	}

	public final float getAndIncrement() {
		return U.getAndAddFloat(this, VALUE, 1f);
	}

	public final float getAndDecrement() {
		return U.getAndAddFloat(this, VALUE, -1f);
	}

	public final float getAndAdd(float delta) {
		return U.getAndAddFloat(this, VALUE, delta);
	}

	public final float incrementAndGet() {
		return U.getAndAddFloat(this, VALUE, 1f) + 1f;
	}

	public final float decrementAndGet() {
		return U.getAndAddFloat(this, VALUE, -1f) - 1f;
	}

	public final float addAndGet(float delta) {
		return U.getAndAddFloat(this, VALUE, delta) + delta;
	}

	public final float getAndUpdate(FloatUnaryOperator updateFunction) {
		float prev = get(), next = 0f;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsFloat(prev);
			if (weakCompareAndSetVolatile(prev, next))
				return prev;
			haveNext = (prev == (prev = get()));
		}
	}

	public final float updateAndGet(FloatUnaryOperator updateFunction) {
		float prev = get(), next = 0f;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = updateFunction.applyAsFloat(prev);
			if (weakCompareAndSetVolatile(prev, next))
				return next;
			haveNext = (prev == (prev = get()));
		}
	}

	public final float getAndAccumulate(float value, FloatBinaryOperator accumulatorFunction) {
		float prev = get(), next = 0f;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsFloat(prev, value);
			if (weakCompareAndSetVolatile(prev, next))
				return prev;
			haveNext = (prev == (prev = get()));
		}
	}

	public final float accumulateAndGet(float value, FloatBinaryOperator accumulatorFunction) {
		float prev = get(), next = 0f;
		for (boolean haveNext = false; ; ) {
			if (!haveNext)
				next = accumulatorFunction.applyAsFloat(prev, value);
			if (weakCompareAndSetVolatile(prev, next))
				return next;
			haveNext = (prev == (prev = get()));
		}
	}

	@Override
	public String toString() {
		return Float.toString(get());
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
		return get();
	}

	@Override
	public double doubleValue() {
		return get();
	}

	public final float getPlain() {
		return U.getFloat(this, VALUE);
	}

	public final void setPlain(float newValue) {
		U.putFloat(this, VALUE, newValue);
	}

	public final float getOpaque() {
		return U.getFloatOpaque(this, VALUE);
	}

	public final void setOpaque(float newValue) {
		U.putFloatOpaque(this, VALUE, newValue);
	}

	public final float getAcquire() {
		return U.getFloatAcquire(this, VALUE);
	}

	public final void setRelease(float newValue) {
		U.putFloatRelease(this, VALUE, newValue);
	}

	public final float compareAndExchange(float expectedValue, float newValue) {
		return U.compareAndExchangeFloat(this, VALUE, expectedValue, newValue);
	}

	public final float compareAndExchangeAcquire(float expectedValue, float newValue) {
		return U.compareAndExchangeFloatAcquire(this, VALUE, expectedValue, newValue);
	}

	public final float compareAndExchangeRelease(float expectedValue, float newValue) {
		return U.compareAndExchangeFloatRelease(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetVolatile(float expectedValue, float newValue) {
		return U.weakCompareAndSetFloat(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetAcquire(float expectedValue, float newValue) {
		return U.weakCompareAndSetFloatAcquire(this, VALUE, expectedValue, newValue);
	}

	public final boolean weakCompareAndSetRelease(float expectedValue, float newValue) {
		return U.weakCompareAndSetFloatRelease(this, VALUE, expectedValue, newValue);
	}
}
