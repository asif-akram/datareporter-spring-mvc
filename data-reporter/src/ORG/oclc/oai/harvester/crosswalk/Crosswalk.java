
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
*The Original Code is Crosswalk.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.crosswalk;

import java.util.StringTokenizer;
import ORG.oclc.oai.harvester.verb.CannotCrosswalkFormatException;
import ORG.oclc.oai.harvester.verb.Record;

/**
 * Converts an OAI "record" to a native "item" for an OAI metadataFormat.
 * 
 */
public abstract class Crosswalk {
    /**
     * The schemaLocation supported by this crosswalk
     */
    private String schemaLocation;

    /**
     * Constructor
     *
     * @param schemaLocation the schemaLocation supported by this crosswalk
     */
    public Crosswalk(String schemaLocation) { this.schemaLocation = schemaLocation; }

    /**
     * returns the schemaLocation
     *
     * @return the schemaLocation
     */
    public String getSchemaLocation() { return schemaLocation; }

    /**
     * parse the schemaURL from the schemaLocation
     *
     * @return the schemaURL portion of the schemaLocation
     */
    public String getSchemaURL() {
	StringTokenizer tokenizer = new StringTokenizer(schemaLocation, " ");
	tokenizer.nextToken();
	return tokenizer.nextToken();
    }

    public abstract boolean isAvailableFor(Record record);
    
    /**
     * The crosswalk method
     *
     * @param the native "item" to be converted
     * @return the XML string ready to be inserted into the OAI <metadata> element
     */
    public abstract Object toItem(Record record)
	throws CannotCrosswalkFormatException;

    public String toString() {
	return schemaLocation;
    }
}
