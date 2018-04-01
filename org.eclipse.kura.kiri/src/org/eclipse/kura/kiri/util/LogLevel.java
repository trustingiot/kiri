package org.eclipse.kura.kiri.util;

/**
 * An enumeration of log levels
 */
public enum LogLevel {
	info {
		@Override
		void log(Loggable loggable, String pattern, Object... arguments) {
			loggable.info(pattern, arguments);
		}
	},
	debug {
		@Override
		void log(Loggable loggable, String pattern, Object... arguments) {
			loggable.debug(pattern, arguments);
		}
	},
	warn {
		@Override
		void log(Loggable loggable, String pattern, Object... arguments) {
			loggable.warn(pattern, arguments);
		}
	},
	error {
		@Override
		void log(Loggable loggable, String pattern, Object... arguments) {
			loggable.error(pattern, arguments);
		}
	};

	/**
	 * Log a message
	 * 
	 * @param loggable
	 *            A object that can log
	 * @param pattern
	 *            Message pattern
	 * @param arguments
	 *            Message arguments
	 */
	abstract void log(Loggable loggable, String pattern, Object... arguments);

}
