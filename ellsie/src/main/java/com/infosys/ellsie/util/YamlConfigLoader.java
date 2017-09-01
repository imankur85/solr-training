/**
 * 
 */
package com.infosys.ellsie.util;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * @author Ankur_Jain22
 * 
 * Reads YAML config file
 *
 */
public class YamlConfigLoader implements ConfigLoader {

	private String filePath;
	
	private Configuration config;
	
	private static final Logger LOG = LoggerFactory.getLogger(YamlConfigLoader.class); 

	public YamlConfigLoader(String filePath) {
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.infosys.ellsie.util.ConfigLoader#getConfig()
	 */
	@Override
	public Configuration getConfig() {
		Yaml yaml = new Yaml();
		try (InputStream in = getClass().getClassLoader().getResource(filePath).openStream()) {
			config = yaml.loadAs(in, Configuration.class);
		} catch (IOException e) {
			LOG.error("Error reading config file: {} {}", filePath, e);
		}

		return config;
	}

}
