/**
 * 
 */
package com.infosys.ellsie.data.parser;

import java.io.File;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infosys.ellsie.data.arxiv.ArXivType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;

/**
 * Parse input xml to jaxb object
 * 
 * @author Ankur_Jain22
 *  
 */
public class XmlParser {
	private static final Logger LOG = LoggerFactory.getLogger(XmlParser.class);
	private static JAXBContext context;
	
	static {
		try {
			context = JAXBContext.newInstance(OAIPMHtype.class, ArXivType.class);
		} catch (JAXBException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public OAIPMHtype xmlToObject (File xmlFile) throws JAXBException {
        Unmarshaller un = context.createUnmarshaller();
        JAXBElement<OAIPMHtype> oaiElement = (JAXBElement<OAIPMHtype>)un.unmarshal(xmlFile);
        return oaiElement.getValue();
	}

	public OAIPMHtype xmlToObject(String content) throws JAXBException {
		Unmarshaller un = context.createUnmarshaller();
		StringReader reader = new StringReader(content);
        JAXBElement<OAIPMHtype> oaiElement = (JAXBElement<OAIPMHtype>)un.unmarshal(reader);
        return oaiElement.getValue();
	}

}
