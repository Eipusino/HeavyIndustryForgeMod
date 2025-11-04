package heavyindustry.math;

public final class Mathf {
	private Mathf() {}

	/** Returns the next power of two. Returns the specified value if the value is already a power of two. */
	public static int nextPowerOfTwo(int value) {
		if (value == 0) return 1;
		value--;
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}

	public static boolean isPowerOfTwo(int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	public static byte clamp(byte value, byte min, byte max) {
		return value > max ? max : value < min ? min : value;
	}

	public static byte max(byte a, byte b) {
		return (a >= b) ? a : b;
	}

	public static byte min(byte a, byte b) {
		return (a <= b) ? a : b;
	}

	public static short clamp(short value, short min, short max) {
		return value > max ? max : value < min ? min : value;
	}

	public static short max(short a, short b) {
		return (a >= b) ? a : b;
	}

	public static short min(short a, short b) {
		return (a <= b) ? a : b;
	}

	public static char max(char a, char b) {
		return (a >= b) ? a : b;
	}

	public static char min(char a, char b) {
		return (a <= b) ? a : b;
	}

	public static int clamp(int value, int min, int max) {
		return value > max ? max : value < min ? min : value;
	}

	public static long clamp(long value, long min, long max) {
		return value > max ? max : value < min ? min : value;
	}

	public static float clamp(float value, float min, float max) {
		return value > max ? max : value < min ? min : value;
	}

	public static double clamp(double value, double min, double max) {
		return value > max ? max : value < min ? min : value;
	}

	public static char clamp(char value, char min, char max) {
		return value > max ? max : value < min ? min : value;
	}
}
