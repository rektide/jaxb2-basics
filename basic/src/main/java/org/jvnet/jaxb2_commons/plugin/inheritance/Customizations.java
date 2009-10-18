package org.jvnet.jaxb2_commons.plugin.inheritance;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPluginCustomization;

public class Customizations {

	public static String NAMESPACE_URI = "http://jaxb2-commons.dev.java.net/basic/inheritance";

	public static QName EXTENDS_ELEMENT_NAME = new QName(NAMESPACE_URI,
			"extends");

	public static QName IMPLEMENTS_ELEMENT_NAME = new QName(NAMESPACE_URI,
			"implements");

	private static final JAXBContext context;
	static {
		try {
			context = JAXBContext.newInstance(

			ExtendsClass.class, ImplementsInterface.class);
		} catch (JAXBException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static JAXBContext getContext() {
		return context;
	}

	public static Object unmarshall(final JAXBContext context,
			final CPluginCustomization customization) throws AssertionError {
		if (customization == null) {
			return null;
		} else

		{
			final Unmarshaller unmarshaller;
			try {
				unmarshaller = context.createUnmarshaller();
			} catch (JAXBException ex) {
				final AssertionError error = new AssertionError(
						"Unmarshaller could not be created.");
				error.initCause(ex);
				throw error;
			}

			try {
				final Object result = unmarshaller.unmarshal(new DOMSource(
						customization.element));
				final JAXBIntrospector introspector = context
						.createJAXBIntrospector();
				if (introspector.isElement(result)) {
					return JAXBIntrospector.getValue(result);
				} else {
					return result;
				}
			} catch (JAXBException ex) {
				throw new IllegalArgumentException(
						"Could not unmarshal the customization.", ex);
			}

		}
	}

	public static CPluginCustomization marshal(final JAXBContext context,
			final Object object) {

		try {
			final Marshaller marshaller = context.createMarshaller();

			final DOMResult result = new DOMResult();

			marshaller.marshal(object, result);

			final Node node = result.getNode();

			final Element element;
			if (node instanceof Element)

			{
				element = (Element) node;
			} else if (node instanceof Document) {
				element = ((Document) node).getDocumentElement();
			} else {
				element = null;
				throw new IllegalArgumentException(
						"Could not marhsall object into an element.");
			}
			return new CPluginCustomization(element, null);
		} catch (JAXBException jaxbex) {
			throw new IllegalArgumentException(
					"Could not marhsall object into an element.", jaxbex);

		}
	}

	public static void _extends(CClassInfo classInfo, String className) {
		final ExtendsClass extendsClass = new ExtendsClass();
		extendsClass.setClassName(className);
		classInfo.getCustomizations().add(marshal(getContext(), extendsClass));

	}

	public static void _implements(CClassInfo classInfo,
			String... interfaceNames) {
		for (String interfaceName : interfaceNames) {
			final ImplementsInterface implementsInterface = new ImplementsInterface();
			implementsInterface.setInterfaceName(interfaceName);
			classInfo.getCustomizations().add(
					marshal(getContext(), implementsInterface));
		}

	}

}
