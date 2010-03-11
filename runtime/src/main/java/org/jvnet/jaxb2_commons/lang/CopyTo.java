package org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;

public interface CopyTo {
	
	public Object createNewInstance();

	public Object copyTo(Object target, CopyBuilder copyBuilder);

}
