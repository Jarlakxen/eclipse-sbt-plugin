package com.github.jarlakxen.scala.sbt;

import java.io.File;
import java.lang.reflect.Method;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Manages the SBT project configuration.
 * 
 * @author Naoki Takezoe
 */
public class SbtProjectConfiguration {

	public static final String PROP_SBT_VERSION = "sbtVersion";

	private IEclipsePreferences preferences;
	private SbtVersion sbtVersion;

	/**
	 * Loads configurations from the project property.
	 * 
	 * @param project
	 *            the project
	 * @param sbtVersion
	 *            the sbt version
	 */
	public SbtProjectConfiguration(IProject project) {
		IScopeContext projectScope = new ProjectScope(project);
		this.preferences = projectScope.getNode(SbtPlugin.PLUGIN_ID);
		loadConfiguration();
	}

	/**
	 * Loads configurations from the project property.
	 * 
	 * @param project
	 *            the project
	 * @param sbtVersion
	 *            the sbt version
	 */
	public SbtProjectConfiguration(IProject project, SbtVersion sbtVersion) {
		IScopeContext projectScope = new ProjectScope(project);
		this.preferences = projectScope.getNode(SbtPlugin.PLUGIN_ID);
		this.sbtVersion = sbtVersion;
	}

	private void loadConfiguration() {
		// load configurations
		try {
			sbtVersion = getEnumProperty(SbtVersion.class, PROP_SBT_VERSION, SbtVersion.getDefault());
		} catch (Exception ex) {
			SbtPlugin.logException(ex);
		}
	}

	public void setSbtVersion(SbtVersion sbtVersion) {
		this.sbtVersion = sbtVersion;
	}

	public SbtVersion getSbtVersion() {
		return this.sbtVersion;
	}

	public File getProjectSbtRuntime() {
		try {
			return SbtLaunchJarManager.getLauncher(getSbtVersion());
		} catch (Exception ex) {
		}
		return null;
	}

	private <T> T getEnumProperty(Class<T> enumType, String name, T defaultValue) {
		if (name == null) {
			return defaultValue;
		}
		try {
			Method method = enumType.getMethod("valueOf", String.class);

			@SuppressWarnings("unchecked")
			T value = (T) method.invoke(null, getProperty(name));

			return value;

		} catch (Exception ex) {
			return defaultValue;
		}
	}

	/**
	 * Stores configurations into the project property.
	 */
	public void saveConfiguration() {
		// save configurations
		try {
			saveProperty(PROP_SBT_VERSION, sbtVersion.name());
		} catch (Exception ex) {
			SbtPlugin.logException(ex);
		}
	}

	private String getProperty(String prop) throws CoreException {
		String value = preferences.get(prop, ""); // this.project.getPersistentProperty(new
													// QualifiedName(SbtPlugin.PLUGIN_ID,
													// prop));
		if (value == null) {
			value = "";
		}
		return value;
	}

	private void saveProperty(String prop, String value) throws CoreException {
		if (value == null) {
			value = "";
		}
		preferences.put(prop, value);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			SbtPlugin.logException(e);
		}
	}

}
