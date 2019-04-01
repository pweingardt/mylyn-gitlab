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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import org.eclipse.mylyn.tasks.core.data.TaskAttribute;

public enum GitlabAttribute {
	
	BODY("Description", TaskAttribute.DESCRIPTION,
			TaskAttribute.TYPE_LONG_RICH_TEXT),

	TITLE("Summary", TaskAttribute.SUMMARY, 
			TaskAttribute.TYPE_SHORT_RICH_TEXT),

	STATUS("Status", TaskAttribute.STATUS, 
			TaskAttribute.TYPE_SHORT_TEXT, GitlabFlag.ATTRIBUTE, GitlabFlag.READ_ONLY),

	LABELS("Labels", "de.weingardt.gitlab.issue.labels",
			TaskAttribute.TYPE_LONG_TEXT, GitlabFlag.ATTRIBUTE),

	UPDATED("Updated", TaskAttribute.DATE_MODIFICATION,
			TaskAttribute.TYPE_DATETIME, GitlabFlag.READ_ONLY, GitlabFlag.ATTRIBUTE),

	CREATED("Created", TaskAttribute.DATE_CREATION, 
			TaskAttribute.TYPE_DATETIME, GitlabFlag.READ_ONLY, GitlabFlag.ATTRIBUTE),
			
	COMPLETED("Completed", TaskAttribute.DATE_COMPLETION, 
			TaskAttribute.TYPE_DATETIME, GitlabFlag.READ_ONLY),

	AUTHOR("Author", TaskAttribute.USER_REPORTER,
			TaskAttribute.TYPE_PERSON, GitlabFlag.READ_ONLY, GitlabFlag.ATTRIBUTE),

	PROJECT("Project", TaskAttribute.PRODUCT,
			TaskAttribute.TYPE_SHORT_TEXT, GitlabFlag.READ_ONLY, GitlabFlag.ATTRIBUTE),

	ASSIGNEE("Assignee", TaskAttribute.USER_ASSIGNED,
			TaskAttribute.TYPE_PERSON, GitlabFlag.ATTRIBUTE),
			
	MILESTONE("Milestone", "de.weingardt.gitlab.issue.milestone", 
			TaskAttribute.TYPE_SINGLE_SELECT, GitlabFlag.ATTRIBUTE),
	
	IID("IID", TaskAttribute.TASK_KEY, TaskAttribute.TYPE_INTEGER,
			GitlabFlag.READ_ONLY),
			
	PRIORITY("Priority", TaskAttribute.PRIORITY,
			TaskAttribute.TYPE_SHORT_TEXT, GitlabFlag.READ_ONLY), 
			
	TYPE("Type", TaskAttribute.TASK_KIND,
			TaskAttribute.TYPE_SHORT_TEXT, GitlabFlag.READ_ONLY),
	
	URL("Url", TaskAttribute.TASK_URL,
			TaskAttribute.TYPE_URL, GitlabFlag.READ_ONLY);

	public static final String TypeBug = "bug";
	public static final String TypeFeature = "feature";
	public static final String TypeStory = "story";
	
	private Set<GitlabFlag> flags;

	private final String prettyName;

	private final String taskKey;

	private final String type;

	public String getKind() {
		if(type.equals(TaskAttribute.TYPE_PERSON)) {
			return TaskAttribute.KIND_PEOPLE;
		} else if (flags.contains(GitlabFlag.ATTRIBUTE)) {
			return TaskAttribute.KIND_DEFAULT;
		}
		return null;
	}

	public boolean isReadOnly() {
		return flags.contains(GitlabFlag.READ_ONLY);
	}

	GitlabAttribute(String prettyName, String taskKey, String type, GitlabFlag... flags) {
		this.taskKey = taskKey;
		this.prettyName = prettyName;
		this.type = type;
		if (flags == null || flags.length == 0) {
			this.setFlags(EnumSet.noneOf(GitlabFlag.class));
		} else {
			this.setFlags(EnumSet.copyOf(Arrays.asList(flags)));
		}
	}

	GitlabAttribute(String prettyName, String taskKey, String type) {
		this(prettyName, taskKey, type, new GitlabFlag[] {});
	}

	public Set<GitlabFlag> getFlags() {
		return flags;
	}

	public void setFlags(Set<GitlabFlag> flags) {
		this.flags = flags;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return this.prettyName;
	}

	public static GitlabAttribute get(String key) {
		for(GitlabAttribute attr : GitlabAttribute.values()) {
			if(attr.getTaskKey().equals(key)) {
				return attr;
			}
		}
		return null;
	}
	
}
