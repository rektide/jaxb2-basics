package org.jvnet.jaxb2_commons.locator;

import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.w3c.dom.Node;

/**
 * Abstract base class for event locators.
 * 
 * @author Aleksei Valikov
 */
public abstract class AbstractObjectLocator implements ObjectLocator {
	/**
	 * Parent locator.
	 */
	protected final ObjectLocator parentLocator;
	/**
	 * Object.
	 */
	protected final Object object;

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
	protected AbstractObjectLocator(final ObjectLocator parentLocator,
			final Object object) {
		this.object = object;
		this.parentLocator = parentLocator;
	}

	/**
	 * Returns parent locator.
	 * 
	 * @return Parent locator.
	 */
	public ObjectLocator getParentLocator() {
		return parentLocator;
	}

	public ObjectLocator[] getPath() {
		final ObjectLocator[] path = new ObjectLocator[getAncestorCount(this) + 1];
		fillPath(this, path, path.length - 1);
		return path;
	}

	private void fillPath(ObjectLocator locator, ObjectLocator[] path, int index) {
		path[index] = locator;
		final ObjectLocator parent = locator.getParentLocator();
		if (parent != null)
			fillPath(parent, path, index - 1);
	}

	private int getAncestorCount(ObjectLocator locator) {
		final ObjectLocator parent = locator.getParentLocator();
		if (parent == null)
			return 0;
		else
			return 1 + getAncestorCount(parent);
	}

	public Object getObject() {
		return object;
	}

	public int getColumnNumber() {
		return 0;
	}

	public int getLineNumber() {
		return 0;
	}

	public int getOffset() {
		return 0;
	}

	public URL getURL() {
		return null;
	}

	public Node getNode() {
		return null;
	}

	// /**
	// * Returns expression step (for EL and JXPath expressions).
	// * @return Expression step.
	// */
	// public abstract String getStep();

	public String toString() {
		return getMessage();
	}

	/**
	 * Returns message code.
	 * 
	 * @return Message code.
	 */
	public String getMessageCode() {
		return getClass().getName();
	}

	public Object[] getMessageParameters() {
		return new Object[] { getObject() };
	}

	public String getMessage(ResourceBundle bundle) {
		try {
			final String messageTemplate = bundle.getString(getMessageCode());
			return MessageFormat
					.format(messageTemplate, getMessageParameters());
		} catch (MissingResourceException mrex) {
			return MessageFormat.format("Object: {0}.", getMessageParameters());
		}
	}

	/**
	 * Returns location message.
	 * 
	 * @return Location message.
	 */
	public String getMessage() {
		return getMessage(ResourceBundle.getBundle(getClass().getPackage()
				.getName()
				+ ".messages"));
	}

	public int hashCode() {
		int hashCode = getObject().hashCode();
		return hashCode;
	}

	public ListEntryObjectLocator entry(int index) {
		return new DefaultListEntryObjectLocator(this, index);
	}

	public ListEntryObjectLocator entry(Object object, int index) {
		return new DefaultListEntryObjectLocator(this, object, index);
	}

	public FieldObjectLocator field(String name) {
		return new DefaultFieldObjectLocator(this, name);
	}

	public FieldObjectLocator field(Object object, String name) {
		return new DefaultFieldObjectLocator(this, object, name);
	}
}
