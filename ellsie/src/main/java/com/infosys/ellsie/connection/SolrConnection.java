package com.infosys.ellsie.connection;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public enum SolrConnection {
	INSTANCE;
	private HttpSolrClient solr = new HttpSolrClient.Builder().withBaseSolrUrl("").build();
	//private ConcurrentUpdateSolrClient solr = new ConcurrentUpdateSolrClient.Builder(urlString).withQueueSize(20000).withThreadCount(10).build();
	
	public SolrClient getSolr(String url) {
		solr.setBaseURL(url);
		solr.setParser(new XMLResponseParser());
		solr.setMaxTotalConnections(4);
		return solr;
	}
	
}
