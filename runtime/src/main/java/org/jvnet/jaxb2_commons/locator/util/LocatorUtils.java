package org.jvnet.jaxb2_commons.locator.util;

import org.xml.sax.Locator;

public class LocatorUtils {

  private LocatorUtils() {

  }

  public static String getLocation(Locator locator) {
    if (locator == null) {
      return "<unknown>";
    }
    else {
      return locator.getPublicId()
          + ":"
          + locator.getSystemId()
          + ":"
          + locator.getLineNumber()
          + ":"
          + locator.getColumnNumber();
    }
  }

}
