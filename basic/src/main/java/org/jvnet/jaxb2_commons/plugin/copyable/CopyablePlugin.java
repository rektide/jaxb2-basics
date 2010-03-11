package org.jvnet.jaxb2_commons.plugin.copyable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.util.ClassUtils;
import org.jvnet.jaxb2_commons.util.FieldAccessorFactory;
import org.jvnet.jaxb2_commons.xjc.outline.FieldAccessorEx;
import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class CopyablePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "Xcopyable";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}

	private Class<? extends CopyBuilder> copyBuilderClass = JAXBCopyBuilder.class;

	public void setCopyBuilderClass(
			final Class<? extends CopyBuilder> copyBuilderClass) {
		this.copyBuilderClass = copyBuilderClass;
	}

	public Class<? extends CopyBuilder> getCopyBuilderClass() {
		return copyBuilderClass;
	}

	public JExpression createCopyBuilder(JCodeModel codeModel) {
		final Class<? extends CopyBuilder> copyBuilderClass = getCopyBuilderClass();
		final JClass copyBuilderJClass = codeModel.ref(copyBuilderClass);
		try {
			Method getInstanceMethod = copyBuilderClass.getMethod(
					"getInstance", new Class<?>[0]);
			if (getInstanceMethod != null
					&& CopyBuilder.class.isAssignableFrom(getInstanceMethod
							.getReturnType())
					&& Modifier.isStatic(getInstanceMethod.getModifiers())
					&& Modifier.isPublic(getInstanceMethod.getModifiers())) {
				return copyBuilderJClass.staticInvoke("getInstance");
			}

		} catch (Exception ignored) {
			// Nothing to do
		}
		try {
			final Field instanceField = copyBuilderClass.getField("INSTANCE");
			if (instanceField != null
					&& CopyBuilder.class.isAssignableFrom(instanceField
							.getType())
					&& Modifier.isStatic(instanceField.getModifiers())
					&& Modifier.isPublic(instanceField.getModifiers())) {
				return copyBuilderJClass.staticRef("INSTANCE");
			}
		} catch (Exception ignored) {
			// Nothing to do
		}
		return JExpr._new(copyBuilderJClass);
	}

	private Ignoring ignoring = new CustomizedIgnoring(
			org.jvnet.jaxb2_commons.plugin.copyable.Customizations.IGNORED_ELEMENT_NAME,
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
						org.jvnet.jaxb2_commons.plugin.copyable.Customizations.IGNORED_ELEMENT_NAME,
						Customizations.IGNORED_ELEMENT_NAME,
						Customizations.GENERATED_ELEMENT_NAME);
	}

	@Override
	public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
		for (final ClassOutline classOutline : outline.getClasses())
			if (!getIgnoring().isIgnored(classOutline)) {

				processClassOutline(classOutline);
			}
		return true;
	}

	protected void processClassOutline(ClassOutline classOutline) {
		final JDefinedClass theClass = classOutline.implClass;

		@SuppressWarnings("unused")
		final JMethod copyTo$copyTo = generateCopyTo$CopyTo(classOutline,
				theClass);
		@SuppressWarnings("unused")
		final JMethod copyable$copyTo = generateCopyable$CopyTo(classOutline,
				theClass);

		// @SuppressWarnings("unused")
		// final JMethod copyFrom$copyFrom = generateCopyFrom$CopyFrom(
		// classOutline, theClass);
		// @SuppressWarnings("unused")
		// final JMethod copyable$copyFrom = generateCopyable$CopyFrom(
		// classOutline, theClass);
		if (!classOutline.target.isAbstract()) {
			@SuppressWarnings("unused")
			final JMethod createCopy = generate$createNewInstance(classOutline,
					theClass);
		}
	}

	protected JMethod generate$createNewInstance(
			final ClassOutline classOutline, final JDefinedClass theClass) {

		final JMethod createCopy = theClass.method(JMod.PUBLIC, theClass
				.owner().ref(Object.class), "createNewInstance");
		{
			final JBlock body = createCopy.body();
			body._return(JExpr._new(theClass));
		}
		return createCopy;
	}

	protected JMethod generateCopyable$CopyTo(final ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(Copyable.class));

		final JMethod copyable$copyTo = theClass.method(JMod.PUBLIC, theClass
				.owner().ref(Object.class), "copyTo");
		{
			final JVar target = copyable$copyTo.param(Object.class, "target");
			final JBlock body = copyable$copyTo.body();

			final JVar copyBuilder = body.decl(JMod.FINAL, theClass.owner()
					.ref(CopyBuilder.class), "copyBuilder",
					createCopyBuilder(theClass.owner()));

			body._return(JExpr.invoke("copyTo").arg(target).arg(copyBuilder));
		}
		return copyable$copyTo;
	}

	protected JMethod generateCopyTo$CopyTo(ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(CopyTo.class));

		final JMethod copyTo = theClass.method(JMod.PUBLIC, theClass.owner()
				.ref(Object.class), "copyTo");
		{
			final JVar target = copyTo.param(Object.class, "target");
			final JVar copyBuilder = copyTo.param(CopyBuilder.class,
					"copyBuilder");

			final JBlock body = copyTo.body();

			final JVar draftCopy;
			if (!classOutline.target.isAbstract()) {
				draftCopy = body.decl(JMod.FINAL, theClass.owner().ref(
						Object.class), "draftCopy",

				JOp.cond(JOp.eq(target, JExpr._null()), JExpr
						.invoke("createNewInstance"), target));
			} else {
				body
						._if(JExpr._null().eq(target))
						._then()
						._throw(
								JExpr
										._new(
												theClass
														.owner()
														.ref(
																IllegalArgumentException.class))
										.arg(
												"Target argument must not be null for abstract copyable classes."));
				draftCopy = target;
			}

			if (classOutline.target.getBaseClass() != null
					|| classOutline.target.getRefBaseClass() != null) {
				body.invoke(JExpr._super(), "copyTo").arg(draftCopy).arg(
						copyBuilder);
			}

			final JBlock bl = body._if(draftCopy._instanceof(theClass))._then();

			final JVar copy = bl.decl(JMod.FINAL, theClass, "copy", JExpr.cast(
					theClass, draftCopy));

			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					final JBlock block = bl.block();

					final FieldAccessorEx sourceFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, JExpr._this());
					final FieldAccessorEx copyFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, copy);

					final JBlock setValueBlock;
					final JBlock unsetValueBlock;

					final JExpression valueIsSet = sourceFieldAccessor
							.hasSetValue();

					if (valueIsSet != null) {
						final JConditional ifValueIsSet = block._if(valueIsSet);
						setValueBlock = ifValueIsSet._then();
						unsetValueBlock = ifValueIsSet._else();
					} else {
						setValueBlock = block;
						unsetValueBlock = null;
					}

					if (setValueBlock != null) {

						final JType copyFieldType = sourceFieldAccessor
								.getType();
						final JVar sourceField = setValueBlock.decl(
								copyFieldType, "source"
										+ fieldOutline.getPropertyInfo()
												.getName(true));
						sourceFieldAccessor.toRawValue(setValueBlock,
								sourceField);
						final JExpression builtCopy = JExpr.invoke(copyBuilder,
								"copy").arg(sourceField);
						final JVar copyField = setValueBlock.decl(
								copyFieldType, "copy"
										+ fieldOutline.getPropertyInfo()
												.getName(true), copyFieldType
										.isPrimitive() ? builtCopy :

								JExpr.cast(copyFieldType, builtCopy));
						if (copyFieldType instanceof JClass
								&& ((JClass) copyFieldType).isParameterized()) {
							copyField.annotate(SuppressWarnings.class).param(
									"value", "unchecked");
						}
						copyFieldAccessor.fromRawValue(setValueBlock, "unique"
								+ fieldOutline.getPropertyInfo().getName(true),
								copyField);
					}
					if (unsetValueBlock != null) {
						copyFieldAccessor.unsetValues(unsetValueBlock);

					}
				}

			body._return(draftCopy);
		}
		return copyTo;
	}

	protected JMethod generateCopyable$CopyFrom(
			final ClassOutline classOutline, final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(Copyable.class));

		final JMethod copyable$copyTo = theClass.method(JMod.PUBLIC, theClass
				.owner().ref(Object.class), "copyFrom");
		{
			final JVar source = copyable$copyTo.param(Object.class, "source");
			final JBlock body = copyable$copyTo.body();

			final JVar copyBuilder = body.decl(JMod.FINAL, theClass.owner()
					.ref(CopyBuilder.class), "copyBuilder",
					createCopyBuilder(theClass.owner()));

			body._return(body.invoke("copyFrom").arg(source).arg(copyBuilder));
		}
		return copyable$copyTo;
	}

	// protected JMethod generateCopyFrom$CopyFrom(ClassOutline classOutline,
	// final JDefinedClass theClass) {
	// ClassUtils._implements(theClass, theClass.owner().ref(CopyFrom.class));
	//
	// final JMethod copyTo = theClass.method(JMod.PUBLIC, theClass.owner()
	// .ref(Object.class), "copyFrom");
	// {
	// final JVar source = copyTo.param(Object.class, "source");
	// final JVar copyBuilder = copyTo.param(CopyBuilder.class,
	// "copyBuilder");
	//
	// final JBlock body = copyTo.body();
	//
	// final JVar copy;
	// if (!classOutline.target.isAbstract()) {
	// copy = body.decl(JMod.FINAL, theClass, "copy",
	//
	// JOp.cond(JOp.eq(source, JExpr._null()), JExpr.cast(theClass,
	// JExpr.invoke("createCopy")), JExpr.cast(theClass,
	// source)));
	// } else {
	// body
	// ._if(JExpr._null().eq(source))
	// ._then()
	// ._throw(
	// JExpr
	// ._new(
	// theClass
	// .owner()
	// .ref(
	// IllegalArgumentException.class))
	// .arg(
	// "Target argument must not be null for abstract copyable classes."));
	// copy = body.decl(JMod.FINAL, theClass, "copy", JExpr.cast(
	// theClass, source));
	// }
	//
	// if (classOutline.target.getBaseClass() != null
	// || classOutline.target.getRefBaseClass() != null) {
	// body.invoke(JExpr._super(), "copyFrom").arg(copy).arg(
	// copyBuilder);
	// }
	//
	// for (final FieldOutline fieldOutline : classOutline
	// .getDeclaredFields())
	// if (!getIgnoring().isIgnored(fieldOutline)) {
	// final JBlock block = body.block();
	//
	// final FieldAccessorEx sourceFieldAccessor = FieldAccessorFactory
	// .createFieldAccessor(fieldOutline, JExpr._this());
	// final FieldAccessorEx copyFieldAccessor = FieldAccessorFactory
	// .createFieldAccessor(fieldOutline, copy);
	//
	// final JBlock setValueBlock;
	// final JBlock unsetValueBlock;
	//
	// final JExpression valueIsSet = copyFieldAccessor
	// .hasSetValue();
	//
	// if (valueIsSet != null) {
	// final JConditional ifValueIsSet = block._if(valueIsSet);
	// setValueBlock = ifValueIsSet._then();
	// unsetValueBlock = ifValueIsSet._else();
	// } else {
	// setValueBlock = block;
	// unsetValueBlock = null;
	// }
	//
	// if (setValueBlock != null) {
	//
	// final JType sourceFieldType = copyFieldAccessor
	// .getType();
	// final JVar sourceField = setValueBlock.decl(
	// sourceFieldType, "target"
	// + fieldOutline.getPropertyInfo()
	// .getName(true));
	// copyFieldAccessor
	// .toRawValue(setValueBlock, sourceField);
	// final JExpression builtCopy = JExpr.invoke(copyBuilder,
	// "copy").arg(sourceField);
	// final JVar copyField = setValueBlock.decl(
	// sourceFieldType, "copy"
	// + fieldOutline.getPropertyInfo()
	// .getName(true), sourceFieldType
	// .isPrimitive() ? builtCopy :
	//
	// JExpr.cast(sourceFieldType, builtCopy));
	// if (sourceFieldType instanceof JClass
	// && ((JClass) sourceFieldType).isParameterized()) {
	// copyField.annotate(SuppressWarnings.class).param(
	// "value", "unchecked");
	// }
	// sourceFieldAccessor.fromRawValue(setValueBlock,
	// "unique"
	// + fieldOutline.getPropertyInfo()
	// .getName(true), copyField);
	// }
	// if (unsetValueBlock != null) {
	// sourceFieldAccessor.unsetValues(unsetValueBlock);
	//
	// }
	// }
	//
	// body._return(JExpr._this());
	// }
	// return copyTo;
	// }

}
