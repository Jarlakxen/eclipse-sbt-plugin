package com.github.jarlakxen.scala.sbt.wizard.create;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.github.jarlakxen.scala.sbt.configurations.CreateSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.util.UIUtil;

/**
 * WizardPage for configuring SBT project options.
 * 
 * @see SbtWizard
 * @author Facundo Viale
 */
public class SbtOptionsWizardPage extends WizardPage {

	private CreateSbtProjectConfiguration configuration;
	
	private Button addWebNatureCheckbox;

	public SbtOptionsWizardPage(CreateSbtProjectConfiguration configuration) {
		super("SBTOptionsWizardPage");
		this.configuration = configuration;
	}

	@Override
	public void createControl(Composite parent) {
		setTitle("SBT Project Options");
		setDescription("Input SBT Project Options.");
		
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		UIUtil.createLabel(composite, "Add Web Project Nature:");
		
		addWebNatureCheckbox = new Button(composite, SWT.CHECK);
		addWebNatureCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configuration.setWebNature(addWebNatureCheckbox.getSelection());
			}
		});
		addWebNatureCheckbox.setSelection(false);
		
		setControl(composite);
	}
}
