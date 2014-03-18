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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositorySettingsPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import de.weingardt.mylyn.gitlab.core.GitlabConnector;
import de.weingardt.mylyn.gitlab.core.GitlabPluginCore;

public class GitlabRepositorySettingsPage extends AbstractRepositorySettingsPage {

	private Button useToken;
	
	public GitlabRepositorySettingsPage(String title, String description,
			TaskRepository taskRepository) {
		super(title, description, taskRepository);
		
		setNeedsValidateOnFinish(true);
	}

	@Override
	protected void createAdditionalControls(final Composite composite) {
		savePasswordButton.setSelection(true);

		useToken = new Button(composite, SWT.CHECK);
		useToken.setText("Use private token instead of username/password");
		GridDataFactory.fillDefaults().span(2, 1).applyTo(useToken);

		useToken.addSelectionListener(new SelectionAdapter() {
	
			@Override
			public void widgetSelected(SelectionEvent e) {
				setUsernameFieldEnabled(!useToken.getSelection());
				getWizard().getContainer().updateButtons();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		if (serverUrlCombo.getText().length() == 0) {
			// This means, that there the user is *not* editing an existing repository configuration
			serverUrlCombo.setText("https://your-host.org/namespace/project.git");
		} else {
			if(getRepository().getProperty("usePrivateToken").equals("true")) {
				useToken.setSelection(true);
				setUsernameFieldEnabled(false);
			}
		}
	}
	
	private void setUsernameFieldEnabled(boolean enabled) {
		if(enabled) {
			repositoryUserNameEditor.getTextControl(compositeContainer).setEnabled(true);
			repositoryUserNameEditor.setEmptyStringAllowed(false);
			repositoryPasswordEditor.setLabelText(LABEL_PASSWORD);
			compositeContainer.layout();
		} else {
			repositoryUserNameEditor.setStringValue("");
			repositoryUserNameEditor.getTextControl(compositeContainer).setEnabled(false);
			repositoryUserNameEditor.setEmptyStringAllowed(true);
			repositoryPasswordEditor.setLabelText("Private token:");					
			compositeContainer.layout();
		}
	}

	@Override
	public String getConnectorKind() {
		return GitlabPluginCore.CONNECTOR_KIND;
	}

	@Override
	public TaskRepository createTaskRepository() {
		TaskRepository repo = super.createTaskRepository();
		return repo;
	}
	
	@Override
	public void applyTo(TaskRepository repository) {
		repository.setCategory(TaskRepository.CATEGORY_BUGS);
		super.applyTo(repository);
		if(useToken.getSelection()) {
			repository.setProperty("usePrivateToken", "true");
		} else {
			repository.setProperty("usePrivateToken", "false");
		}
	}
	
	@Override
	protected boolean isMissingCredentials() {
		if(useToken != null && useToken.getSelection()) {
			return repositoryPasswordEditor.getStringValue().trim().equals("");
		} else {
			return super.isMissingCredentials();
		}
	}
	
	@Override
	protected Validator getValidator(final TaskRepository repository) {
		return new Validator() {

			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				GitlabConnector.validate(repository);
			}
			
		};
	}


}
