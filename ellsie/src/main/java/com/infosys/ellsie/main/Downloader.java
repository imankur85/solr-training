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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.connection.ArxivConnection;
import com.infosys.ellsie.data.oaipmh.HeaderType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;
import com.infosys.ellsie.util.ConfigLoaderFactory;
import com.infosys.ellsie.util.Configuration;

/**
 * @author Ankur_Jain22
 *
 */
public class Downloader {

	private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);
	

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
		if (args.length != 3) {
			LOG.error("Please provide config file, username and password as cmd line arguments.");
		} else {
			// load config
			final Configuration config = ConfigLoaderFactory.loadConfig(args[0]);
			ArxivConnection arconn = new ArxivConnection(args[1], args[2]);
			String content = arconn.listIdentifiers(toDate, new Date());
			Files.write(Paths.get("d:/arxiv2/identifiers.xml"), content.getBytes(StandardCharsets.UTF_8));

			// parse headers

			XmlParser parser = new XmlParser();
			OAIPMHtype oai = parser.xmlToObject(new File("d:/arxiv2/identifiers.xml"));
			System.out.println(oai.getListIdentifiers().getHeader().size());

			// Download metadata for each identifier
			for (HeaderType header : oai.getListIdentifiers().getHeader()) {
				String identifier = header.getIdentifier();
				String record = arconn.getRecord(identifier);
				Files.write(Paths.get(config.getWriteFilePath(), identifier.replaceAll(":", "_").replaceAll("/", "_") + ".xml"),
						record.getBytes(StandardCharsets.UTF_8));
			}
		}

	}

}
