package org.jvnet.jaxb2_commons.locator;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Locator for the collection entry.
 */
public final class DefaultListEntryObjectLocator extends AbstractObjectLocator implements ListEntryObjectLocator
{
  /**
   * Entry index.
   */
  protected final int index;

  /**
   * Constructs a new entry locator.
   *
   * @param parentLocator parent locator.
   * @param object        master object.
   * @param fieldName     field name.
   * @param index         entry index.
   */
  protected DefaultListEntryObjectLocator(final ObjectLocator parentLocator, final Object object, final int index)
  {
    super(parentLocator, object);
    this.index = index;
  }
  
  protected DefaultListEntryObjectLocator(final ObjectLocator parentLocator, final int index)
  {
    super(parentLocator, parentLocator.getObject());
    this.index = index;
  }
  

  /**
   * Returns entry index.
   *
   * @return Index of the entry.
   */
  public int getIndex()
  {
    return index;
  }

//  /**
//   * Returns step expression (<code><em>name</em>[<em>index</em>]</code>).
//   *
//   * @return Step expression.
//   */
//  public String getStep()
//  {
//    return getFieldName() + "[" + getIndex() + "]";
//  }


  public Object[] getMessageParameters()
  {
    return new Object[]
    {
      getObject(),
      new Integer(getIndex())
    };
  }

  public String getMessage(final ResourceBundle bundle)
  {
    try
    {
      final String messageTemplate = bundle.getString(getMessageCode());
      return MessageFormat.format(messageTemplate, getMessageParameters());
    }
    catch (MissingResourceException mrex)
    {
      return
        MessageFormat.format("Object: {0}\nField: {1}\nEntry index: {2}.", getMessageParameters());
    }
  }

  public boolean equals(final Object obj)
  {
    boolean result = false;
    if (obj instanceof ListEntryObjectLocator)
    {
      final ListEntryObjectLocator locator = (ListEntryObjectLocator) obj;
      result = (getObject() == locator.getObject()) &&
        getIndex() == locator.getIndex();

    }
    return result;
  }

  public int hashCode()
  {
    return super.hashCode() * 23 + getIndex();
  }
}
