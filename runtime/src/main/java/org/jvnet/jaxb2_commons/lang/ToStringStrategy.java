package org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public interface ToStringStrategy {

	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, boolean value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, byte value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, char value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, double value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, float value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, int value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, long value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, short value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, Object value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, boolean[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, byte[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, char[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, double[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, float[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, int[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, long[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, short[] value);
	public StringBuffer append(ObjectLocator locator, StringBuffer stringBuffer, Object[] value);

	public StringBuffer appendStart(ObjectLocator parentLocator, Object parent, StringBuffer stringBuffer);
	public StringBuffer appendEnd(ObjectLocator parentLocator, Object parent, StringBuffer stringBuffer);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, boolean value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, byte value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, char value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, double value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, float value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, int value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, long value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, short value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, Object value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, boolean[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, byte[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, char[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, double[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, float[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, int[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, long[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, short[] value);
	public StringBuffer appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuffer stringBuffer, Object[] value);
}
