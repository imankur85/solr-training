package com.infosys.ellsie.model.tasks;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.model.Configuration;
import com.infosys.ellsie.model.Task;
import com.infosys.ellsie.util.ConfigLoaderFactory;
import com.infosys.ellsie.util.SparkHelper;

public class DataProcessor implements Task {
	
	private static final long serialVersionUID = 1L;
	//private FileHelper fileHelper = new FileHelper();
	private SparkHelper sparkHelper = new SparkHelper();
	private static final Logger LOG = LoggerFactory.getLogger(DataProcessor.class);
	private Configuration config = ConfigLoaderFactory.getConfig();


	@Override
	public void executeTask() {
		
		try {
			sparkHelper.processXml(config);
		} catch (SolrServerException | IOException | JAXBException | InterruptedException | ExecutionException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public String getTaskName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTaskName(String name) {
		// TODO Auto-generated method stub

	}

}
