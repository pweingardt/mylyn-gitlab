package de.weingardt.gitlab.ui;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.mylyn.tasks.core.IRepositoryQuery;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.wizards.AbstractRepositoryQueryPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GitlabQueryPage extends AbstractRepositoryQueryPage implements
		IWizardPage {

	private TextSearchField queryLabel;
	private TextSearchField title;
	private TextSearchField description;
	private TextSearchField label;
	private TextSearchField assignee;
	
	private ComboSearchField state;
	
	private List<String> issueStates = Arrays.asList("opened", "closed");
	
	public GitlabQueryPage(String pageName, TaskRepository taskRepository, IRepositoryQuery query) {
		super(pageName, taskRepository, query);
		
		setTitle("Gitlab Issue Query");
		setDescription("Gitlab Issue Query");		
	}

	@Override
	public void createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		control.setLayout(layout);

		createDefaultGroup(control);
		
		if(getQuery() != null) {
			updateControls(getQuery());
		}
		
		setControl(control);
	}

	private void createDefaultGroup(Composite parent) {
		queryLabel = new TextSearchField(parent, "Query Label: ");
		
		Label l = new Label(parent, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		l.setLayoutData(data);
		
		title = new TextSearchField(parent, "Title: ");
		description = new TextSearchField(parent, "Description: ");
		label = new TextSearchField(parent, "Labels: ");
		assignee = new TextSearchField(parent, "Assignee: ");
		
		state = new ComboSearchField(parent, "State: ", issueStates);
	}

	@Override
	public void applyTo(IRepositoryQuery query) {
		query.setSummary(queryLabel.get());
		query.setAttribute("title", title.get());
		query.setAttribute("description", description.get());
		query.setAttribute("label", label.get());
		query.setAttribute("assignee", assignee.get());
		
		query.setAttribute("state", state.get());
	}

	private void updateControls(IRepositoryQuery query) {
		queryLabel.set(query.getSummary());
		title.set(query.getAttribute("title"));
		description.set(query.getAttribute("description"));
		label.set(query.getAttribute("label"));
		assignee.set(query.getAttribute("assignee"));
		
		state.set(query.getAttribute("state"));
	}

	@Override
	public String getQueryTitle() {
		return "New Gitlab Issue Query";
	}
	
	private class TextSearchField {
		private Text textField;

		public TextSearchField(Composite parent, String name) {
			Label label = new Label(parent, SWT.LEFT);
			label.setText(name);

			this.textField = new Text(parent, SWT.LEFT | SWT.BORDER);
			GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
			this.textField.setLayoutData(gd);
		}

		public String get() {
			return this.textField.getText();
		}

		public void set(String text) {
			if(text == null) {
				text = "";
			}
			this.textField.setText(text);
		}
	}
	
	private class ComboSearchField {
		private Combo combo;
		private List<String> values;

		public ComboSearchField(Composite parent, String name, List<String> values) {
			this.values = values;
			Label label = new Label(parent, SWT.LEFT);			
			label.setText(name);

			combo = new Combo(parent, SWT.READ_ONLY);
			GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
			this.combo.setLayoutData(gd);

			combo.add("");
			for(String s : values) {
				combo.add(s);
			}
		}

		public String get() {
			return this.combo.getText();
		}

		public void set(String text) {
			for(int i = 0; i < this.values.size(); ++i) {
				if(this.values.get(i).equals(text)) {
					this.combo.select(i + 1);
				}
			}
		}
	}
	
}
