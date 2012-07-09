package org.jvnet.jaxb2_commons.plugin.enumerated;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.lang.Enumerated;
import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.jvnet.jaxb2_commons.plugin.Customizations;
import org.jvnet.jaxb2_commons.plugin.CustomizedIgnoring;
import org.jvnet.jaxb2_commons.plugin.Ignoring;
import org.jvnet.jaxb2_commons.util.ClassUtils;
import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CEnumLeafInfo;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.Outline;

public class EnumeratedPlugin extends AbstractParameterizablePlugin {

	@Override
	public String getOptionName() {
		return "Xenumerated";
	}

	@Override
	public String getUsage() {
		return "Forces generated @XmlEnums implement the org.jvnet.jaxb2_commons.lang.Enumerated<T> interface.";
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
				.asList(org.jvnet.jaxb2_commons.plugin.equals.Customizations.IGNORED_ELEMENT_NAME,
						Customizations.IGNORED_ELEMENT_NAME,
						Customizations.GENERATED_ELEMENT_NAME);
	}

	@Override
	public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
		for (final EnumOutline enumOutline : outline.getEnums()) {
			if (!getIgnoring().isIgnored(enumOutline)) {
				processEnumOutline(enumOutline);
			}
		}
		return true;
	}

	protected void processEnumOutline(EnumOutline enumOutline) {
		CEnumLeafInfo enumLeafInfo = enumOutline.target;
		JClass enumType = enumLeafInfo.base.toType(enumOutline.parent(),
				Aspect.EXPOSED).boxify();

		final JDefinedClass theClass = enumOutline.clazz;

		ClassUtils._implements(theClass, theClass.owner().ref(Enumerated.class)
				.narrow(enumType));

		final JMethod enumerated$enumValue = theClass.method(JMod.PUBLIC,
				enumType, "enumValue");
		enumerated$enumValue.body()._return(JExpr._this().invoke("value"));
	}
}
