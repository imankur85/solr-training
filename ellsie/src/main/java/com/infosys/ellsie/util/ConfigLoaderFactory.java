/**
 * 
 */
package com.infosys.ellsie.util;

import com.infosys.ellsie.model.ConfigLoader;
import com.infosys.ellsie.model.Configuration;

/**
 * @author Ankur_Jain22
 *
 */
public class ConfigLoaderFactory {
	
	
	private static ConfigLoader configLoader  = new DefaultConfigLoader();

	public static Configuration loadConfig(String filePath) {
		if (filePath.toLowerCase().endsWith(".yml")) {
			configLoader = new YamlConfigLoader(filePath);
		}
		
		return configLoader.getConfig();
	}
	
	public static Configuration getConfig() {
		return configLoader.getConfig();
	}
}
