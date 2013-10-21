package com.github.jarlakxen.scala.sbt;

import com.github.jarlakxen.scala.sbt.wizard.create.SbtWizard;

/**
 * TemplateBuilder for SBT files
 * 
 * @see SbtWizard
 * @author Facundo Viale
 */
public class TemplateBuilder {
	private static final String BR = System.getProperty("line.separator");

	// Template for build.sbt
	public static SbtTemplateBuilder createSbtTemplate() {
		return new SbtTemplateBuilder();
	}

	public static class SbtTemplateBuilder {
		private ScalaVersion scalaVersion;
		private String organization;
		private String projectName;
		private String productVersion;
		private boolean webNature = false;

		private SbtTemplateBuilder() {

		}

		public SbtTemplateBuilder scalaVersion(ScalaVersion scalaVersion) {
			this.scalaVersion = scalaVersion;
			return this;
		}

		public SbtTemplateBuilder organization(String organization) {
			this.organization = organization;
			return this;
		}

		public SbtTemplateBuilder projectName(String projectName) {
			this.projectName = projectName;
			return this;
		}

		public SbtTemplateBuilder productVersion(String productVersion) {
			this.productVersion = productVersion;
			return this;
		}

		public SbtTemplateBuilder webNature(boolean webNature) {
			this.webNature = webNature;
			return this;
		}

		public String build() {

			StringBuilder builder = new StringBuilder();

			builder.append("name := \"").append(projectName).append("\"").append(BR).append(BR);
			builder.append("organization := \"").append(organization).append("\"").append(BR).append(BR);
			builder.append("version := \"").append(productVersion).append("\"").append(BR).append(BR);
			builder.append("scalaVersion := \"").append(scalaVersion.getText()).append("\"").append(BR).append(BR);

			builder.append("scalacOptions <++= scalaVersion map { v =>").append(BR);
			builder.append("\tif (v.startsWith(\"2.10\"))").append(BR);
			builder.append(
					"\t\tSeq(\"-unchecked\", \"-deprecation\", \"-feature\", \"-language:implicitConversions\", \"-language:postfixOps\", \"-language:existentials\" )")
					.append(BR);
			builder.append("\telse").append(BR);
			builder.append("\t\tSeq(\"-unchecked\", \"-deprecation\" )").append(BR);
			builder.append("}").append(BR).append(BR);

			builder.append("EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource").append(BR).append(BR);
			builder.append("EclipseKeys.withSource := true").append(BR).append(BR);

			if (webNature) {
				builder.append("seq(webSettings :_*)").append(BR).append(BR);
			}

			builder.append("libraryDependencies ++= Seq(");
			if (webNature) {
				builder.append(BR).append("\"org.mortbay.jetty\" % \"jetty\" % \"6.1.22\" % \"container\"").append(BR);
			}
			builder.append(")").append(BR).append(BR);

			return builder.toString();
		}
	}

	// Template for project/build.properties
	public static SbtPropertiesTemplateBuilder createSbtPropertiesTemplate() {
		return new SbtPropertiesTemplateBuilder();
	}

	public static class SbtPropertiesTemplateBuilder {
		private SbtVersion sbtVersion;

		private SbtPropertiesTemplateBuilder() {

		}

		public SbtPropertiesTemplateBuilder sbtVersion(SbtVersion sbtVersion) {
			this.sbtVersion = sbtVersion;
			return this;
		}

		public String build() {
			return "sbt.version=" + sbtVersion.getLastVersion();
		}
	}

	// Template for project/plugins.sbt
	public static SbtPluginsTemplateBuilder createSbtPluginsTemplate() {
		return new SbtPluginsTemplateBuilder();
	}

	public static class SbtPluginsTemplateBuilder {

		private boolean webNature = false;

		private SbtPluginsTemplateBuilder() {

		}

		public SbtPluginsTemplateBuilder webNature(boolean webNature) {
			this.webNature = webNature;
			return this;
		}

		public String fill(String pluginsFile) {
			String pluginsFileLines[] = pluginsFile.split("[\\r?\\n]+");

			for (String necesaryLines : build().split("[\\r?\\n]+")) {
				boolean isNecesaryLinePresent = false;
				for (String currentLine : pluginsFileLines) {
					if (currentLine.startsWith(necesaryLines.substring(0, necesaryLines.lastIndexOf("%")))) {
						isNecesaryLinePresent = true;
						break;
					}
				}

				if (isNecesaryLinePresent) {
					pluginsFile = pluginsFile + BR + BR + necesaryLines;
				}
			}

			return pluginsFile;
		}

		public String build() {
			StringBuilder builder = new StringBuilder();

			builder.append("addSbtPlugin(\"com.timushev.sbt\" % \"sbt-updates\" % \"0.1.2\")");
			builder.append(BR);
			builder.append(BR);
			builder.append("addSbtPlugin(\"com.typesafe.sbteclipse\" % \"sbteclipse-plugin\" % \"2.3.0\")");

			if (webNature) {
				builder.append(BR);
				builder.append(BR);
				builder.append("addSbtPlugin(\"com.earldouglas\" % \"xsbt-web-plugin\" % \"0.4.2\")");
			}

			return builder.toString();
		}
	}
}
