/*******************************************************************************
 * Copyright (c) 2014, Paul Weingardt
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Paul Weingardt - initial API and implementation
 *******************************************************************************/



package de.weingardt.mylyn.gitlab.core;

import org.gitlab.api.models.GitlabIssue;

/**
 * Represents an action that tell Gitlab what to do with
 * the issue, e.g. "close" will close the issue in Gitlab.
 * This wrapper is necessary.
 *
 * @author paul
 *
 */
public enum GitlabAction {

	LEAVE("leave"),
	CLOSE("close"),
	REOPEN("reopen");

	/**
	 * The valid actions for an open issue
	 */
	private final static GitlabAction[] opened = {LEAVE, CLOSE};

	/**
	 * The valid actions for a closed issue
	 */
	private final static GitlabAction[] closed = {LEAVE, REOPEN};

	private GitlabAction(String label) {
		this.label = label;
	}

	public final String label;

	/**
	 * Returns all valid actions for the given issue.
	 * @param issue
	 * @return
	 */
	public static GitlabAction[] getActions(GitlabIssue issue) {
		if(issue.getState().equals(GitlabIssue.STATE_CLOSED)) {
			return closed;
		} else {
			return opened;
		}
	}

	/**
	 * Returns the GitlabAction enum for the given action string.
	 * @param action
	 * @return
	 */
	public static GitlabAction find(String action) {
		for(GitlabAction a : values()) {
			if(a.label.equals(action)) {
				return a;
			}
		}
		return LEAVE;
	}

	/**
	 * Wrapper for the GitlabAPI Issue Action enum.
	 * @return
	 */
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