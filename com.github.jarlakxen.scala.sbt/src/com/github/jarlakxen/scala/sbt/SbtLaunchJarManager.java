package com.github.jarlakxen.scala.sbt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * Manages built-in SNT runtime.
 * 
 * @author Facundo Viale
 */
public class SbtLaunchJarManager {

	public static final Map<SbtVersion, File> SBT_JAR_FILES;

	static {
		File dir = SbtPlugin.getDefault().getStateLocation().toFile();
		
		Map<SbtVersion, File> sbtJarFiles = new HashMap<SbtVersion, File>( SbtVersion.values().length);
		
		for(SbtVersion version : SbtVersion.values()){
			sbtJarFiles.put(version, new File(dir, version.getLauncherJarName()));
		}
		
		SBT_JAR_FILES = Collections.unmodifiableMap(sbtJarFiles);
	}

	/**
	 * Copy built-in SBT runtime to the plugin state location when the
	 * {@link SbtPlugin} is started.
	 * 
	 * @throws CoreException
	 */
	public static void deploy() throws CoreException {
		Bundle bundle = SbtPlugin.getDefault().getBundle();
		
		for( Entry<SbtVersion, File> entry : SBT_JAR_FILES.entrySet()){
			copyFile(bundle.getEntry("/sbt/" + entry.getKey().getPrefix() + "/" + entry.getKey().getLauncherJarName()), entry.getValue());
		}
	}

	public static File getLauncher(SbtVersion version) {
		return SBT_JAR_FILES.get(version);
	}

	/**
	 * Delete built-in SBT runtime from plugin state location when the
	 * {@link SbtPlugin} is shutdown.
	 */
	public static void undeploy() {
		for( Entry<SbtVersion, File> entry : SBT_JAR_FILES.entrySet()){
			entry.getValue().delete();
		}
	}

	private static void copyFile(URL url, File file) throws CoreException {
		try {
			InputStream in = url.openStream();
			OutputStream out = new FileOutputStream(file);
			try {
				byte[] buf = new byte[1024 * 8];
				int length = 0;
				while ((length = in.read(buf)) != -1) {
					out.write(buf, 0, length);
				}
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		} catch (Exception ex) {
			IStatus status = new Status(IStatus.ERROR, SbtPlugin.PLUGIN_ID, 0, ex.toString(), ex);
			throw new CoreException(status);
		}
	}

}
