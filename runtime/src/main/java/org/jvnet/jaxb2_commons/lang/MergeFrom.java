package org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.lang.builder.MergeBuilder;

public interface MergeFrom {

	public Object mergeFrom(Object left, Object right, MergeBuilder mergeBuilder);

}
