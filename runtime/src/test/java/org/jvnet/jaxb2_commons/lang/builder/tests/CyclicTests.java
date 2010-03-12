package org.jvnet.jaxb2_commons.lang.builder.tests;

import java.util.IdentityHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.locator.DefaultRootObjectLocator;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

public class CyclicTests extends TestCase {

	public interface CopyToInstance extends CopyTo {
	}

	public static class A implements CopyToInstance {
		public B b;

		public Object createNewInstance() {
			return new A();
		}

		public Object copyTo(Object target, CopyBuilder copyBuilder) {
			return copyTo(null, target, copyBuilder);
		}

		public Object copyTo(ObjectLocator locator, Object target,
				CopyBuilder copyBuilder) {
			final A that = (A) target;
			that.b = (B) copyBuilder.copy(LocatorUtils.field(locator, "b"),
					this.b);
			return that;
		}

	}

	public static class B implements CopyToInstance {
		public A a;

		public Object createNewInstance() {
			return new B();
		}

		public Object copyTo(Object target, CopyBuilder copyBuilder) {
			return copyTo(null, target, copyBuilder);
		}

		public Object copyTo(ObjectLocator locator, Object target,
				CopyBuilder copyBuilder) {
			final B that = (B) target;
			that.a = (A) copyBuilder.copy(LocatorUtils.field(locator, "a"),
					this.a);
			return that;
		}
	}

	public void testCycle() throws Exception {
		final A a = new A();
		final B b = new B();
		a.b = b;
		b.a = a;

		final A a1 = (A) new JAXBCopyBuilder() {
			private Map<Object, Object> copies = new IdentityHashMap<Object, Object>();

			@Override
			public Object copy(ObjectLocator locator, Object object) {
				final Object existingCopy = copies.get(object);
				if (existingCopy != null) {
					return existingCopy;
				} else {
					if (object instanceof CopyToInstance) {
						final CopyToInstance source = (CopyToInstance) object;
						final Object newCopy = source.createNewInstance();
						copies.put(object, newCopy);
						source.copyTo(newCopy, this);
						return newCopy;
					} else {
						final Object newCopy = super.copy(locator, object);
						copies.put(object, newCopy);
						return newCopy;
					}
				}
			}
		}.copy(new DefaultRootObjectLocator(a), a);

		assertSame(a1.b.a, a1);
	}

}
