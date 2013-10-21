package com.github.jarlakxen.scala.sbt.action;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

import com.github.jarlakxen.scala.sbt.SbtPlugin;

/**
 * Updates project dependency.
 *
 * @author Facundo Viale
 */
public class UpdateProjectConfigurationAction extends AbstractSbtCommandAction {

	public UpdateProjectConfigurationAction() {
		super("update eclipse");
	}

	@Override
	public void run(IAction action) {
		super.run(action);
		try {
			project.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			SbtPlugin.addProjectNatures(project);
		} catch(CoreException ex){
			SbtPlugin.logException(ex);
		}
	}
	
}
