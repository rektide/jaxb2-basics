package org.jvnet.jaxb2_commons.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.Validate;

public class ContextUtils {

	public static String getContextPath(Class<?>... classes) {
		Validate.noNullElements(classes);

		final StringBuffer contextPath = new StringBuffer();

		for (int index = 0; index < classes.length; index++) {
			if (index > 0) {
				contextPath.append(':');
			}
			contextPath.append(classes[index].getPackage().getName());
		}
		return contextPath.toString();
	}

	public static String toString(JAXBContext context, Object object)
			throws JAXBException {
		final Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		final StringWriter sw = new StringWriter();
		marshaller.marshal(object, sw);
		return sw.toString();
	}
}
