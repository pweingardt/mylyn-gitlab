package de.weingardt.gitlab.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.gitlab.api.GitlabAPI;

public class GitlabConnection {

	public final String host;
	public final String project;
	public final String privateToken;
	
	public GitlabConnection(TaskRepository repository) throws MalformedURLException {
		URL url = new URL(repository.getUrl());
		this.host = url.getProtocol() + "://" + url.getHost();
		this.project = url.getPath().substring(1);
		this.privateToken = repository.getCredentials(AuthenticationType.REPOSITORY).getPassword();
	}
	
	public GitlabAPI api() {
		return GitlabAPI.connect(host, privateToken);
	}
	
}
