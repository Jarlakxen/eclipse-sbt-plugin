package com.github.jarlakxen.scala.sbt.wizard.migrate;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.github.jarlakxen.scala.sbt.configurations.MigrateToSbtProjectConfiguration;

/**
 * WizardPage to select whether to add sbteclipse to project/plugins.sbt.
 * 
 * @see MigrationWizard
 * @author Facundo Viale
 */
public class MigrationWizardPage extends WizardPage {

	private MigrateToSbtProjectConfiguration configuration;
	
	private Button addSbtCheckbox;
	private Button addWebNatureCheckbox;
	
	public MigrationWizardPage(MigrateToSbtProjectConfiguration configuration) {
		super("MigrationWizardPage", "Migrate to ScalaIDE", null);
		this.configuration = configuration;
		setDescription("Migrate an existing SBT project to ScalaIDE.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout());
		
		addSbtCheckbox = new Button(composite, SWT.CHECK);
		addSbtCheckbox.setText("Add Default build.sbt");
		addSbtCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configuration.setForceDefaultSbtFile(addSbtCheckbox.getSelection());
			}
		});
		addSbtCheckbox.setSelection(configuration.isForceDefaultSbtFile());
		
		addWebNatureCheckbox = new Button(composite, SWT.CHECK);
		addWebNatureCheckbox.setText("Add Web Project Nature");
		addWebNatureCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configuration.setWebNature(addWebNatureCheckbox.getSelection());
			}
		});
		addWebNatureCheckbox.setSelection(configuration.isWebNature());
		
		setControl(parent);
	}

}
