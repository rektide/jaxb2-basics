package org.jvnet.jaxb2_commons.plugin;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.Outline;

public abstract class AbstractPlugin extends Plugin {

	@Override
	public void onActivated(Options options) throws BadCommandLineException {
		super.onActivated(options);
		try {
			init(options);
		} catch (Exception ex) {
			throw new BadCommandLineException(
					"Could not initialize the plugin [" + getOptionName()
							+ "].", ex);
		}
	}

	@Override
	public boolean run(Outline outline, Options options,
			ErrorHandler errorHandler) throws SAXException {
/*		try {
			init(options);
		} catch (Exception ex) {
			SAXParseException saxex = new SAXParseException(
					"Could not initialize Spring context.", null, ex);
			errorHandler.fatalError(saxex);
			throw saxex;
		}*/
		try {
			return run(outline, options);
		} catch (Exception ex) {
			final SAXParseException saxex = new SAXParseException(
					"Error during plugin execution.", null, ex);
			errorHandler.error(saxex);
			throw saxex;
		}
	}

	protected boolean run(Outline outline, Options options) throws Exception {
		return true;
	}

	protected void init(Options options) throws Exception {
	}

}
