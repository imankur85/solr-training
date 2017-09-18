package com.infosys.ellsie.model.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.connection.ArxivConnection;
import com.infosys.ellsie.data.oaipmh.HeaderType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;
import com.infosys.ellsie.data.parser.XmlParser;
import com.infosys.ellsie.model.Configuration;
import com.infosys.ellsie.model.Task;
import com.infosys.ellsie.util.ConfigLoaderFactory;

public class Downloader implements Task {

	private final Configuration config = ConfigLoaderFactory.getConfig();
	private ArxivConnection arconn = new ArxivConnection(config.getUsername(), config.getPassword());
	private Date toDate;
	private XmlParser parser = XmlParser.getInstance();
	private OAIPMHtype oai;
	private static final Logger LOG = LoggerFactory.getLogger(Downloader.class);
	
	private String taskName = "DownloadTask";

	@Override
	public void executeTask() {
		try {

			toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01");

			String content;

			content = arconn.listIdentifiers(toDate, new Date());

			Files.write(Paths.get("d:/arxiv2/identifiers.xml"), content.getBytes(StandardCharsets.UTF_8));

			// parse headers

			oai = parser.xmlToObject(new File("d:/arxiv2/identifiers.xml"));

			// System.out.println(oai.getListIdentifiers().getHeader().size());

			// Download metadata for each identifier
			for (HeaderType header : oai.getListIdentifiers().getHeader()) {
				String identifier = header.getIdentifier();
				String record;

				record = arconn.getRecord(identifier);

				Files.write(
						Paths.get(config.getWriteFilePath(),
								identifier.replaceAll(":", "_").replaceAll("/", "_") + ".xml"),
						record.getBytes(StandardCharsets.UTF_8));

			}
		} catch (ParseException | JAXBException | IOException e) {
			
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);

		}

	}

	@Override
	public String getTaskName() {
		return taskName;
	}

	@Override
	public void setTaskName(String name) {
		taskName = name;
	}

}
