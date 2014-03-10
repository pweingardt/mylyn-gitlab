package de.weingardt.gitlab.core;

import org.gitlab.api.models.GitlabIssue;


public enum GitlabAction {

	LEAVE("leave"),
	CLOSE("close"),
	REOPEN("reopen");

	private final static GitlabAction[] opened = {LEAVE, CLOSE};

	private final static GitlabAction[] closed = {LEAVE, REOPEN};

	private GitlabAction(String label) {
		this.label = label;
	}

	public final String label;

	public static GitlabAction[] getActions(GitlabIssue issue) {
		switch(issue.getState()) {
		case GitlabIssue.StateClosed:
			return closed;
			
		default:
			return opened;
		}
	}

	public static GitlabAction find(String action) {
		for(GitlabAction a : values()) {
			if(a.label.equals(action)) {
				return a;
			}
		}
		return LEAVE;
	}
	
	public GitlabIssue.Action getGitlabIssueAction() {
		switch(this) {
		case CLOSE:
			return GitlabIssue.Action.CLOSE;
			
		case LEAVE:
			return GitlabIssue.Action.LEAVE;
			
		case REOPEN:
			return GitlabIssue.Action.REOPEN;
		}
		return GitlabIssue.Action.LEAVE;
	}

}