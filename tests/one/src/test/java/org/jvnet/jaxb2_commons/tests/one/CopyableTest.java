package org.jvnet.jaxb2_commons.tests.one;

import java.io.File;

import junit.framework.Assert;

import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.test.AbstractSamplesTest;

public class CopyableTest extends AbstractSamplesTest {

	@Override
	protected void checkSample(File sample) throws Exception {

		final Object object = createContext().createUnmarshaller().unmarshal(
				sample);
		final JAXBCopyBuilder builder = new JAXBCopyBuilder();
		final Object copy = builder.copy(object);
		final JAXBEqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
		Assert.assertTrue("Source and copy must be equal.", equalsBuilder
				.append(object, copy).isEquals());
	}

}
