package com.infosys.ellsie.connection;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public enum SolrConnection {
	INSTANCE;
	private String urlString = "http://localhost:8983/solr/arxiv-training";
	private HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
	//private ConcurrentUpdateSolrClient solr = new ConcurrentUpdateSolrClient.Builder(urlString).withQueueSize(20000).withThreadCount(10).build();
	
	SolrConnection() {
		solr.setParser(new XMLResponseParser());
		solr.setMaxTotalConnections(4);
	}
	
	public SolrClient getSolr() {
		return solr;
	}
	

}
