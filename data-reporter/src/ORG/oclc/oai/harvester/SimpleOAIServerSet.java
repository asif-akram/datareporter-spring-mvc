
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
*The Original Code is OAIServerSet.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import ORG.oclc.util.XMLUtil;

/**
 * A Set of OAIServer objects loaded according to
 * specified Properties.
 */
public class SimpleOAIServerSet extends OAIServerSet {
    private static final String USAGE =
	"java SimpleOAIServerSet <in.xml> <out.ser>";

    private String collectionFileName;
    private static XMLUtil xmlUtil = new XMLUtil(false, false);
    
    public SimpleOAIServerSet(Properties properties, String baseURL, String setSpec)
        throws FileNotFoundException {
	super(getCollection(properties, baseURL, setSpec));
	collectionFileName =
	    properties.getProperty("SimpleOAIServerSet.collectionFileName");
        if (collectionFileName == null) {
            throw new
                FileNotFoundException("SimpleOAIServerSet.collectionFileName is missing from properties file");
        }
    }

    public void store () throws IOException {
        /* write the collection as a ser output file */
        FileOutputStream fos = new FileOutputStream(collectionFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(getCollection());
        oos.close();
        fos.close();
    }

    private static Collection getCollection(Properties properties,
                                     String baseURL, String setSpec)
        throws FileNotFoundException {
	Collection collection = null;
	String collectionFileName =
	    properties.getProperty("SimpleOAIServerSet.collectionFileName");
        if (collectionFileName == null) {
            throw new
                FileNotFoundException("SimpleOAIServerSet.collectionFileName is missing from properties file");
        }
	if (baseURL != null) {
	    collection = new ArrayList(1);
	    collection.add(new OAIServer(baseURL, null, setSpec));
	} else {
	    try {
		FileInputStream fis = new FileInputStream(collectionFileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		collection = (Collection)ois.readObject();
		ois.close();
		fis.close();
            } catch (FileNotFoundException e) {
                throw e;
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}
	return collection;
    }

    public static final void main(String[] args) {
	if (args.length < 2) {
	    System.out.println(USAGE);
	    return;
	}
	String inFileName = args[0];
	String outFileName = args[1];

// 	/* Construct an xml parser */
// 	DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
	try {
	    /* read the xml input Document */
	    FileInputStream fis = new FileInputStream(inFileName);
	    Document inDoc = xmlUtil.parse(fis);
	    fis.close();
	    
	    /* create a collection of OAIServer Documents */
	    int numRecs = xmlUtil.getSize(inDoc, "OAIServer");
	    Collection collection = new ArrayList(numRecs);
	    for (int i=0; i<numRecs; ++i) {
		Element inEl = xmlUtil.getElement(inDoc, "OAIServer", i);
		String baseURL = xmlUtil.getValue(inEl, "baseURL");
		String setSpec = xmlUtil.getValue(inEl, "setSpec");
		String lastHarvestDate = xmlUtil.getValue(inEl,
							  "lastHarvestDate");
		System.out.println("baseURL: " + baseURL +
				   " lastHarvestDate: " + lastHarvestDate +
				   " setSpec: " + setSpec);
		collection.add(new OAIServer(baseURL, lastHarvestDate, setSpec));
	    }
	    
	    /* write the collection as a ser output file */
	    FileOutputStream fos = new FileOutputStream(outFileName);
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    oos.writeObject(collection);
	    oos.close();
	    fos.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (SAXException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
