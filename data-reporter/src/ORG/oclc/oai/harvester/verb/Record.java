
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
 *The Original Code is Record.java.
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
import java.util.Map;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import ORG.oclc.oai.util.OAIUtil;

/**
 * Represents a single record from an OAI ListRecords response.
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public class Record extends DefaultHandler {
    private static final boolean debug = false;
    
    private static HashMap xmlReaders = new HashMap();

    private String record = null;
    
    private boolean identifierCapture = false;
    private StringBuffer identifier = new StringBuffer();

    private boolean datestampCapture = false;
    private StringBuffer datestamp = new StringBuffer();

    private boolean setSpecCapture = false;
    private ArrayList setSpecs = new ArrayList();
    private StringBuffer setSpec;

    private String status = null;

    private boolean metadataCapture = false;
    private StringBuffer metadata = new StringBuffer();

    private boolean aboutCapture = false;
    private ArrayList abouts = new ArrayList();
    private StringBuffer about = new StringBuffer();
    
    private HashMap xmlnsMap = new HashMap();
    
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
     * Provides access to discrete parts of the OAI &lt;record&gt; response.
     *
     * @param record String containing the &lt;record&gt; portion of the OAI XML response.
     *
     * @exception SAXException XML parser problem
     * @exception IOException
     */
    public Record(String record)
	throws SAXException, IOException {
	this.record = record;
	XMLReader xmlReader = getXMLReader();
	xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
	xmlReader.setContentHandler(this);
	try {
	    if (debug) {
		System.out.println("Record.Record: record=" + record);
	    }
	    xmlReader.parse(new InputSource(new StringReader(record)));
	} catch (SAXException e) {
  	    System.out.println("Record.Record: record=" + record);
  	    e.printStackTrace();
  	    throw e;
  	}
    }

    /**
     * Get the XML &lt;record&gt;
     *
     * @return the record as an XML string
     */
    public String getRecordXML() {
	return record;
    }

    /**
     * Does the record contain a status="deleted" attribute?
     *
     * @return true=record is flagged as deleted, false=record is not flagged as deleted.
     */
    public boolean isDeleted() {
	return "deleted".equals(status);
    }

    /**
     * Get the content of the &lt;identifier&gt; element
     * @return The record's OAI identifier
     */
    public String getIdentifier() { return identifier.toString(); }

    /**
     * Get the content of the &lt;datestamp&gt; element.
     * @return the record's datestamp.
     */
    public String getDatestamp() { return datestamp.toString(); }

    /**
     * Get the record's setSpecs.
     * @return an Iterator containing Strings of setSpec values. (null of none)
     */
    public Iterator getSetSpecs() {
	if (setSpecs.size() > 0)
	    return setSpecs.iterator();
	else
	    return null;
    }

    /**
     * Get the content of the record's &lt;metadata&gt; element.
     * @return an XML String containing the metadata content for the record.
     */
    public String getMetadata() {
	if (metadata.length() > 0)
	    return metadata.toString();
	else
	    return null;
    }

    /**
     * Get the record's 'about' elements.
     * @return an Iterator containing XML Strings for each &lt;about&gt; entry for the record.
     */
    public Iterator getAbouts() {
	if (about.length() > 0)
	    return abouts.iterator();
	else
	    return null;
    }

    /**
     * Get a String containing the content of the record formatted for human display.
     * @return A human readable string representation of the record.
     */
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("Record.identifier=");
	sb.append(getIdentifier());
	sb.append("\nRecord.datestamp=");
	sb.append(getDatestamp());
	Iterator setSpecs = getSetSpecs();
	if (setSpecs != null) {
	    while (setSpecs.hasNext()) {
		sb.append("\nRecord.setSpec=");
		sb.append((String)setSpecs.next());
	    }
	}
	sb.append("\nRecord.status=");
	sb.append(status);
	sb.append("\nRecord.metadata=");
	sb.append(getMetadata());
	Iterator abouts = getAbouts();
	if (abouts != null) {
	    while (abouts.hasNext()) {
		sb.append("\nRecord.about=");
		sb.append((String)abouts.next());
	    }
	}
	sb.append("\n");
	return sb.toString();
    }

    /**
     * SAX parser call-back method for extracting record content.
     */
    public void startElement(String namespaceURI, String localName, String qName,
			     Attributes attrs) {
	if (debug) {
	    System.out.println("startElement: namespaceURI=" + namespaceURI + ", localName=" + localName + ", qName=" + qName + ", attrs=" + attrs);
	}
	// 	String fullName = localName;
	// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = HarvesterVerb.fullName(namespaceURI, localName);
	
	int length = attrs.getLength();
	
	if (metadataCapture == false && fullName.equals("#metadata")) {
	    metadata.setLength(0);
	    metadataCapture = true;
	    addXmlns(attrs, xmlnsMap);
	} else if (metadataCapture == false && aboutCapture == false && fullName.equals("#about")) {
	    about.setLength(0);
	    aboutCapture = true;
	    addXmlns(attrs, xmlnsMap);
	} else if (metadataCapture) {
	    metadata.append("<");
	    metadata.append(qName);
	    HashMap tempMap = new HashMap();
	    tempMap.putAll(xmlnsMap);
	    for (int i = 0; i < length; ++i) {
                metadata.append(" ");
                String aEName = attrs.getQName(i);
                metadata.append(aEName);
                metadata.append("=\"");
                metadata.append(OAIUtil.xmlEncode(attrs.getValue(i)));
                metadata.append("\"");
                tempMap.remove(aEName);
            }
	    Iterator iter = tempMap.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry entry = (Map.Entry) iter.next();
	        String aEName = (String) entry.getKey();
	        metadata.append(" ")
	        .append(aEName)
	        .append("=\"")
	        .append(entry.getValue())
	        .append("\"");
	    }
	    metadata.append(">");
	} else if (aboutCapture) {
	    about.append("<");
	    about.append(qName);
	    HashMap tempMap = new HashMap();
	    tempMap.putAll(xmlnsMap);
	    for (int i = 0; i < length; ++i) {
                about.append(" ");
                String aEName = attrs.getQName(i);
                about.append(aEName);
                about.append("=\"");
                about.append(OAIUtil.xmlEncode(attrs.getValue(i)));
                about.append("\"");
                tempMap.remove(aEName);
            }
	    Iterator iter = tempMap.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry entry = (Map.Entry) iter.next();
	        String aEName = (String) entry.getKey();
	        about.append(" ")
	        .append(aEName)
	        .append("=\"")
	        .append(entry.getValue())
	        .append("\"");
	    }
	    about.append(">");
	} else if (fullName.equals("#identifier")) {
	    identifierCapture = true;
	} else if (fullName.equals("#datestamp")) {
	    datestampCapture = true;
	} else if (fullName.equals("#setSpec")) {
	    setSpecCapture = true;
            setSpec = new StringBuffer();
	} else if (fullName.equals("#record")) {
	    // ignore
	    addXmlns(attrs, xmlnsMap);
	} else if (fullName.equals("#header")) {
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
     * @param attrs
     * @param xmlnsMap2
     */
    private void addXmlns(Attributes attrs, HashMap xmlnsMap) {
        int length = attrs.getLength();
        for (int i=0; i<length; ++i) {
            String aEName = attrs.getQName(i);
            if (aEName.startsWith("xmlns:")) {
                xmlnsMap.put(aEName, attrs.getValue(i));
            }
        }
    }

    /**
     * SAX parser call-back method for extracting record content.
     */
    public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
	if (debug) {
	    System.out.println("endElement: namespaceURI=" + namespaceURI + ", localName=" + localName + ", qName=" + qName);
	}
	// 	String fullName = localName;
	// 	if ("".equals(fullName)) fullName = qName; // namespaceAware = false
	String fullName = HarvesterVerb.fullName(namespaceURI, localName);

	if (fullName.equals("#metadata")) {
	    metadataCapture = false;
 	} else if (fullName.equals("#about")) {
 	    aboutCapture = false;
	}
	
	if (metadataCapture) {
	    metadata.append("</");
	    metadata.append(qName);
	    metadata.append(">");
	} else if (aboutCapture) {
	    about.append("</");
	    about.append(qName);
	    about.append(">");
	} else if (identifierCapture) {
	    // do nothing
	} else if (datestampCapture) {
	    // do nothing
	} else if (setSpecCapture) {
	    // do nothing
	}
	if (fullName.equals("#about")) {
	    aboutCapture = false;
	    abouts.add(about.toString());
	} else if (fullName.equals("#identifier")) {
	    identifierCapture = false;
	} else if (fullName.equals("#datestamp")) {
	    datestampCapture = false;
	} else if (fullName.equals("#setSpec")) {
            setSpecs.add(setSpec.toString());
	    setSpecCapture = false;
	}
    }
    
    /**
     * SAX parser call-back method for extracting record content.
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
	} else if (metadataCapture) {
	    metadata.append(OAIUtil.xmlEncode(s));
	} else if (aboutCapture) {
	    about.append(OAIUtil.xmlEncode(s));
	} else if (s.trim().length() > 0) {
	    System.out.println("Unrecognized content:" + s);
	}
    }
}
