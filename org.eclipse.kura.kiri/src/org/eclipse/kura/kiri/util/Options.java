package org.eclipse.kura.kiri.util;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a service options
 */
public abstract class Options {

	protected final Map<String, Object> properties;

	/**
	 * @param properties
	 *            Service properties
	 */
	public Options(Map<String, Object> properties) {
		this.properties = properties;
	}

	/**
	 * Reads the property of type T 'propertyName' from 'properties' and returns it.
	 * Returns 'defaultValue' if:
	 * <ul>
	 * <li>'properties' contains no mapping for the key of the property.</li>
	 * <li>T is not a superclass or superinterface of its class.</li>
	 * </ul>
	 * 
	 * @param propertyName
	 *            Property key
	 * @param defaultValue
	 *            Property default value
	 * 
	 * @return Property value
	 */
	protected <T> T read(String propertyName, T defaultValue) {
		return getValid(properties.getOrDefault(propertyName, defaultValue), defaultValue);
	}

	/**
	 * Returns valid value for 'candidate'. The valid value is:
	 * <ul>
	 * <li>'candidate' if its class or interface is a superclass or superinterface
	 * of 'defaultValue'.</li>
	 * <li>'defaultValue' otherwise.</li>
	 * </ul>
	 * 
	 * @param candidate
	 *            Candidate
	 * @param defaultValue
	 *            Default value
	 * 
	 * @return Valid value for 'candidate'
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getValid(Object candidate, T defaultValue) {
		return validProperty(candidate, defaultValue) ? (T) candidate : defaultValue;
	}

	/**
	 * Checks if class of 'prop' is a superclass or superinterface of
	 * 'defaultValue'.
	 * 
	 * @param prop
	 *            Instance whose class or interface should be a superclass or
	 *            superinterface of 'defaultValue'
	 * @param defaultValue
	 *            Instance to check
	 * 
	 * @return True if class of 'prop' is a superclass or superinterface of
	 *         'defaultValue'
	 */
	private static <T> boolean validProperty(Object prop, T defaultValue) {
		return (prop != null && prop.getClass().isAssignableFrom(defaultValue.getClass()));
	}

	/**
	 * Reads the property of type T 'propertyName' from 'properties' and returns it
	 * cast to R by applying 'validator'. Returns 'defaultValue' if:
	 * <ul>
	 * <li>'properties' contains no mapping for the key of the property.</li>
	 * <li>T is not a superclass or superinterface of its class.</li>
	 * <li>The conversion fails</li>
	 * </ul>
	 * 
	 * @param propertyName
	 *            Property key
	 * @param defaultValue
	 *            Property default value
	 * @param validator
	 *            Property validator and converter
	 * 
	 * @return Property value
	 */
	protected <T, R> R read(String propertyName, T defaultValue, BiFunction<T, T, R> validator) {
		return validator.apply(read(propertyName, defaultValue), defaultValue);
	}

	/**
	 * Reads the property of type R 'propertyName' from 'properties' and returns it
	 * cast to T by applying 'converter'. Returns 'defaultValue' if:
	 * <ul>
	 * <li>'properties' contains no mapping for the key of the property.</li>
	 * <li>T is not a superclass or superinterface of its class.</li>
	 * <li>The conversion fails</li>
	 * </ul>
	 * 
	 * @param propertyName
	 *            Property key
	 * @param defaultValue
	 *            Property default value
	 * @param converter
	 *            Property converter
	 * 
	 * @return Property value
	 */
	protected <T, R> T read(String propertyName, T defaultValue, Function<R, T> converter) {
		Object property = properties.get(propertyName);
		return getValid(convert(property, converter), defaultValue);
	}

	/**
	 * Converts to type T a cast from 'property' to R by applying 'converter'
	 * 
	 * @param property
	 *            Property to be converted
	 * @param converter
	 *            Property converter
	 * 
	 * @return Converted property. Null if the conversion fails
	 */
	@SuppressWarnings("unchecked")
	protected static <T, R> T convert(Object property, Function<R, T> converter) {
		try {
			return (property != null) ? converter.apply((R) property) : null;
		} catch (Exception e) {
			return null;
		}
	}
}
