package com.infosys.ellsie.util;

import com.infosys.ellsie.model.ConfigLoader;
import com.infosys.ellsie.model.Configuration;

public class DefaultConfigLoader implements ConfigLoader {

	@Override
	public Configuration getConfig() {
		return new Configuration();
	}

}
