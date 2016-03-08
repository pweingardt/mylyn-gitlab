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

public class ConnectionManager {
	
	private static HashMap<String, GitlabConnection> connections = new HashMap<String, GitlabConnection>();
	private static Pattern URLPattern = Pattern.compile("((?:http|https)://(?:.*))/((?:[^\\/]*?)/(?:[^\\/]*?))$");
	
	static public GitlabConnection get(TaskRepository repository) throws GitlabException {
		return get(repository, false);
	}
	
	static public GitlabConnection getSafe(TaskRepository repository) {
		try {
			return get(repository);
		} catch (GitlabException e) {
			return null;
		}
	}
	
	private static String constructURL(TaskRepository repository) {
		String username = repository.getCredentials(AuthenticationType.REPOSITORY).getUserName();
		String password = repository.getCredentials(AuthenticationType.REPOSITORY).getPassword();
		return repository.getUrl() + "?username=" + username + "&password=" + password.hashCode();
	}
	
	public final static String IGNORE_CERTIFICATE_ERROR_PROPERTY = "ignoreCertificateError";
	
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
			boolean ignoreCertificateErrors = repository.getProperty(IGNORE_CERTIFICATE_ERROR_PROPERTY).contentEquals("true");
			GitlabSession session = null;
			if(repository.getProperty("usePrivateToken").equals("true")) {
				session = GitlabAPI.connect(host,  password).ignoreCertificateErrors(ignoreCertificateErrors).getCurrentSession();
			} else {
				String tailUrl = GitlabSession.URL;
		    	session = GitlabAPI.connect(host, null).ignoreCertificateErrors(ignoreCertificateErrors).dispatch().
		    			with("login", username).with("password", password).to(tailUrl, GitlabSession.class);
			}
			GitlabAPI api = GitlabAPI.connect(host, session.getPrivateToken());
			api.ignoreCertificateErrors(ignoreCertificateErrors);
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
