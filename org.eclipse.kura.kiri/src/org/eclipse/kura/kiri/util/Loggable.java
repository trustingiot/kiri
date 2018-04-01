package org.eclipse.kura.kiri.util;

import org.slf4j.*;

/**
 * Allows the classes that implements it to log messages
 */
public interface Loggable {

	/**
	 * Returns the class logger instance (instantiating it if it does not exists)
	 * 
	 * @return Class logger
	 */
	default Logger logger() {
		return LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * Log an info message
	 * 
	 * @param pattern
	 *            Message pattern
	 * @param arguments
	 *            Message arguments
	 */
	default void info(String pattern, Object... arguments) {
		logger().info(pattern, arguments);
	}

	/**
	 * Log a debug message
	 * 
	 * @param pattern
	 *            Message pattern
	 * @param arguments
	 *            Message arguments
	 */
	default void debug(String pattern, Object... arguments) {
		logger().debug(pattern, arguments);
	}

	/**
	 * Log a warn message
	 * 
	 * @param pattern
	 *            Message pattern
	 * @param arguments
	 *            Message arguments
	 */
	default void warn(String pattern, Object... arguments) {
		logger().warn(pattern, arguments);
	}

	/**
	 * Log an error message
	 * 
	 * @param pattern
	 *            Message pattern
	 * @param arguments
	 *            Message arguments
	 */
	default void error(String pattern, Object... arguments) {
		logger().error(pattern, arguments);
	}

	/**
	 * Log a message
	 * 
	 * @param level
	 *            Log level
	 * @param pattern
	 *            Message pattern
	 * @param arguments
	 *            Message arguments
	 */
	default void log(LogLevel level, String pattern, Object... arguments) {
		level.log(this, pattern, arguments);
	}
}
