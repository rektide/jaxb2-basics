package org.jvnet.jaxb2_commons.locator;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Validation event locator.
 * 
 * @author Aleksei Valikov
 */
public final class DefaultFieldObjectLocator extends AbstractObjectLocator
		implements FieldObjectLocator {

	/**
	 * Field name.
	 */
	protected final String fieldName;

	/**
	 * Constructs a new validation event locator.
	 * 
	 * @param parentLocator
	 *            parent location (may be <code>null</code>).
	 * @param object
	 *            object.
	 * @param fieldName
	 *            field name.
	 */
	protected DefaultFieldObjectLocator(final ObjectLocator parentLocator,
			final String fieldName, final Object fieldValue) {
		super(parentLocator, fieldValue);
		this.fieldName = fieldName;
	}

	// /**
	// * Returns step for this locator. The step is a single part of location
	// expression.
	// *
	// * @return Step for this locator.
	// */
	// public String getStep()
	// {
	// return getFieldName();
	// }

	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Returns parameters for message formatting.
	 * 
	 * @return Message formatting parameters.
	 */
	public Object[] getMessageParameters() {
		return new Object[] { getObject(), getFieldName() };
	}

	@Override
	protected String getDefaultMessage() {
		return MessageFormat.format("Field: {1}\nField value: {0}.",
				getMessageParameters());
	}

}
