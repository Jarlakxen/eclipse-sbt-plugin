package com.github.jarlakxen.scala.sbt.jobs;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

import com.github.jarlakxen.scala.sbt.SbtPlugin;
import com.github.jarlakxen.scala.sbt.SbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.TemplateBuilder;
import com.github.jarlakxen.scala.sbt.action.UpdateProjectConfigurationAction;
import com.github.jarlakxen.scala.sbt.configurations.CreateSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.util.ProjectUtils;
import com.github.jarlakxen.scala.sbt.wizard.create.SbtWizard;

/**
 * Job that create the SBT project.
 * 
 * @see SbtWizard
 * @author Facundo Viale
 */
public class CreateSbtProjectJob extends WorkspaceJob {

	private CreateSbtProjectConfiguration configuration;

	public CreateSbtProjectJob(CreateSbtProjectConfiguration configuration) {
		super("Create a New SBT Project");
		this.configuration = configuration;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("Create a New SBT Project", 6);
			IProject project = configuration.getProject();

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// ////////////////////////////////////////////////////////////////////
			// create source folders
			// ////////////////////////////////////////////////////////////////////
			monitor.setTaskName("Creating source folders...");
			IFolder src = project.getFolder("src");
			if (!src.exists()) {
				src.create(true, true, null);
			}
			createSourceFolder(project, "src/main");
			createSourceFolder(project, "src/test");

			monitor.worked(1);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// ////////////////////////////////////////////////////////////////////
			// Create Project Configuration
			// ////////////////////////////////////////////////////////////////////
			monitor.setTaskName("Creating project configuration...");
			String content;
			IFolder projectDir = project.getFolder("project");
			if (!projectDir.exists()) {
				projectDir.create(true, true, null);
			}

			IFile projectFile = project.getFile("build.sbt");
			if (!projectFile.exists()) {
				content = TemplateBuilder.createSbtTemplate().projectName(configuration.getProjectName())
						.organization(configuration.getOrganization()).productVersion(configuration.getProductVersion())
						.scalaVersion(configuration.getScalaVersion()).webNature(configuration.isWebNature()).build();
				projectFile.create(IOUtils.toInputStream(content, "UTF-8"), true, null);
			}

			// Create plugins.sbt
			IFile plugins = project.getFile("project/plugins.sbt");
			if (!plugins.exists()) {
				plugins.create(IOUtils.toInputStream(TemplateBuilder.createSbtPluginsTemplate().webNature(configuration.isWebNature())
						.build(), "UTF-8"), true, null);
			} else {
				plugins.setContents(IOUtils.toInputStream(TemplateBuilder.createSbtPluginsTemplate().webNature(configuration.isWebNature())
						.build(), "UTF-8"), true, false, null);
			}

			// Create build.properties
			IFile build = project.getFile("project/build.properties");
			if (!build.exists()) {
				content = TemplateBuilder.createSbtPropertiesTemplate().sbtVersion(configuration.getSbtVersion()).build();
				build.create(IOUtils.toInputStream(content, "UTF-8"), true, null);
			}

			monitor.worked(1);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// ////////////////////////////////////////////////////////////////////
			// add natures
			// ////////////////////////////////////////////////////////////////////
			monitor.setTaskName("Adding natures to the project...");
			SbtPlugin.addProjectNatures(project);

			SbtProjectConfiguration config = new SbtProjectConfiguration(project, configuration.getSbtVersion());
			config.saveConfiguration();

			monitor.worked(1);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// ////////////////////////////////////////////////////////////////////
			// run sbteclipse to generate Eclipse configuration
			// files
			// ////////////////////////////////////////////////////////////////////
			monitor.setTaskName("Generating project configuration files...");

			ISchedulingRule rules = ProjectUtils.mutexRuleFor(project);
			Job.getJobManager().beginRule(rules, monitor);
			
			ILaunch launch = new UpdateProjectConfigurationAction().runFor(project);
			while (!launch.getProcesses()[0].isTerminated()) {
				Thread.sleep(500);
			}
			Job.getJobManager().endRule(rules);
			
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// refresh
			monitor.setTaskName("Refreshing the project ...");
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			monitor.worked(1);
			
			monitor.setTaskName("Update project nature ...");
			SbtPlugin.addProjectNatures(project);
			monitor.worked(1);
			
			// refresh
			monitor.setTaskName("Refreshing the project ...");
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			monitor.worked(1);

			monitor.done();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return Status.OK_STATUS;
	}

	private static void createSourceFolder(IProject project, String rootPath) throws CoreException {
		IFolder root = project.getFolder(rootPath);
		if (!root.exists()) {
			root.create(true, true, null);
		}

		IFolder scala = project.getFolder(rootPath + "/scala");
		if (!scala.exists()) {
			scala.create(true, true, null);
		}

		IFolder resources = project.getFolder(rootPath + "/resources");
		if (!resources.exists()) {
			resources.create(true, true, null);
		}
	}

}
