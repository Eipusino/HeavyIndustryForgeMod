package com.lonetrail.util;

import com.lonetrail.util.struct.Seq;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Utilities for threaded programming.
 */
public class Threads {
	public static <T> ThreadLocal<T> local(Supplier<T> prov) {
		return ThreadLocal.withInitial(prov);
	}

	public static <T> T await(Future<T> future) {
		try {
			return future.get();
		} catch (ExecutionException | InterruptedException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}

	public static void awaitAll(Seq<Future<?>> futures) {
		try {
			for (Future<?> f : futures) {
				f.get();
			}
		} catch (ExecutionException | InterruptedException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}

	/** @return an executor with a fixed number of threads which do not expire */
	public static ExecutorService executor(String name, int threads) {
		return Executors.newFixedThreadPool(threads, r -> newThread(r, name, true));
	}

	/** @return an executor with a fixed number of threads which do not expire */
	public static ExecutorService executor(int threads) {
		return Executors.newFixedThreadPool(threads, r -> newThread(r, null, true));
	}

	/** @see #executor(String, int) */
	public static ExecutorService executor(String name) {
		return executor(name, OS.cores);
	}

	/** @see #executor(String, int) */
	public static ExecutorService executor() {
		return executor(null);
	}

	/**
	 * @param min the number of threads to keep alive at all times after they are first started
	 * @return an executor with no max thread count. threads expire after 1 minute of inactivity
	 */
	public static ExecutorService unboundedExecutor(String name, int min) {
		return new ThreadPoolExecutor(min, Integer.MAX_VALUE, 1, TimeUnit.MINUTES, new SynchronousQueue<>(), r -> newThread(r, name, true));
	}

	/** @see #unboundedExecutor(String, int) */
	public static ExecutorService unboundedExecutor(String name) {
		return unboundedExecutor(name, 0);
	}

	/** @see #unboundedExecutor(String, int) */
	public static ExecutorService unboundedExecutor() {
		return unboundedExecutor(null);
	}

	/**
	 * @param max maximum number of threads to create
	 * @return an executor with a max thread count. threads expire after 1 minute of inactivity
	 */
	public static ExecutorService boundedExecutor(String name, int max) {
		ThreadPoolExecutor exec = new ThreadPoolExecutor(max, max, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), r -> newThread(r, name, true));
		exec.allowCoreThreadTimeOut(true);
		return exec;
	}

	/** Shuts down the executor and waits for its termination indefinitely. */
	public static void await(ExecutorService exec) {
		try {
			exec.shutdown();
			exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void sleep(long ms, int ns) {
		try {
			Thread.sleep(ms, ns);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/** Starts a new non-daemon thread. */
	public static Thread thread(Runnable runnable) {
		return thread(null, runnable);
	}

	/** Starts a new non-daemon thread. */
	public static Thread thread(String name, Runnable runnable) {
		Thread thread = newThread(runnable, name, false);
		thread.start();
		return thread;
	}

	/** Starts a new daemon thread. */
	public static Thread daemon(Runnable runnable) {
		return daemon(null, runnable);
	}

	/** Starts a new daemon thread. */
	public static Thread daemon(String name, Runnable runnable) {
		Thread thread = newThread(runnable, name, true);
		thread.start();
		return thread;
	}

	private static Thread newThread(Runnable r, String name, boolean daemon) {
		Thread thread = name == null ? new Thread(r) : new Thread(r, name);
		thread.setDaemon(daemon);
		//thread.setUncaughtExceptionHandler((t, e) -> Log.err(e));
		return thread;
	}
}
