package heavyindustry.util;

import heavyindustry.util.function.BooleanUnaryOperator;
import heavyindustry.util.function.FloatSupplier;
import heavyindustry.util.function.FloatUnaryOperator;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import java.util.function.Supplier;

/**
 * The appended variable interface is used to provide dynamic append variables for types,
 * providing operations such as {@code get}, {@code set}, {@code handle} on variables, but non-removable variables.
 * <br>(However, in fact, you can forcibly delete the variable mapping table by calling the {@link #extra()} method,
 * but this goes against the original design intention of this type.)
 *
 * @apiNote I really hate the encapsulation of raw data types, it is definitely the most disgusting design in Java.
 */
public interface ExtraVariableComp {
	/**
	 * Variable mapping table entry, automatically or manually bound to a mapping object,
	 * <br><i>which will serve as a container for storing dynamic variables as objects.</i>
	 */
	Map<String, Object> extra();

	/**
	 * Get the value of a dynamic variable, return null if the variable does not exist.
	 *
	 * @param <T>   Get the type of variable
	 * @param field Variable name
	 */
	@SuppressWarnings("unchecked")
	default <T> T getVar(String field) {
		return (T) extra().get(field);
	}

	/**
	 * Retrieve the value of a dynamic variable, and if the variable does not exist, return the default value given.
	 * <br><strong>Note: </strong>If the variable does not exist, the default value is returned directly and will not be added to the variable table.
	 *
	 * @param <T>   Get the type of variable
	 * @param field Variable name
	 * @param def   Default value
	 */
	@SuppressWarnings("unchecked")
	default <T> T getVar(String field, T def) {
		return (T) extra().getOrDefault(field, def);
	}

	/**
	 * Get the value of a dynamic variable. If the variable does not exist,
	 * return the return value of the given initialization function and assign this value to the given variable.
	 * This is usually used for convenient variable value initialization.
	 *
	 * @param <T>	 Get the type of variable
	 * @param field   Variable name
	 * @param initial Initial value function
	 */
	@SuppressWarnings("unchecked")
	default <T> T getVar(String field, Supplier<T> initial) {
		return (T) extra().computeIfAbsent(field, e -> initial.get());
	}

	/**
	 * Get the value of a dynamic variable, throw an exception if the variable does not exist
	 *
	 * @param <T>   Get the type of variable
	 * @param field Variable name
	 * @throws NoSuchFieldException If the obtained variable does not exist
	 */
	@SuppressWarnings("unchecked")
	default <T> T getVarThr(String field) throws NoSuchFieldException {
		if (!extra().containsKey(field))
			throw new NoSuchFieldException("no such field with name: " + field);

		return (T) extra().get(field);
	}

	/**
	 * Set the value of the specified variable
	 *
	 * @param <T>   Set the type of variable
	 * @param field Variable name
	 * @param value Variable values set
	 * @return The original value of the variable before it was set
	 */
	@SuppressWarnings("unchecked")
	default <T> T setVar(String field, T value) {
		return (T) extra().put(field, value);
	}

	/**
	 * Use a function to process the value of a variable and update the value of the variable with the return value
	 *
	 * @param <T>   Set the type of variable
	 * @param field Variable name
	 * @param cons  Variable handling function
	 * @param def   Default value of variable
	 * @return The updated variable value, which is the return value of the function
	 */
	default <T> T handleVar(String field, Function<T, T> cons, T def) {
		T res;
		setVar(field, res = cons.apply(getVar(field, def)));

		return res;
	}

	//-----------------------
	//Optimization and overloading of primitive data type operations
	//
	//Java's primitive data type boxing must be one of the top ten foolish behaviors in programming language history.
	//-----------------------

	/**
	 * Set boolean type variable value
	 *
	 * @throws ClassCastException If the variable already exists and is not a boolean wrapper type or atomized reference
	 * @see #setVar(String, Object)
	 */
	default boolean setVar(String field, boolean value) {
		Object res = getVar(field);

		return switch (res) {
			case AtomicBoolean b -> {
				boolean r = b.get();
				b.set(value);
				yield r;
			}
			case Boolean n -> {
				extra().put(field, new AtomicBoolean(value));
				yield n;
			}
			case null -> {
				extra().put(field, new AtomicBoolean(value));
				yield false;
			}
			default -> throw new ClassCastException(res + " is not a boolean value or atomic boolean");
		};
	}

	/**
	 * Get boolean variable value.
	 *
	 * @throws ClassCastException If the variable already exists and is not a boolean wrapper type or atomized reference
	 * @see #getVar(String, Object)
	 */
	default boolean getVar(String field, boolean def) {
		Object res = getVar(field);
		return switch (res) {
			case null -> def;
			case AtomicBoolean i -> i.get();
			case Boolean n -> n;
			default -> throw new ClassCastException(res + " is not a boolean value or atomic boolean");
		};
	}

