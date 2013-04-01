
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
*The Original Code is ListSets.java______________________________.
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
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import ORG.oclc.oai.util.OAIUtil;

/**
 * This class represents an ListSets response on either the server or
 * on the client
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public class ListSets extends HarvesterVerb {
    private static final boolean debug = false;

    private boolean resumptionTokenCapture = false;
    private StringBuffer resumptionToken = new StringBuffer();
    private boolean setCapture = false;
    private StringBuffer set;
    private ArrayList sets = new ArrayList();


    /**
     * Client-side ListSets verb constructor
     *
     * @param baseURL the baseURL of the server to be queried
     * @exception MalformedURLException the baseURL is bad
     * @exception SAXException the xml response is bad
     * @exception IOException an I/O error occurred
     */
    public ListSets(URL baseURL)
	throws MalformedURLException, SAXException, IOException,
               StupidJavaBugException {
	StringBuffer sb = new StringBuffer();
	sb.append(baseURL.toString());
	sb.append("?");
	sb.append(getQuery());
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
     * Client-side ListSets verb constructor
     *
     * @param baseURL the baseURL of the server being queried
     * @param resumptionToken the resumption token
     * @exception MalformedURLException bad baseURL
     * @exception SAXException XML parser problem
     * @exception IOException an I/O error occurred
     */
    public ListSets(URL baseURL, String resumptionToken)
	throws MalformedURLException, SAXException, IOException {
	StringBuffer query = new StringBuffer();
	query.append(baseURL.toString());
	query.append("?");
	query.append(getParams(resumptionToken));
	XMLReader xmlReader = getXMLReader();
	xmlReader.setContentHandler(this);
	xmlReader.parse(new InputSource(getResponseStream(query.toString())));
    }

    /**
     * Construct the query portion of the http request.
     *
     * @param resumptionToken the resumptionToken
     * @return a String containing the query portion of the http request.
     */
    private static String getParams(String resumptionToken) throws IOException {
        try {
            StringBuffer query =  new StringBuffer();
            query.append("verb=ListSets");
            query.append("&resumptionToken=");
            query.append(URLEncoder.encode(resumptionToken, "UTF-8"));
            return query.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }
    
    /**
     * Construct the query portion of the http request
     *
     * @return a String containing the query portion of the http request
     */
    private static String getQuery() {
	StringBuffer query =  new StringBuffer();
	query.append("verb=ListSets");
	return query.toString();
    }
    
    public List getSets() {
	if (sets.size() > 0)
	    return sets;
	else
	    return null;
    }

    /**
     * Get the resumptionToken from this ListRecord response
     *
     * @return a String containing the resumptionToken
     */
    public String getResumptionToken() {
	if (resumptionToken.length() == 0) return null;
	else return resumptionToken.toString();
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("resumptionToken: ");
	sb.append(resumptionToken);
	sb.append("\n");
	sb.append(super.toString());
// 	sb.append("\nListSets.sets=" + getSets());
	return sb.toString();
    }
        
    public void startElement(String namespaceURI, String localName, String qName,
			     Attributes attrs) {
// 	String fullName = localName;
// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);
        if (debug) System.out.println("ListSets.startElement: fullName=" + fullName);
	
	if (setCapture == false && fullName.equals(OAI20_SET)) {
	    set = new StringBuffer();
	    setCapture = true;
	}
	if (setCapture) {
	    set.append("<");
	    set.append(qName);
	    int length = attrs.getLength();
	    for (int i=0; i<length; ++i) {
		set.append(" ");
		String aEName = attrs.getQName(i);
// 		if ("".equals(aEName)) aEName = attrs.getQName(i);
		set.append(aEName);
		set.append("=\"");
		set.append(OAIUtil.xmlEncode(attrs.getValue(i)));
		set.append("\"");
	    }
	    set.append(">");
	} else if (fullName.equals(OAI20_RESUMPTION_TOKEN)) {
            if (debug) {
                System.out.println("ListSets.startElement: found <resumptionToken>");
            }
	    resumptionTokenCapture = true;
	} else {
	    super.startElement(namespaceURI, localName, qName, attrs);
	}
    }

    public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
// 	String fullName = localName;
// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = fullName(namespaceURI, localName);
        if (debug) System.out.println("ListSets.endElement: fullName=" + fullName);
	
	if (setCapture) {
	    set.append("</");
	    set.append(qName);
	    set.append(">");
	} else if (resumptionTokenCapture) {
            if (debug) {
                System.out.println("ListSets.endElement: found </resumptionToken>");
                System.out.println("ListSets.endElement: resumptionToken=" + resumptionToken.toString());
            }
	    // do nothing
	} else {
	    super.endElement(namespaceURI, localName, qName);
	}
	if (fullName.equals(OAI20_SET)) {
	    sets.add(set.toString());
	    setCapture = false;
	} else if (fullName.equals(OAI20_RESUMPTION_TOKEN)) {
	    resumptionTokenCapture = false;
	}
    }
    
    public void characters(char[] buf, int offset, int len) {
	if (resumptionTokenCapture) {
	    resumptionToken.append(new String(buf, offset, len));
	} else if (setCapture) {
	    set.append(OAIUtil.xmlEncode(new String(buf, offset, len)));
	} else {
	    super.characters(buf, offset, len);
	}
    }
}
