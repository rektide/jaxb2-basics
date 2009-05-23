package org.jvnet.jaxb2_commons.plugin.copyable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

public class CopyablePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "Xcopyable";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}
	
	@Override
	public List<String> getCustomizationURIs() {
		return Arrays.asList(Customizations.NAMESPACE_URI,
				org.jvnet.jaxb2_commons.plugin.copyable.Customizations.NAMESPACE_URI);
	}

	private Class copyBuilderClass = JAXBCopyBuilder.class;

	public void setCopyBuilderClass(final Class copyBuilderClass) {
		this.copyBuilderClass = copyBuilderClass;
	}

	public Class getCopyBuilderClass() {
		return copyBuilderClass;
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
		final JMethod copyTo$copyTo = generateCopyTo$CopyTo(classOutline,
				theClass);
		@SuppressWarnings("unused")
		final JMethod copyable$copyTo = generateCopyable$CopyTo(classOutline,
				theClass);

		if (!classOutline.target.isAbstract()) {
			@SuppressWarnings("unused")
			final JMethod createCopy = generate$createCopy(classOutline,
					theClass);
		}
	}

	protected JMethod generate$createCopy(final ClassOutline classOutline,
			final JDefinedClass theClass) {

		final JMethod createCopy = theClass.method(JMod.PUBLIC, theClass
				.owner().ref(Object.class), "createCopy");
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
					.ref(CopyBuilder.class), "copyBuilder", JExpr._new(theClass
					.owner().ref(getCopyBuilderClass())));

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

			final JVar copy;
			if (!classOutline.target.isAbstract()) {
				copy = body.decl(JMod.FINAL, theClass, "copy",

				JOp.cond(JOp.eq(target, JExpr._null()), JExpr.cast(theClass,
						JExpr.invoke("createCopy")), JExpr.cast(theClass,
						target)));
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
				copy = body.decl(JMod.FINAL, theClass, "copy", JExpr.cast(
						theClass, target));
			}

			if (classOutline.target.getBaseClass() != null
					|| classOutline.target.getRefBaseClass() != null) {
				body.invoke(JExpr._super(), "copyTo").arg(copy)
						.arg(copyBuilder);
			}

			for (final FieldOutline fieldOutline : classOutline
					.getDeclaredFields())
				if (!getIgnoring().isIgnored(fieldOutline)) {
					final JBlock block = body.block();
					final JVar sourceField = block.decl(fieldOutline
							.getRawType(), "source"
							+ fieldOutline.getPropertyInfo().getName(true));
					FieldAccessorFactory.createFieldAccessor(fieldOutline,
							JExpr._this()).toRawValue(block, sourceField);
					final JVar copyField = block.decl(
							fieldOutline.getRawType(), "copy"
									+ fieldOutline.getPropertyInfo().getName(
											true),

							JExpr.cast(fieldOutline.getRawType(), JExpr.invoke(
									copyBuilder, "copy").arg(sourceField)));
					FieldAccessorFactory
							.createFieldAccessor(fieldOutline, copy)
							.fromRawValue(
									block,
									"unique"
											+ fieldOutline.getPropertyInfo()
													.getName(true), copyField);
					// block.invoke(equalsBuilder, "append").arg(lhsValue).arg(
					// rhsValue);
				}

			body._return(copy);
		}
		return copyTo;
	}
}
