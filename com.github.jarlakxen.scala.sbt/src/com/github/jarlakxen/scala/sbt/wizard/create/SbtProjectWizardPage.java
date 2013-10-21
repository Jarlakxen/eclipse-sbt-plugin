package com.github.jarlakxen.scala.sbt.wizard.create;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.github.jarlakxen.scala.sbt.SbtVersion;
import com.github.jarlakxen.scala.sbt.ScalaVersion;
import com.github.jarlakxen.scala.sbt.configurations.CreateSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.util.UIUtil;

/**
 * WizardPage for configuring SBT project information.
 * 
 * @see SbtWizard
 * @author Facundo Viale
 */
public class SbtProjectWizardPage extends WizardPage {

	private CreateSbtProjectConfiguration configuration;
	private WizardNewProjectCreationPage mainPage;

	private List<Button> sbtRadioButtons;
	private Combo scalaVersion;
	private Text organization;
	private Text projectName;
	private Text productVersion;

	public SbtProjectWizardPage(WizardNewProjectCreationPage mainPage, CreateSbtProjectConfiguration configuration) {
		super("SBTProjectWizardPage");
		this.configuration = configuration;
		this.mainPage = mainPage;
	}

	@Override
	public void createControl(Composite parent) {
		setTitle("SBT Project Information");
		setDescription("Input SBT Project Information.");

		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		UIUtil.createLabel(composite, "SBT Version:");
		createSbtSelectorPanel(composite);

		UIUtil.createLabel(composite, "Scala Version:");
		scalaVersion = new Combo(composite, SWT.DROP_DOWN);
		for(ScalaVersion version : ScalaVersion.values()){
			scalaVersion.add(version.getText());
		}
		scalaVersion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selectedValue = scalaVersion.getItem(scalaVersion.getSelectionIndex());
				configuration.setScalaVersion(ScalaVersion.versionOf(selectedValue));
			}
		});
		scalaVersion.select(0);

		UIUtil.createLabel(composite, "Project Organization:");
		organization = new Text(composite, SWT.BORDER);
		organization.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		organization.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (validate(organization, "Organization")) {
					configuration.setOrganization(organization.getText());
				}
			}
		});

		UIUtil.createLabel(composite, "Project Name:");
		projectName = new Text(composite, SWT.BORDER);
		projectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (validate(projectName, "Project Name")) {
					configuration.setProjectName(projectName.getText());
				}
			}
		});

		UIUtil.createLabel(composite, "Product Version:");
		productVersion = new Text(composite, SWT.BORDER);
		productVersion.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		productVersion.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (validate(productVersion, "Product Version")) {
					configuration.setProductVersion(productVersion.getText());
				}
			}
		});
		productVersion.setText(configuration.getProductVersion());

		setControl(composite);
	}

	private Composite createSbtSelectorPanel(Composite parent) {
		Composite group = new Composite(parent, SWT.NULL);
		group.setLayout(new RowLayout(SbtVersion.values().length));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		sbtRadioButtons = new ArrayList<Button>();
		for (final SbtVersion version : SbtVersion.values()) {
			Button sbtRadioButton = new Button(group, SWT.RADIO);
			sbtRadioButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					configuration.setSbtVersion(version);
				}
			});
			sbtRadioButton.setText("sbt " + version.getPrefix());
			sbtRadioButton.setData(version);
			
			if(version.equals(configuration.getSbtVersion())){
				sbtRadioButton.setSelection(true);
			}
			
			sbtRadioButtons.add(sbtRadioButton);
		}

		return group;
	}

	private boolean validate(Text textField, String name) {
		if (textField.getText().length() == 0) {
			setErrorMessage(name + " is required.");
			setPageComplete(false);
			return false;
		}
		setErrorMessage(null);
		setPageComplete(true);
		return true;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible == true && projectName.getText().length() == 0) {
			projectName.setText(mainPage.getProjectName());
		}
	}

}
