package org.jvnet.jaxb2_commons.locator.util;

import org.jvnet.jaxb2_commons.locator.FieldObjectLocator;
import org.jvnet.jaxb2_commons.locator.ListEntryObjectLocator;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.xml.sax.Locator;

public class LocatorUtils {

	private LocatorUtils() {

	}

	public static String getLocation(Locator locator) {
		if (locator == null) {
			return "<unknown>";
		} else {
			return locator.getPublicId() + ":" + locator.getSystemId() + ":"
					+ locator.getLineNumber() + ":" + locator.getColumnNumber();
		}
	}
	
	public static FieldObjectLocator field(ObjectLocator locator, String name)
	{
		return locator == null ? null : locator.field(name);
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator, int index)
	{
		return locator == null ? null : locator.entry(index);
	}

}
