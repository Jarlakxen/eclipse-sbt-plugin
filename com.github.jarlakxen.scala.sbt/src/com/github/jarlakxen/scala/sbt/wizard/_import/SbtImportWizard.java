package com.github.jarlakxen.scala.sbt.wizard._import;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import com.github.jarlakxen.scala.sbt.configurations.ImportSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.jobs.ImportSbtProjectJob;

public class SbtImportWizard extends Wizard implements IImportWizard {

	private ImportSbtProjectConfiguration configuration = new ImportSbtProjectConfiguration();

	private SbtImportWizardPage page;

	@Override
	public void addPages() {
		super.addPages();
		addPage(page = new SbtImportWizardPage(configuration));
	}


	@Override
	public boolean performFinish() {
		if (!page.isPageComplete()) {
			return false;
		}
		
		new ImportSbtProjectJob(configuration).schedule();
		
		return true;
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {

	}
	
}
