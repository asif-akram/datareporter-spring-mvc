
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
*The Original Code is XMLUtil.java______________________________.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.util;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.InterruptedException;
import java.util.*;
import java.net.*;
import java.io.*;


public class XMLUtil {
    private static final boolean debug = false;
    private DocumentBuilder parser;

    public XMLUtil() {
	this(false,false);
    }
    
    public XMLUtil(boolean validating, boolean namespaceAware) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(namespaceAware);
	try {
	    parser = factory.newDocumentBuilder();
	    parser.setErrorHandler(new ErrorHandler() {
		    public void fatalError(SAXParseException e) throws SAXException {
			System.out.println("fatalError");
			e.printStackTrace();
			throw e;
		    }
		    public void error(SAXParseException e) throws SAXParseException {
			System.out.println("error");
			e.printStackTrace();
			throw e;
		    }
		    public void warning(SAXParseException e) throws SAXParseException {
			System.out.println("warning");
			e.printStackTrace();
			System.out.println("** Warning, line" + e.getLineNumber() +
					   ", uri " + e.getSystemId());
			System.out.println(" " + e.getMessage());
		    }
		});
	} catch (ParserConfigurationException e) {
	    e.printStackTrace(System.err);
	}
    }

    public Document newDocument() {
        return parser.newDocument();
    }

    public Document parse(String uri)
        throws SAXException, IOException {
//         return parser.parse(uri);
	URL url = new URL(uri);
	HttpURLConnection con = null;
	int responseCode = 0;
	do {
	    con = (HttpURLConnection)url.openConnection();
	    con.setRequestProperty("User-Agent", "OAIHarvester/1.1");
	    try {
		responseCode = con.getResponseCode();
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
	InputStream is = con.getInputStream();
	return parser.parse(is);
    }
    
    public Document parse(InputSource s)
        throws SAXException, IOException {
        return parser.parse(s);
    }

    public Document parse(InputStream s)
        throws SAXException, IOException {
        return parser.parse(s);
    }

    public Document parse(Reader r)
        throws SAXException, IOException {
        InputSource is = new InputSource(r);
        return parser.parse(is);
    }

//     public static void write(Document doc, OutputStream out)
//         throws IOException {
//         XMLWriter writer = new XMLWriter(out);
//         writer.write(doc);
//     }

//     public static void write(Document doc, Writer out)
//         throws IOException {
//         XMLWriter writer = new XMLWriter(out);
//         writer.write(doc);
//     }

    public static Element getElement(Document doc, String tagName) {
	return getElement(doc.getDocumentElement(), tagName);
    }
    
    public static Element getElement(Document doc, String tagName, int index) {
	return getElement(doc.getDocumentElement(), tagName, index);
    }
    
    public static Element getElement(Element el, String tagName) {
	return getElement(el, tagName, 0);
    }
    
    public static Element getElement(Element el, String tagName, int index) {
	NodeList list = el.getElementsByTagName(tagName);
	return (Element)list.item(index);
    }

    public static Element getElementNS(Element el, String nameSpaceURI, String tagName) {
	return getElementNS(el, nameSpaceURI, tagName, 0);
    }
    
    public static Element getElementNS(Element el, String nameSpaceURI, String tagName, int index) {
	NodeList list = el.getElementsByTagNameNS(nameSpaceURI, tagName);
	return (Element)list.item(index);
    }

    public static int getSize(Document doc, String tagName) {
	return getSize(doc.getDocumentElement(), tagName);
    }
    
    public static int getSize(Element el, String tagName) {
	NodeList list = el.getElementsByTagName(tagName);
	return list.getLength();
    }

    public static int getSizeNS(Element el, String nameSpaceURI, String tagName) {
	NodeList list = el.getElementsByTagNameNS(nameSpaceURI, tagName);
	return list.getLength();
    }

    public static String getValue(Document doc, String tagName) {
        return getValue(doc.getDocumentElement(), tagName);
    }

    public static String getValue(Element el, String tagName) {
	return getValue(el, tagName, 0);
    }

    public static String getValue(Element el, String tagName, int index) {
	return getValue(getElement(el, tagName, index));
    }

    public static String getValue(Element el) {
	if (el != null) {
	    NodeList nodes = el.getChildNodes();
            StringBuffer sb = new StringBuffer();
            //	    String s;
	    int length = nodes.getLength();
	    for (int i=0; i<length; ++i) {
                Node node = nodes.item(i);
		String s = null;
		s = node.getNodeValue();
// 		System.out.println("XMLUtil.getValue: s=" + s);
		if (s != null)
		    sb.append(s.trim());
	    }
            if (sb.length() > 0) {
                if (debug) {
                    System.out.println("XMLUtil.getValue: sb=" + sb.toString());
                }
                return sb.toString();
            }
	}
	return null;
    }

    public static final void main(String[] args) {
        try {
             URL url = new URL(args[0]);
             HttpURLConnection conn = (HttpURLConnection)url.openConnection();
             conn.setUseCaches(false);
             InputStream is = conn.getInputStream();
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr);
             String buffer;
             while ((buffer = br.readLine()) != null) {
                 System.out.println(buffer);
             }
//             Document doc = parser.parse(args[0]);
//             XmlDocument xdoc = (XmlDocument)doc;
//             xdoc.write(System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
