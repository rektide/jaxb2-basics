package org.jvnet.jaxb2_commons.locator;

import javax.xml.bind.ValidationEventLocator;

import org.jvnet.jaxb2_commons.i18n.Reportable;

public interface ObjectLocator extends ValidationEventLocator, Reportable {

	public ObjectLocator getParentLocator();

	public ObjectLocator[] getPath();

	public FieldObjectLocator field(String name);

	public ListEntryObjectLocator entry(int index);

}
