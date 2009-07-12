package org.jvnet.jaxb2_commons.plugin.mergeable;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.lang.MergeFrom;
import org.jvnet.jaxb2_commons.lang.Mergeable;
import org.jvnet.jaxb2_commons.lang.builder.MergeBuilder;
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
import com.sun.codemodel.JOp;
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

	private Class mergeBuilderClass = MergeBuilder.class;

	public void setMergeBuilderClass(final Class mergeBuilderClass) {
		this.mergeBuilderClass = mergeBuilderClass;
	}

	public Class getMergeBuilderClass() {
		return mergeBuilderClass;
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
		final JMethod mergeFrom$mergeFrom = generateMergeFrom$MergeFrom(
				classOutline, theClass);
		@SuppressWarnings("unused")
		final JMethod mergeable$mergeFrom = generateMergeable$MergeFrom(
				classOutline, theClass);
	}

	protected JMethod generateMergeable$MergeFrom(
			final ClassOutline classOutline, final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(Mergeable.class));

		final JMethod mergeable$mergeFrom = theClass.method(JMod.PUBLIC,
				theClass.owner().ref(Object.class), "mergeFrom");
		{
			final JVar left = mergeable$mergeFrom.param(Object.class, "left");
			final JVar right = mergeable$mergeFrom.param(Object.class, "right");
			final JBlock body = mergeable$mergeFrom.body();

			final JVar mergeBuilder = body.decl(JMod.FINAL, theClass.owner()
					.ref(MergeBuilder.class), "mergeBuilder", JExpr
					._new(theClass.owner().ref(getMergeBuilderClass())));

			body._return(JExpr.invoke("mergeFrom").arg(left).arg(right).arg(
					mergeBuilder));
		}
		return mergeable$mergeFrom;
	}

	protected JMethod generateMergeFrom$MergeFrom(ClassOutline classOutline,
			final JDefinedClass theClass) {
		ClassUtils._implements(theClass, theClass.owner().ref(MergeFrom.class));

		final JMethod mergeFrom = theClass.method(JMod.PUBLIC, theClass.owner()
				.ref(Object.class), "mergeFrom");
		{
			final JVar leftObject = mergeFrom.param(Object.class, "leftObject");
			final JVar rightObject = mergeFrom.param(Object.class,
					"rightObject");

			final JVar mergeBuilder = mergeFrom.param(MergeBuilder.class,
					"mergeBuilder");

			final JBlock methodBody = mergeFrom.body();

			final JBlock body = methodBody._if(
					JOp.cand(leftObject._instanceof(theClass), rightObject
							._instanceof(theClass)))._then();

			if (classOutline.target.getBaseClass() != null
					|| classOutline.target.getRefBaseClass() != null) {
				body.invoke(JExpr._super(), "mergeFrom").arg(leftObject).arg(
						rightObject).arg(mergeBuilder);
			}

			JVar target = body.decl(JMod.FINAL, theClass, "target", JExpr
					._this());
			JVar left = body.decl(JMod.FINAL, theClass, "left", JExpr.cast(
					theClass, leftObject));
			JVar right = body.decl(JMod.FINAL, theClass, "right", JExpr.cast(
					theClass, rightObject));

			// if (!classOutline.target.isAbstract()) {
			// merge = body.decl(JMod.FINAL, theClass, "merge",
			//
			// JOp.cond(JOp.eq(target, JExpr._null()), JExpr._new(theClass),
			// JExpr.cast(theClass, target)));
			// } else {
			// body
			// ._if(JExpr._null().eq(target))
			// ._then()
			// ._throw(
			// JExpr
			// ._new(
			// theClass
			// .owner()
			// .ref(
			// IllegalArgumentException.class))
			// .arg(
			// "Target argument must not be null for abstract mergeable
			// classes."));
			// merge = body.decl(JMod.FINAL, theClass, "merge", JExpr.cast(
			// theClass, target));
			// }
			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					final JBlock block = body.block();
					final JVar leftField = block.decl(
							fieldOutline.getRawType(), "left"
									+ fieldOutline.getPropertyInfo().getName(
											true));
					FieldAccessorFactory
							.createFieldAccessor(fieldOutline, left)
							.toRawValue(block, leftField);
					final JVar rightField = block.decl(fieldOutline
							.getRawType(), "right"
							+ fieldOutline.getPropertyInfo().getName(true));

					FieldAccessorFactory.createFieldAccessor(fieldOutline,
							right).toRawValue(block, rightField);

					FieldAccessorFactory.createFieldAccessor(fieldOutline,
							target).fromRawValue(
							block,
							"unique"
									+ fieldOutline.getPropertyInfo().getName(
											true),

							mergeBuilder.invoke("merge").arg(JExpr._this())
									.arg(target).arg(
											fieldOutline.getPropertyInfo()
													.getName(true)).arg(
											leftField).arg(rightField));
				}

			methodBody._return(JExpr._this());
		}
		return mergeFrom;
	}
}
