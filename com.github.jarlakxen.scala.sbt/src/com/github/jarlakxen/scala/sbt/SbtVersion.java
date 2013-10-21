package com.github.jarlakxen.scala.sbt;

/**
 * The enumeration type to express the SBT version.
 * 
 * @author Facundo Viale
 */
public enum SbtVersion {

	/**
	 * SBT 0.11
	 */
	SBT011("0.11", "0.11.3-2"),

	/**
	 * SBT 0.12
	 */
	SBT012("0.12", "0.12.4"),

	/**
	 * SBT 0.13
	 */
	SBT013("0.13", "0.13.0");

	public static SbtVersion getDefault() {
		return SBT013;
	}

	public static SbtVersion getVersion(String version) {
		for (SbtVersion value : values()) {
			if (version.startsWith(value.prefix)) {
				return value;
			}
		}
		return null;
	}

	private String prefix;
	private String lastVersion;

	private SbtVersion(String prefix, String lastVersion) {
		this.prefix = prefix;
		this.lastVersion = lastVersion;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getLastVersion() {
		return lastVersion;
	}

	public String getLauncherJarName() {
		return "sbt-launch-" + lastVersion + ".jar";
	}
}
