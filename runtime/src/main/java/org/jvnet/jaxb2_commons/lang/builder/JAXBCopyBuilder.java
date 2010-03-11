package org.jvnet.jaxb2_commons.lang.builder;

import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.field;

import javax.xml.bind.JAXBElement;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.w3c.dom.Node;

public class JAXBCopyBuilder extends CopyBuilder {

	@SuppressWarnings("unchecked")
	@Override
	public Object copy(ObjectLocator locator, Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Node) {
			return ((Node) object).cloneNode(true);
		} else if (object instanceof JAXBElement) {
			final JAXBElement sourceElement = (JAXBElement) object;
			final Object sourceObject = sourceElement.getValue();
			final Object copyObject = copy(field(locator, "value"),
					sourceObject);
			final JAXBElement copyElement = new JAXBElement(sourceElement
					.getName(), sourceElement.getDeclaredType(), sourceElement
					.getScope(), copyObject);
			return copyElement;
		} else {
			return super.copy(locator, object);
		}
	}

	public static JAXBCopyBuilder INSTANCE = new JAXBCopyBuilder();
}
