package com.infosys.ellsie.index;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.spark.api.java.JavaFutureAction;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.connection.SparkConnection;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;

public class SparkHelper {

	private SparkSession sparkSession = new SparkConnection().getSparkSession();
	private static final Logger LOG = LoggerFactory.getLogger(SparkHelper.class);
	private XmlParser parser = new XmlParser();
	private SolrIndexHelper solrHelper = new SolrIndexHelper();


	public void processXml() throws SolrServerException, IOException, JAXBException, InterruptedException, ExecutionException {

		List<String> xmlList = sparkSession.sparkContext()
				.wholeTextFiles("hdfs://10.188.49.222:9000/arxiv_data/arxiv/*init.xml", 1)
				.toJavaRDD()
				.mapPartitions( iter -> {
					String content = iter.next()._2;
					return Arrays.asList(content).iterator();

				})
				//.top(10);
				.collectAsync()
				.get();

		sparkSession.stop();
		
		
		xmlList.parallelStream()
		.forEach(content -> {
			OAIPMHtype oai = null;
			try {
				oai = parser.xmlToObject(content);
			} catch (JAXBException e) {
				LOG.error(e.getMessage(), e);
			}
			try {
				solrHelper.processOAIType(oai);
			} catch (SolrServerException | IOException e) {
				LOG.error(e.getMessage(), e);
			}
		});

		// check and commit if docs are remaining
		if (!solrHelper.getSolrDocs().isEmpty()) {
			solrHelper.commitDocs(solrHelper.getSolrDocs());
		}

	}

}
