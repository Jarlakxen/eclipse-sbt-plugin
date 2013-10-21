package com.github.jarlakxen.scala.sbt.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.github.jarlakxen.scala.sbt.wizard.migrate.MigrationWizard;

/**
 * Migrates the selected SBT project to ScalaIDE.
 *
 * @author Naoki Takezoe
 */
public class MigrateAction implements IObjectActionDelegate {

	private IProject project;
	private IWorkbenchPart targetPart;

	@Override
	public void run(IAction action) {
		WizardDialog dialog = new WizardDialog(targetPart.getSite().getShell(), new MigrationWizard(project));
		dialog.open();
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.project = null;

		if(selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection){
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if(obj instanceof IJavaProject){
				this.project = ((IJavaProject) obj).getProject();
			} else if(obj instanceof IProject){
				this.project = (IProject) obj;
			}
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.targetPart = targetPart;
	}

}
