package heavyindustry.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/**
 * The anti modularization tool only provides one main method {@link Demodulator#openModule(Module, String, Module)} to
 * force software packages that open modules to the required modules.
 * <p>This class behavior may completely break the modular access protection and is inherently insecure. If it is
 * not necessary, please try to avoid using this class.
 * <p><strong>This class is only available after JDK9 to avoid referencing its methods in earlier versions, and it is
 * only available on standard JVM platforms. There is no guarantee that this class will be available on
 * other unofficial JVMs.</strong>
 */
public final class Demodulator {
	static MethodHandle implAddOpens;

	private Demodulator() {}

	// The exceptions thrown during initialization are collectively handled in a try-catch block.
	public static void init() throws NoSuchMethodException, IllegalAccessException {
		implAddOpens = ReflectUtils.lookup.findVirtual(Module.class, "implAddOpens", MethodType.methodType(void.class, String.class));
	}

	/**
	 * @param from To open the module of the package
	 * @param pn The package name of the module to export the package
	 * @param to The module to be exported to.
	 */
	public static void openModule(Module from, String pn, Module to) throws Throwable {
		implAddOpens.invokeExact(from, pn, to);
	}

	public static void openModules() throws Throwable {
		Module from = Object.class.getModule(), to = Demodulator.class.getModule();

		openModule(from, "jdk.internal.misc", to);
		openModule(from, "jdk.internal.reflect", to);

		//MethodHandle addReads = ReflectUtils.lookup.findStatic(Module.class, "addReads0", MethodType.methodType(void.class, Module.class, Module.class));
	}
}
