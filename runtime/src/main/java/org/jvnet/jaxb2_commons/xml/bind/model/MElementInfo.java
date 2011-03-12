package org.jvnet.jaxb2_commons.xml.bind.model;

import javax.xml.namespace.QName;

import org.jvnet.jaxb2_commons.xml.bind.model.origin.MElementInfoOrigin;
import org.jvnet.jaxb2_commons.xml.bind.model.origin.MOriginated;

public interface MElementInfo<T, C> extends MElementTypeInfo<T, C>, MPackaged,
		MOriginated<MElementInfoOrigin> {

	public QName getElementName();

	public MTypeInfo<T, C> getScope();

	public QName getSubstitutionHead();

}
