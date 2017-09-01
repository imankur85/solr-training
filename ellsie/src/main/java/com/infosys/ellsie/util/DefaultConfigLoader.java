package com.infosys.ellsie.util;

public class DefaultConfigLoader implements ConfigLoader {

	@Override
	public Configuration getConfig() {
		return new Configuration();
	}

}
