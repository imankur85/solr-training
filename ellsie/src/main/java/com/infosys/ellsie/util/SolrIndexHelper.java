package com.infosys.ellsie.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.connection.SolrConnection;
import com.infosys.ellsie.data.arxiv.ArXivType;
import com.infosys.ellsie.data.arxiv.AuthorType;
import com.infosys.ellsie.data.arxiv.AuthorsType;
import com.infosys.ellsie.data.oaipmh.MetadataType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.model.ArxivSchema;
import com.infosys.ellsie.model.Configuration;

public class SolrIndexHelper {

	// static config loaded during start-up
	private Configuration config = ConfigLoaderFactory.getConfig();
	
	private static final Logger LOG = LoggerFactory.getLogger(SolrIndexHelper.class);
	private SolrClient solrClient ;
	private List<SolrInputDocument> solrDocs = new ArrayList<SolrInputDocument>();
	// private AtomicInteger counter = new AtomicInteger(0);

	public SolrIndexHelper(ArxivSchema schema) throws SolrServerException, IOException {
		schema.getAllfields().stream().forEach(field -> {
			try {
				LOG.info("field updated: {}", createMetadataSchema(field));
			} catch (SolrServerException | IOException e) {
				LOG.error(e.getMessage(), e);
			}
		});
	}

	public SolrIndexHelper() {
		// for local use
		solrClient = SolrConnection.INSTANCE.getSolr(config.getSolrUrl());
	}
	
	/**
	 * To be used with spark
	 * @param config
	 */
	public SolrIndexHelper(Configuration config) {
		this.config = config;
		solrClient = SolrConnection.INSTANCE.getSolr(config.getSolrUrl());
	}

	private UpdateResponse createMetadataSchema(Map<String, Object> fieldAttribs)
			throws SolrServerException, IOException {
		SchemaRequest.ReplaceField replaceFieldUpdateSchemaRequest = new SchemaRequest.ReplaceField(fieldAttribs);
		SchemaResponse.UpdateResponse addFieldResponse = replaceFieldUpdateSchemaRequest.process(solrClient);
		return addFieldResponse;
	}

	public NamedList<Object> addFileDocument(ContentStreamUpdateRequest stream)
			throws SolrServerException, IOException {
		// System.out.println(solrClient.ping());
		NamedList<Object> result = solrClient.request(stream);
		return result;
	}

	public void addArxMetadata(ArXivType arx) throws SolrServerException, IOException {
		SolrInputDocument solrDoc = new SolrInputDocument();

		arx.getIdOrCreatedOrUpdated().stream().forEach(element -> {

			final String localPart = element.getName().getLocalPart();

			if (localPart.equals("authors")) {
				processAuthors(solrDoc, element);

			} else if (localPart.equals("msc-class") || localPart.equals("report-no")) {
				// split and remove ,
				List<String> values = Arrays.asList(element.getValue().toString().split("\\s*,\\s*"));
				solrDoc.addField(localPart, values);
			} else if (localPart.equals("acm-class")) {
				// split and remove ;
				List<String> values = Arrays.asList(element.getValue().toString().split(";"));
				solrDoc.addField(localPart, values);
			} else {
				solrDoc.addField(localPart, element.getValue());
			}
		});

		solrDocs.add(solrDoc);

	}

	private void processAuthors(SolrInputDocument solrDoc, JAXBElement<?> element) {
		AuthorsType authors = (AuthorsType) element.getValue();

		List<String> keynames = new ArrayList<String>();
		List<String> forenames = new ArrayList<String>();
		List<String> affiliation = new ArrayList<String>();
		List<String> suffix = new ArrayList<String>();

		for (AuthorType author : authors.getAuthor()) {
			keynames.add(author.getKeyname());
			forenames.add(author.getForenames());
			affiliation.addAll(author.getAffiliation());
			suffix.add(author.getSuffix());
		}

		solrDoc.addField("author_keyname", keynames);
		solrDoc.addField("author_forenames", forenames);
		solrDoc.addField("author_affiliation", affiliation);
		solrDoc.addField("author_suffix", suffix);
	}

	public void commitDocs(List<SolrInputDocument> docs, int commitInMs) throws SolrServerException, IOException {
		LOG.info("Added: {}", solrClient.add(docs, commitInMs));
	}
	
	public void commitDocs(List<SolrInputDocument> docs) throws SolrServerException, IOException {
		LOG.info("Added: {}", solrClient.add(docs));
		LOG.info("Commit: {}", solrClient.commit().getStatus());
		solrClient.close();
	}

	public List<SolrInputDocument> getSolrDocs() {
		return solrDocs;
	}

	@SuppressWarnings("unchecked")
	public void processOAIType(OAIPMHtype oaiType) throws SolrServerException, IOException {
		// process records in each xml file
		oaiType.getListRecords().getRecord()
		.stream()
		.forEach(record -> {
			MetadataType metadata = record.getMetadata();
			if (null != metadata && null != metadata.getAny()) {
				ArXivType arx = ((JAXBElement<ArXivType>) (metadata.getAny())).getValue();
				try {
					addArxMetadata(arx);
				} catch (SolrServerException | IOException e) {
					LOG.error(e.getMessage(), e);
				}

			} else {
				LOG.error("skipped record: {}", record.getHeader().getIdentifier());
			}

		});

		// commit docs of each xml
		synchronized (solrDocs) {
			if (solrDocs.size() > config.getFileBatchSize()) {
				commitDocs(new ArrayList<SolrInputDocument>(solrDocs), config.getCommitInMs());
				solrDocs.clear();
			}
		}
	}
}
