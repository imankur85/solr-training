package com.infosys.ellsie.document;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;

public class FileDocument {
	
	
	public ContentStreamUpdateRequest createDoc(File file, String type) throws IOException {
		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
		req.addFile(file, type);
		req.setParam("literal.id", file.getName());
		req.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
		return req;
	}

}
