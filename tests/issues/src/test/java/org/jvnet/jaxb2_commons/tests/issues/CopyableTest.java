package org.jvnet.jaxb2_commons.tests.issues;

import java.io.File;

import junit.framework.Assert;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.ExtendedJAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.test.AbstractSamplesTest;

public class CopyableTest extends AbstractSamplesTest {

	@Override
	protected void checkSample(File sample) throws Exception {

		final Object object = createContext().createUnmarshaller().unmarshal(
				sample);
		final CopyBuilder builder = new JAXBCopyBuilder();
		final Object copy = builder.copy(object);
		final EqualsBuilder equalsBuilder = new ExtendedJAXBEqualsBuilder();
		Assert.assertTrue("Source and copy must be equal.", equalsBuilder
				.append(object, copy).isEquals());
	}

}
