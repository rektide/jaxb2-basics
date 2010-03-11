package org.jvnet.jaxb2_commons.lang.builder;

import static org.jvnet.jaxb2_commons.locator.util.LocatorUtils.entry;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class CopyBuilder {

	public Object copy(ObjectLocator locator, Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof String) {
			return object;
		} else if (object instanceof Number) {
			return object;
		} else if (object instanceof List<?>) {
			return copy(locator, (List<?>) object);
		} else if (object instanceof Set<?>) {
			return copy(locator, (Set<?>) object);
		} else if (object instanceof CopyTo) {
			final Object newInstance = ((CopyTo) object).createNewInstance();
			return ((CopyTo) object).copyTo(newInstance, this);
		} else if (object instanceof Copyable) {
			final Object newInstance = ((Copyable) object).createNewInstance();
			return ((Copyable) object).copyTo(newInstance);
		} else if (object instanceof Cloneable) {
			return cloneCloneable((Cloneable) object);
		} else {
			return object;
		}
	}

	public long copy(ObjectLocator locator, long value) {
		return value;
	}

	public int copy(ObjectLocator locator, int value) {
		return value;
	}

	public short copy(ObjectLocator locator, short value) {
		return value;
	}

	public char copy(ObjectLocator locator, char value) {
		return value;
	}

	public byte copy(ObjectLocator locator, byte value) {
		return value;
	}

	public double copy(ObjectLocator locator, double value) {
		return value;
	}

	public float copy(ObjectLocator locator, float value) {
		return value;
	}

	public boolean copy(ObjectLocator locator, boolean value) {
		return value;
	}

	public Object[] copy(ObjectLocator locator, Object[] array) {
		final Object[] copy = new Object[array.length];
		for (int index = 0; index < array.length; index++) {
			final Object element = array[index];
			final Object elementCopy = copy(entry(locator, index), element);
			copy[index] = elementCopy;
		}
		return copy;
	}

	public long[] copy(ObjectLocator locator, long[] array) {
		final long[] copy = new long[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public int[] copy(ObjectLocator locator, int[] array) {
		final int[] copy = new int[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public short[] copy(ObjectLocator locator, short[] array) {
		final short[] copy = new short[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public char[] copy(ObjectLocator locator, char[] array) {
		final char[] copy = new char[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public byte[] copy(ObjectLocator locator, byte[] array) {
		final byte[] copy = new byte[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public double[] copy(ObjectLocator locator, double[] array) {
		final double[] copy = new double[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public float[] copy(ObjectLocator locator, float[] array) {
		final float[] copy = new float[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public boolean[] copy(ObjectLocator locator, boolean[] array) {
		final boolean[] copy = new boolean[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	private Object cloneCloneable(Cloneable object) {
		Method method = null;

		try {
			method = object.getClass().getMethod("clone", (Class[]) null);
		} catch (NoSuchMethodException nsmex) {
			method = null;
		}

		if (method == null || !Modifier.isPublic(method.getModifiers())) {

			throw new UnsupportedOperationException(
					"Could not clone object [" + object + "].",
					new CloneNotSupportedException(
							"Object class ["
									+ object.getClass()
									+ "] implements java.lang.Cloneable interface, "
									+ "but does not provide a public no-arg clone() method. "
									+ "By convention, classes that implement java.lang.Cloneable "
									+ "should override java.lang.Object.clone() method (which is protected) "
									+ "with a public method."));
		}

		final boolean wasAccessible = method.isAccessible();
		try {
			if (!wasAccessible) {
				try {
					method.setAccessible(true);
				} catch (SecurityException ignore) {
				}
			}

			return method.invoke(object, (Object[]) null);
		} catch (Exception ex) {
			throw new UnsupportedOperationException(
					"Could not clone the object ["
							+ object
							+ "] as invocation of the clone() method has thrown an exception.",
					ex);
		} finally {
			if (!wasAccessible) {
				try {
					method.setAccessible(false);
				} catch (SecurityException ignore) {
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Object copy(ObjectLocator locator, List list) {
		final List copy = new ArrayList(list.size());
		int index = 0;
		for (final Object element : list) {
			final Object copyElement = copy(entry(locator, index++), element);
			copy.add(copyElement);
		}
		return copy;
	}

	@SuppressWarnings("unchecked")
	public Object copy(ObjectLocator locator, Set set) {
		final Set copy = new HashSet(set.size());
		int index = 0;
		for (final Object element : set) {
			final Object copyElement = copy(entry(locator, index++), element);
			copy.add(copyElement);
		}
		return copy;
	}
}
