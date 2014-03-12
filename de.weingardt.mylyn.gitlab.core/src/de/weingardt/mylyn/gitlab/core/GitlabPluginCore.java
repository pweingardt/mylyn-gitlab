/*******************************************************************************
 * Copyright (c) 2014, Paul Weingardt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Paul Weingardt - initial API and implementation
 *******************************************************************************/



package de.weingardt.mylyn.gitlab.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class GitlabPluginCore extends Plugin {
	
	private static GitlabPluginCore plugin;

	public static final String ID_PLUGIN = "de.weingardt.gitlab.core";

	public static final String CONNECTOR_KIND = "gitlab";

	public static final String ENCODING_UTF_8 = "UTF-8";

	public GitlabPluginCore() {
	}

	public static GitlabPluginCore get() {
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
