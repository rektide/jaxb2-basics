package org.jvnet.jaxb2_commons.util;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.outline.FieldOutline;

public class FieldAccessorUtils {

	private static final JType[] NONE = new JType[0];
	

	public static JExpression get(FieldOutline fieldOutline)
	{
		return get(JExpr._this(), fieldOutline);
	}

	public static JExpression get(JExpression _this, FieldOutline fieldOutline)
	{
		final JMethod getter = getter(fieldOutline);
		return _this.invoke(getter);
	}

	public static JMethod getter(FieldOutline fieldOutline) {
		final JDefinedClass theClass = fieldOutline.parent().implClass;
		final String publicName = fieldOutline.getPropertyInfo().getName(true);
		final JMethod getgetter = theClass.getMethod("get" + publicName, NONE);
		if (getgetter != null) {
			return getgetter;
		} else {
			final JMethod isgetter = theClass
					.getMethod("is" + publicName, NONE);
			if (isgetter != null) {
				return isgetter;
			} else {
				return null;
			}
		}
	}

	public static JMethod issetter(FieldOutline fieldOutline) {
		final JDefinedClass theClass = fieldOutline.parent().implClass;
		final String publicName = fieldOutline.getPropertyInfo().getName(true);
		final String name = "isSet" + publicName;
		return theClass.getMethod(name, NONE);
	}

	public static JFieldVar field(FieldOutline fieldOutline) {
		final JDefinedClass theClass = fieldOutline.parent().implClass;
		return theClass.fields().get(
				fieldOutline.getPropertyInfo().getName(false));
	}

	public static JMethod setter(FieldOutline fieldOutline) {
		final JMethod getter = getter(fieldOutline);
		assert getter != null : "Getter is required.";
		final JType type = getter.type();
		final JDefinedClass theClass = fieldOutline.parent().implClass;
		final String publicName = fieldOutline.getPropertyInfo().getName(true);
		final String name = "set" + publicName;
		return theClass.getMethod(name, new JType[] { type });
	}
	
	public static JStatement set(FieldOutline fieldOutline, JExpression value)
	{
		return set(JExpr._this(), fieldOutline, value);
	}

	public static JStatement set(JExpression _this, FieldOutline fieldOutline, JExpression value)
	{
		final JMethod setter = setter(fieldOutline);
		return _this.invoke(setter).arg(value);
	}
	

}
