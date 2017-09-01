package com.infosys.ellsie.util;

public final class Configuration {
	
	private String readFilePath = "D:/arxiv/arxiv/";
	
	private int threadPoolSize = 2;
	
	private int fileBatchSize = 5000;
	
	private String writeFilePath = "d:/arxiv2";
	
	private String arxivURL = "export.arxiv.org";
	
	private String proxyHost = "";
	
	private int proxyPort = 80;
	
	private int commitInMs = 10000;

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
	
}
