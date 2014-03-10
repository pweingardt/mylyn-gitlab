package de.weingardt.gitlab.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class GitlabUIPlugin extends AbstractUIPlugin {

	private static GitlabUIPlugin plugin;

	public GitlabUIPlugin() {
		plugin = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static GitlabUIPlugin getDefault() {
		return plugin;
	}
	
}
