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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.gitlab.api.models.GitlabIssue;

public class GitlabIssueSearch {

	private final static String priorityPatternFormat = "priority:(%s)";
	private final static String typePatternFormat = "type:(%s)";
	
	
	private Pattern titlePattern;
	private Pattern descriptionPattern;
	
	private String assignee;
	private String milestone;
	private String state;
	
	private List<Pattern> labelPatterns = new ArrayList<Pattern>();
	
	private static int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	
	public GitlabIssueSearch(IRepositoryQuery query) {
		titlePattern = Pattern.compile(query.getAttribute("title"), flags);
		descriptionPattern = Pattern.compile(query.getAttribute("description"), flags);

		if(!query.getAttribute("label").equals("")) {
			labelPatterns.add(Pattern.compile(query.getAttribute("label"), flags));
		}
		
		if(!query.getAttribute("priority").equals("")) {
			labelPatterns.add(
					Pattern.compile(String.format(priorityPatternFormat, query.getAttribute("priority")), 
							flags));
		}
		
		if(!query.getAttribute("type").equals("")) {
			labelPatterns.add(
					Pattern.compile(String.format(typePatternFormat, query.getAttribute("type")), 
							flags));
		}
		
		milestone = query.getAttribute("milestone");
		assignee = query.getAttribute("assignee");
		state = query.getAttribute("state");
	}
	
	public boolean doesMatch(GitlabIssue issue) {
		if(!titlePattern.pattern().equals("") && !titlePattern.matcher(issue.getTitle()).find()) {
			return false;
		}
		
		if(!descriptionPattern.pattern().equals("") && !descriptionPattern.matcher(issue.getDescription()).find()) {
			return false;
		}
		
		Set<Pattern> matches = new HashSet<>();
		for(Pattern p : labelPatterns) {
			for(String label : issue.getLabels()) {
				if(p.matcher(label).find()) {
					matches.add(p);
				}
			}
		}
		if(matches.size() != labelPatterns.size()) {
			return false;
		}
		
		if(!assignee.equals("") && 
				(issue.getAssignee() == null || !assignee.equals(issue.getAssignee().getUsername()))) {
			return false;
		}
		
		
		if(!milestone.equals("") && 
				(issue.getMilestone() == null || !milestone.equals(issue.getMilestone().getTitle()))) {
			return false;
		}
		
		if(!state.equals("") && !state.equals(issue.getState())) {
			return false;
		}
		
		return true;
	}
	
}
