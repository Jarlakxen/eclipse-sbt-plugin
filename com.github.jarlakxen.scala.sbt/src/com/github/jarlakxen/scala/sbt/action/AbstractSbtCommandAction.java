package com.github.jarlakxen.scala.sbt.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.github.jarlakxen.scala.sbt.SbtExecutor;

/**
 * The base class for actions that run the SBT command.
 *
 * @author Naoki Takezoe
 */
public abstract class AbstractSbtCommandAction implements IObjectActionDelegate {

	protected IProject project;

	protected String command;
	protected boolean terminate;

	/**
	 * The constructor without command. This action starts SBT in the interactive mode.
	 */
	public AbstractSbtCommandAction(){
		this.terminate = false;
	}
	
	/**
	 * The constructor with command. This action executes specified command.
	 *
	 * @param command the SBT command
	 */
	public AbstractSbtCommandAction(String command){
		this.command = command;
		this.terminate = true;
	}

	public ILaunch runFor(IProject project) {
		return new SbtExecutor(project).execute(command, terminate);
	}
	
	@Override
	public void run(IAction action) {
		new SbtExecutor(project).execute(command, terminate);
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
	}

}
