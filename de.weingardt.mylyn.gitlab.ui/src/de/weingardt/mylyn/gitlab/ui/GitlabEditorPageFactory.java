package de.weingardt.mylyn.gitlab.ui;

import org.eclipse.mylyn.commons.ui.CommonImages;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.mylyn.tasks.ui.TasksUiImages;
import org.eclipse.mylyn.tasks.ui.editors.AbstractTaskEditorPageFactory;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditor;
import org.eclipse.mylyn.tasks.ui.editors.TaskEditorInput;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.forms.editor.IFormPage;

import de.weingardt.mylyn.gitlab.core.GitlabPlugin;

public class GitlabEditorPageFactory extends AbstractTaskEditorPageFactory {

	@Override
	public boolean canCreatePageFor(TaskEditorInput arg0) {
		return true;
	}

	@Override
	public IFormPage createPage(TaskEditor editor) {
		return new GitlabEditorPage(editor, GitlabPlugin.CONNECTOR_KIND);
	}

	@Override
	public Image getPageImage() {
		return CommonImages.getImage(TasksUiImages.TASK);
	}

	@Override
	public String getPageText() {
		return "Gitlab Issue";
	}
	
	@Override
	public String[] getConflictingIds(TaskEditorInput input) {
		return new String[] {ITasksUiConstants.ID_PAGE_PLANNING};
	}

}
