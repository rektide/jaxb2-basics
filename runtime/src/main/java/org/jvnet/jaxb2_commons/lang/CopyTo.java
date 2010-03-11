package org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public interface CopyTo {

	public Object createNewInstance();

	public Object copyTo(Object target, CopyBuilder copyBuilder);

	public Object copyTo(ObjectLocator locator, Object target,
			CopyBuilder copyBuilder);

}
