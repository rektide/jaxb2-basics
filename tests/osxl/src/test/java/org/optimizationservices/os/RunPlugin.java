package org.optimizationservices.os;

import java.io.File;

import org.jvnet.jaxb2.maven2.XJC2Mojo;
import org.jvnet.jaxb2.maven2.test.RunXJC2Mojo;

public class RunPlugin extends RunXJC2Mojo {

	@Override
	public File getSchemaDirectory() {
		return new File(getBaseDir(), "src/main/resources");
	}
	
	@Override
	protected void configureMojo(XJC2Mojo mojo) {
		mojo.setExtension(true);
		mojo.setSchemaIncludes(new String[]{"OSiL.xsd"});
		super.configureMojo(mojo);
		mojo.setForceRegenerate(true);
	}
}
