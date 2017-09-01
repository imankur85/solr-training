package com.infosys.ellsie.main;
import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.NamedList;

import com.infosys.ellsie.document.FileDocument;
import com.infosys.ellsie.index.SolrIndexHelper;

/**
 * Main program to run
 * @author Ankur_Jain22
 *
 */
public class RunSolr {
	
	public static void main (String ar[]) {
		
		FileDocument file = new FileDocument();
		SolrIndexHelper index = new SolrIndexHelper();
		
		try {
			ContentStreamUpdateRequest createDoc = file.createDoc(new File("NLPSearch_Mentoring_scopeV1.pdf"), "pdf");
			System.out.println(createDoc.getPath());
			NamedList<Object> addFileDocument = index.addFileDocument(createDoc);
			System.out.println(addFileDocument);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
