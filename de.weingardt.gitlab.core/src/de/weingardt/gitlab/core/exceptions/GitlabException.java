package de.weingardt.gitlab.core.exceptions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import de.weingardt.gitlab.core.GitlabPlugin;

public class GitlabException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8626757701151868815L;

	public GitlabException(String message) {
		super(new Status(Status.ERROR, GitlabPlugin.ID_PLUGIN, message));
	}

}
