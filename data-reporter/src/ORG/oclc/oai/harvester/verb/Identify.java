
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
*The Original Code is Identify.java______________________________.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.verb;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import ORG.oclc.oai.util.OAIUtil;

/**
 * This class represents an Identify response on either the server or
 * on the client
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public class Identify extends HarvesterVerb {
    private static final boolean debug = false;

    private boolean repositoryNameCapture = false;
    private StringBuffer repositoryName = new StringBuffer();
    
    private boolean baseURLCapture = false;
    private StringBuffer baseURL = new StringBuffer();
    
    private boolean protocolVersionCapture = false;
    private StringBuffer protocolVersion = new StringBuffer();
    
    private boolean deletedRecordCapture = false;
    private StringBuffer deletedRecord = new StringBuffer();

    private boolean earliestDatestampCapture = false;
    private StringBuffer earliestDatestamp = new StringBuffer();

    private boolean adminEmailCapture = false;
    private StringBuffer adminEmail;
    private ArrayList adminEmails = new ArrayList();

    private boolean granularityCapture = false;
    private StringBuffer granularity = new StringBuffer();

    private boolean compressionCapture = false;
    private StringBuffer compression;
    private ArrayList compressions = new ArrayList();
    
    private boolean descriptionCapture = false;
    private StringBuffer description;
    private ArrayList descriptions = new ArrayList();

    /**
     * Client-side Identify verb constructor
     *
     * @param baseURL the baseURL of the server to be queried
     * @exception MalformedURLException the baseURL is bad
     * @exception SAXException the xml response is bad
     * @exception IOException an I/O error occurred
     */
    public Identify(URL baseURL)
	throws MalformedURLException, SAXException, IOException,
               StupidJavaBugException {
	StringBuffer query = new StringBuffer();
	query.append(baseURL.toString());
	query.append("?");
	query.append(getQuery());
  	try {
	    XMLReader xmlReader = getXMLReader();
	    xmlReader.setContentHandler(this);
	    xmlReader.parse(new InputSource(getResponseStream(query.toString())));
  	} catch (SAXException e) {
  	    System.out.println("HarvesterVerb.HarvesterVerb: query=" + query.toString());
  	    e.printStackTrace();
  	    throw e;
  	}
    }

    /**
     * Construct the query portion of the http request
     *
     * @return a String containing the query portion of the http request
     */
    private static String getQuery() {
	StringBuffer query =  new StringBuffer();
	query.append("verb=Identify");
	return query.toString();
    }
    
    public String getRepositoryName() { return repositoryName.toString(); }
    public String getBaseURL() { return baseURL.toString(); }
    public String getProtocolVersion() { return protocolVersion.toString(); }
    /**
     * @deprecated use getDeletedRecord instead
     */
    public String getDeletedItem() { return deletedRecord.toString(); }
    public String getDeletedRecord() { return deletedRecord.toString(); }
    public String getEarliestDatestamp() { return earliestDatestamp.toString(); }
    public List getAdminEmails() {
	if (adminEmails.size() > 0)
	    return adminEmails;
	else
	    return null;
    }
    public String getGranularity() { return granularity.toString(); }
    public List getCompressions() {
	if (compressions.size() > 0)
	    return compressions;
	else
	    return null;
    }
    public List getDescriptions() {
	if (descriptions.size() > 0)
	    return descriptions;
	else
	    return null;
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("Identify.repositoryName=" + getRepositoryName());
	sb.append("\nIdentify.baseURL=" + getBaseURL());
	sb.append("\nIdentify.protocolVersion=" + getProtocolVersion());
	sb.append("\nIdentify.deletedRecord=" + getDeletedRecord());
	sb.append("\nIdentify.earliestDatestamp=" + getEarliestDatestamp());
	sb.append("\nIdentify.adminEmails=" + getAdminEmails());
	sb.append("\nIdentify.granularity=" + getGranularity());
	sb.append("\nIdentify.compressions=" + getCompressions());
	sb.append("\nIdentify.descriptions=" + getDescriptions());
	return sb.toString();
    }
        
    public void startElement(String namespaceURI, String localName, String qName,
			     Attributes attrs) {
	String eName = localName;
	if ("".equals(eName)) eName = qName; // namespaceAware = false
        if (debug) System.out.println("Identify.startElement.startElement: namespaceURI=" + namespaceURI);
	
	if (descriptionCapture == false && eName.equals("description")) {
	    description = new StringBuffer();
	    descriptionCapture = true;
	}
	if (descriptionCapture) {
	    description.append("<");
	    description.append(eName);
	    int length = attrs.getLength();
	    for (int i=0; i<length; ++i) {
		description.append(" ");
		String aEName = attrs.getLocalName(i);
		if ("".equals(aEName)) aEName = attrs.getQName(i);
		description.append(aEName);
		description.append("=\"");
		description.append(OAIUtil.xmlEncode(attrs.getValue(i)));
		description.append("\"");
	    }
	    description.append(">");
	} else if (eName.equals("repositoryName")) {
	    repositoryNameCapture = true;
	} else if (eName.equals("baseURL")) {
	    baseURLCapture = true;
	} else if (eName.equals("protocolVersion")) {
	    protocolVersionCapture = true;
	} else if (eName.equals("deletedRecord")) {
	    deletedRecordCapture = true;
	} else if (eName.equals("earliestDatestamp")) {
	    earliestDatestampCapture = true;
	} else if (eName.equals("adminEmail")) {
	    adminEmailCapture = true;
            adminEmail = new StringBuffer();
	} else if (eName.equals("granularity")) {
	    granularityCapture = true;
	} else if (eName.equals("compression")) {
            compression = new StringBuffer();
	    compressionCapture = true;
	} else {
	    super.startElement(namespaceURI, localName, qName, attrs);
	}
    }

    public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
        if (debug) System.out.println("Identify.startElement.endElement: namespaceURI=" + namespaceURI);
	String eName = localName;
	if ("".equals(eName)) eName = qName; // namespaceAware = false
	
	if (descriptionCapture) {
	    description.append("</");
	    description.append(eName);
	    description.append(">");
	} else if (repositoryNameCapture
		   || baseURLCapture
		   || protocolVersionCapture
		   || deletedRecordCapture
		   || earliestDatestampCapture
		   || adminEmailCapture
		   || granularityCapture
		   || compressionCapture) {
	    // do nothing
	} else {
	    super.endElement(namespaceURI, localName, qName);
	}
	if (eName.equals("description")) {
	    descriptions.add(description.toString());
	    descriptionCapture = false;
	} else if (eName.equals("repositoryName")) {
	    repositoryNameCapture = false;
	} else if (eName.equals("baseURL")) {
	    baseURLCapture = false;
	} else if (eName.equals("protocolVersion")) {
	    protocolVersionCapture = false;
	} else if (eName.equals("deletedRecord")) {
	    deletedRecordCapture = false;
	} else if (eName.equals("earliestDatestamp")) {
	    earliestDatestampCapture = false;
	} else if (eName.equals("adminEmail")) {
	    adminEmails.add(adminEmail.toString());
	    adminEmailCapture = false;
	} else if (eName.equals("granularity")) {
	    granularityCapture = false;
	} else if (eName.equals("compression")) {
	    compressions.add(compression.toString());
	    compressionCapture = false;
	}
    }
    
    public void characters(char[] buf, int offset, int len) {
	if (repositoryNameCapture) {
	    repositoryName.append(new String(buf, offset, len));
	} else if (baseURLCapture) {
	    baseURL.append(new String(buf, offset, len));
	} else if (protocolVersionCapture) {
	    protocolVersion.append(new String(buf, offset, len));
	} else if (deletedRecordCapture) {
	    deletedRecord.append(new String(buf, offset, len));
	} else if (earliestDatestampCapture) {
	    earliestDatestamp.append(new String(buf, offset, len));
	} else if (adminEmailCapture) {
	    adminEmail.append(OAIUtil.xmlEncode(new String(buf, offset, len)));
	} else if (granularityCapture) {
	    granularity.append(new String(buf, offset, len));
	} else if (compressionCapture) {
	    compression.append(new String(buf, offset, len));
	} else if (descriptionCapture) {
	    description.append(OAIUtil.xmlEncode(new String(buf, offset, len)));
	} else {
	    super.characters(buf, offset, len);
	}
    }
}
