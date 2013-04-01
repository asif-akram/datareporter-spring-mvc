
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
*The Original Code is HarvesterVerb.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.verb;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.GZIPInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * HarvesterVerb is the parent class for each of the OAI verbs.
 *
 * @author Jefffrey A. Young, OCLC Online Computer Library Center
 */
public abstract class HarvesterVerb extends DefaultHandler {
    public static final String OAI20_NS = "http://www.openarchives.org/OAI/2.0/";
    public static final String OAI20_OAIPMH = fullName(OAI20_NS, "OAI-PMH");
    public static final String OAI20_METADATA = fullName(OAI20_NS, "metadata");
    public static final String OAI20_ABOUT = fullName(OAI20_NS, "about");
    public static final String OAI20_IDENTIFIER = fullName(OAI20_NS, "identifier");
    public static final String OAI20_DATESTAMP = fullName(OAI20_NS, "datestamp");
    public static final String OAI20_SETSPEC = fullName(OAI20_NS, "setSpec");
    public static final String OAI20_RECORD = fullName(OAI20_NS, "record");
    public static final String OAI20_SET = fullName(OAI20_NS, "set");
    public static final String OAI20_METADATAFORMAT = fullName(OAI20_NS, "metadataFormat");
    public static final String OAI20_HEADER = fullName(OAI20_NS, "header");
    public static final String OAI20_ERROR = fullName(OAI20_NS, "error");
    public static final String OAI20_RESPONSE_DATE = fullName(OAI20_NS, "responseDate");
    public static final String OAI20_REQUEST_URL = fullName(OAI20_NS, "request");
    public static final String OAI20_RESUMPTION_TOKEN = fullName(OAI20_NS, "resumptionToken");
    public static final String OAI20_STATUS = fullName(OAI20_NS, "status");
    
    private static final boolean debug = false;

    private static int INITIAL_BAOS_SIZE = 768*1024;
    private static HashMap xmlReaders = new HashMap();

    private byte[] responseBuffer = null;
        
    private String fullName = null;
    private String responseDate;
    private String request;

    private boolean errorCapture = false;
    private String errorCode = null;
    private StringBuffer errorMessage;
    private ArrayList errors = new ArrayList();
    private byte[] response = null;

    protected XMLReader getXMLReader()
	throws SAXException {
	Thread currentThread = Thread.currentThread();
	XMLReader xmlReader = (XMLReader)xmlReaders.get(currentThread);
	if (xmlReader == null) {
	    try { // Xerces
		xmlReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
	    } catch (SAXException e) {
		try { // Crimson
		    xmlReader = XMLReaderFactory.createXMLReader("org.apache.crimson.parser.XMLReaderImpl");
		} catch (SAXException e1) {
		    try { // Piccolo
			xmlReader = XMLReaderFactory.createXMLReader("com.bluecase.xml.Piccolo");
		    } catch (SAXException e2) {
			try { // Oracle
			    xmlReader = XMLReaderFactory.createXMLReader("oracle.xml.parser.v2.SAXParser");
			} catch (SAXException e3) {
			    try { // default
				xmlReader = XMLReaderFactory.createXMLReader();
			    } catch (SAXException e4) {
				throw new SAXException("No SAX parser available");
			    }
			}
		    }
		}
	    }
	    xmlReaders.put(currentThread, xmlReader);
	}
	return xmlReader;
    }
    
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("HarvesterVerb.responseDate: ");
	sb.append(responseDate);
	sb.append("\nHarvesterVerb.request: ");
	sb.append(request);
	sb.append("\n");
	Iterator iterator = errors.iterator();
	while (iterator.hasNext()) {
	    sb.append("HarvesterVerb.errors: ");
	    sb.append(iterator.next());
	    sb.append("\n");
	}
	return sb.toString();
    }

    /**
     * Get the OAI response date from the verb response
     *
     * @return the current verb's responseDate value
     */
    public String getResponseDate() {
	return responseDate;
    }

    /**
     * @deprecated use getRequest instead
     */
    public String getRequestURL() {
	return request;
    }

    public String getRequest() {
	return request;
    }

    public byte[] getResponseBuffer() {
        return responseBuffer;
    }

    public ArrayList getErrors() {
	if (errors.size() != 0)
	    return errors;
	else
	    return null;
    }
    
    public void startElement(String namespaceURI, String sName, String qName,
				      Attributes attrs) {
// 	fullName = sName;
// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
        if (debug) System.out.println("HarvesterVerb.startElement: namespaceURI=" + namespaceURI);
	fullName = fullName(namespaceURI, sName);
	if (fullName.equals(OAI20_ERROR)) {
	    errorCode = attrs.getValue("code");
            errorCapture = true;
            errorMessage = new StringBuffer();
	}
    }

    public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
	if (debug) {
	    System.out.println("HarvesterVerb.endElement: namespaceURI=" + namespaceURI + ", localName=" + localName + ", qName=" + qName);
	}
