package com.github.jarlakxen.scala.sbt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.jarlakxen.scala.sbt.listener.SbtProjectFilesChangeListener;

import scala.tools.eclipse.ScalaPlugin;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author Naoki Takezoe
 */
public class SbtPlugin extends AbstractUIPlugin {

	/** The plug-in ID */
	public static final String PLUGIN_ID = "com.github.jarlakxen.scala.sbt"; //$NON-NLS-1$

	/** The nature ID */
	public static final String NATURE_ID = PLUGIN_ID + ".SbtProjectNature"; //$NON-NLS-1$

	// The shared instance
	private static SbtPlugin plugin;

	public static final String ICON_SETTING_KEY = "/icons/setting_key.gif";
	public static final String ICON_SETTING_VALUE = "/icons/setting_value.gif";

	/**
	 * The constructor
	 */
	public SbtPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		SbtProjectFilesChangeListener.register();
		SbtLaunchJarManager.deploy();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		SbtLaunchJarManager.undeploy();
		plugin = null;
		super.stop(context);
	}

	protected void initializeImageRegistry(ImageRegistry registory) {
		super.initializeImageRegistry(registory);
		registory.put(ICON_SETTING_KEY, ImageDescriptor.createFromURL(getBundle().getEntry(ICON_SETTING_KEY)));
		registory.put(ICON_SETTING_VALUE, ImageDescriptor.createFromURL(getBundle().getEntry(ICON_SETTING_VALUE)));
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static SbtPlugin getDefault() {
		return plugin;
	}

	public static void logException(Exception ex){
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, ex.toString(), ex);
		getDefault().getLog().log(status);
	}
	
	public static void addProjectNatures(IProject project) throws CoreException {
		IProjectDescription desc = project.getDescription();
		String[] natureIds = new String[]{
				ScalaPlugin.plugin().natureId(),
				"org.eclipse.jdt.core.javanature",
				SbtPlugin.NATURE_ID
		};
		desc.setNatureIds(natureIds);
		project.setDescription(desc, null);
	}

}
