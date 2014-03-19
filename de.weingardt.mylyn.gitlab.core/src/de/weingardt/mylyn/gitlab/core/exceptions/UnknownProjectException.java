package de.weingardt.mylyn.gitlab.core.exceptions;

public class UnknownProjectException extends GitlabException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9197943019104391768L;

	public UnknownProjectException(String project) {
		super("Unknown project " + project + " or insufficient access rights");
	}
	
}
