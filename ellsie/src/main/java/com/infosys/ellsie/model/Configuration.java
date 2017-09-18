package com.infosys.ellsie.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public final class Configuration implements Serializable{
	
	private String readFilePath = "D:/arxiv/arxiv/";
	
	private int threadPoolSize = 2;
	
	private int fileBatchSize = 5000;
	
	private String writeFilePath = "d:/arxiv2";
	
	private String arxivURL = "export.arxiv.org";
	
	private String proxyHost = "";
	
	private int proxyPort = 80;
	
	private int commitInMs = 10000;

	private String sparkUrl;
	
	private String username;
	
	private String password;
	
	private String solrUrl;
	
	private String hdfsUrl;
	
	private int rddPartitions;
	
	private Map<String, List<String>> jobs;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReadFilePath() {
		return readFilePath;
	}

	public void setReadFilePath(String readFilePath) {
		this.readFilePath = readFilePath;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public int getFileBatchSize() {
		return fileBatchSize;
	}

	public void setFileBatchSize(int fileBatchSize) {
		this.fileBatchSize = fileBatchSize;
	}

	public String getWriteFilePath() {
		return writeFilePath;
	}

	public void setWriteFilePath(String writeFilePath) {
		this.writeFilePath = writeFilePath;
	}

	public String getArxivURL() {
		return arxivURL;
	}

	public void setArxivURL(String arxivURL) {
		this.arxivURL = arxivURL;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public int getCommitInMs() {
		return commitInMs;
	}

	public void setCommitInMs(int commitInMs) {
		this.commitInMs = commitInMs;
	}

	public String getSparkUrl() {
		return sparkUrl;
	}
	
	public void setSparkUrl(String url) {
		sparkUrl = url;
	}

	public Map<String, List<String>> getJobs() {
		return jobs;
	}

	public void setJobs(Map<String, List<String>> jobs) {
		this.jobs = jobs;
	}

	public String getSolrUrl() {
		return solrUrl;
	}

	public void setSolrUrl(String solrUrl) {
		this.solrUrl = solrUrl;
	}

	public String getHdfsUrl() {
		return hdfsUrl;
	}

	public void setHdfsUrl(String hdfsUrl) {
		this.hdfsUrl = hdfsUrl;
	}

	public int getRddPartitions() {
		return rddPartitions;
	}

	public void setRddPartitions(int rddPartitions) {
		this.rddPartitions = rddPartitions;
	}
	
}
