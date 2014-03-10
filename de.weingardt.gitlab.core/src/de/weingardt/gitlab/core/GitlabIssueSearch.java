package de.weingardt.gitlab.core;

import java.util.regex.Pattern;

import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.gitlab.api.models.GitlabIssue;

public class GitlabIssueSearch {

	private Pattern titlePattern;
	private Pattern descriptionPattern;
	private Pattern labelPattern;
	private Pattern assigneePattern;
	private String state;
	
	private static int flags = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
	
	public GitlabIssueSearch(IRepositoryQuery query) {
		titlePattern = Pattern.compile(query.getAttribute("title"), flags);
		descriptionPattern = Pattern.compile(query.getAttribute("description"), flags);
		labelPattern = Pattern.compile(query.getAttribute("label"), flags);
		assigneePattern = Pattern.compile(query.getAttribute("assignee"), flags);
		
		state = query.getAttribute("state");
	}
	
	public boolean doesMatch(GitlabIssue issue) {
		if(!titlePattern.pattern().equals("") && !titlePattern.matcher(issue.getTitle()).find()) {
			return false;
		}
		
		if(!descriptionPattern.pattern().equals("") && !descriptionPattern.matcher(issue.getTitle()).find()) {
			return false;
		}
		
		if(!labelPattern.pattern().equals("") && !labelPattern.matcher(issue.getTitle()).find()) {
			return false;
		}
		
		if(!assigneePattern.pattern().equals("") && !assigneePattern.matcher(issue.getTitle()).find()) {
			return false;
		}
		
		if(!state.equals("") && !state.equals(issue.getState())) {
			return false;
		}
		
		return true;
	}
	
}
