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

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;
import org.osgi.framework.BundleContext;

import de.weingardt.mylyn.gitlab.core.exceptions.GitlabException;

public class GitlabPluginCore extends Plugin {
	
	private static GitlabPluginCore plugin;

	public static final String ID_PLUGIN = "de.weingardt.gitlab.core";

	public static final String CONNECTOR_KIND = "gitlab";

	public static final String ENCODING_UTF_8 = "UTF-8";
		
	private HashMap<String, GitlabConnection> connections = new HashMap<>(); 

	public GitlabPluginCore() {
	}
	
	public GitlabConnection get(TaskRepository repository) throws GitlabException {
		return get(repository, false);
	}
	
	GitlabConnection get(TaskRepository repository, boolean forceUpdate) throws GitlabException {
		if(connections.containsKey(repository.getUrl()) && !forceUpdate) {
			return connections.get(repository.getUrl());
		} else {
			try {
				URL url = new URL(repository.getUrl());
				String projectPath = url.getPath().substring(1);
				String host = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
				String username = repository.getCredentials(AuthenticationType.REPOSITORY).getUserName();
				String password= repository.getCredentials(AuthenticationType.REPOSITORY).getPassword();
				
				GitlabSession session = GitlabAPI.connect(host, username, password);
				
				GitlabAPI api = GitlabAPI.connect(host, session.getPrivateToken());
				
				List<GitlabProject> projects = api.getProjects();
				for(GitlabProject p : projects) {
					if(p.getPathWithNamespace().equals(projectPath)) {
						GitlabConnection connection = new GitlabConnection(host, p, session, 
								new GitlabAttributeMapper(repository));
						connection.update();
						connections.put(repository.getUrl(), connection);
						return connection;
					}
				}
			} catch(Exception | Error e) {
				throw new GitlabException("Failed to connect to: " + repository.getUrl());
			}
		}
		throw new GitlabException("Failed to connect to: " + repository.getUrl());
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
