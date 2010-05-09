package org.jvnet.jaxb2_commons.lang;

import javax.xml.bind.JAXBElement;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class JAXBToStringStrategy extends DefaultToStringStrategy {

	private String jaxbElementStart = "<";

	private String jaxbElementEnd = ">";

	protected void appendJAXBElementStart(StringBuffer stringBuffer) {
		stringBuffer.append(jaxbElementStart);
	}

	protected void appendJAXBElementEnd(StringBuffer stringBuffer) {
		stringBuffer.append(jaxbElementEnd);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected StringBuffer appendInternal(ObjectLocator locator,
			StringBuffer stringBuffer, Object value) {
		if (value instanceof JAXBElement) {
			final JAXBElement jaxbElement = (JAXBElement) value;
			appendInternal(locator, stringBuffer, jaxbElement);
		} else {
			super.appendInternal(locator, stringBuffer, value);
		}
		return stringBuffer;
	}

	@SuppressWarnings("unchecked")
	protected StringBuffer appendInternal(ObjectLocator locator,
			StringBuffer stringBuffer, JAXBElement value) {
		appendJAXBElementStart(stringBuffer);
		stringBuffer.append(value.getName());
		appendContentStart(stringBuffer);
		append(locator, stringBuffer, value.getValue());
		appendContentEnd(stringBuffer);
		appendJAXBElementEnd(stringBuffer);
		return stringBuffer;
	}
	
	public static final ToStringStrategy INSTANCE = new JAXBToStringStrategy();

}
