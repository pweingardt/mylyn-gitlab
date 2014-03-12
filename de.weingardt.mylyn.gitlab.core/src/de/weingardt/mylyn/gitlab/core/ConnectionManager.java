package de.weingardt.mylyn.gitlab.core;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;

import de.weingardt.mylyn.gitlab.core.exceptions.GitlabException;

public class ConnectionManager {
	
	private static HashMap<String, GitlabConnection> connections = new HashMap<>();
	
	static public GitlabConnection get(TaskRepository repository) throws GitlabException {
		return get(repository, false);
	}
	
	static GitlabConnection get(TaskRepository repository, boolean forceUpdate) throws GitlabException {
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

}
