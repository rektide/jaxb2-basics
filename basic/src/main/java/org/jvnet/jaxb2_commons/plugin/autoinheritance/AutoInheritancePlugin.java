package org.jvnet.jaxb2_commons.plugin.autoinheritance;

import java.util.LinkedList;
import java.util.List;

import org.jvnet.jaxb2_commons.plugin.AbstractParameterizablePlugin;
import org.xml.sax.ErrorHandler;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CElementInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.ElementOutline;
import com.sun.tools.xjc.outline.Outline;

public class AutoInheritancePlugin extends AbstractParameterizablePlugin {

	private String globalElementsExtend = null;
	private List<String> globalElementsImplement = new LinkedList<String>();

	private String globalComplexTypesExtend = null;
	private List<String> globalComplexTypesImplement = new LinkedList<String>();

	private List<String> globalJAXBElementsImplement = new LinkedList<String>();

	public String getGlobalElementsExtend() {
		return globalElementsExtend;
	}

	public void setGlobalElementsExtend(String globalElementsExtend) {
		this.globalElementsExtend = globalElementsExtend;
	}

	public String getGlobalElementsImplement() {
		return globalElementsImplement.toString();
	}

	public void setGlobalElementsImplement(String globalElementsImplement) {
		this.globalElementsImplement.add(globalElementsImplement);
	}

	public String getGlobalComplexTypesExtend() {
		return globalComplexTypesExtend;
	}

	public void setGlobalComplexTypesExtend(String globalComplexTypesExtend) {
		this.globalComplexTypesExtend = globalComplexTypesExtend;
	}

	public String getGlobalComplexTypesImplement() {
		return globalComplexTypesImplement.toString();
	}

	public void setGlobalComplexTypesImplement(
			String globalComplexTypesImplement) {
		this.globalComplexTypesImplement.add(globalComplexTypesImplement);
	}
	
	public String getGlobalJAXBElementsImplement() {
		return globalJAXBElementsImplement.toString();
	}

	public void setGlobalJAXBElementsImplement(
			String globalJAXBElementsImplement) {
		this.globalJAXBElementsImplement.add(globalJAXBElementsImplement);
	}
	

	@Override
	public String getOptionName() {
		return "XautoInheritance";
	}

	@Override
	public String getUsage() {
		return "TBD";
	}

	@Override
	public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
		for (final ClassOutline classOutline : outline.getClasses()) {
			if (classOutline.target.getElementName() != null) {
				processGlobalElement(classOutline);
			} else {
				processGlobalComplexType(classOutline);
			}
		}
		for (final CElementInfo elementInfo : outline.getModel()
				.getAllElements()) {
			final ElementOutline elementOutline = outline
					.getElement(elementInfo);
			if (elementOutline != null) {
				processGlobalJAXBElement(elementOutline);
			}
		}
		return true;
	}

	protected void processGlobalElement(ClassOutline classOutline) {

		generateExtends(classOutline.implClass, globalElementsExtend);
		generateImplements(classOutline.implClass, globalElementsImplement);

	}

	protected void processGlobalJAXBElement(ElementOutline elementOutline) {

		generateImplements(elementOutline.implClass, globalJAXBElementsImplement);

	}

	protected void processGlobalComplexType(ClassOutline classOutline) {

		generateExtends(classOutline.implClass, globalComplexTypesExtend);
		generateImplements(classOutline.implClass, globalComplexTypesImplement);

	}

	private void generateExtends(JDefinedClass theClass, String name) {
		if (name != null) {
			final JClass targetClass = theClass.owner().ref(name);
			if (theClass._extends() == theClass.owner().ref(Object.class)) {
				theClass._extends(targetClass);
			}
		}
	}

	private void generateImplements(JDefinedClass theClass, String name) {
		if (name != null) {
			final JClass targetClass = theClass.owner().ref(name);
			theClass._implements(targetClass);
		}
	}

	private void generateImplements(JDefinedClass theClass, List<String> names) {
		if (names != null && !names.isEmpty()) {
			for (String name : names) {
				generateImplements(theClass, name);
			}
		}
	}

}
