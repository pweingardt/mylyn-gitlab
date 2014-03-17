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



package de.weingardt.mylyn.gitlab.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskComment;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;

import de.weingardt.mylyn.gitlab.core.GitlabAttribute;
import de.weingardt.mylyn.gitlab.core.GitlabPluginCore;

public class GitlabConnectorUI extends AbstractRepositoryConnectorUi {
	
	@Override
	public String getConnectorKind() {
		return GitlabPluginCore.CONNECTOR_KIND;
	}

	@Override
	public IWizard getNewTaskWizard(TaskRepository repository, ITaskMapping mapping) {
		return new NewTaskWizard(repository, mapping);
	}

	@Override
	public IWizard getQueryWizard(TaskRepository repository, IRepositoryQuery query) {
		RepositoryQueryWizard wizard = new RepositoryQueryWizard(repository);
		wizard.addPage(new GitlabQueryPage("New Page", repository, query));
		return wizard;
	}

	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository repository) {
		return new GitlabRepositorySettingsPage(Strings.NEW_REPOSITORY, Strings.SETTINGS_PAGE, repository);
	}

	@Override
	public boolean hasSearchPage() {
		return false;
	}
	
	@Override
	public ImageDescriptor getTaskKindOverlay(ITask task) {
		String kind = task.getTaskKind();
		if(kind.equals(GitlabAttribute.TypeBug)) {
			return GitlabImages.OVERLAY_BUG;
		} else if(kind.equals(GitlabAttribute.TypeFeature)) {
			return GitlabImages.OVERLAY_FEATURE;
		} else if(kind.equals(GitlabAttribute.TypeStory)) {
			return GitlabImages.OVERLAY_STORY;
		}
		return super.getTaskKindOverlay(task);
	}
	
	@Override
	public String getReplyText(TaskRepository taskRepository, ITask task,
			ITaskComment taskComment, boolean includeTask) {
		return "Reply to " + taskComment.getAuthor();
	}

}
