/**
 * 
 */
package com.infosys.ellsie.connection;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import com.infosys.ellsie.model.Configuration;
import com.infosys.ellsie.util.ConfigLoaderFactory;

/**
 * @author Ankur_Jain22
 *
 */
public class SparkConnection {
	
	private SparkConf sparkConf ;
	private JavaSparkContext javaSparkSc; 
	
	private Configuration config = ConfigLoaderFactory.getConfig();
	
	public SparkConnection() {
		sparkConf = new SparkConf().setAppName("Arxiv Training").setMaster(config.getSparkUrl());
		javaSparkSc = new JavaSparkContext(sparkConf);
	}

	public JavaSparkContext getSparkSession() {
		return javaSparkSc;
	}

	public void setSparkSession(JavaSparkContext javaSparkSc) {
		this.javaSparkSc = javaSparkSc;
	}
	
}
