package com.jason.ueditor.util;

import java.lang.reflect.InvocationTargetException;


public final class ExceptionUtils {

	/**
	 * 
	 * @param e
	 * @return
	 */
	public static RuntimeException toUnchecked(Exception e) {
		return toUnchecked(e, "Unexpected Checked Exception");
	}

	/**
	 * 
	 * @param e
	 * @param message
	 * @return
	 */
	public static RuntimeException toUnchecked(Exception e, String message) {

		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}

		if (e instanceof InvocationTargetException) {
			Throwable cause = e.getCause();
			return new RuntimeException(message, cause);
		}

		return new RuntimeException(message, e);
	}

	private ExceptionUtils() {
	}

}
