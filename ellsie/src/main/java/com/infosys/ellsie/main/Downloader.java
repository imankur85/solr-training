package com.infosys.ellsie.main;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.solr.client.solrj.SolrServerException;

import com.infosys.ellsie.connection.ArxivConnection;
import com.infosys.ellsie.data.oaipmh.HeaderType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;

/**
 * @author Ankur_Jain22
 *
 */
public class Downloader {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws SolrServerException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, JAXBException, SolrServerException, ParseException {
		
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01");
		
		// Download headers
		ArxivConnection arconn = new ArxivConnection() ;
		String content = arconn.listIdentifiers(toDate, new Date());
		Files.write(Paths.get("d:/arxiv2/identifiers.xml"), content.getBytes(StandardCharsets.UTF_8) );
		
		// parse headers
		
		XmlParser parser = new XmlParser();
		OAIPMHtype oai =  parser.xmlToObject(new File("d:/arxiv2/identifiers.xml"));
		System.out.println(oai.getListIdentifiers().getHeader().size());
		
		// Download metadata for each identifier
		for (HeaderType header : oai.getListIdentifiers().getHeader()) {
			String identifier = header.getIdentifier();
			String record = arconn.getRecord (identifier);
			Files.write(Paths.get("d:/arxiv2", identifier.replaceAll(":", "_").replaceAll("/", "_") + ".xml" ), record.getBytes(StandardCharsets.UTF_8));
		}
		
	}

}
