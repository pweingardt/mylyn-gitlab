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
import de.weingardt.mylyn.gitlab.core.GitlabPlugin;

public class GitlabConnectorUI extends AbstractRepositoryConnectorUi {
	
	@Override
	public String getConnectorKind() {
		return GitlabPlugin.CONNECTOR_KIND;
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
	public ImageDescriptor getTaskPriorityOverlay(ITask task) {
		ImageDescriptor image = super.getTaskPriorityOverlay(task);
		return image;
	}

	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository repository) {
		return new GitlabRepositorySettingsPage(Strings.NEW_REPOSITORY, "description", repository);
	}

	@Override
	public boolean hasSearchPage() {
		return false;
	}
	
	@Override
	public ImageDescriptor getTaskKindOverlay(ITask task) {
		switch(task.getTaskKind()) {
		case GitlabAttribute.TypeBug:
			return GitlabImages.OVERLAY_BUG;
			
		case GitlabAttribute.TypeFeature:
			return GitlabImages.OVERLAY_FEATURE;
			
		case GitlabAttribute.TypeStory:
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
