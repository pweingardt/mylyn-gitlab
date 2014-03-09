package de.weingardt.gitlab.ui;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.ITaskMapping;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.AbstractRepositoryConnectorUi;
import org.eclipse.mylyn.tasks.ui.wizards.ITaskRepositoryPage;
import org.eclipse.mylyn.tasks.ui.wizards.NewTaskWizard;
import org.eclipse.mylyn.tasks.ui.wizards.RepositoryQueryWizard;

import de.weingardt.gitlab.core.GitlabPlugin;

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
//		try {
			RepositoryQueryWizard wizard = new RepositoryQueryWizard(repository);
//			wizard.addPage(new GitlabQueryPage(repository, query));
			return wizard;
//		} catch (MTrackException e) {
//			StatusManager.getManager().handle(e.getStatus(), StatusManager.BLOCK | StatusManager.SHOW);
//			return null;
//		}
	}

	@Override
	public ITaskRepositoryPage getSettingsPage(TaskRepository repository) {
		return new GitlabRepositorySettingsPage(Strings.NEW_REPOSITORY, "description", repository);
	}

	@Override
	public boolean hasSearchPage() {
		return false;
	}

}
