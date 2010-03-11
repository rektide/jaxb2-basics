package org.jvnet.jaxb2_commons.lang;

public interface Copyable {

	public Object createNewInstance();

	public Object copyTo(Object target);

}
