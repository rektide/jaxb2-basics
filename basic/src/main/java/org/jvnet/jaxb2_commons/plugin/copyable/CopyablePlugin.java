package org.jvnet.jaxb2_commons.plugin.copyable;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.plugin.util.StrategyClassUtils;
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

	private Class<? extends CopyStrategy> copyStrategy = JAXBCopyStrategy.class;

	public void setCopyStrategy(final Class<? extends CopyStrategy> copyStrategy) {
		this.copyStrategy = copyStrategy;
	}

	public Class<? extends CopyStrategy> getCopyStrategy() {
		return copyStrategy;
	}

	public JExpression createCopyStrategy(JCodeModel codeModel) {
		return StrategyClassUtils.createStrategyInstanceExpression(codeModel,
				getCopyStrategy());
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

		ClassUtils._implements(theClass, theClass.owner().ref(Cloneable.class));
		@SuppressWarnings("unused")
		final JMethod object$clone = generateObject$clone(classOutline,
				theClass);
		ClassUtils._implements(theClass, theClass.owner().ref(CopyTo.class));

		@SuppressWarnings("unused")
		final JMethod copyTo$copyTo = generateCopyTo$copyTo(classOutline,
				theClass);
		@SuppressWarnings("unused")
		final JMethod copyTo$copyTo1 = generateCopyTo$copyTo1(classOutline,
				theClass);
		// @SuppressWarnings("unused")
		// final JMethod copyable$copyTo = generateCopyable$CopyTo(classOutline,
		// theClass);
		// @SuppressWarnings("unused")
		// final JMethod copyable$copyTo1 =
		// generateCopyable$CopyTo1(classOutline,
		// theClass);

		// @SuppressWarnings("unused")
		// final JMethod copyFrom$copyFrom = generateCopyFrom$CopyFrom(
		// classOutline, theClass);
		// @SuppressWarnings("unused")
		// final JMethod copyable$copyFrom = generateCopyable$CopyFrom(
		// classOutline, theClass);
		if (!classOutline.target.isAbstract()) {
			@SuppressWarnings("unused")
			final JMethod createCopy = generateCopyTo$createNewInstance(
					classOutline, theClass);

		}
	}

	protected JMethod generateCopyTo$createNewInstance(
			final ClassOutline classOutline, final JDefinedClass theClass) {

		final JMethod createCopy = theClass.method(JMod.PUBLIC, theClass
				.owner().ref(Object.class), "createNewInstance");
		{
			final JBlock body = createCopy.body();
			body._return(JExpr._new(theClass));
		}
		return createCopy;
	}

	protected JMethod generateObject$clone(final ClassOutline classOutline,
			final JDefinedClass theClass) {

		final JMethod clone = theClass.method(JMod.PUBLIC, theClass.owner()
				.ref(Object.class), "clone");
		{
			final JBlock body = clone.body();
			body._return(JExpr.invoke("copyTo").arg(
					JExpr.invoke("createNewInstance")));
		}
		return clone;
	}

	// protected JMethod generateCopyable$copyTo(final ClassOutline
	// classOutline,
	// final JDefinedClass theClass) {
	//
	// final JMethod copyable$copyTo = theClass.method(JMod.PUBLIC, theClass
	// .owner().ref(Object.class), "copyTo");
	// {
	// final JVar target = copyable$copyTo.param(Object.class, "target");
	// final JBlock body = copyable$copyTo.body();
	//
	// body._return(JExpr.invoke("copyTo").arg(JExpr._null()).arg(target));
	// }
	// return copyable$copyTo;
	// }

	// protected JMethod generateCopyable$CopyTo1(final ClassOutline
	// classOutline,
	// final JDefinedClass theClass) {
	// ClassUtils._implements(theClass, theClass.owner().ref(Copyable.class));
	//
	// final JMethod copyable$copyTo = theClass.method(JMod.PUBLIC, theClass
	// .owner().ref(Object.class), "copyTo");
	// {
	// final JVar locator = copyable$copyTo.param(ObjectLocator.class,
	// "locator");
	// final JVar target = copyable$copyTo.param(Object.class, "target");
	// final JBlock body = copyable$copyTo.body();
	//
	// final JVar copyBuilder = body.decl(JMod.FINAL, theClass.owner()
	// .ref(CopyBuilder.class), "copyBuilder",
	// createCopyBuilder(theClass.owner()));
	//
	// body._return(JExpr.invoke("copyTo").arg(locator).arg(target).arg(
	// copyBuilder));
	// }
	// return copyable$copyTo;
	// }

	protected JMethod generateCopyTo$copyTo(final ClassOutline classOutline,
			final JDefinedClass theClass) {

		final JCodeModel codeModel = theClass.owner();
		final JMethod copyTo$copyTo = theClass.method(JMod.PUBLIC, codeModel
				.ref(Object.class), "copyTo");
		{
			final JVar target = copyTo$copyTo.param(Object.class, "target");

			final JBlock body = copyTo$copyTo.body();
			final JVar copyStrategy = body.decl(JMod.FINAL, codeModel
					.ref(CopyStrategy.class), "copyStrategy",
					createCopyStrategy(codeModel));

			body._return(JExpr.invoke("copyTo").arg(JExpr._null()).arg(target)
					.arg(copyStrategy));
		}
		return copyTo$copyTo;
	}

	protected JMethod generateCopyTo$copyTo1(ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(CopyTo.class));

		final JMethod copyTo = theClass.method(JMod.PUBLIC, theClass.owner()
				.ref(Object.class), "copyTo");
		{
			final JVar locator = copyTo.param(ObjectLocator.class, "locator");
			final JVar target = copyTo.param(Object.class, "target");
			final JVar copyStrategy = copyTo.param(CopyStrategy.class,
					"copyStrategy");

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

			Boolean superClassImplementsCopyTo = StrategyClassUtils
					.superClassImplements(classOutline, getIgnoring(),
							CopyTo.class);

			if (superClassImplementsCopyTo == null) {

			} else if (superClassImplementsCopyTo.booleanValue()) {
				body.invoke(JExpr._super(), "copyTo").arg(locator).arg(
						draftCopy).arg(copyStrategy);

			} else {

			}

			final JBlock bl = body._if(draftCopy._instanceof(theClass))._then();

			final JVar copy = bl.decl(JMod.FINAL, theClass, "copy", JExpr.cast(
					theClass, draftCopy));

			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {

					final FieldAccessorEx sourceFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, JExpr._this());
					final FieldAccessorEx copyFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, copy);

					if (sourceFieldAccessor.isConstant()) {
						continue;
					}

					final JBlock block = bl.block();

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
						final JExpression builtCopy = JExpr.invoke(
								copyStrategy, "copy").arg(
								theClass.owner().ref(LocatorUtils.class)
										.staticInvoke("field").arg(locator)
										.arg(
												fieldOutline.getPropertyInfo()
														.getName(false))).arg(
								sourceField);
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

	// protected JMethod generateCopyable$CopyFrom(
	// final ClassOutline classOutline, final JDefinedClass theClass) {
	// ClassUtils._implements(theClass, theClass.owner().ref(Copyable.class));
	//
	// final JMethod copyable$copyTo = theClass.method(JMod.PUBLIC, theClass
	// .owner().ref(Object.class), "copyFrom");
	// {
	// final JVar source = copyable$copyTo.param(Object.class, "source");
	// final JBlock body = copyable$copyTo.body();
	//
	// final JVar copyBuilder = body.decl(JMod.FINAL, theClass.owner()
	// .ref(CopyBuilder.class), "copyBuilder",
	// createCopyBuilder(theClass.owner()));
	//
	// body._return(body.invoke("copyFrom").arg(source).arg(copyBuilder));
	// }
	// return copyable$copyTo;
	// }

	// protected JMethod generateCopyFrom$CopyFrom(ClassOutline classOutline,
	// final JDefinedClass theClass) {
	// ClassUtils._implements(theClass, theClass.owner().ref(CopyFrom.class));
	//
	// final JMethod copyTo = theClass.method(JMod.PUBLIC, theClass.owner()
	// .ref(Object.class), "copyFrom");
	// {
	// final JVar source = copyTo.param(Object.class, "source");
	// final JVar copyBuilder = copyTo.param(CopyStrategy.class,
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
