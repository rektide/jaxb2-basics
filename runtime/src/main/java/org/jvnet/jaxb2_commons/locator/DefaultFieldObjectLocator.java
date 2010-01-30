package org.jvnet.jaxb2_commons.locator;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Validation event locator.
 *
 * @author Aleksei Valikov
 */
public final class DefaultFieldObjectLocator extends AbstractObjectLocator implements FieldObjectLocator {

  /**
   * Field name.
   */
  protected final String fieldName;

  /**
   * Constructs a new validation event locator.
   *
   * @param parentLocator parent location (may be <code>null</code>).
   * @param object        object.
   * @param fieldName     field name.
   */
  protected DefaultFieldObjectLocator(
      final ObjectLocator parentLocator,
      final Object object,
      final String fieldName) {
    super(parentLocator, object);
    this.fieldName = fieldName;
  }

  protected DefaultFieldObjectLocator(final ObjectLocator locator, final String fieldName) {
    super(locator.getParentLocator(), locator.getObject());
    this.fieldName = fieldName;
  }

  //  /**
  //   * Returns step for this locator. The step is a single part of location expression.
  //   *
  //   * @return Step for this locator.
  //   */
  //  public String getStep()
  //  {
  //    return getFieldName();
  //  }

  public String getFieldName() {
    return fieldName;
  }

  /**
   * Returns parameters for message formatting.
   *
   * @return Message formatting parameters.
   */
  public Object[] getMessageParameters() {
    return new Object[]{ getObject(), getFieldName() };
  }

  /**
   * Returns location message.
   *
   * @param bundle resource bundle to be used to locate message template.
   * @return location message.
   */
  public String getMessage(final ResourceBundle bundle) {
    try {
      final String messageTemplate = bundle.getString(getMessageCode());
      return MessageFormat.format(messageTemplate, getMessageParameters());
    }
    catch (MissingResourceException mrex) {
      return MessageFormat.format("Object: {0}\nField: {1}.", getMessageParameters());
    }
  }

  public int hashCode() {
    final String fieldName = getFieldName();
    int hashCode = super.hashCode();
    hashCode = hashCode * 49 + (fieldName == null ? 0 : fieldName.hashCode());
    return hashCode;
  }

  public boolean equals(final Object obj) {
    boolean result = false;
    if (obj instanceof FieldObjectLocator) {
      final FieldObjectLocator locator = (FieldObjectLocator) obj;
      result = (getObject() == locator.getObject())
          && (getFieldName().equals(locator.getFieldName()));
    }
    return result;
  }

}
