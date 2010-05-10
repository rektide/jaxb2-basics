package org.jvnet.jaxb2_commons.plugin.mergeable;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.util.ClassUtils;
import org.jvnet.jaxb2_commons.lang.JAXBMergeStrategy;
import org.jvnet.jaxb2_commons.lang.MergeFrom;
import org.jvnet.jaxb2_commons.lang.MergeStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.plugin.util.StrategyClassUtils;
import org.jvnet.jaxb2_commons.util.FieldAccessorFactory;
import org.jvnet.jaxb2_commons.xjc.outline.FieldAccessorEx;
import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class MergeablePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "Xmergeable";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}

	private Class<?> mergeStrategyClass = JAXBMergeStrategy.class;

	public void setMergeStrategyClass(final Class<?> mergeStrategyClass) {
		this.mergeStrategyClass = mergeStrategyClass;
	}

	public Class<?> getMergeStrategyClass() {
		return mergeStrategyClass;
	}

	public JExpression createMergeStrategy(JCodeModel codeModel) {
		return StrategyClassUtils.createStrategyInstanceExpression(codeModel,
				getMergeStrategyClass());
	}

	private Ignoring ignoring = new CustomizedIgnoring(
			org.jvnet.jaxb2_commons.plugin.mergeable.Customizations.IGNORED_ELEMENT_NAME,
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
						org.jvnet.jaxb2_commons.plugin.mergeable.Customizations.IGNORED_ELEMENT_NAME,
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
		ClassUtils._implements(theClass, theClass.owner().ref(MergeFrom.class));

		@SuppressWarnings("unused")
		final JMethod mergeFrom$mergeFrom0 = generateMergeFrom$mergeFrom0(
				classOutline, theClass);
		@SuppressWarnings("unused")
		final JMethod mergeFrom$mergeFrom = generateMergeFrom$mergeFrom(
				classOutline, theClass);
	}

	protected JMethod generateMergeFrom$mergeFrom0(
			final ClassOutline classOutline, final JDefinedClass theClass) {

		JCodeModel codeModel = theClass.owner();
		final JMethod mergeFrom$mergeFrom = theClass.method(JMod.PUBLIC,
				codeModel.VOID, "mergeFrom");
		{
			final JVar that = mergeFrom$mergeFrom.param(Object.class, "that");
			final JBlock body = mergeFrom$mergeFrom.body();

			final JVar mergeStrategy = body.decl(JMod.FINAL, codeModel
					.ref(MergeStrategy.class), "strategy",
					createMergeStrategy(codeModel));

			body.invoke("mergeFrom").arg(JExpr._null()).arg(JExpr._null()).arg(
					that).arg(mergeStrategy);
		}
		return mergeFrom$mergeFrom;
	}

	protected JMethod generateMergeFrom$mergeFrom(ClassOutline classOutline,
			final JDefinedClass theClass) {
		final JCodeModel codeModel = theClass.owner();

		final JMethod mergeFrom = theClass.method(JMod.PUBLIC, codeModel.VOID,
				"mergeFrom");
		{
			final JVar leftLocator = mergeFrom.param(ObjectLocator.class,
					"leftLocator");
			final JVar rightLocator = mergeFrom.param(ObjectLocator.class,
					"rightLocator");
			final JVar that = mergeFrom.param(Object.class, "that");

			final JVar mergeStrategy = mergeFrom.param(MergeStrategy.class,
					"strategy");

			final JBlock methodBody = mergeFrom.body();

			Boolean superClassImplementsMergeFrom = StrategyClassUtils
					.superClassImplements(classOutline, getIgnoring(),
							MergeFrom.class);

			if (superClassImplementsMergeFrom == null) {

			} else if (superClassImplementsMergeFrom.booleanValue()) {
				methodBody.invoke(JExpr._super(), "mergeFrom").arg(leftLocator)
						.arg(rightLocator).arg(that).arg(mergeStrategy);
			} else {

			}

			final JBlock body = methodBody._if(that._instanceof(theClass))
					._then();

			JVar target = body.decl(JMod.FINAL, theClass, "target", JExpr
					._this());
			JVar left = body.decl(JMod.FINAL, theClass, "left", JExpr._this());
			JVar right = body.decl(JMod.FINAL, theClass, "right", JExpr.cast(
					theClass, that));
			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					final FieldAccessorEx leftFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, left);
					final FieldAccessorEx rightFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, right);
					if (leftFieldAccessor.isConstant()
							|| rightFieldAccessor.isConstant()) {
						continue;
					}
					final JBlock block = body.block();
					final JVar leftField = block.decl(leftFieldAccessor
							.getType(), "left"
							+ fieldOutline.getPropertyInfo().getName(true));
					leftFieldAccessor.toRawValue(block, leftField);
					final JVar rightField = block.decl(rightFieldAccessor
							.getType(), "right"
							+ fieldOutline.getPropertyInfo().getName(true));

					rightFieldAccessor.toRawValue(block, rightField);

					final JExpression leftFieldLocator = codeModel.ref(
							LocatorUtils.class).staticInvoke("field").arg(
							leftLocator).arg(
							fieldOutline.getPropertyInfo().getName(false)).arg(
							leftField);
					final JExpression rightFieldLocator = codeModel.ref(
							LocatorUtils.class).staticInvoke("field").arg(
							rightLocator).arg(
							fieldOutline.getPropertyInfo().getName(false)).arg(
							rightField);

					final FieldAccessorEx targetFieldAccessor = FieldAccessorFactory
							.createFieldAccessor(fieldOutline, target);
					targetFieldAccessor.fromRawValue(block, "unique"
							+ fieldOutline.getPropertyInfo().getName(true),

					JExpr.cast(targetFieldAccessor.getType(),

					mergeStrategy.invoke("merge").arg(leftFieldLocator).arg(
							rightFieldLocator).arg(leftField).arg(rightField)));
				}

		}
		return mergeFrom;
	}
}
