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



package de.weingardt.mylyn.gitlab.ui;

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
