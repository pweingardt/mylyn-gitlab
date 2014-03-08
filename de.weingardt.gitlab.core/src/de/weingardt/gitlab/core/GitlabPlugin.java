package de.weingardt.gitlab.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class GitlabPlugin extends Plugin {
	
	private static GitlabPlugin plugin;

	public static final String ID_PLUGIN = "de.weingardt.gitlab.core";

	public static final String CONNECTOR_KIND = "gitlab";

	public static final String ENCODING_UTF_8 = "UTF-8";

	public static GitlabPlugin getDefault() {
		return plugin;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

}
