/**
 * 
 */
package com.infosys.ellsie.connection;

import org.apache.spark.sql.SparkSession;

import com.infosys.ellsie.util.ConfigLoaderFactory;
import com.infosys.ellsie.util.Configuration;

/**
 * @author Ankur_Jain22
 *
 */
public class SparkConnection {
	
	private SparkSession sparkSession ;
	
	private Configuration config = ConfigLoaderFactory.getConfig();
	
	public SparkConnection() {
		setSparkSession(SparkSession.builder()
				.appName("Arxiv Training")
				.master(config.getSparkUrl())
				.getOrCreate());
	}

	public SparkSession getSparkSession() {
		return sparkSession;
	}

	public void setSparkSession(SparkSession sparkSession) {
		this.sparkSession = sparkSession;
	}
	
}
