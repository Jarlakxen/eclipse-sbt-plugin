package com.github.jarlakxen.scala.sbt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SbtParser {

	public static List<SbtSetting> parse(String source) {
		List<SbtSetting> settings = new ArrayList<SbtSetting>();

		String[] lines = source.split("[\\r?\\n]+");
		for (int i = 0; i < lines.length; i++) {
			String line = fixWhitespace(removeComment(lines[i]));
			if (StringUtils.isBlank(line)) {
				SbtSetting setting = parseProperty(line.toString());
				if (setting != null) {
					setting.line = i;
					settings.add(setting);
				}

			}
		}

		return settings;
	}

	private static SbtSetting parseProperty(String line) {
		SbtSetting property = createProperty(line, ":=");
		if (property == null) {
			property = createProperty(line, "++=");
		}
		if (property == null) {
			property = createProperty(line, "+=");
		}
		if (property == null) {
			property = new SbtSetting(line.trim());
		}
		return property;
	}

	private static SbtSetting createProperty(String line, String separator) {
		int index = line.indexOf(separator);
		if (index >= 0) {
			String key = line.substring(0, index).trim();
			String value = line.substring(index + separator.length()).trim();
			String[] values = parseSeq(value);
			if (values == null) {
				return new SbtSetting(key, value);
			} else {
				return new SbtSetting(key, values);
			}
		}
		return null;
	}

	private static String[] parseSeq(String value) {
		if (value.startsWith("Seq")) {
			value = value.replaceFirst("^Seq\\s*\\(", "");
			value = value.replaceFirst("\\)$", "");
			String[] values = value.split(",");
			for (int i = 0; i < values.length; i++) {
				values[i] = values[i].trim();
				values[i] = values[i].replaceAll("\\s+", " ");
			}
			return values;
		}
		return null;
	}

	/**
	 * utility method to remove comment.
	 */
	private static String removeComment(String value) {
		return value.replaceFirst("\\/\\/.*", "");
	}

	/**
	 * utility method to fix whitespaces to a single space.
	 */
	private static String fixWhitespace(String value) {
		return value.trim().replaceAll("\\s+", " ");
	}

	public static class SbtSetting {

		public int line;
		public String key;
		public String value;
		public String[] values;

		public SbtSetting(String key) {
			this.key = key;
		}

		public SbtSetting(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public SbtSetting(String key, String[] values) {
			this.key = key;
			this.values = values;
		}

		@Override
		public String toString() {
			if (value != null) {
				return key + ": " + value;
			} else if (values != null) {
				StringBuilder sb = new StringBuilder();
				for (String value : values) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append("  " + value);
				}
				return key + ": \n" + sb.toString();
			} else {
				return key;
			}
		}
	}
}
