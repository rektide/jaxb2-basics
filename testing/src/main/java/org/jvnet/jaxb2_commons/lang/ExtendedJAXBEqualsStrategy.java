package org.jvnet.jaxb2_commons.lang;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.dom.DOMSource;

import org.custommonkey.xmlunit.Diff;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.w3c.dom.Node;

public class ExtendedJAXBEqualsStrategy extends JAXBEqualsStrategy {

	@Override
	protected boolean equalsInternal(ObjectLocator leftLocator,
			ObjectLocator rightLocator, Object lhs, Object rhs) {

		if (lhs instanceof Node && rhs instanceof Node) {
			final Diff diff = new Diff(new DOMSource((Node) lhs),
					new DOMSource((Node) rhs));
			return diff.identical();
		} else if (lhs instanceof XMLGregorianCalendar
				&& rhs instanceof XMLGregorianCalendar) {
			return equals(leftLocator, rightLocator,
					((XMLGregorianCalendar) lhs).normalize()
							.toGregorianCalendar().getTimeInMillis(),
					((XMLGregorianCalendar) rhs).normalize()
							.toGregorianCalendar().getTimeInMillis());

		} else {
			return super.equalsInternal(leftLocator, rightLocator, lhs, rhs);
		}
	}

}
