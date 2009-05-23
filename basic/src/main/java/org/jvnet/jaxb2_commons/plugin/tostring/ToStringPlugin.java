package org.jvnet.jaxb2_commons.plugin.tostring;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.util.ClassUtils;
import org.jvnet.jaxb2_commons.util.FieldAccessorFactory;
import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class ToStringPlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "XtoString";
	}

	@Override
	public String getUsage() {
		return ""
				+ "-XtoString:  generates toString() method based on Jakarta Commons Lang"
				+ "-XtoString-toStringBuilder:  toString builder class to use. Defaults to "
				+ JAXBToStringBuilder.class.getName() + ".";
	}
	
	@Override
	public List<String> getCustomizationURIs() {
		return Arrays.asList(Customizations.NAMESPACE_URI,
				org.jvnet.jaxb2_commons.plugin.tostring.Customizations.NAMESPACE_URI);
	}

	private Class toStringBuilder = JAXBToStringBuilder.class;

	public void setToStringBuilder(Class equalsBuilderClass) {
		if (!ToStringBuilder.class.isAssignableFrom(equalsBuilderClass))
			throw new IllegalArgumentException("The class must extend ["
					+ ToStringBuilder.class.getName() + "].");
		this.toStringBuilder = equalsBuilderClass;
	}

	public Class getToStringBuilder() {
		return toStringBuilder;
	}

	private Ignoring ignoring = new CustomizedIgnoring(
			org.jvnet.jaxb2_commons.plugin.tostring.Customizations.IGNORED_ELEMENT_NAME,
			Customizations.IGNORED_ELEMENT_NAME,
			Customizations.GENERATED_ELEMENT_NAME);

	public Ignoring getIgnoring() {
		return ignoring;
	}

	public void setIgnoring(Ignoring ignoring) {
		this.ignoring = ignoring;
	}

	@Override
	public boolean run(Outline outline, @SuppressWarnings("unused")
	Options opt, @SuppressWarnings("unused")
	ErrorHandler errorHandler) {
		for (final ClassOutline classOutline : outline.getClasses())
			if (!getIgnoring().isIgnored(classOutline)) {
				processClassOutline(classOutline);
			}
		return true;
	}

	protected void processClassOutline(ClassOutline classOutline) {
		final JDefinedClass theClass = classOutline.implClass;

		@SuppressWarnings("unused")
		final JMethod toString$toString = generateToString$toString(
				classOutline, theClass);
		@SuppressWarnings("unused")
		final JMethod object$toString = generateObject$toString(classOutline,
				theClass);
	}

	protected JMethod generateObject$toString(final ClassOutline classOutline,
			final JDefinedClass theClass) {
		final JMethod object$toString = theClass.method(JMod.PUBLIC, theClass
				.owner().ref(String.class), "toString");
		{
			final JBlock body = object$toString.body();

			final JVar toStringBuilder = body.decl(JMod.FINAL, theClass.owner()
					.ref(ToStringBuilder.class), "toStringBuilder", JExpr._new(
					theClass.owner().ref(getToStringBuilder())).arg(
					JExpr._this()));
			body.invoke("toString").arg(toStringBuilder);
			body._return(toStringBuilder.invoke("toString"));
		}
		return object$toString;
	}

	protected JMethod generateToString$toString(ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(ToString.class));

		final JMethod toString$toString = theClass.method(JMod.PUBLIC, theClass
				.owner().VOID, "toString");
		{
			final JVar toStringBuilder = toString$toString.param(
					ToStringBuilder.class, "toStringBuilder");
			final JBlock body = toString$toString.body();

			if (classOutline.target.getBaseClass() != null
					|| classOutline.target.getRefBaseClass() != null) {
				body.invoke(JExpr._super(), "toString").arg(toStringBuilder);
			}

			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					final JBlock block = body.block();
					final JVar theValue = block.decl(fieldOutline.getRawType(),
							"the"
									+ fieldOutline.getPropertyInfo().getName(
											true));
					FieldAccessorFactory.createFieldAccessor(fieldOutline,
							JExpr._this()).toRawValue(block, theValue);

					block.invoke(toStringBuilder, "append").arg(
							JExpr.lit(fieldOutline.getPropertyInfo().getName(
									false))).arg(theValue);
				}
		}
		return toString$toString;
	}

}
