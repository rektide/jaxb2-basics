package org.jvnet.jaxb2_commons.tests.one;

import java.io.File;

import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.test.AbstractSamplesTest;

public class EqualsTest extends AbstractSamplesTest {

  @Override
  protected void checkSample(File sample) throws Exception {

    final Object lhs = createContext().createUnmarshaller().unmarshal(sample);
    final Object rhs = createContext().createUnmarshaller().unmarshal(sample);
    final JAXBEqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
    assertTrue("Values must be equal.", equalsBuilder.append(lhs, rhs).isEquals());
  }
}