package com.infosys.ellsie.index;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
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

public class SolrIndex {
	private static final Logger LOG = LoggerFactory.getLogger(SolrIndex.class);
	private HttpSolrClient solrClient = SolrConnection.INSTANCE.getSolr();

	public SolrIndex(ArxivSchema schema) throws SolrServerException, IOException {
		schema.getAllfields().stream().forEach(field -> {
			try {
				LOG.info("field updated: {}" , createMetadataSchema(field));
			} catch (SolrServerException | IOException e) {
				LOG.error(e.getMessage(), e);
			}
		});
	}

	public SolrIndex() {
	}

	private UpdateResponse createMetadataSchema(Map<String, Object> fieldAttribs)
			throws SolrServerException, IOException {
		SchemaRequest.ReplaceField replaceFieldUpdateSchemaRequest = new SchemaRequest.ReplaceField(fieldAttribs);
		SchemaResponse.UpdateResponse addFieldResponse = replaceFieldUpdateSchemaRequest.process(solrClient);
		return addFieldResponse;
	}

	public NamedList<Object> addFileDocument(ContentStreamUpdateRequest stream)
			throws SolrServerException, IOException {
		//System.out.println(solrClient.ping());
		NamedList<Object> result = solrClient.request(stream);
		return result;
	}

	public void addArxMetadata(ArXivType arx) throws SolrServerException, IOException {
		SolrInputDocument solrDoc = new SolrInputDocument();
		
		arx.getIdOrCreatedOrUpdated().parallelStream()
		.forEach(element -> {
			if (element.getName().getLocalPart().equals("authors")) {
				AuthorsType authors = (AuthorsType) element.getValue();
				for (AuthorType author : authors.getAuthor()) {
					solrDoc.addField("author_keyname", author.getKeyname());
					solrDoc.addField("author_forenames", author.getForenames());
				}
			}// multi-value type
			else if (element.getName().getLocalPart().equals("msc-class")) {
				// split and remove whitespace
				List<String> values = Arrays.asList(element.getValue().toString().split("\\s*,\\s*"));
				solrDoc.addField(element.getName().getLocalPart(), values);
			} else {
				solrDoc.addField(element.getName().getLocalPart(), element.getValue());
			}
		});
		
		LOG.info("Added: {}" , solrClient.add(solrDoc));
		solrClient.commit();
	}

}
