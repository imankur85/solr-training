/**
 * 
 */
package com.infosys.ellsie.data.parser;

import java.io.File;

import javax.xml.bind.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.infosys.ellsie.data.arxiv.ArXivType;
import com.infosys.ellsie.data.oaipmh.OAIPMHtype;

/**
 * Parse input xml to jaxb object
 * 
 * @author Ankur_Jain22
 *  
 */
public class XmlParser {
	
	private JAXBContext context;
	
	public XmlParser() throws JAXBException {
		context = JAXBContext.newInstance(OAIPMHtype.class, ArXivType.class);
	}
	
	@SuppressWarnings("unchecked")
	public OAIPMHtype xmlToObject (File xmlFile) throws JAXBException {
        Unmarshaller un = context.createUnmarshaller();
        JAXBElement<OAIPMHtype> oaiElement = (JAXBElement<OAIPMHtype>)un.unmarshal(xmlFile);
        return oaiElement.getValue();
	}

}
