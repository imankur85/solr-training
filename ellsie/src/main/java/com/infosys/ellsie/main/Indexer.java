/**
 * 
 */
package com.infosys.ellsie.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.data.arxiv.ArXivType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;
import com.infosys.ellsie.index.ArxivSchema;
import com.infosys.ellsie.index.SolrIndex;

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
	 */
	public static void main(String[] args) throws JAXBException, SolrServerException, IOException {

		// index all meta data XMLs
		XmlParser parser = new XmlParser();
		SolrIndex index = new SolrIndex(new ArxivSchema());
		// FileDocument fileDoc = new FileDocument();

		Files.list(Paths.get("d:/sundeep/arxiv/")).parallel().forEach(path -> {
			try {
				path.
				indexListRecordViaSolrDoc(parser, index, path.toFile());
			} catch (JAXBException e) {
				LOG.error(e.getMessage(), e);
			}
		});

		/*
		 * for (File xmlFile : Files.list(Paths.get("d:/sundeep/arxiv/"))) {
		 * //indexGetRecordViaSolrDoc(parser, index, xmlFile);
		 * indexListRecordViaSolrDoc(parser, index, xmlFile); //
		 * indexViaStream(fileDoc, index, xmlFile); }
		 */

	}

	@SuppressWarnings("unchecked")
	private static void indexListRecordViaSolrDoc(XmlParser parser, SolrIndex index, File xmlFile)
			throws JAXBException {

		OAIPMHtype oaiType = parser.xmlToObject(xmlFile);
		oaiType.getListRecords().getRecord().parallelStream().forEach(record -> {
			ArXivType arx = ((JAXBElement<ArXivType>) (record.getMetadata().getAny())).getValue();
			try {
				LOG.info("Adding file: {} ", xmlFile.getName());
				index.addArxMetadata(arx);
			} catch (SolrServerException | IOException e) {
				LOG.error(e.getMessage(), e);
			}
		});

	}

	/*
	 * private static void indexViaStream(FileDocument fileDoc, SolrIndex index,
	 * File xmlFile) throws IOException, SolrServerException {
	 * ContentStreamUpdateRequest createDoc = fileDoc.createDoc(xmlFile, "xml");
	 * NamedList<Object> response = index.addFileDocument(createDoc);
	 * System.out.println("Added: " + xmlFile.getName() + response); }
	 */

	@SuppressWarnings("unused")
	private static void indexGetRecordViaSolrDoc(XmlParser parser, SolrIndex index, File xmlFile)
			throws JAXBException, SolrServerException, IOException {
		OAIPMHtype oaiType = parser.xmlToObject(xmlFile);
		@SuppressWarnings("unchecked")
		ArXivType arx = ((JAXBElement<ArXivType>) (oaiType.getGetRecord().getRecord().getMetadata().getAny()))
				.getValue();

		LOG.info("Adding file: {} ", xmlFile.getName());
		index.addArxMetadata(arx);
	}

}
