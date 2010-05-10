package org.jvnet.jaxb2_commons.locator.util;

import org.jvnet.jaxb2_commons.locator.FieldObjectLocator;
import org.jvnet.jaxb2_commons.locator.ListEntryObjectLocator;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.xml.sax.Locator;

public class LocatorUtils {

	private LocatorUtils() {

	}

	public static String getLocation(Locator locator) {
		if (locator == null) {
			return "<unknown>";
		} else {
			return locator.getPublicId() + ":" + locator.getSystemId() + ":"
					+ locator.getLineNumber() + ":" + locator.getColumnNumber();
		}
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			Object value) {
		return locator == null ? null : locator.field(name, value);
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			boolean value) {
		return locator == null ? null : locator.field(name, Boolean
				.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			byte value) {
		return locator == null ? null : locator
				.field(name, Byte.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			char value) {
		return locator == null ? null : locator.field(name, Character
				.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			double value) {
		return locator == null ? null : locator.field(name, Double
				.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			float value) {
		return locator == null ? null : locator.field(name, Float
				.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			int value) {
		return locator == null ? null : locator.field(name, Integer
				.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			long value) {
		return locator == null ? null : locator
				.field(name, Long.valueOf(value));
	}

	public static FieldObjectLocator field(ObjectLocator locator, String name,
			short value) {
		return locator == null ? null : locator.field(name, Short
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, Object value) {
		return locator == null ? null : locator.entry(index, value);
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, boolean value) {
		return locator == null ? null : locator.entry(index, Boolean
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, byte value) {
		return locator == null ? null : locator.entry(index, Byte
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, char value) {
		return locator == null ? null : locator.entry(index, Character
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, double value) {
		return locator == null ? null : locator.entry(index, Double
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, float value) {
		return locator == null ? null : locator.entry(index, Float
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, int value) {
		return locator == null ? null : locator.entry(index, Integer
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, long value) {
		return locator == null ? null : locator.entry(index, Long
				.valueOf(value));
	}

	public static ListEntryObjectLocator entry(ObjectLocator locator,
			int index, short value) {
		return locator == null ? null : locator.entry(index, Short
				.valueOf(value));
	}

}
