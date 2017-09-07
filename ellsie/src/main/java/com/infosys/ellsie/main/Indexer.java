/**
 * 
 */
package com.infosys.ellsie.main;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;

import com.infosys.ellsie.index.SparkHelper;
import com.infosys.ellsie.util.ConfigLoaderFactory;

/**
 * Main executor
 * 
 * @author Ankur_Jain22
 *
 */
public class Indexer {


	/**
	 * @param args
	 * @throws JAXBException
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static void main(String[] args)
			throws JAXBException, SolrServerException, IOException, InterruptedException, ExecutionException {

		// load config
		ConfigLoaderFactory.loadConfig(args[0]);

		//FileHelper fileHelper = new FileHelper();
		SparkHelper sparkHelper = new SparkHelper();
		
		// multi-threaded file reader
		//fileHelper.processXmlFiles();
		
		// spark hdfs reader
		sparkHelper.processXml();

	}


}