// 	String fullName = localName;
// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);

	if (fullName.equals(OAI20_ERROR)) {
	    errorCapture = false;
            errors.add(new OAIError(errorCode, errorMessage.toString()));
	}
        this.fullName = null; // ignore stuff outside elements
    }
    
    public void characters(char[] buf, int offset, int len) {
	if (OAI20_RESPONSE_DATE.equals(fullName)) {
	    responseDate = new String(buf, offset, len);
	} else if (OAI20_REQUEST_URL.equals(fullName)) {
	    request = new String(buf, offset, len);
	} else if (OAI20_ERROR.equals(fullName)) {
            errorMessage.append(new String(buf, offset, len));
	}
    }

    protected InputStream getResponseStream(String uri)
        throws IOException {
        InputStream in = null;
	URL url = new URL(uri);
	HttpURLConnection con = null;
	int responseCode = 0;
	do {
	    con = (HttpURLConnection)url.openConnection();
	    con.setRequestProperty("User-Agent", "OAIHarvester/2.0");
	    con.setRequestProperty("Accept-Encoding", "compress, gzip, deflate, identify");
	    try {
		responseCode = con.getResponseCode();
		if (debug) {
		    System.out.println("HarvesterVerb.getResponseStream: responseCode="
				       + responseCode);
		}
	    } catch (FileNotFoundException e) {
		// assume it's a 503 response
		responseCode = HttpURLConnection.HTTP_UNAVAILABLE;
            }

            if (responseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
		long retrySeconds = con.getHeaderFieldInt("Retry-After", -1);
		if (retrySeconds == -1) {
		    long now = (new Date()).getTime();
		    long retryDate = con.getHeaderFieldDate("Retry-After", now);
		    retrySeconds = retryDate - now;
		}
		if (retrySeconds == 0) { // Apparently, it's a bad URL
		    throw new FileNotFoundException("Bad URL?");
		}
		System.err.println("Server response: Retry-After=" + retrySeconds);
		if (retrySeconds > 0) {
		    try {
			Thread.sleep(retrySeconds * 1000);
		    } catch (InterruptedException ex) {
			ex.printStackTrace();
		    }
		}
	    }
	} while (responseCode == HttpURLConnection.HTTP_UNAVAILABLE);
	String contentEncoding = con.getHeaderField("Content-Encoding");
	if (debug) {
	    System.out.println("contentEncoding=" + contentEncoding);
	}
	if ("compress".equals(contentEncoding)) {
	    ZipInputStream zis = new ZipInputStream(con.getInputStream());
	    zis.getNextEntry();
	    in = zis;
	} else if ("gzip".equals(contentEncoding)) {
	    in =  new GZIPInputStream(con.getInputStream());
	} else if ("deflate".equals(contentEncoding)) {
	    in =  new InflaterInputStream(con.getInputStream());
	} else {
	    in = con.getInputStream();
	}
        return captureResponse(in);
    }

    private InputStream captureResponse(InputStream in)
        throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(INITIAL_BAOS_SIZE);
        byte[] buffer = new byte[INITIAL_BAOS_SIZE];
        int count = 0;
        BufferedInputStream bis = new BufferedInputStream(in);
        while ((count = bis.read(buffer)) != -1) {
            baos.write(buffer, 0, count);
        }
        responseBuffer = baos.toByteArray();
        return new ByteArrayInputStream(responseBuffer);
    }

    public static String fullName(String namespaceURI, String localName) {
	StringBuffer sb = new StringBuffer();
	sb.append(namespaceURI);
	sb.append("#");
	sb.append(localName);
	return sb.toString();
    }
}
