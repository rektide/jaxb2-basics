package org.jvnet.jaxb2_commons.lang.builder.tests;

import java.util.IdentityHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;

public class CyclicTests extends TestCase {
	
	public interface CopyToInstance extends CopyTo
	{
		public Object createCopy();
	}

	public static class A implements CopyToInstance {
		public B b;
		
		public Object createCopy() {
			return new A();
		}

		public Object copyTo(Object target, CopyBuilder copyBuilder) {
			final A that = (A) target;
			that.b = (B) copyBuilder.copy(this.b);
			return that;
		}

	}

	public static class B implements CopyToInstance {
		public A a;
		
		public Object createCopy()
		{
			return new B();
		}

		public Object copyTo(Object target, CopyBuilder copyBuilder) {
			final B that = (B) target;
			that.a = (A) copyBuilder.copy(this.a);
			return that;
		}
	}

	public void testCycle() throws Exception {
		final A a = new A();
		final B b = new B();
		a.b = b;
		b.a = a;

		final A a1 = (A) new JAXBCopyBuilder()
		{
			private Map<Object, Object> copies = new IdentityHashMap<Object, Object>();

			public Object copy(Object object) {
				final Object existingCopy = copies.get(object);
				if (existingCopy != null)
				{
					return existingCopy;
				}
				else
				{
					if (object instanceof CopyToInstance)
					{
						final CopyToInstance source = (CopyToInstance) object;
						final Object newCopy = source.createCopy();
						copies.put(object, newCopy);
						source.copyTo(newCopy, this);
						return newCopy;
					}
					else
					{
						final Object newCopy = super.copy(object);
						copies.put(object, newCopy);
						return newCopy;
					}
				}
			}
		}.copy(a);

		assertSame(a1.b.a, a1);
	}

}
