package de.weingardt.gitlab.core;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.core.data.AbstractTaskDataHandler;
import org.eclipse.mylyn.tasks.core.data.TaskData;
import org.eclipse.mylyn.tasks.core.data.TaskDataCollector;
import org.eclipse.mylyn.tasks.core.sync.ISynchronizationSession;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;

import de.weingardt.gitlab.core.exceptions.GitlabException;

public class GitlabConnector extends AbstractRepositoryConnector {

	private HashMap<String, GitlabConnection> connections = new HashMap<>();
	
	private GitlabTaskDataHandler handler = new GitlabTaskDataHandler(this);
	
	public GitlabConnection get(TaskRepository repository) throws CoreException {
		if(connections.containsKey(repository.getUrl())) {
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
				
				List<GitlabProject> projects = api.getAllProjects();
				for(GitlabProject p : projects) {
					if(p.getPathWithNamespace().equals(projectPath)) {
						GitlabConnection connection = new GitlabConnection(host, p, session, 
								new GitlabAttributeMapper(repository));
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
	
	@Override
	public boolean canCreateNewTask(TaskRepository repository) {
		return true;
	}

	@Override
	public boolean canCreateTaskFromKey(TaskRepository repository) {
		return true;
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
	public TaskData getTaskData(TaskRepository repository, String id,
			IProgressMonitor monitor) throws CoreException {
		return handler.getTaskData(repository, id, monitor);
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
		try {
			GitlabPlugin.get().getConnector().get(taskRepo);
		} catch (Exception | Error e) {
			throw new GitlabException("Connection not successful or repository not found!");
		}
	}
	
	@Override
	public AbstractTaskDataHandler getTaskDataHandler() {
		return handler;
	}

	public static Integer getTicketId(String id) {
		return Integer.parseInt(id);
	}

}
