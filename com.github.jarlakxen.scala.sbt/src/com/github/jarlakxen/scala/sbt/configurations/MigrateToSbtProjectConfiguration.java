package com.github.jarlakxen.scala.sbt.configurations;

import org.eclipse.core.resources.IProject;

import com.github.jarlakxen.scala.sbt.action.MigrateAction;
import com.github.jarlakxen.scala.sbt.wizard.migrate.MigrationWizardPage;

/**
 * Configuration for migrate to SBT project.
 * 
 * @see MigrationWizardPage
 * @see MigrateAction
 * @author Facundo Viale
 */
public class MigrateToSbtProjectConfiguration {
	
	public static MigrateToSbtProjectConfiguration getDefault(){
		MigrateToSbtProjectConfiguration configuration = new MigrateToSbtProjectConfiguration();
		configuration.setForceDefaultSbtFile(false);
		configuration.setWebNature(false);
		return configuration;
	}
	
	private IProject project;
	private boolean forceDefaultSbtFile;
	private boolean webNature;
	
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public boolean isForceDefaultSbtFile() {
		return forceDefaultSbtFile;
	}

	public void setForceDefaultSbtFile(boolean forceDefaultSbtFile) {
		this.forceDefaultSbtFile = forceDefaultSbtFile;
	}

	public boolean isWebNature() {
		return webNature;
	}

	public void setWebNature(boolean webNature) {
		this.webNature = webNature;
	}
}
