
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
*The Original Code is OAIReader.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/



package ORG.oclc.oai.harvester.io;

import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;
import ORG.oclc.oai.harvester.verb.ListRecords;
import ORG.oclc.oai.harvester.verb.Record;
import ORG.oclc.oai.harvester.verb.OAIError;
import org.xml.sax.SAXException;

/**
 * Read records from an OAI-input stream, handling ListRecord requests and resumptionTokens
 * transparently.
 */
public class OAIReader {
    private URL baseURL;
    private String resumptionToken = null;
    private Iterator records = null;

    /**
     * Create an OAI record-input stream.
     *
     * @param baseURL The required baseURL for the OAI repository to be harvested.
     * @param from An optional OAI "from" paramter (null if not present).
     * @param until An optional OAI "until" parameter (null if not present).
     * @param set An optional OAI "set" parameter (null if not present).
     * @param metadataPrefix The required OAI "metadataPrefix" argument.
     *
     * @exception MalformedURLException The baseURL is malformed.
     * @exception SAXException The ListRecords response from the repository was unparsable.
     * @exception IOException An IOException occurred.
     * @exception OAIError The ListRecords response contained an OAI error condition.
     */
    public OAIReader(URL baseURL, String from, 
                     String until, String set, 
                     String metadataPrefix) 
        throws MalformedURLException, SAXException, IOException, OAIError {
        this.baseURL = baseURL;
	// 	System.out.println("new ListRecords");
        ListRecords listRecords = new ListRecords(baseURL, from, until,
						  set, metadataPrefix);
	// 	System.out.println(listRecords);
	if (listRecords != null) {
	    List errors = listRecords.getErrors();
	    if (errors != null) {
		throw (OAIError)errors.get(0); // throw the first one
	    }
	    records = listRecords.iterator();
	    resumptionToken = listRecords.getResumptionToken();
	}
    }

    /**
     * Read a single record.
     *
     * @return A single Record object from a ListRecords response.
     *
     * @exception MalformedURLException Shouldn't occur if the OAIReader constructor was successful
     * @exception SAXException The ListRecords response from the repository was unparsable.
     * @exception IOException An IOException occurred.
     * @exception OAIError The ListRecords response contained an OAI error condition.
     */
    public Record readNext() 
	throws MalformedURLException, SAXException, IOException, OAIError {
	while (records != null && !records.hasNext()) {
	    if (resumptionToken == null) {
		return null;
	    } else {
		ListRecords listRecords = new ListRecords(baseURL, resumptionToken);
		if (listRecords != null) {
		    List errors = listRecords.getErrors();
		    if (errors != null) {
			throw (OAIError)errors.get(0); // throw the first one
		    }
		    records = listRecords.iterator();
		    resumptionToken = listRecords.getResumptionToken();
		} else {
		    return null;
		}
	    }
	}
	if (records == null) return null;
	else return (Record)records.next();
    }

    /**
     * Close the stream.
     */
    public void close() { }
}