	/**
	 * Retrieve the boolean variable value and initialize the variable value when it does not exist.
	 *
	 * @throws ClassCastException If the variable already exists and is not a boolean wrapper type or atomized reference
	 * @see #getVar(String, Supplier)
	 */
	default boolean getVar(String field, BooleanSupplier initial) {
		Object res = getVar(field);
		return switch (res) {
			case null -> {
				boolean b = initial.getAsBoolean();
				extra().put(field, new AtomicBoolean(b));
				yield b;
			}
			case AtomicBoolean b -> b.get();
			case Boolean n -> n;
			default -> throw new ClassCastException(res + " is not a boolean value or atomic boolean");
		};
	}

	/**
	 * Use processing functions to handle boolean variable values and update variable values with return values.
	 *
	 * @throws ClassCastException If the variable already exists and is not a boolean wrapper type or atomized reference
	 * @see #handleVar(String, Function, Object)
	 */
	default boolean handleVar(String field, BooleanUnaryOperator handle, boolean def) {
		boolean b;
		setVar(field, b = handle.applyAsBoolean(getVar(field, def)));

		return b;
	}

	/**
	 * Set the value of an int type variable.
	 *
	 * @throws ClassCastException If the variable already exists and is not an int wrapper type or atomized reference
	 * @see #setVar(String, Object)
	 */
	default int setVar(String field, int value) {
		Object res = getVar(field);

		return switch (res) {
			case AtomicInteger i -> {
				int r = i.get();
				i.set(value);
				yield r;
			}
			case Number n -> {
				extra().put(field, new AtomicInteger(value));
				yield n.intValue();
			}
			case null -> {
				extra().put(field, new AtomicInteger(value));
				yield 0;
			}
			default -> throw new ClassCastException(res + " is not a number or atomic integer");
		};
	}

	/**
	 * Get the value of the int variable.
	 *
	 * @throws ClassCastException If the variable already exists and is not an int wrapper type or atomized reference
	 * @see #getVar(String, Object)
	 */
	default int getVar(String field, int def) {
		Object res = getVar(field);
		return switch (res) {
			case null -> def;
			case AtomicInteger i -> i.get();
			case Number n -> n.intValue();
			default -> throw new ClassCastException(res + " is not a number or atomic integer");
		};
	}

	/**
	 * Retrieve the value of an int variable and initialize the variable value when it does not exist.
	 *
	 * @throws ClassCastException If the variable already exists and is not an int wrapper type or atomized reference
	 * @see #getVar(String, Supplier)
	 */
	default int getVar(String field, IntSupplier initial) {
		Object res = getVar(field);
		return switch (res) {
			case null -> {
				int b = initial.getAsInt();
				extra().put(field, new AtomicInteger(b));
				yield b;
			}
			case AtomicInteger i -> i.get();
			case Number n -> n.intValue();
			default -> throw new ClassCastException(res + " is not a number or atomic integer");
		};
	}

	/**
	 * Use processing functions to handle int variable values and update variable values with return values.
	 *
	 * @throws ClassCastException If the variable already exists and is not an int wrapper type or atomized reference
	 * @see #handleVar(String, Function, Object)
	 */
	default int handleVar(String field, IntUnaryOperator handle, int def) {
		int i;
		setVar(field, i = handle.applyAsInt(getVar(field, def)));

		return i;
	}

	/**
	 * Set the value of a long type variable.
	 *
	 * @throws ClassCastException If the variable already exists and is not a long wrapper type or atomized reference
	 * @see #setVar(String, Object)
	 */
	default long setVar(String field, long value) {
		Object res = getVar(field);

		return switch (res) {
			case AtomicLong l -> {
				long r = l.get();
				l.set(value);
				yield r;
			}
			case Number n -> {
				extra().put(field, new AtomicLong(value));
				yield n.longValue();
			}
			case null -> {
				extra().put(field, new AtomicLong(value));
				yield 0;
			}
			default -> throw new ClassCastException(res + " is not a number or atomic long");
		};
	}

	/**
	 * Get the value of a long variable.
	 *
	 * @throws ClassCastException If the variable already exists and is not a long wrapper type or atomized reference
	 * @see #getVar(String, Object)
	 */
	default long getVar(String field, long def) {
		Object res = getVar(field);
		return switch (res) {
			case null -> def;
			case AtomicLong l -> l.get();
			case Number n -> n.longValue();
			default -> throw new ClassCastException(res + " is not a number or atomic long");
		};
	}

	/**
	 * Retrieve the value of a long variable and initialize the variable value when it does not exist.
	 *
	 * @throws ClassCastException If the variable already exists and is not a long wrapper type or atomized reference
	 * @see #getVar(String, Supplier)
	 */
	default long getVar(String field, LongSupplier initial) {
		Object res = getVar(field);
		return switch (res) {
			case null -> {
				long l = initial.getAsLong();
				extra().put(field, new AtomicLong(l));
				yield l;
			}
			case AtomicLong l -> l.get();
			case Number n -> n.longValue();
			default -> throw new ClassCastException(res + " is not a number or atomic long");
		};
	}

