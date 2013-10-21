package com.github.jarlakxen.scala.sbt.jobs;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;

import com.github.jarlakxen.scala.sbt.SbtPlugin;
import com.github.jarlakxen.scala.sbt.action.UpdateProjectConfigurationAction;
import com.github.jarlakxen.scala.sbt.util.UIUtil;

public class UpdateProjectJob extends WorkspaceJob {
	
	private IProject project;
	
	public UpdateProjectJob(IProject project) {
		super("Updating SBT Project");
		this.project = project;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("Updating SBT Project", 2);


			ILaunch launch = new UpdateProjectConfigurationAction().runFor(project);
			while (!launch.getProcesses()[0].isTerminated()) {
				Thread.sleep(500);
			}
			
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// ////////////////////////////////////////////////////////////////////
			// refresh
			// ////////////////////////////////////////////////////////////////////
			monitor.setTaskName("Refreshing the project...");
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			SbtPlugin.addProjectNatures(project);
			monitor.worked(2);

			monitor.done();

			return Status.OK_STATUS;
		} catch (Exception ex) {
			UIUtil.showErrorDialog(ex.toString());
			SbtPlugin.logException(ex);
			return Status.CANCEL_STATUS;
		}
	}
}
