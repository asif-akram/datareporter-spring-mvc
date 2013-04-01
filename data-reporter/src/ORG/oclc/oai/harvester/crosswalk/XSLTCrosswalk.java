
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
*The Original Code is XSLTCrosswalk.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.crosswalk;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import ORG.oclc.oai.harvester.verb.CannotCrosswalkFormatException;
import ORG.oclc.oai.harvester.verb.Record;

/**
 * Convert native "item" to oai_etdms. In this case, the native "item"
 * is assumed to already be formatted as an OAI <record> element,
 * with the possible exception that multiple metadataFormats may
 * be present in the <metadata> element. The "crosswalk", merely
 * involves pulling out the one that is requested.
 */
public class XSLTCrosswalk extends Crosswalk {
    private Transformer transformer = null;
    
    /**
     * The constructor assigns the schemaLocation associated with this crosswalk. Since
     * the crosswalk is trivial in this case, no properties are utilized.
     *
     * @param properties properties that are needed to configure the crosswalk.
     */
    public XSLTCrosswalk(Properties properties) throws IllegalArgumentException {
	super(properties.getProperty("XSLTCrosswalk.schemaLocation"));
        String xsltPath = properties.getProperty("XSLTCrosswalk.xsltPath");
        if (xsltPath != null) {
            try {
                StreamSource xslSource = new StreamSource(new FileInputStream(xsltPath));
                TransformerFactory tFactory = TransformerFactory.newInstance();
                this.transformer = tFactory.newTransformer(xslSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAvailableFor(Record record) {
	return true;
    }
    
    /**
     * createMetadata performs the actual crosswalk.
     *
     * @param nativeItem the native "item". In this case, it is
     * already formatted as an OAI <record> element, with the
     * possible exception that multiple metadataFormats are
     * present in the <metadata> element.
     * @return a String containing the XML <record>.
     */
    public Object toItem(Record record)
	throws CannotCrosswalkFormatException {
        String xmlRec = record.getMetadata();
        if (transformer == null) {
            return xmlRec;
        } else {
            if (xmlRec.startsWith("<?")) {
                int offset = xmlRec.indexOf("?>");
                xmlRec = xmlRec.substring(offset+2);
            }
            StringReader stringReader = new StringReader(xmlRec);
            StreamSource streamSource = new StreamSource(stringReader);
            StringWriter stringWriter = new StringWriter();
            try {
                transformer.transform(streamSource, new StreamResult(stringWriter));
                return stringWriter.toString();
            } catch (TransformerException e) {
                e.printStackTrace();
                throw new CannotCrosswalkFormatException();
            }
        }
    }
}
