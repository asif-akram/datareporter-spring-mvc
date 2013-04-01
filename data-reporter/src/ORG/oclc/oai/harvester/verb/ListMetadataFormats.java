
/**
 *Copyright (c) 2000-2002 OCLC Online Computer Library Center,
 *Inc. and other contributors. All rights reserved.  The contents of this file, as updated
 *from time to time by the OCLC Office of Research, are subject to OCLC Research
 *Public License Version 2.0 (the "License"); you may not use this file except in
 *compliance with the License. You may obtain a current copy of the License at
 *http://purl.oclc.org/oclc/research/ORPL/.  Software distributed under the License is
 *distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express
 *or implied. See the License for the specific language governing rights and limitations
 *under the License.  This software consists of voluntary contributions made by many
 *individuals on behalf of OCLC Research. For more information on OCLC Research,
 *please see http://www.oclc.org/oclc/research/.
 *
 *The Original Code is ListMetadataFormats.java______________________________.
 *The Initial Developer of the Original Code is Jeff Young.
 *Portions created by ______________________ are
 *Copyright (C) _____ _______________________. All Rights Reserved.
 *Contributor(s):______________________________________.
 */

package ORG.oclc.oai.harvester.verb;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import ORG.oclc.oai.util.OAIUtil;

/**
 * This class represents an ListMetadataFormats response on either the server or
 * on the client
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public class ListMetadataFormats extends HarvesterVerb {
    private static final boolean debug = false;

    private boolean metadataFormatCapture = false;
    private StringBuffer metadataFormat;
    private ArrayList metadataFormats = new ArrayList();

    /**
     * Client-side ListMetadataFormats verb constructor
     *
     * @param baseURL the baseURL of the server to be queried
     * @exception MalformedURLException the baseURL is bad
     * @exception SAXException the xml response is bad
     * @exception IOException an I/O error occurred
     */
    public ListMetadataFormats(URL baseURL)
	throws MalformedURLException, SAXException, IOException,
               StupidJavaBugException {
	this(baseURL, (String)null);
    }
    
    /** Client-side ListMetadataFormats verb constructor
     *
     * @param baseURL the baseURL of the server to be queried
     * @param identifier optional item identifier
     * @exception MalformedURLException the baseURL is bad
     * @exception SAXException the xml response is bad
     * @exception IOException an I/O error occurred
     */
    public ListMetadataFormats(URL baseURL, String identifier)
	throws MalformedURLException, SAXException, IOException,
               StupidJavaBugException {
	StringBuffer sb = new StringBuffer();
	sb.append(baseURL.toString());
	sb.append("?");
	sb.append(getQuery(identifier));
	XMLReader xmlReader = getXMLReader();
	xmlReader.setContentHandler(this);
  	try {
	    xmlReader.parse(new InputSource(getResponseStream(sb.toString())));
  	} catch (SAXException e) {
  	    System.out.println("HarvesterVerb.HarvesterVerb: query=" + sb.toString());
  	    e.printStackTrace();
  	    throw e;
  	}
    }

    /**
     * Construct the query portion of the http request
     *
     * @return a String containing the query portion of the http request
     */
    private static String getQuery(String identifier) {
	StringBuffer query =  new StringBuffer();
	query.append("verb=ListMetadataFormats");
	if (identifier != null) {
	    query.append("&identifier=");
	    query.append(identifier);
	}
	return query.toString();
    }
    
    public List getMetadataFormats() {
	if (metadataFormats.size() > 0)
	    return metadataFormats;
	else
	    return null;
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("\nListMetadataFormats.metadataFormats=" + getMetadataFormats());
	return sb.toString();
    }
        
    public void startElement(String namespaceURI, String localName, String qName,
			     Attributes attrs) {
	// 	String fullName = localName;
	// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);
	
	if (metadataFormatCapture == false && fullName.equals(OAI20_METADATAFORMAT)) {
	    metadataFormat = new StringBuffer();
	    metadataFormatCapture = true;
	}
	if (metadataFormatCapture) {
	    metadataFormat.append("<");
	    metadataFormat.append(qName);
	    int length = attrs.getLength();
	    for (int i=0; i<length; ++i) {
		metadataFormat.append(" ");
		String aEName = attrs.getQName(i);
		// 		if ("".equals(aEName)) aEName = attrs.getQName(i);
		metadataFormat.append(aEName);
		metadataFormat.append("=\"");
		metadataFormat.append(OAIUtil.xmlEncode(attrs.getValue(i)));
		metadataFormat.append("\"");
	    }
	    metadataFormat.append(">");
	} else {
	    super.startElement(namespaceURI, localName, qName, attrs);
	}
    }

    public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
	// 	String fullName = localName;
	// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);
	
	if (metadataFormatCapture) {
	    metadataFormat.append("</");
	    metadataFormat.append(qName);
	    metadataFormat.append(">");
	} else {
	    super.endElement(namespaceURI, localName, qName);
	}
	if (fullName.equals(OAI20_METADATAFORMAT)) {
	    metadataFormats.add(metadataFormat.toString());
	    metadataFormatCapture = false;
	}
    }
    
    public void characters(char[] buf, int offset, int len) {
        if (metadataFormatCapture) {
	    metadataFormat.append(OAIUtil.xmlEncode(new String(buf, offset, len)));
	} else {
	    super.characters(buf, offset, len);
	}
    }
}
