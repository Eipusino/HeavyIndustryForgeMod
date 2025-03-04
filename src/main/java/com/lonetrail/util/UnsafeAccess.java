package com.lonetrail.util;

import com.lonetrail.lonetrailmod.LoneTrailMod;
import jdk.internal.misc.Unsafe;

public final class UnsafeAccess {
	static final Unsafe unsafe;

	static {
		try {
			Demodulator.makeModuleOpen(Object.class.getModule(), "jdk.internal.misc", UnsafeAccess.class.getModule());

			var cons = Unsafe.class.getDeclaredConstructor();
			cons.setAccessible(true);
			unsafe = cons.newInstance();

			LoneTrailMod.getLogger().info("lonetrailmod: initial unsafe pass");
		} catch (Throwable e) {
			throw new RuntimeException();
		}
	}

	private UnsafeAccess() {}
}
