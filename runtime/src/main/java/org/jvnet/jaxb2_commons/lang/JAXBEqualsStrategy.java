package org.jvnet.jaxb2_commons.lang;

import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.entry;
import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.field;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class JAXBEqualsStrategy extends DefaultEqualsStrategy {

	@Override
	protected boolean equalsInternal(ObjectLocator leftLocator,
			ObjectLocator rightLocator, Object lhs, Object rhs) {
		if (lhs instanceof JAXBElement<?> && rhs instanceof JAXBElement<?>) {
			final JAXBElement<?> left = (JAXBElement<?>) lhs;
			final JAXBElement<?> right = (JAXBElement<?>) rhs;
			return equalsInternal(leftLocator, rightLocator, left, right);
		} else if (lhs instanceof List<?> && rhs instanceof List<?>) {
			final List<?> left = (List<?>) lhs;
			final List<?> right = (List<?>) rhs;
			return equalsInternal(leftLocator, rightLocator, left, right);
		} else {
			return super.equalsInternal(leftLocator, rightLocator, lhs, rhs);

		}
	}

	protected boolean equalsInternal(ObjectLocator leftLocator,
			ObjectLocator rightLocator, final List<?> left, final List<?> right) {
		final Iterator<?> e1 = left.iterator();
		final Iterator<?> e2 = right.iterator();
		int index = 0;
		while (e1.hasNext() && e2.hasNext()) {
			Object o1 = e1.next();
			Object o2 = e2.next();
			if (!(o1 == null ? o2 == null : equals(
					entry(leftLocator, index, o1), entry(rightLocator, index,
							o2), o1, o2))) {
				return false;
			}
			index = index + 1;
		}
		return !(e1.hasNext() || e2.hasNext());
	}

	protected boolean equalsInternal(ObjectLocator leftLocator,
			ObjectLocator rightLocator, final JAXBElement<?> left,
			final JAXBElement<?> right) {
		return
		//
		equals(field(leftLocator, "name", left.getName()), field(rightLocator,
				"name", right.getName()), left.getName(), right.getName())
				&&
				//
				equals(field(leftLocator, "value", left.getValue()), field(
						rightLocator, "name", right.getValue()), left
						.getValue(), right.getValue());
	}

	public static EqualsStrategy INSTANCE = new JAXBEqualsStrategy();
}
