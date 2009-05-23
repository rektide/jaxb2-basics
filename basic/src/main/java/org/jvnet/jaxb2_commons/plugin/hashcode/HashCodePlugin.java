package org.jvnet.jaxb2_commons.plugin.hashcode;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.util.ClassUtils;
import org.jvnet.jaxb2_commons.util.FieldAccessorUtils;
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

public class HashCodePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "XhashCode";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}
	
	@Override
	public List<String> getCustomizationURIs() {
		return Arrays.asList(Customizations.NAMESPACE_URI,
				org.jvnet.jaxb2_commons.plugin.hashcode.Customizations.NAMESPACE_URI);
	}

	private Class hashCodeBuilder = JAXBHashCodeBuilder.class;

	public void setHashCodeBuilder(Class equalsBuilderClass) {
		this.hashCodeBuilder = equalsBuilderClass;
	}

	public Class getHashCodeBuilder() {
		return hashCodeBuilder;
	}

	private Ignoring ignoring = new CustomizedIgnoring(
			org.jvnet.jaxb2_commons.plugin.hashcode.Customizations.IGNORED_ELEMENT_NAME,
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
		final JMethod hashCode$hashCode = generateHashCode$hashCode(
				classOutline, theClass);
		@SuppressWarnings("unused")
		final JMethod object$hashCode = generateObject$hashCode(classOutline,
				theClass);
	}

	protected JMethod generateObject$hashCode(final ClassOutline classOutline,
			final JDefinedClass theClass) {
		final JMethod object$hashCode = theClass.method(JMod.PUBLIC, theClass
				.owner().INT, "hashCode");
		{
			final JBlock body = object$hashCode.body();

			final JVar hashCodeBuilder = body.decl(JMod.FINAL, theClass.owner()
					.ref(HashCodeBuilder.class), "hashCodeBuilder", JExpr
					._new(theClass.owner().ref(getHashCodeBuilder())));
			body.invoke("hashCode").arg(hashCodeBuilder);
			body._return(hashCodeBuilder.invoke("toHashCode"));
		}
		return object$hashCode;
	}

	protected JMethod generateHashCode$hashCode(ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(HashCode.class));

		final JMethod hashCode$hashCode = theClass.method(JMod.PUBLIC, theClass
				.owner().VOID, "hashCode");
		{
			final JVar hashCodeBuilder = hashCode$hashCode.param(
					HashCodeBuilder.class, "hashCodeBuilder");
			final JBlock body = hashCode$hashCode.body();

			if (classOutline.target.getBaseClass() != null
					|| classOutline.target.getRefBaseClass() != null) {
				body.invoke(JExpr._super(), "hashCode").arg(hashCodeBuilder);
			}

			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					// final JBlock block = body.block();
					// final JVar theValue =
					// block.decl(fieldOutline.getRawType(),
					// "the" + fieldOutline.getPropertyInfo().getName(true));
					// FieldAccessorFactory.createFieldAccessor(fieldOutline,
					// JExpr._this()).toRawValue(block, theValue);

					body.invoke(hashCodeBuilder, "append").arg(
							FieldAccessorUtils.get(fieldOutline));
				}
		}
		return hashCode$hashCode;
	}
}
