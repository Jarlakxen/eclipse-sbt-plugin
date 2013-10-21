package com.github.jarlakxen.scala.sbt.jobs;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;

import com.github.jarlakxen.scala.sbt.SbtPlugin;
import com.github.jarlakxen.scala.sbt.SbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.SbtVersion;
import com.github.jarlakxen.scala.sbt.TemplateBuilder;
import com.github.jarlakxen.scala.sbt.action.MigrateAction;
import com.github.jarlakxen.scala.sbt.action.UpdateProjectConfigurationAction;
import com.github.jarlakxen.scala.sbt.configurations.MigrateToSbtProjectConfiguration;
import com.github.jarlakxen.scala.sbt.wizard.migrate.MigrationWizardPage;

/**
 * Job that migrate the SBT project.
 * 
 * @see MigrationWizardPage
 * @see MigrateAction
 * @author Facundo Viale
 */
public class MigrateToSbtProjectJob extends WorkspaceJob {

	private MigrateToSbtProjectConfiguration configuration;

	public MigrateToSbtProjectJob(MigrateToSbtProjectConfiguration configuration) {
		super("Create a New SBT Project");
		this.configuration = configuration;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("Migrate Project to ScalaIDE", 4);
			IProject project = configuration.getProject();

			// add natures
			monitor.setTaskName("Adding natures...");
			SbtPlugin.addProjectNatures(project);
			monitor.worked(1);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			SbtProjectConfiguration config = new SbtProjectConfiguration(project, getSbtVersion(project));
			config.saveConfiguration();

			// add build.sbt
			if (configuration.isForceDefaultSbtFile()) {
				IFile sbtFile = project.getFile("build.sbt");
				if (!sbtFile.exists()) {
					sbtFile.create(IOUtils.toInputStream(TemplateBuilder.createSbtTemplate().build()), true, null);
				} else {
					sbtFile.setContents(IOUtils.toInputStream(TemplateBuilder.createSbtTemplate().build()), true, false, null);
				}
			}

			IFolder folder = project.getFolder("project");
			if (!folder.exists()) {
				folder.create(true, true, null);
			}
			IFile file = project.getFile("project/plugins.sbt");
			if (file.exists()) {
				String source = IOUtils.toString(file.getContents(), "UTF-8");
				file.setContents(
						IOUtils.toInputStream(TemplateBuilder.createSbtPluginsTemplate().webNature(configuration.isWebNature())
								.fill(source)), true, false, null);
			} else {
				file.create(
						IOUtils.toInputStream(TemplateBuilder.createSbtPluginsTemplate().webNature(configuration.isWebNature()).build()),
						true, null);
			}

			monitor.worked(2);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// run sbteclipse to generate Eclipse configuration files
			monitor.setTaskName("Updating project configuration files...");
			ILaunch launch = new UpdateProjectConfigurationAction().runFor(project);
			while (!launch.getProcesses()[0].isTerminated()) {
				Thread.sleep(500);
			}
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			
			monitor.worked(3);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			// refresh
			monitor.setTaskName("Refreshing the project...");
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			SbtPlugin.addProjectNatures(project);
			monitor.worked(4);

			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}

			monitor.done();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return Status.OK_STATUS;
	}

	private static SbtVersion getSbtVersion(IProject project) {
		// Check build.properties
		Properties props = getBuildProperties(project);

		if (props != null) {
			String propertyVersion = props.getProperty("sbt.version");
			SbtVersion version = SbtVersion.getVersion(propertyVersion);
			if (version != null) {
				return version;
			}
		}
		return SbtVersion.getDefault();
	}

	private static Properties getBuildProperties(IProject project) {
		try {
			IFile file = project.getFile("project/build.properties");
			if (file.exists()) {
				InputStream in = file.getContents();
				try {
					Properties props = new Properties();
					props.load(in);
					return props;
				} finally {
					IOUtils.closeQuietly(in);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}
}
