package org.jvnet.jaxb2_commons.util;

import org.jvnet.jaxb2_commons.xjc.outline.FieldAccessorEx;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.FieldAccessor;
import com.sun.tools.xjc.outline.FieldOutline;

public class FieldAccessorFactory {
	private FieldAccessorFactory() {

	}

	public static FieldAccessorEx createFieldAccessor(FieldOutline fieldOutline,
			JExpression targetObject) {
		return new PropertyFieldAccessor(fieldOutline, targetObject);

	}

	private static class PropertyFieldAccessor implements FieldAccessorEx {
		private static final JType[] ABSENT = new JType[0];
		private final FieldOutline fieldOutline;
		private final JExpression targetObject;
		private final JMethod isSetter;
		private final JMethod unSetter;
		private final JMethod getter;
		private final JMethod setter;
		private FieldAccessor fieldAccessor;
		private final JType type;

		public PropertyFieldAccessor(final FieldOutline fieldOutline,
				JExpression targetObject) {
			super();
			this.fieldOutline = fieldOutline;
			this.targetObject = targetObject;
			this.fieldAccessor = fieldOutline.create(targetObject);
			final String publicName = fieldOutline.getPropertyInfo().getName(
					true);
			final JDefinedClass theClass = fieldOutline.parent().implClass;
			final String getterName = "get" + publicName;
			final String setterName = "set" + publicName;
			this.getter = theClass.getMethod(getterName, ABSENT);
			this.type = this.getter != null ? this.getter.type() : fieldOutline
					.getRawType();
			// fieldOutline.getRawType();
			final JType rawType = fieldOutline.getRawType();
			final JMethod boxifiedSetter = theClass.getMethod(setterName,
					new JType[] { rawType.boxify() });
			final JMethod unboxifiedSetter = theClass.getMethod(setterName,
					new JType[] { rawType.unboxify() });
			this.setter = boxifiedSetter != null ? boxifiedSetter
					: unboxifiedSetter;
			this.isSetter = theClass.getMethod("isSet" + publicName, ABSENT);
			this.unSetter = theClass.getMethod("unset" + publicName, ABSENT);
		}
		
		public JType getType() {
			return type;
		}

		public FieldOutline owner() {
			return fieldOutline;
		}

		public CPropertyInfo getPropertyInfo() {
			return fieldOutline.getPropertyInfo();
		}

		public JExpression hasSetValue() {
			if (isSetter != null) {
				return targetObject.invoke(isSetter);
			} else {
				return fieldAccessor.hasSetValue();
			}
		}

		public void unsetValues(JBlock body) {
			if (unSetter != null) {
				body.invoke(targetObject, unSetter);
			} else {

				fieldAccessor.unsetValues(body);
			}
		}

		public void fromRawValue(JBlock block, String uniqueName,
				JExpression $var) {
			if (setter != null) {
				block.invoke(targetObject, setter).arg($var);
			} else {
				unsetValues(block);
				fieldAccessor.fromRawValue(block, uniqueName, $var);
			}
		}

		public void toRawValue(JBlock block, JVar $var) {
			if (getter != null) {
				block.assign($var, targetObject.invoke(getter));
			} else {
				fieldAccessor.toRawValue(block, $var);
			}
		}
	}

}