	/**
	 * Use processing functions to handle long variable values and update variable values with return values.
	 *
	 * @throws ClassCastException If the variable already exists and is not a long wrapper type or atomized reference
	 * @see #handleVar(String, Function, Object)
	 */
	default long handleVar(String field, LongUnaryOperator handle, long def) {
		long l;
		setVar(field, l = handle.applyAsLong(getVar(field, def)));

		return l;
	}

	/**
	 * Set float type variable value.
	 *
	 * @throws ClassCastException If the variable already exists and is not a float wrapper type or a single element float array
	 * @see #setVar(String, Object)
	 */
	default float setVar(String field, float value) {
		Object res = getVar(field);

		return switch (res) {
			case AtomicFloat a -> {
				float r = a.get();
				a.set(value);
				yield r;
			}
			case Number n -> {
				extra().put(field, new AtomicFloat(value));
				yield n.floatValue();
			}
			case null -> {
				extra().put(field, new AtomicFloat(value));
				yield 0f;
			}
			default -> throw new ClassCastException(res + " is not a number or atomic float");
		};
	}

	/**
	 * Get float variable value.
	 *
	 * @throws ClassCastException If the variable already exists and is not a float wrapper type or a single element float array
	 * @see #getVar(String, Object)
	 */
	default float getVar(String field, float def) {
		Object res = getVar(field);
		return switch (res) {
			case null -> def;
			case AtomicFloat f -> f.get();
			case Number n -> n.floatValue();
			default -> throw new ClassCastException(res + " is not a number or atomic float");
		};
	}

	/**
	 * Retrieve the float variable value and initialize the variable value when it does not exist.
	 *
	 * @throws ClassCastException If the variable already exists and is not a float wrapper type or a single element float array
	 * @see #getVar(String, Supplier)
	 */
	default float getVar(String field, FloatSupplier initial) {
		Object res = getVar(field);
		return switch (res) {
			case null -> {
				float f = initial.getAsFloat();
				extra().put(field, new AtomicFloat(f));
				yield f;
			}
			case AtomicFloat l -> l.get();
			case Number n -> n.floatValue();
			default -> throw new ClassCastException(res + " is not a number or atomic float");
		};
	}

	/**
	 * Use processing functions to handle float variable values and update variable values with return values.
	 *
	 * @throws ClassCastException If the variable already exists and is not a float wrapper type or a single element float array
	 * @see #handleVar(String, Function, Object)
	 */
	default float handleVar(String field, FloatUnaryOperator handle, float def) {
		float trans;
		setVar(field, trans = handle.applyAsFloat(getVar(field, def)));

		return trans;
	}

	/**
	 * Set the value of a double type variable.
	 *
	 * @throws ClassCastException If the variable already exists and is not a float wrapper type or a single element double array
	 * @see #setVar(String, Object)
	 */
	default double setVar(String field, double value) {
		Object res = getVar(field);

		switch (res) {
			case AtomicDouble a -> {
				double r = a.get();
				a.set(value);
				return r;
			}
			case Number n -> {
				extra().put(field, new AtomicDouble(value));
				return n.doubleValue();
			}
			case null -> {
				extra().put(field, new AtomicDouble(value));
				return 0d;
			}
			default -> throw new ClassCastException(res + " is not a number or atomic double");
		}
	}

	/**
	 * Get double variable value.
	 *
	 * @throws ClassCastException If the variable already exists and is not a float wrapper type or a single element double array
	 * @see #getVar(String, Object)
	 */
	default double getVar(String field, double def) {
		Object res = getVar(field);
		return switch (res) {
			case null -> def;
			case AtomicDouble f -> f.get();
			case Number n -> n.doubleValue();
			default -> throw new ClassCastException(res + " is not a number or atomic double");
		};
	}

	/**
	 * Retrieve the value of a double variable and initialize the variable value when it does not exist.
	 *
	 * @throws ClassCastException If the variable already exists and is not a double wrapper type or a single element double array
	 * @see #getVar(String, Supplier)
	 */
	default double getVar(String field, DoubleSupplier initial) {
		Object res = getVar(field);
		return switch (res) {
			case null -> {
				double d = initial.getAsDouble();
				extra().put(field, new AtomicDouble(d));
				yield d;
			}
			case AtomicDouble d -> d.get();
			case Number n -> n.doubleValue();
			default -> throw new ClassCastException(res + " is not a number or atomic double");
		};
	}

	/**
	 * Use processing functions to handle double variable values and update variable values with return values.
	 *
	 * @throws ClassCastException If the variable already exists and is not a double wrapper type or a single element double array
	 * @see #handleVar(String, Function, Object)
	 */
	default double handleVar(String field, DoubleUnaryOperator handle, double def) {
		double d;
		setVar(field, d = handle.applyAsDouble(getVar(field, def)));

		return d;
	}
}
