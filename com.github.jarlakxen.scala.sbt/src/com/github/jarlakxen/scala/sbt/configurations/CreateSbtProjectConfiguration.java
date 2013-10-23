package com.github.jarlakxen.scala.sbt.configurations;

import org.eclipse.core.resources.IProject;

import com.github.jarlakxen.scala.sbt.SbtVersion;
import com.github.jarlakxen.scala.sbt.ScalaVersion;
import com.github.jarlakxen.scala.sbt.builder.TestLibrary;
import com.github.jarlakxen.scala.sbt.wizard.create.SbtWizard;

/**
 * Configuration for create SBT project.
 * 
 * @see SbtWizard
 * @author Facundo Viale
 */
public class CreateSbtProjectConfiguration {
	
	public static CreateSbtProjectConfiguration getDefault(){
		CreateSbtProjectConfiguration configuration = new CreateSbtProjectConfiguration();
		configuration.setSbtVersion(SbtVersion.SBT013);
		configuration.setProductVersion("0.0.1");
		configuration.setScalaVersion(ScalaVersion.getDefault());
		configuration.setWebNature(false);
		return configuration;
	}
	
	private IProject project;
	private SbtVersion sbtVersion;
	private ScalaVersion scalaVersion;
	private String organization;
	private String projectName;
	private String productVersion;
	private TestLibrary testLibrary;
	private boolean webNature;
	
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public SbtVersion getSbtVersion() {
		return sbtVersion;
	}

	public void setSbtVersion(SbtVersion sbtVersion) {
		this.sbtVersion = sbtVersion;
	}

	public ScalaVersion getScalaVersion() {
		return scalaVersion;
	}

	public void setScalaVersion(ScalaVersion scalaVersion) {
		this.scalaVersion = scalaVersion;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	
	public void setTestLibrary(TestLibrary testLibrary) {
		this.testLibrary = testLibrary;
	}
	
	public TestLibrary getTestLibrary() {
		return testLibrary;
	}

	public boolean isWebNature() {
		return webNature;
	}

	public void setWebNature(boolean webNature) {
		this.webNature = webNature;
	}
}
