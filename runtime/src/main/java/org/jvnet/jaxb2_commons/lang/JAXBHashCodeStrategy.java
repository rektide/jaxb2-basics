package org.jvnet.jaxb2_commons.lang;

import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.entry;
import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.field;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class JAXBHashCodeStrategy extends DefaultHashCodeStrategy {

	public JAXBHashCodeStrategy() {
		super();
	}

	public JAXBHashCodeStrategy(int multiplierNonZeroOddNumber) {
		super(multiplierNonZeroOddNumber);
	}

	protected int hashCodeInternal(ObjectLocator locator, int hashCode,
			Object value) {
		if (value instanceof JAXBElement<?>) {
			final JAXBElement<?> element = (JAXBElement<?>) value;
			return hashCodeInternal(locator, hashCode, element);
		} else if (value instanceof List<?>) {
			final List<?> list = (List<?>) value;
			return hashCodeInternal(locator, hashCode, list);
		} else {
			return super.hashCodeInternal(locator, hashCode, value);
		}
	}

	protected int hashCodeInternal(ObjectLocator locator, int hashCode,
			final JAXBElement<?> element) {
		int currentHashCode = hashCode;
		currentHashCode = hashCode(field(locator, "name"), currentHashCode,
				element.getName());
		currentHashCode = hashCode(field(locator, "declaredType"),
				currentHashCode, element.getDeclaredType());
		currentHashCode = hashCode(field(locator, "scope"), currentHashCode,
				element.getScope());
		currentHashCode = hashCode(field(locator, "value"), currentHashCode,
				element.getValue());
		return currentHashCode;
	}

	protected int hashCodeInternal(ObjectLocator locator, int hashCode,
			final List<?> list) {
		int currentHashCode = hashCode;
		for (int index = 0; index < list.size(); index++) {
			final Object item = list.get(index);
			currentHashCode = hashCode(entry(locator, index), currentHashCode,
					item);
		}
		return currentHashCode;
	}

	public static HashCodeStrategy INSTANCE = new JAXBHashCodeStrategy();

}
