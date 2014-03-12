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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;
import org.gitlab.api.models.GitlabMilestone;
import org.gitlab.api.models.GitlabProjectMember;

public class GitlabAttributeMapper extends TaskAttributeMapper {
	
	public GitlabAttributeMapper(TaskRepository taskRepository) throws CoreException, IOException {
		super(taskRepository);
	}
	
	@Override
	public Map<String, String> getOptions(TaskAttribute attribute) {
		switch(attribute.getId()) {
		case GitlabAttributeKeys.milestone:
			return getAsMap(getMilestones());
		}
		return super.getOptions(attribute);
	}
	
	private GitlabConnection getConnection() throws CoreException {
		return ConnectionManager.get(getTaskRepository());
	}
	
	public GitlabProjectMember findProjectMemberByName(String name) {
		try {
			List<GitlabProjectMember> members = getConnection().getProjectMembers();
			for(GitlabProjectMember member : members) {
				if(member.getName().equals(name) || member.getUsername().equals(name)) {
					return member;
				}
			}
		} catch (CoreException e) {
		}		
		return null;
	}
	
	public GitlabMilestone findMilestoneByName(String name) {
		try {
			List<GitlabMilestone> milestones = getConnection().getMilestones();
			for(GitlabMilestone m : milestones) {
				if(m.getTitle().equals(name)) {
					return m;
				}
			}
		} catch (CoreException e) {
		}
		return null;
	}
	
	private List<String> getMilestones() {
		List<String> target = new ArrayList<>();
		try {			
			List<GitlabMilestone> milestones = getConnection().getMilestones();
			for(GitlabMilestone m : milestones) {
				target.add(m.getTitle());
			}
		} catch (CoreException e) {
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
