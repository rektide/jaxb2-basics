package org.jvnet.jaxb2_commons.plugin.equals;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.util.ClassUtils;
import org.jvnet.jaxb2_commons.util.FieldAccessorUtils;
import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class EqualsPlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "Xequals";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}

	private Class equalsBuilderClass = JAXBEqualsBuilder.class;

	public void setEqualsBuilderClass(Class equalsBuilderClass) {
		this.equalsBuilderClass = equalsBuilderClass;
	}

	public Class getEqualsBuilderClass() {
		return equalsBuilderClass;
	}

	private Ignoring ignoring = new CustomizedIgnoring(
			org.jvnet.jaxb2_commons.plugin.equals.Customizations.IGNORED_ELEMENT_NAME,
			Customizations.IGNORED_ELEMENT_NAME,
			Customizations.GENERATED_ELEMENT_NAME);

	public Ignoring getIgnoring() {
		return ignoring;
	}

	public void setIgnoring(Ignoring ignoring) {
		this.ignoring = ignoring;
	}

	@Override
	public Collection<QName> getCustomizationElementNames() {
		return Arrays
				.asList(
						org.jvnet.jaxb2_commons.plugin.equals.Customizations.IGNORED_ELEMENT_NAME,
						Customizations.IGNORED_ELEMENT_NAME,
						Customizations.GENERATED_ELEMENT_NAME);
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
		final JMethod equals = generateEquals$Equals(classOutline, theClass);
		@SuppressWarnings("unused")
		final JMethod objectEquals = generateObject$Equals(classOutline,
				theClass);
	}

	protected JMethod generateObject$Equals(final ClassOutline classOutline,
			final JDefinedClass theClass) {
		final JMethod objectEquals = theClass.method(JMod.PUBLIC, theClass
				.owner().BOOLEAN, "equals");
		{
			final JVar object = objectEquals.param(Object.class, "object");
			final JBlock body = objectEquals.body();

			body._if(JOp.not(object._instanceof(theClass)))._then()._return(
					JExpr.FALSE);
			body._if(JExpr._this().eq(object))._then()._return(JExpr.TRUE);

			final JVar equalsBuilder = body.decl(JMod.FINAL, theClass.owner()
					.ref(EqualsBuilder.class), "equalsBuilder", JExpr
					._new(theClass.owner().ref(getEqualsBuilderClass())));
			body.invoke("equals").arg(object).arg(equalsBuilder);
			body._return(equalsBuilder.invoke("isEquals"));
		}
		return objectEquals;
	}

	protected JMethod generateEquals$Equals(ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(Equals.class));

		final JMethod equals = theClass.method(JMod.PUBLIC,
				theClass.owner().VOID, "equals");
		{
			final JBlock body = equals.body();
			final JVar object = equals.param(Object.class, "object");
			final JVar equalsBuilder = equals.param(EqualsBuilder.class,
					"equalsBuilder");

			final JConditional ifNotInstanceof = body._if(JOp.not(object
					._instanceof(theClass)));
			ifNotInstanceof._then().invoke(equalsBuilder, "appendSuper").arg(
					JExpr.FALSE);
			ifNotInstanceof._then()._return();
			body._if(JExpr._this().eq(object))._then()._return();

			if (classOutline.target.getBaseClass() != null
					|| classOutline.target.getRefBaseClass() != null) {
				body.invoke(JExpr._super(), "equals").arg(object).arg(
						equalsBuilder);
			}
			final JExpression _this = JExpr._this();

			final JVar _that = body.decl(JMod.FINAL, theClass, "that", JExpr
					.cast(theClass, object));

			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					// final JBlock block = body.block();
					//				
					// final JVar lhsValue =
					// block.decl(fieldOutline.getRawType(),
					// "lhs" + fieldOutline.getPropertyInfo().getName(true));
					// FieldAccessorFactory.createFieldAccessor(fieldOutline,
					// JExpr._this()).toRawValue(block, lhsValue);
					//
					// final JVar rhsValue =
					// block.decl(fieldOutline.getRawType(),
					// "rhs" + fieldOutline.getPropertyInfo().getName(true));
					// FieldAccessorFactory.createFieldAccessor(fieldOutline,
					// that)
					// .toRawValue(block, rhsValue);
					body.invoke(equalsBuilder, "append").arg(
							FieldAccessorUtils.get(_this, fieldOutline)).arg(
							FieldAccessorUtils.get(_that, fieldOutline));
				}
		}
		return equals;
	}
}
