package com.github.jarlakxen.scala.sbt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * The project nature for the SBT project.
 *
 * @author Naoki Takezoe
 */
public class SbtProjectNature implements IProjectNature {

	private IProject project;

	@Override
	public void configure() throws CoreException {
		// Nothing to do
	}

	@Override
	public void deconfigure() throws CoreException {
		// Nothing to do
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
