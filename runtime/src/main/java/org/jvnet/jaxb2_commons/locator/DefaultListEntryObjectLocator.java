package org.jvnet.jaxb2_commons.locator;

import java.text.MessageFormat;

/**
 * Locator for the collection entry.
 */
public final class DefaultListEntryObjectLocator extends AbstractObjectLocator
		implements ListEntryObjectLocator {
	/**
	 * Entry index.
	 */
	protected final int index;

	/**
	 * Constructs a new entry locator.
	 * 
	 * @param parentLocator
	 *            parent locator.
	 * @param object
	 *            master object.
	 * @param fieldName
	 *            field name.
	 * @param index
	 *            entry index.
	 */
	protected DefaultListEntryObjectLocator(final ObjectLocator parentLocator,
			final int index, Object entryValue) {
		super(parentLocator, entryValue);
		this.index = index;
	}

	/**
	 * Returns entry index.
	 * 
	 * @return Index of the entry.
	 */
	public int getIndex() {
		return index;
	}

	// /**
	// * Returns step expression (<code><em>name</em>[<em>index</em>]</code>).
	// *
	// * @return Step expression.
	// */
	// public String getStep()
	// {
	// return getFieldName() + "[" + getIndex() + "]";
	// }

	public Object[] getMessageParameters() {
		return new Object[] { getObject(), Integer.valueOf(getIndex()) };
	}

	@Override
	protected String getDefaultMessage() {
		return MessageFormat.format("Entry index: {1}\nEntry value: {0}.",
				getMessageParameters());
	}

}
