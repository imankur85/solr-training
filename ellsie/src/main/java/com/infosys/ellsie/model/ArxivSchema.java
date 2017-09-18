/**
 * 
 */
package com.infosys.ellsie.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solr schema for arxive
 * 
 * @author Ankur_Jain22
 *
 */
public class ArxivSchema {

	private static final Map<String, Object> MSC_CLASS = Collections
			.unmodifiableMap(Stream.of(new SimpleEntry<>("name", "msc-class"), new SimpleEntry<>("type", "strings"))
					.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));
	
	public List<Map<String, Object>> getAllfields() {
		return Arrays.asList(MSC_CLASS);
	}
	
}
