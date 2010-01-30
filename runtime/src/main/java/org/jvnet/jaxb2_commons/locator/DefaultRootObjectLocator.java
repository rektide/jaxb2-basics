package org.jvnet.jaxb2_commons.locator;

public final class DefaultRootObjectLocator extends AbstractObjectLocator
    implements
    RootObjectLocator {

  public DefaultRootObjectLocator(Object object) {
    super(null, object);
  }

  public boolean equals(Object object) {
    if (this == object)
      return true;
    if (object == null)
      return false;
    if (object instanceof RootObjectLocator) {
      return ((RootObjectLocator) object).getObject() == getObject();
    }
    else {
      return false;
    }
  }
}
