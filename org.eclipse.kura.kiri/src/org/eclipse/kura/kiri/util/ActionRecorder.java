package org.eclipse.kura.kiri.util;

import java.text.MessageFormat;
import java.util.function.Consumer;

/**
 * Allows the classes that implements it to execute actions and logs it.
 * 
 * Implements the Loggable trait
 */
public interface ActionRecorder extends Loggable {

	/**
	 * Executes an actions and logs it
	 * 
	 * @param logLevel
	 *            Log level
	 * @param verb
	 *            Action verb
	 * @param consumer
	 *            Action consumer
	 * @param argument
	 *            Action argument
	 */
	default <T> void performRegisteredAction(LogLevel logLevel, String verb, Consumer<T> consumer, T argument) {
		String actionId = getActionID(verb);
		log(logLevel, actionId);
		try {
			consumer.accept(argument);
			log(logLevel, "{} done", actionId);
		} catch (Exception e) {
			error("{} failed. Message: {}", actionId, e.getMessage());
		}
	}

	/**
	 * Generates action id
	 * 
	 * @param verb
	 *            Action verb
	 * @return Action id
	 */
	default String getActionID(String verb) {
		return MessageFormat.format("{0} {1}", verb, getID());
	}

	/**
	 * Executes an actions and logs an info message
	 * 
	 * @param verb
	 *            Action verb
	 * @param consumer
	 *            Action consumer
	 * @param argument
	 *            Action argument
	 */
	default <T> void performRegisteredAction(String verb, Consumer<T> consumer, T argument) {
		performRegisteredAction(LogLevel.info, verb, consumer, argument);
	}

	/**
	 * Executes a runnable and logs it
	 * 
	 * @param logLevel
	 *            Log level
	 * @param verb
	 *            Action verb
	 * @param runnable
	 *            Runnable
	 */
	default void performRegisteredAction(LogLevel logLevel, String verb, Runnable runnable) {
		performRegisteredAction(logLevel, verb, (nothing) -> runnable.run(), null);
	}

	/**
	 * Executes a runnable and logs and info message
	 * 
	 * @param verb
	 *            Action verb
	 * @param runnable
	 *            Runnable
	 */
	default void performRegisteredAction(String verb, Runnable runnable) {
		performRegisteredAction(LogLevel.info, verb, runnable);
	}

	/**
	 * Returns class id
	 * 
	 * @return Class id
	 */
	String getID();
}
