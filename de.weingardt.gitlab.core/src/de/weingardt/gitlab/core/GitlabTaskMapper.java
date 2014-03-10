package de.weingardt.gitlab.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITask.PriorityLevel;
import org.eclipse.mylyn.tasks.core.data.TaskAttribute;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskMapper;

public class GitlabTaskMapper extends TaskMapper {

	private static Pattern priorityPattern = Pattern.compile("priority:(high|normal|low)");
	
	public GitlabTaskMapper(TaskData taskData) {
		super(taskData);
	}
	
	@Override
	public PriorityLevel getPriorityLevel() {
		TaskAttribute labels = getTaskData().getRoot().getAttribute(GitlabAttribute.LABELS.getTaskKey());
		Matcher matcher = priorityPattern.matcher(labels.getValue());
		
		if(matcher.find()) {
			String match = matcher.group(1);
			switch(match) {
			case "high":
				return PriorityLevel.P1;

			case "low":
				return PriorityLevel.P5;
	
			default:
				return PriorityLevel.P3;
			}
		}
		
		return PriorityLevel.P3;
	}
	
	@Override
	public String getPriority() {
		return getPriorityLevel().toString();
	}
	
	@Override
	public boolean applyTo(ITask task) {
		task.setPriority(getPriorityLevel().toString());
		return super.applyTo(task);
	}

}
