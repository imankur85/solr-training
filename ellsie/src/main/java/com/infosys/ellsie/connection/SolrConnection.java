package com.infosys.ellsie.connection;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;

public enum SolrConnection {
	INSTANCE;
	//private String urlString = "http://localhost:8983/solr/ellsie-training";
	private String urlString = "http://localhost:8983/solr/arxiv-training";
	private HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
	
	SolrConnection() {
		solr.setParser(new XMLResponseParser());
	}
	
	public HttpSolrClient getSolr() {
		return solr;
	}
	public void setSolr(HttpSolrClient solr) {
		this.solr = solr;
	}

}
