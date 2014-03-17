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
		if(issue.getState().equals(GitlabIssue.StateClosed)) {
			return closed;
		} else {
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