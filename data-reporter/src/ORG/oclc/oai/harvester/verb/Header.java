
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
 *The Original Code is Header.java.
 *The Initial Developer of the Original Code is Jeff Young.
 *Portions created by ______________________ are
 *Copyright (C) _____ _______________________. All Rights Reserved.
 *Contributor(s):______________________________________.
 */

package ORG.oclc.oai.harvester.verb;

import java.io.IOException;
import java.io.StringReader;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import ORG.oclc.oai.util.OAIUtil;

/**
 * Represents a single header from an OAI ListIdentifiers response.
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public class Header extends DefaultHandler {
    private static final boolean debug = false;
    
    private String header = null;

    private static HashMap xmlReaders = new HashMap();
    
    private boolean identifierCapture = false;
    private StringBuffer identifier = new StringBuffer();

    private boolean datestampCapture = false;
    private StringBuffer datestamp = new StringBuffer();

    private boolean setSpecCapture = false;
    private ArrayList setSpecs = new ArrayList();
    private StringBuffer setSpec;

    private String status = null;

    /**
     * Provides access to discrete parts of the OAI &lt;header&gt; response.
     *
     * @param header String containing the &lt;header&gt; portion of the OAI XML response.
     *
     * @exception SAXException XML parser problem
     * @exception IOException
     */
    public Header(String header)
	throws SAXException, IOException {
	this.header = header;
	XMLReader xmlReader = getXMLReader();
	xmlReader.setContentHandler(this);
	try {
	    if (debug) {
		System.out.println("Header.Header: header=" + header);
	    }
	    xmlReader.parse(new InputSource(new StringReader(header)));
	} catch (SAXException e) {
	    System.out.println("Header.Header: header=" + header);
	    e.printStackTrace();
	    throw e;
	}
    }
    
    private XMLReader getXMLReader()
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
    
    /**
     * Get the XML &lt;header&gt;
     *
     * @return the header as an XML string
     */
    public String getHeaderXML() {
	return header;
    }

    /**
     * Does the header contain a status="deleted" attribute?
     *
     * @return true=header is flagged as deleted, false=header is not flagged as deleted.
     */
    public boolean isDeleted() {
	return "deleted".equals(status);
    }

    /**
     * Get the content of the &lt;identifier&gt; element
     * @return The header's OAI identifier
     */
    public String getIdentifier() { return identifier.toString(); }

    /**
     * Get the content of the &lt;datestamp&gt; element.
     * @return the header's datestamp.
     */
    public String getDatestamp() { return datestamp.toString(); }

    /**
     * Get the header's setSpecs.
     * @return an Iterator containing Strings of setSpec values. (null of none)
     */
    public Iterator getSetSpecs() {
	if (setSpecs.size() > 0)
	    return setSpecs.iterator();
	else
	    return null;
    }

    /**
     * Get a String containing the content of the header formatted for human display.
     * @return A human readable string representation of the header.
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("Header.identifier=");
	sb.append(getIdentifier());
	sb.append("\nHeader.datestamp=");
	sb.append(getDatestamp());
	Iterator setSpecs = getSetSpecs();
	if (setSpecs != null) {
	    while (setSpecs.hasNext()) {
		sb.append("\nHeader.setSpec=");
		sb.append((String)setSpecs.next());
	    }
	}
	sb.append("\nHeader.status=");
	sb.append(status);
	sb.append("\n");
	return sb.toString();
    }

    /**
     * SAX parser call-back method for extracting header content.
     */
    public void startElement(String namespaceURI, String localName, String qName,
			     Attributes attrs) {
	if (debug) {
	    System.out.println("startElement: namespaceURI=" + namespaceURI + ", localName=" + localName + ", qName=" + qName + ", attrs=" + attrs);
	}
	// 	String fullName = localName;
	// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = HarvesterVerb.fullName(namespaceURI, localName);
	
        if (fullName.equals("#identifier")) {
	    identifierCapture = true;
	} else if (fullName.equals("#datestamp")) {
	    datestampCapture = true;
	} else if (fullName.equals("#setSpec")) {
	    setSpecCapture = true;
            setSpec = new StringBuffer();
	} else if (fullName.equals("#header")) {
	    // ignore
	} else if (fullName.equals("#header")) {
	    int length = attrs.getLength();
	    for (int i=0; i<length; ++i) {
		String aEName = HarvesterVerb.fullName(attrs.getURI(i), attrs.getLocalName(i));
		if (HarvesterVerb.OAI20_STATUS.equals(aEName)) {
		    status = attrs.getValue(i);
		}
	    }
	} else {
	    System.out.println("Unrecognized element: " + qName);
	}
    }

    /**
     * SAX parser call-back method for extracting header content.
     */
    public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
	if (debug) {
	    System.out.println("endElement: namespaceURI=" + namespaceURI + ", localName=" + localName + ", qName=" + qName);
	}
	// 	String fullName = localName;
	// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = HarvesterVerb.fullName(namespaceURI, localName);

        if (identifierCapture) {
	    // do nothing
	} else if (datestampCapture) {
	    // do nothing
	} else if (setSpecCapture) {
	    // do nothing
	}
        if (fullName.equals("#identifier")) {
	    identifierCapture = false;
	} else if (fullName.equals("#datestamp")) {
	    datestampCapture = false;
	} else if (fullName.equals("#setSpec")) {
            setSpecs.add(setSpec.toString());
	    setSpecCapture = false;
	}
    }
    
    /**
     * SAX parser call-back method for extracting header content.
     */
    public void characters(char[] buf, int offset, int len) {
	String s = new String(buf, offset, len);
	if (debug) {
	    System.out.println("characters: s=" + s);
	}
	if (identifierCapture) {
	    identifier.append(OAIUtil.xmlEncode(s));
	} else if (datestampCapture) {
	    datestamp.append(OAIUtil.xmlEncode(s));
	} else if (setSpecCapture) {
            setSpec.append(OAIUtil.xmlEncode(s));
	} else if (s.trim().length() > 0) {
	    System.out.println("Unrecognized content:" + s);
	}
    }
}
