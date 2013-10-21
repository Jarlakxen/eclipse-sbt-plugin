package com.github.jarlakxen.scala.sbt;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.github.jarlakxen.scala.sbt.util.UIUtil;

/**
 *
 * @author Naoki Takezoe
 */
public class SbtExecutor {

	private IProject project;
	private File workDir;

	/**
	 * Constructor.
	 *
	 * @param project the target project
	 */
	public SbtExecutor(IProject project){
		this.project = project;
	}

	public void setWorkDirectory(File workDir){
		this.workDir = workDir;
	}

	/**
	 * Executes the given SBT command.
	 * <p>
	 * This method creates Run Configuration for execution of SBT command automatically.
	 * And execution results are displayed in the Console view.
	 *
	 * @param command the SBT command
	 * @param terminate whether terminate the process if the project definition is invalid
	 */
	public ILaunch execute(String command, boolean terminate){
		if(project == null){
			return null;
		}

		SbtProjectConfiguration projectConfig = new SbtProjectConfiguration(project);
		try {
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();

			ILaunchConfigurationType type
			   = manager.getLaunchConfigurationType(
			     IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);

			// Creates new configuration
			ILaunchConfigurationWorkingCopy wc = null;
			if(command == null || command.length() == 0){
				wc = type.newInstance(null, String.format("sbt - %s", project.getName()));
			} else {
				wc = type.newInstance(null, String.format("sbt %s - %s", command, project.getName()));
			}

			// Sets the project name
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, project.getName());

			// Sets the main class
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "xsbt.boot.Boot");

			// Sets the VM arguments
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, createVMarguments(projectConfig));

			// Sets the working directory if it's specified
			if(this.workDir != null){
				wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, workDir.getAbsolutePath());
			}

			// Sets the classpath
			File jarFile = projectConfig.getProjectSbtRuntime();
			URL sbtRuntimeUrl = null;
			if(jarFile != null){
				sbtRuntimeUrl = jarFile.toURI().toURL();
			}
			if(sbtRuntimeUrl == null){
				UIUtil.showErrorDialog("Can't find SBT runtime in your project.");
				return null;
			}

			IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(sbtRuntimeUrl.getPath()));

			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, Arrays.asList(entry.getMemento()));
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);

			// Sets the command
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, command);

			// Saves the created configuration
			ILaunchConfiguration launchConfig = wc.doSave();

			// Runs the process using the created configuration
			ILaunch launch = launchConfig.launch(ILaunchManager.RUN_MODE, null);
			
			final IProcess process = launch.getProcesses()[0];
			if(terminate){
				process.getStreamsProxy().getOutputStreamMonitor().addListener(new IStreamListener() {
					@Override
					public void streamAppended(String message, IStreamMonitor monitor) {
						if(message.trim().endsWith("(r)etry, (q)uit, (l)ast, or (i)gnore?")){
							try {
								process.terminate();
							} catch(Exception ex){
							}
						}
					}
				});
			}
			
			return launch;

		} catch(Exception ex){
			SbtPlugin.logException(ex);
		}

		return null;
	}
	
	private static class ProxyConfig {
		public String host     = null;
		public int   port      = -1;
		public String userId   = null;
		public String password = null;
		
		public ProxyConfig(IProxyService proxyService, URI uri){
			IProxyData[] proxyDataForHost = proxyService.select(uri);
			for (IProxyData data : proxyDataForHost) {
				if (data.getHost() != null) {
					host      = data.getHost();
					port      = data.getPort();
					userId    = data.getUserId();
					password  = data.getPassword();
				}
			}
		}
	}

	private static String createVMarguments(SbtProjectConfiguration projectConfig){
		try {
			BundleContext context = SbtPlugin.getDefault().getBundle().getBundleContext();
			ServiceReference<IProxyService> ref = context.getServiceReference(IProxyService.class);
			IProxyService proxyService = context.getService(ref);

			ProxyConfig httpProxyConfig  = new ProxyConfig(proxyService, new URI("http://www.google.co.jp/"));
			ProxyConfig httpsProxyConfig = new ProxyConfig(proxyService, new URI("https://www.google.co.jp/"));
			
			StringBuilder sb = new StringBuilder();
	
			sb.append("-Djline.WindowsTerminal.directConsole=false ");
	
			// http proxy
			if(httpProxyConfig.host != null){
				sb.append("-Dhttp.proxyHost=").append(httpProxyConfig.host).append(" ");
			}
			if(httpProxyConfig.port >= 0){
				sb.append("-Dhttp.proxyPort=").append(httpProxyConfig.port).append(" ");
			}
			if(httpProxyConfig.userId != null){
				sb.append("-Dhttp.proxyUser=").append(httpProxyConfig.userId).append(" ");
			}
			if(httpProxyConfig.password != null){
				sb.append("-Dhttp.proxyPassword=").append(httpProxyConfig.password).append(" ");
			}
			
			// https proxy
			if(httpsProxyConfig.host != null){
				sb.append("-Dhttps.proxyHost=").append(httpsProxyConfig.host).append(" ");
			}
			if(httpsProxyConfig.port >= 0){
				sb.append("-Dhttps.proxyPort=").append(httpsProxyConfig.port).append(" ");
			}
			if(httpsProxyConfig.userId != null){
				sb.append("-Dhttps.proxyUser=").append(httpsProxyConfig.userId).append(" ");
			}
			if(httpsProxyConfig.password != null){
				sb.append("-Dhttps.proxyPassword=").append(httpsProxyConfig.password).append(" ");
			}
			
			return sb.toString();
		
		} catch(Exception ex) {
			SbtPlugin.logException(ex);
			return "";
		}
	}
}
