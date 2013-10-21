package com.github.jarlakxen.scala.sbt.wizard.migrate;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;

import com.github.jarlakxen.scala.sbt.SbtPlugin;
import com.github.jarlakxen.scala.sbt.action.MigrateAction;
import com.github.jarlakxen.scala.sbt.configurations.MigrateToSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.jobs.MigrateToSbtProjectJob;
import com.github.jarlakxen.scala.sbt.util.UIUtil;

/**
 * Migrates an extsting SBT project to ScalaIDE.
 * <p>
 * This wizard is called from {@link MigrateAction} and do below:
 * <ul>
 *   <li>Add natures of JDT, ScalaIDE and eclipse-scala-tools.</li>
 *   <li>Add sbteclipse to project/plugins.sbt (Optional)</li>
 *   <li>Run "sbt eclipse" to update Eclipse project configuration files.</li>
 * </ul>
 * 
 * @see MigrationWizardPage
 * @see MigrateAction
 * @author Facundo Viale
 */
public class MigrationWizard extends Wizard {
	
	private MigrateToSbtProjectConfiguration configuration = MigrateToSbtProjectConfiguration.getDefault();
	
	public MigrationWizard(IProject project){
		configuration.setProject(project);
		setWindowTitle("Add SBT Nature");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		addPage(new MigrationWizardPage(configuration));
	}

	@Override
	public boolean performFinish() {
		
		try {
			MigrateToSbtProjectJob job = new MigrateToSbtProjectJob(configuration);
			job.schedule();
			return true;
		} catch (Exception e) {
			UIUtil.showErrorDialog(e.toString());
			SbtPlugin.logException(e);
			return false;
		}
	}
}
