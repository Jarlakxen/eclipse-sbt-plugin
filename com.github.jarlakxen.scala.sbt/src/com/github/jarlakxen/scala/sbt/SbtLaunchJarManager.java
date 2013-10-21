package com.github.jarlakxen.scala.sbt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * Manages built-in SNT runtime.
 * 
 * @author Naoki Takezoe
 */
public class SbtLaunchJarManager {

	/**
	 * built-in SBT 0.11 JAR file
	 */
	public static final File SBT011_JAR_FILE;

	/**
	 * built-in SBT 0.12 JAR file
	 */
	public static final File SBT012_JAR_FILE;

	/**
	 * built-in SBT 0.13 JAR file
	 */
	public static final File SBT013_JAR_FILE;

	static {
		File dir = SbtPlugin.getDefault().getStateLocation().toFile();
		SBT011_JAR_FILE = new File(dir, SbtVersion.SBT011.getLauncherJarName());
		SBT012_JAR_FILE = new File(dir, SbtVersion.SBT012.getLauncherJarName());
		SBT013_JAR_FILE = new File(dir, SbtVersion.SBT013.getLauncherJarName());
	}

	/**
	 * Copy built-in SBT runtime to the plugin state location when the
	 * {@link SbtPlugin} is started.
	 * 
	 * @throws CoreException
	 */
	public static void deploy() throws CoreException {
		Bundle bundle = SbtPlugin.getDefault().getBundle();
		copyFile(bundle.getEntry("/sbt/0.11/" + SbtVersion.SBT011.getLauncherJarName()), SBT011_JAR_FILE);
		copyFile(bundle.getEntry("/sbt/0.12/" + SbtVersion.SBT012.getLauncherJarName()), SBT012_JAR_FILE);
		copyFile(bundle.getEntry("/sbt/0.13/" + SbtVersion.SBT013.getLauncherJarName()), SBT013_JAR_FILE);
	}

	public static File getLauncher(SbtVersion version) {
		if (version == SbtVersion.SBT011) {
			return SbtLaunchJarManager.SBT011_JAR_FILE;
		} else if (version == SbtVersion.SBT012) {
			return SbtLaunchJarManager.SBT012_JAR_FILE;
		} else if (version == SbtVersion.SBT013) {
			return SbtLaunchJarManager.SBT013_JAR_FILE;
		}

		return null;
	}

	/**
	 * Delete built-in SBT runtime from plugin state location when the
	 * {@link SbtPlugin} is shutdown.
	 */
	public static void undeploy() {
		SBT011_JAR_FILE.delete();
		SBT012_JAR_FILE.delete();
		SBT013_JAR_FILE.delete();
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
