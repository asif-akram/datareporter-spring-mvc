
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
*The Original Code is GetRecord.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.verb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import ORG.oclc.oai.util.OAIUtil;

/**
 * Represents an OAI GetRecord Verb response. This class is used on both
 * the client-side and on the server-side to represent a GetRecord response
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public class GetRecord extends HarvesterVerb {
    private static final boolean debug = false;

    private boolean recordCapture = false;
    private StringBuffer record = new StringBuffer();
    private ArrayList records = new ArrayList();
    private HashMap xmlnsMap = new HashMap();

    /**
     * Client-side GetRecord verb constructor
     *
     * @param baseURL the baseURL of the server to be queried
     * @param identifier oai record identifier
     * @param metadataPrefix the desired metadata format
     * @exception MalformedURLException bad baseURL
     * @exception SAXException XML parser problem
     * @exception IOException an I/O error occurred
     */
    public GetRecord(URL baseURL, String identifier, String metadataPrefix)
	throws MalformedURLException, SAXException, IOException {
	StringBuffer query = new StringBuffer();
	query.append(baseURL.toString());
	query.append("?");
	query.append(getParams(identifier, metadataPrefix));
	if (debug) {
	    System.out.println("GetRecord.GetRecord: query=" + query);
	}
	XMLReader xmlReader = getXMLReader();
	xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
	xmlReader.setContentHandler(this);
 	try {
	    xmlReader.parse(new InputSource(getResponseStream(query.toString())));
 	} catch (SAXException e) {
   	    System.out.println("HarvesterVerb.HarvesterVerb: query=" + query);
   	    e.printStackTrace();
   	    throw e;
   	}
    }
    
    /**
     * Construct the query portion of the http request.
     *
     * @param identifier oai record identifier
     * @param metadataPrefix the desired metadata format
     * @return a String containing the query portion of the http request.
     */
    private static String getParams(String identifier,
				   String metadataPrefix)
        throws IOException {
        try {
            StringBuffer query =  new StringBuffer();
            query.append("verb=GetRecord");
            query.append("&identifier=");
            query.append(URLEncoder.encode(identifier, "UTF-8"));
            query.append("&metadataPrefix=");
            query.append(URLEncoder.encode(metadataPrefix, "UTF-8"));
            return query.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Get an iterator containing the Records in this GetRecord response
     *
     * @return an Iterator containing Records pulled from this response.
     */
    public Iterator iterator() {
	return records.iterator();
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(super.toString());
	return sb.toString();
    }

    public void startElement(String namespaceURI,
			     String localName,
			     String qName,
			     Attributes attrs) {
// 	String fullName = localName;
// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);
	
	if (recordCapture == false && fullName.equals(OAI20_RECORD)) {
	    record.setLength(0);
	    recordCapture = true;
	}
    int length = attrs.getLength();
	if (recordCapture) {
	    record.append("<");
	    record.append(qName);
	    for (int i=0; i<length; ++i) {
		record.append(" ");
		String aEName = attrs.getQName(i);
// 		if ("".equals(aEName)) aEName = attrs.getQName(i);
		record.append(aEName);
		record.append("=\"");
		record.append(OAIUtil.xmlEncode(attrs.getValue(i)));
		record.append("\"");
		if (aEName.startsWith("xmlns:")) {
		    xmlnsMap.remove(aEName);
		}
	    }
	    if (fullName.equals(OAI20_RECORD)) {
	        Iterator iter = xmlnsMap.entrySet().iterator();
	        while (iter.hasNext()) {
	            Map.Entry entry = (Entry) iter.next();
	            record.append(" ")
	            .append(entry.getKey())
	            .append("\"")
	            .append(OAIUtil.xmlEncode((String) entry.getValue()))
	            .append("\"");
	        }
	    }
	    record.append(">");
	} else {
	    for (int i=0; i<length; ++i) {
	        String aEName = attrs.getQName(i);
	        if (aEName.startsWith("xmlns:")) {
	            xmlnsMap.put(aEName, attrs.getValue(i));
	        }
	    }
	    super.startElement(namespaceURI, localName, qName, attrs);
	}
    }

    public void endElement(String namespaceURI,
			   String localName,
			   String qName)
	throws SAXException {
// 	String fullName = localName;
// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);
	
	if (recordCapture) {
	    record.append("</");
	    record.append(qName);
	    record.append(">");
	} else {
	    super.endElement(namespaceURI, localName, qName);
	}
	if (fullName.equals(OAI20_RECORD)) {
	    try {
		records.add(new Record(record.toString()));
	    } catch (IOException e) {
		e.printStackTrace();
		throw new SAXException(e.getMessage());
	    }
	    recordCapture = false;
	}
    }
    
    public void characters(char[] buf, int offset, int len) {
	if (recordCapture) {
	    record.append(OAIUtil.xmlEncode(new String(buf, offset, len)));
	} else {
	    super.characters(buf, offset, len);
	}
    }
}
