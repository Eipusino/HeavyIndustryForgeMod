package heavyindustry.util;

import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;

public final class ReflectUtils {
	/** An instance of ReflectionFactory for temporary use. */
	static ReflectionFactory factory;
	/**
	 * A lookup with trusted permission.
	 * <br>It completely violates Java's encapsulation security, do not use it unless necessary.
	 * <p>If needed, please call the method: {@link #lookup()}.
	 *
	 * @see #lookup()
	 */
	static Lookup lookup;

	static MethodHandle getDeclaredFields, getDeclaredMethods, getDeclaredConstructors;

	// The exceptions thrown during initialization are collectively handled in a try-catch block.
	public static void init() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		// Get an instance of ReflectionFactory. I hope it won't be intercepted by the Security Manager.
		factory = ReflectionFactory.getReflectionFactory();

		// Bypass security checks to obtain a constructor of an accessible Lookup class and create a trusted Lookup with permission.
		Constructor<?> constructor = factory.newConstructorForSerialization(Lookup.class, Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class));
		lookup = (Lookup) constructor.newInstance(Object.class, null, -1);

		// Obtain native reflection methods, which do not have filtering checks.
		getDeclaredFields = lookup.findVirtual(Class.class, "getDeclaredFields0", MethodType.methodType(Field[].class, boolean.class));
		getDeclaredMethods = lookup.findVirtual(Class.class, "getDeclaredMethods0", MethodType.methodType(Method[].class, boolean.class));
		getDeclaredConstructors = lookup.findVirtual(Class.class, "getDeclaredConstructors0", MethodType.methodType(Constructor[].class, boolean.class));
	}

	/**
	 * Search for fields by name without throwing exceptions and without being restricted by filtering
	 * checks.
	 *
	 * @return The field, or {@code null} if not found.
	 */
	public static Field getField(Class<?> type, String name) {
		Field[] fields = getFields(type);
		for (Field field : fields) {
			if (field.getName().equals(name)) return field;
		}
		return null;
	}

	/**
	 * Search for fields based on custom criteria without throwing exceptions and without being restricted
	 * by filtering checks.
	 *
	 * @return The field, or {@code null} if not found.
	 */
	public static Field getField(Class<?> type, Predicate<Field> predicate) {
		Field[] fields = getFields(type);
		for (Field field : fields) {
			if (predicate.test(field)) return field;
		}
		return null;
	}

	public static Method getMethod(Class<?> type, String name, Class<?>... args) {
		Method[] methods = getMethods(type);
		for (Method method : methods) {
			if (method.getName().equals(name) && Arrays.equals(method.getParameterTypes(), args)) return method;
		}
		return null;
	}

	public static Method getMethod(Class<?> type, Predicate<Method> predicate) {
		Method[] methods = getMethods(type);
		for (Method method : methods) {
			if (predicate.test(method)) return method;
		}
		return null;
	}

	public static Constructor<?> getConstructor(Class<?> type, Class<?>... args) {
		Constructor<?>[] constructors = getConstructors(type);
		for (Constructor<?> constructor : constructors) {
			if (Arrays.equals(constructor.getParameterTypes(), args)) return constructor;
		}
		return null;
	}

	public static Constructor<?> getConstructor(Class<?> type, Predicate<Constructor<?>> predicate) {
		Constructor<?>[] constructors = getConstructors(type);
		for (Constructor<?> constructor : constructors) {
			if (predicate.test(constructor)) return constructor;
		}
		return null;
	}

	/**
	 * Unrestricted access to all defined fields of the class without throwing exceptions.
	 */
	public static Field[] getFields(Class<?> type) {
		try {
			return (Field[]) getDeclaredFields.invokeExact(type, false);
		} catch (Throwable e) {
			return type.getDeclaredFields();
		}
	}

	/**
	 * Unrestricted access to all methods defined by the class without throwing exceptions.
	 */
	public static Method[] getMethods(Class<?> type) {
		try {
			return (Method[]) getDeclaredMethods.invokeExact(type, false);
		} catch (Throwable e) {
			return type.getDeclaredMethods();
		}
	}

	/**
	 * Unrestricted access to all Constructors defined by the class without throwing exceptions.
	 */
	public static Constructor<?>[] getConstructors(Class<?> type) {
		try {
			return (Constructor<?>[]) getDeclaredConstructors.invokeExact(type, false);
		} catch (Throwable e) {
			return type.getDeclaredConstructors();
		}
	}

	public static Lookup lookup() {
		return lookup;
	}
}
