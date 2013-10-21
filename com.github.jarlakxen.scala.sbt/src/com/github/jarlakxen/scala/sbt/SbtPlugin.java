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

import scala.tools.eclipse.ScalaPlugin;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author Naoki Takezoe
 */
public class SbtPlugin extends AbstractUIPlugin {

	/** The plug-in ID */
	public static final String PLUGIN_ID = "jp.sf.amateras.scala.sbt"; //$NON-NLS-1$

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

//	/**
//	 * Returns completion proposals for SBT command.
//	 *
//	 * @param project the target project
//	 * @return completion proposals for SBT command
//	 */
//	public static List<SbtCommandProposal> getCommandProposal(IJavaProject project){
//		SbtProjectConfiguration config = new SbtProjectConfiguration(project.getProject());
//
//		File jarFile = config.getProjectSbtRuntime();
//		if(jarFile == null){
//			return Collections.emptyList();
//		}
//
//		List<SbtCommandProposal> list = new ArrayList<SbtPlugin.SbtCommandProposal>();
//
//		try {
//			// TODO javaコマンドで実行しているのがいまいち…
//			Process process = new ProcessBuilder()
//				.command("java", "-jar", jarFile.getName(), "actions")
//				.directory(jarFile.getParentFile())
//				.start();
//
//			InputStream in = process.getInputStream();
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//			byte[] buf = new byte[1024 * 8];
//			int length = 0;
//			while((length = in.read(buf)) != -1){
//				out.write(buf, 0, length);
//			}
//
//			in.close();
//			out.close();
//
//			process.destroy();
//
//			String result = new String(out.toByteArray());
//
//			BufferedReader reader = new BufferedReader(new StringReader(result));
//			String line = null;
//			while((line = reader.readLine()) != null){
//				if(line.startsWith("\t")){
//					line = line.trim();
//
//					String command = null;
//					String description = null;
//
//					int index = line.indexOf(':');
//					if(index >= 0){
//						command = line.substring(0, index);
//						description = line.substring(index + 1).trim();
//					} else {
//						command = line;
//						description = "";
//					}
//
//					list.add(new SbtCommandProposal(command, description));
//				}
//			}
//
//		} catch(IOException ex){
//			SbtPlugin.logException(ex);
//		}
//
//		return list;
//	}
//
//	public static class SbtCommandProposal {
//
//		public String command;
//		public String description;
//
//		public SbtCommandProposal(String command, String description){
//			this.command = command;
//			this.description = description;
//		}
//
//	}

}
