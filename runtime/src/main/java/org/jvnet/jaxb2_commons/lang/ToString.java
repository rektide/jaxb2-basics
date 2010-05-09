package org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public interface ToString {

	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer,
			ToStringStrategy toStringStrategy);

	public StringBuffer appendFields(ObjectLocator locator, StringBuffer stringBuffer,
			ToStringStrategy toStringStrategy);

}
