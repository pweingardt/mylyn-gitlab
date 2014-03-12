package de.weingardt.mylyn.gitlab.core.exceptions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import de.weingardt.mylyn.gitlab.core.GitlabPluginCore;

public class GitlabException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8626757701151868815L;

	public GitlabException(String message) {
		super(new Status(Status.ERROR, GitlabPluginCore.ID_PLUGIN, message));
	}

}
