/**
 * 
 */
package com.infosys.ellsie.index;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
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

	public static final Map<String, Object> MSC_CLASS = Collections
			.unmodifiableMap(Stream.of(new SimpleEntry<>("name", "msc-class"), new SimpleEntry<>("type", "strings"))
					.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

}
