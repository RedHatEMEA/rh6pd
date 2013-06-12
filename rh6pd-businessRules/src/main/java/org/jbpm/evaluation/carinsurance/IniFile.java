package org.jbpm.evaluation.carinsurance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class IniFile {
	private File file;
	private FileReader reader;
	private FileWriter writer;
	private final HashMap<String, String> values = new HashMap<String, String>();

	public void close() throws Exception {
		if (this.reader != null) {
			this.reader.close();
		}

		if (this.writer != null) {
			this.writer.close();
		}
	}

	public int getInt(String key) {
		return Integer.parseInt(this.values.get(key));
	}

	public String getString(String key) {
		return this.values.get(key);
	}

	public void load() throws Exception {
		this.values.clear();

		this.reader = new FileReader(this.file);

		BufferedReader br = new BufferedReader(this.reader);
		String line;

		while ((line = br.readLine()) != null) {
			String[] parts = line.split("=", 2);

			this.values.put(parts[0], parts[1]);
		}
	}

	public void open(String filename) throws Exception {
		this.file = new File(filename);

		if (!this.file.exists()) {
			this.file.createNewFile();
		}

		this.load();
	}

	public void putInt(String key, int i) {
		this.values.put(key, Integer.toString(i));
	}

	public void putString(String key, String value) {
		this.values.put(key, value);
	}

	public void save() throws Exception {
		this.writer = new FileWriter(this.file, false);

		for (String k : this.values.keySet()) {
			this.writer.write(k + "=" + this.values.get(k));
		}

		this.writer.flush();

	}

}
