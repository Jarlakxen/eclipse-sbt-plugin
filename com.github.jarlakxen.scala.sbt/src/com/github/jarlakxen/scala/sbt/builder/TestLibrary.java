package com.github.jarlakxen.scala.sbt.builder;

import java.util.HashMap;
import java.util.Map;

import com.github.jarlakxen.scala.sbt.ScalaVersion;

public enum TestLibrary {
	SPECS2 {
		@Override
		protected void populate(Map<String, String> dependancyMap) {
			dependancyMap.put("2.10", "\"org.specs2\" %% \"specs2\" % \"2.2.3\" % \"test\"");
			dependancyMap.put("2.9", "\"org.specs2\" %% \"specs2\" % \"1.12.4.1\" % \"test\"");
		}
	};

	private Map<String, String> dependancyMap;

	private TestLibrary() {
		dependancyMap = new HashMap<String, String>();
		populate(dependancyMap);
	}

	protected abstract void populate(Map<String, String> dependancyMap);

	public String getDependancy(ScalaVersion version) {
		if(dependancyMap.containsKey(version.getText())){
			return dependancyMap.get(version.getText());
		}
		
		return dependancyMap.get(version.getBaseVersion());
	}

}