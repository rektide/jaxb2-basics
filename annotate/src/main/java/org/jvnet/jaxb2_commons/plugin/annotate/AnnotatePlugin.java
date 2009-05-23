package org.jvnet.jaxb2_commons.plugin.annotate;

import java.util.Collections;
import java.util.List;

import org.jvnet.annox.Constants;
import org.jvnet.annox.model.XAnnotation;
import org.jvnet.annox.parser.XAnnotationParser;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.util.CustomizationUtils;
import org.jvnet.jaxb2_commons.util.FieldAccessorUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CCustomizations;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class AnnotatePlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "Xannotate";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}

	private String defaultFieldTarget = "getter";

	public String getDefaultFieldTarget() {
		return defaultFieldTarget;
	}

	public void setDefaultFieldTarget(String defaultFieldTarget) {
		if ("getter".equals(defaultFieldTarget)
				|| "setter".equals(defaultFieldTarget)
				|| "field".equals(defaultFieldTarget)) {
			this.defaultFieldTarget = defaultFieldTarget;
		} else {
			throw new IllegalArgumentException("Invalid default field target.");
		}
	}

	private XAnnotationParser annotationParser = XAnnotationParser.GENERIC;

	public XAnnotationParser getAnnotationParser() {
		return annotationParser;
	}

	public void setAnnotationParser(XAnnotationParser annotationParser) {
		this.annotationParser = annotationParser;
	}

	private Annotator annotator = new Annotator();

	public Annotator getAnnotator() {
		return annotator;
	}

	public void setAnnotator(Annotator annotator) {
		this.annotator = annotator;
	}

	@Override
	public boolean run(Outline outline, Options options,
			ErrorHandler errorHandler) {
		for (final ClassOutline classOutline : outline.getClasses()) {

			processClassOutline(classOutline, options, errorHandler);
		}
		return true;
	}

	protected void processClassOutline(ClassOutline classOutline,
			Options options, ErrorHandler errorHandler) {

		final CCustomizations customizations = CustomizationUtils
				.getCustomizations(classOutline);

		annotateClassOutline(classOutline.ref.owner(), classOutline,
				customizations, errorHandler);

		for (final FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
			processFieldOutline(classOutline, fieldOutline, options,
					errorHandler);
		}

	}

	private void processFieldOutline(ClassOutline classOutline,
			FieldOutline fieldOutline, Options options,
			ErrorHandler errorHandler) {

		final CCustomizations customizations = CustomizationUtils
				.getCustomizations(fieldOutline);
		annotate(fieldOutline.parent().ref.owner(), fieldOutline,
				customizations, errorHandler);
	}

	protected void annotateClassOutline(final JCodeModel codeModel,
			final ClassOutline classOutline,
			final CCustomizations customizations, ErrorHandler errorHandler) {
		for (final CPluginCustomization customization : customizations) {
			final Element element = customization.element;
			final String namespaceURI = element.getNamespaceURI();
			if (Constants.NAMESPACE_URI.equals(namespaceURI)) {
				customization.markAsAcknowledged();

				final JAnnotatable annotatable = classOutline.ref;

				annotate(codeModel, errorHandler, customization, element,
						annotatable);
			}
		}
	}

	protected void annotate(final JCodeModel codeModel,
			final FieldOutline fieldOutline,
			final CCustomizations customizations, ErrorHandler errorHandler) {
		for (final CPluginCustomization customization : customizations) {
			final Element element = customization.element;
			final String namespaceURI = element.getNamespaceURI();
			if (Constants.NAMESPACE_URI.equals(namespaceURI)) {
				customization.markAsAcknowledged();

				final JAnnotatable annotatable;

				final String draftTarget = element.getAttribute("target");

				final String target;

				if (draftTarget == null || "".equals(draftTarget)) {
					target = getDefaultFieldTarget();
				} else {
					target = draftTarget;
				}

				if ("class".equals(target)) {
					annotatable = fieldOutline.parent().ref;
				} else if ("getter".equals(target)) {
					annotatable = FieldAccessorUtils.getter(fieldOutline);
				} else if ("setter".equals(target)) {
					annotatable = FieldAccessorUtils.setter(fieldOutline);

				} else if ("field".equals(target)) {
					annotatable = FieldAccessorUtils.field(fieldOutline);

				} else {
					logger.error("Invalid annotation target [" + target + "].");
					annotatable = null;
				}

				if (annotatable != null) {

					annotate(codeModel, errorHandler, customization, element,
							annotatable);
				}
			}
		}
	}

	private void annotate(final JCodeModel codeModel,
			ErrorHandler errorHandler,
			final CPluginCustomization customization, final Element element,
			final JAnnotatable annotatable) {
		final NodeList elements = element.getChildNodes();
		for (int index = 0; index < elements.getLength(); index++) {
			final Node node = elements.item(index);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final Element child = (Element) node;

				try {
					final XAnnotation annotation = getAnnotationParser().parse(
							child);
					getAnnotator().annotate(codeModel, annotatable, annotation);
				} catch (Exception ex) {
					try {
						errorHandler.error(new SAXParseException(
								"Error parsing annotation.",
								customization.locator, ex));
					} catch (SAXException ignored) {
						// Nothing to do
					}
				}
			}
		}
	}

	@Override
	public List<String> getCustomizationURIs() {
		return Collections.singletonList(Constants.NAMESPACE_URI);
	}

	@Override
	public boolean isCustomizationTagName(String namespaceURI, String localName) {
		return Constants.NAMESPACE_URI.equals(namespaceURI)
				&& "annotate".equals(localName);
	}

}
