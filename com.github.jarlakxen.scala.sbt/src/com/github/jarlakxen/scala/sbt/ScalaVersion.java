package com.github.jarlakxen.scala.sbt;

public enum ScalaVersion {
	V_2_10_3("2.10.3"),
	V_2_10_2("2.10.2"),
	V_2_10_1("2.10.1"),
	V_2_9_3("2.9.3"),
	V_2_9_2("2.9.2");

	public static ScalaVersion getDefault() {
		return V_2_10_3;
	}
	
	public static ScalaVersion versionOf(String text){
		for(ScalaVersion version : values()){
			if(version.getText().equals(text)){
				return version;
			}
		}
		return null;
	}

	private final String text;

	private ScalaVersion(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
