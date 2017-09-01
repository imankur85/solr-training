/**
 * 
 */
package com.infosys.ellsie.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.data.arxiv.ArXivType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;
import com.infosys.ellsie.index.SolrIndexHelper;
import com.infosys.ellsie.util.ConfigLoaderFactory;
import com.infosys.ellsie.util.Configuration;

/**
 * Main executor
 * 
 * @author Ankur_Jain22
 *
 */
public class Indexer {

	private static final Logger LOG = LoggerFactory.getLogger(Indexer.class);

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
		final Configuration config = ConfigLoaderFactory.loadConfig(args[0]);

		XmlParser parser = new XmlParser();
		SolrIndexHelper index = new SolrIndexHelper();
		
		// multi-threaded fork join pool
		new ForkJoinPool(config.getThreadPoolSize()).submit(() -> {
			try {
				Files.list(Paths.get(config.getReadFilePath())).parallel().forEach(path -> {
					try {
						indexListRecordViaSolrDoc(parser, index, new File(path.toUri().getRawPath()));
					} catch (JAXBException | SolrServerException | IOException e) {
						LOG.error(e.getMessage(), e);
					}
				});
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}).get();

		// check and commit if docs are remaining
		if (!index.getSolrDocs().isEmpty()) {
			index.commitDocs(index.getSolrDocs());
		}

	}

	private static void indexListRecordViaSolrDoc(XmlParser parser, SolrIndexHelper index, File xmlFile) throws JAXBException, SolrServerException, IOException {
		
		OAIPMHtype oaiType = parser.xmlToObject(xmlFile);
		LOG.info("Adding file: {} ", xmlFile.getName());
		index.processOAIType(oaiType);
	}


	/*
	 * private static void indexViaStream(FileDocument fileDoc, SolrIndex index,
	 * File xmlFile) throws IOException, SolrServerException {
	 * ContentStreamUpdateRequest createDoc = fileDoc.createDoc(xmlFile, "xml");
	 * NamedList<Object> response = index.addFileDocument(createDoc);
	 * System.out.println("Added: " + xmlFile.getName() + response); }
	 */

	@SuppressWarnings("unused")
	private static void indexGetRecordViaSolrDoc(XmlParser parser, SolrIndexHelper index, File xmlFile)
			throws JAXBException, SolrServerException, IOException {
		OAIPMHtype oaiType = parser.xmlToObject(xmlFile);
		@SuppressWarnings("unchecked")
		ArXivType arx = ((JAXBElement<ArXivType>) (oaiType.getGetRecord().getRecord().getMetadata().getAny()))
				.getValue();

		LOG.info("Adding file: {} ", xmlFile.getName());
		index.addArxMetadata(arx);
	}

}
