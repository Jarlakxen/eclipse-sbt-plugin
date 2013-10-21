package com.github.jarlakxen.scala.sbt.wizard._import;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

public class SbtImportWizard extends Wizard implements IImportWizard {

	@Override
	public void addPages() {
		super.addPages();
		IWizardPage pages[] = getPages();
		System.out.println(pages);
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	


}
