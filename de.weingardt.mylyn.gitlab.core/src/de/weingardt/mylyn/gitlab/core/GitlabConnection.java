package de.weingardt.mylyn.gitlab.core;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabProjectMember;
import org.gitlab.api.models.GitlabSession;


public class GitlabConnection {

	public final String host;
	public final GitlabSession session;
	public final GitlabProject project;
	public final GitlabAttributeMapper mapper;
	
	private List<GitlabMilestone> milestones;
	private List<GitlabProjectMember> members;
	
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
	
	public void update() throws IOException {
		milestones = api().getMilestones(project);
		members = api().getProjectMembers(project);
	}

	public List<GitlabMilestone> getMilestones() {
		return Collections.unmodifiableList(milestones);
	}
	
	public List<GitlabProjectMember> getProjectMembers() {
		return Collections.unmodifiableList(members);
	}
	
}
