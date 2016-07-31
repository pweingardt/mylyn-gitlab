package de.weingardt.mylyn.gitlab.core;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;

import de.weingardt.mylyn.gitlab.core.exceptions.GitlabException;
import de.weingardt.mylyn.gitlab.core.exceptions.GitlabExceptionHandler;
import de.weingardt.mylyn.gitlab.core.exceptions.UnknownProjectException;

/**
 * The ConnectionManager is a singleton that handles all
 * GitlabConnection instances in a HashMap. The key in this HashMap
 * is the URL to the Gitlab instance constructed using a TaskRepository
 * class.
 * @author paul
 *
 */
public class ConnectionManager {

	/**
	 * The HashMap used to store all GitlabConnections
	 */
	private static HashMap<String, GitlabConnection> connections = new HashMap<String, GitlabConnection>();

	/**
	 * The pattern is used to verify a ULR to a valid Gitlab project URL.
	 */
	private static Pattern URLPattern = Pattern.compile("((?:http|https)://(?:.*))/((?:[^\\/]*?)/(?:[^\\/]*?))$");

	/**
	 * Returns the GitlabConnection for the given task repository
	 * @param repository
	 * @return
	 * @throws GitlabException
	 */
	static public GitlabConnection get(TaskRepository repository) throws GitlabException {
		return get(repository, false);
	}

	/**
	 * Returns the GitlabConnection for the given task repository. If it
	 * failes for whatever reason, it returns null.
	 * @param repository
	 * @return
	 */
	static public GitlabConnection getSafe(TaskRepository repository) {
		try {
			return get(repository);
		} catch (GitlabException e) {
			return null;
		}
	}

	/**
	 * Constructs a URL string for the given task repository.
	 * @param repository
	 * @return
	 */
	private static String constructURL(TaskRepository repository) {
		String username = repository.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = repository.getCredentials(AuthenticationType.REPOSITORY).getPassword();
		return repository.getUrl() + "?username=" + username + "&password=" + password.hashCode();
	}

	/**
	 * Validates the given task repository and returns a GitlabConnection if
	 * the task repository is a valid repository.
	 * @param repository
	 * @return
	 * @throws GitlabException
	 */
	static GitlabConnection validate(TaskRepository repository) throws GitlabException {
		try {
			Matcher matcher = URLPattern.matcher(repository.getUrl());
			if(!matcher.find()) {
				throw new GitlabException("Invalid Project-URL!");
			}

			String projectPath = matcher.group(2);
			String host = matcher.group(1);
			String username = repository.getCredentials(AuthenticationType.REPOSITORY).getUserName();
			String password= repository.getCredentials(AuthenticationType.REPOSITORY).getPassword();

			GitlabSession session = null;
			if(repository.getProperty("usePrivateToken") != null && repository.getProperty("usePrivateToken").equals("true")) {
				session = GitlabAPI.connect(host,  password).getCurrentSession();
			} else {
				session = GitlabAPI.connect(host, username, password);
			}

			GitlabAPI api = GitlabAPI.connect(host, session.getPrivateToken());

			if(projectPath.endsWith(".git")) {
				projectPath = projectPath.substring(0, projectPath.length() - 4);
			}

			List<GitlabProject> projects = api.getProjects();
			for(GitlabProject p : projects) {
				if(p.getPathWithNamespace().equals(projectPath)) {
					GitlabConnection connection = new GitlabConnection(host, p, session,
							new GitlabAttributeMapper(repository));
					return connection;
				}
			}
			// At this point the authentication was successful, but the corresponding project
			// could not be found!
			throw new UnknownProjectException(projectPath);
		} catch(GitlabException e) {
			throw e;
		} catch(Exception e) {
			throw GitlabExceptionHandler.handle(e);
		} catch(Error e) {
			throw GitlabExceptionHandler.handle(e);
		}
	}

	/**
	 * Returns a *valid* GitlabConnection, otherwise this method throws an exception.
	 * @param repository
	 * @param forceUpdate if true, a new GitlabConnection instance will be created, even if a Gitlab
	 * 	Connection already exists for the given task repository
	 * @return
	 * @throws GitlabException
	 */
	static GitlabConnection get(TaskRepository repository, boolean forceUpdate) throws GitlabException {
		try {
			String hash = constructURL(repository);
			if(connections.containsKey(hash) && !forceUpdate) {
				return connections.get(hash);
			} else {
				GitlabConnection connection = validate(repository);

				connections.put(hash, connection);
				connection.update();

				return connection;
			}
		} catch(GitlabException e) {
			throw e;
		} catch(Exception e) {
			throw GitlabExceptionHandler.handle(e);
		} catch(Error e) {
			throw GitlabExceptionHandler.handle(e);
		}
	}

}
