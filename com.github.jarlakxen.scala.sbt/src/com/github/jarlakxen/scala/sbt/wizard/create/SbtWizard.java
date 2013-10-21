package com.github.jarlakxen.scala.sbt.wizard.create;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import com.github.jarlakxen.scala.sbt.SbtPlugin;
import com.github.jarlakxen.scala.sbt.configurations.CreateSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.jobs.CreateSbtProjectJob;
import com.github.jarlakxen.scala.sbt.util.UIUtil;

/**
 * Creates new SBT project.
 * <p>
 * This wizard creates a new Scala project by SBT layout, put sbt-launcher.jar
 * into the project root directory and add the classpath container to your
 * project for adding depended libraries into project classpath.
 * 
 * @see SbtProjectWizardPage SbtOptionsWizardPage
 * @author Facundo Viale
 */
public class SbtWizard extends BasicNewProjectResourceWizard {

	private CreateSbtProjectConfiguration configuration = CreateSbtProjectConfiguration.getDefault();

	@Override
	public void addPages() {
		super.addPages();
		WizardNewProjectCreationPage mainPage = (WizardNewProjectCreationPage) getPage("basicNewProjectPage");
		addPage(new SbtProjectWizardPage(mainPage, configuration));
		addPage(new SbtOptionsWizardPage(configuration));
	}

	@Override
	public boolean performFinish() {
		if (super.performFinish() == false) {
			return false;
		}

		configuration.setProject(getNewProject());

		try {
			CreateSbtProjectJob job = new CreateSbtProjectJob(configuration);
			job.schedule();
			return true;
		} catch (Exception e) {
			UIUtil.showErrorDialog(e.toString());
			SbtPlugin.logException(e);
			return false;
		}
	}
}
