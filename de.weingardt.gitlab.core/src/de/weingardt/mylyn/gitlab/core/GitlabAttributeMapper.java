package de.weingardt.mylyn.gitlab.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProjectMember;

public class GitlabAttributeMapper extends TaskAttributeMapper {

	private List<GitlabMilestone> milestones;
	private List<GitlabProjectMember> members;
	
	public GitlabAttributeMapper(TaskRepository taskRepository) throws CoreException, IOException {
		super(taskRepository);
	}
	
	public void update(GitlabConnection connection) throws IOException {
		GitlabAPI api = connection.api();
		milestones = api.getMilestones(connection.project);
		members = api.getProjectMembers(connection.project);
	}
	
	@Override
	public Map<String, String> getOptions(TaskAttribute attribute) {
		switch(attribute.getId()) {
		case GitlabAttributeKeys.milestone:
			return getAsMap(getMilestones());
		}
		return super.getOptions(attribute);
	}
	
	public GitlabProjectMember findProjectMemberByName(String name) {
		for(GitlabProjectMember member : members) {
			if(member.getName().equals(name) || member.getUsername().equals(name)) {
				return member;
			}
		}
		return null;
	}
	
	public GitlabMilestone findMilestoneByName(String name) {
		for(GitlabMilestone m : milestones) {
			if(m.getTitle().equals(name)) {
				return m;
			}
		}
		return null;
	}
	
	private List<String> getMilestones() {
		List<String> target = new ArrayList<>();
		for(GitlabMilestone m : milestones) {
			target.add(m.getTitle());
		}
		return target;
	}
	
	private HashMap<String, String> getAsMap(List<String> list) {
		HashMap<String, String> map = new HashMap<>();
		map.put("", "");
		for(String s : list) {
			map.put(s, s);
		}
		return map;
	}

}
