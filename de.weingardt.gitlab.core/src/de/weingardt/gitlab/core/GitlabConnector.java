package de.weingardt.gitlab.core;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;

import de.weingardt.gitlab.core.exceptions.GitlabException;

public class GitlabConnector extends AbstractRepositoryConnector {

	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return true;
	}

	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		return false;
	}

	@Override
	public String getConnectorKind() {
		return GitlabPlugin.CONNECTOR_KIND;
	}

	@Override
	public String getLabel() {
		return "Gitlab Connector";
	}

	@Override
	public String getRepositoryUrlFromTaskUrl(String arg0) {
		return "null-repository";
	}

	@Override
	public TaskData getTaskData(TaskRepository repository, String arg1,
			IProgressMonitor monitor) throws CoreException {
		return null;
	}

	@Override
	public String getTaskIdFromTaskUrl(String url) {
		return null;
	}

	@Override
	public String getTaskUrl(String arg0, String arg1) {
		return null;
	}

	@Override
	public boolean hasTaskChanged(TaskRepository repository, ITask task, TaskData data) {
		return false;
	}

	@Override
	public IStatus performQuery(TaskRepository repository, IRepositoryQuery query,
			TaskDataCollector collector, ISynchronizationSession session,
			IProgressMonitor monitor) {
		return null;
	}

	@Override
	public void updateRepositoryConfiguration(TaskRepository repository,
			IProgressMonitor monitor) throws CoreException {
	}

	@Override
	public void updateTaskFromTaskData(TaskRepository repository, ITask task,
			TaskData taskData) {
	}

	public static void validate(TaskRepository taskRepo) throws CoreException {
		boolean found = false;
		try {
			GitlabConnection c = new GitlabConnection(taskRepo);

			if (c.privateToken.length() != 20) {
				throw new GitlabException("Invalid private token!");
			}

			GitlabAPI api = c.api();

			List<GitlabProject> projects = api.getAllProjects();
			for(GitlabProject p : projects) {
				if(p.getPathWithNamespace().equals(c.project)) {
					found = true;
				}
			}
		} catch (Exception | Error e) {
			throw new GitlabException("Connection not successful!");
		}
		
		if(!found) {
			throw new GitlabException("Invalid project path!");
		}
	}

}
