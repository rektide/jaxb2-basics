package org.jvnet.jaxb2_commons.lang.builder;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.custommonkey.xmlunit.Diff;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.w3c.dom.Node;

public class ExtendedJAXBEqualsBuilder extends JAXBEqualsBuilder {

  @Override
  public EqualsBuilder append(Object lhs, Object rhs) {

    if (!isEquals()) {
      return this;
    }
    if (lhs == rhs) {
      return this;
    }
    if (lhs == null || rhs == null) {
      this.setEquals(false);
      return this;
    }
    final Class lhsClass = lhs.getClass();
    if (lhsClass.isArray()) {
      super.append(lhs, rhs);
    }
    else {
      if (lhs instanceof Node && rhs instanceof Node) {
        final Diff diff = new Diff(new DOMSource((Node) lhs), new DOMSource((Node) rhs));
        this.setEquals(diff.identical());
      }
      else if (lhs instanceof XMLGregorianCalendar && rhs instanceof XMLGregorianCalendar) {
        append(
            ((XMLGregorianCalendar) lhs).normalize().toGregorianCalendar().getTimeInMillis(),
            ((XMLGregorianCalendar) rhs).normalize().toGregorianCalendar().getTimeInMillis());

      }
      else {
        super.append(lhs, rhs);
      }
    }
    return this;
  }

}
