package de.weingardt.gitlab.core;

import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskAttributeMapper;

public class GitlabAttributeMapper extends TaskAttributeMapper {

	public GitlabAttributeMapper(TaskRepository taskRepository) {
		super(taskRepository);
	}
	
	@Override
	public String getLabel(TaskAttribute taskAttribute) {
		return super.getLabel(taskAttribute);
	}

}
