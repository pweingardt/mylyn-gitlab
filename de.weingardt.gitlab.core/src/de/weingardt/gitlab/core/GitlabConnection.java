package de.weingardt.gitlab.core;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;


public class GitlabConnection {

	public final String host;
	public final GitlabSession session;
	public final GitlabProject project;
	public final GitlabAttributeMapper mapper;
	
	public GitlabConnection(String host, GitlabProject project, GitlabSession session,
			GitlabAttributeMapper mapper) {
		this.host = host;
		this.project = project;
		this.session = session;
		this.mapper = mapper;
	}
	
	public GitlabAPI api() {
		return GitlabAPI.connect(host, session.getPrivateToken());
	}
	
}
