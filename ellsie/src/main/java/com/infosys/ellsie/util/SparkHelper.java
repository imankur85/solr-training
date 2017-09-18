package com.infosys.ellsie.util;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.connection.SparkConnection;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;
import com.infosys.ellsie.model.Configuration;

public class SparkHelper {

	private JavaSparkContext javaSparkSc = new SparkConnection().getSparkSession();
	private static final Logger LOG = LoggerFactory.getLogger(SparkHelper.class);

	public void processXml(Configuration config)
			throws SolrServerException, IOException, JAXBException, InterruptedException, ExecutionException {

		// List<String> xmlList =
		javaSparkSc.wholeTextFiles(config.getHdfsUrl(), config.getRddPartitions()).foreach(item -> {
			String content = item._2;
			// XmlParser parser = XmlParser.getInstance();
			OAIPMHtype oai = XmlParser.getInstance().xmlToObject(content);
			SolrIndexHelper solrHelper = new SolrIndexHelper(config);
			solrHelper.processOAIType(oai);
			// check and commit if docs are remaining
			if (!solrHelper.getSolrDocs().isEmpty()) {
				solrHelper.commitDocs(solrHelper.getSolrDocs());
			}
			// return Arrays.asList(content).iterator();

		});
		// .top(10);
		// .collect();

		javaSparkSc.stop();

		/*
		 * xmlList.parallelStream() .forEach(content -> { OAIPMHtype oai = null; try {
		 * oai = parser.xmlToObject(content); } catch (JAXBException e) {
		 * LOG.error(e.getMessage(), e); } try { solrHelper.processOAIType(oai); } catch
		 * (SolrServerException | IOException e) { LOG.error(e.getMessage(), e); } });
		 */

	}

}
