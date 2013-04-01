
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
*The Original Code is OAIUnionCatalogFunction.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester;

import java.util.Iterator;
import java.util.Properties;
import ORG.oclc.oai.harvester.crosswalk.Crosswalk;
import ORG.oclc.oai.harvester.catalog.AbstractCatalog;
import ORG.oclc.oai.harvester.verb.Record;
import ORG.oclc.oai.harvester.verb.Identify;

public class OAIUnionCatalogFunction extends OAIHarvesterFunction {
    // INSTANCE VARIABLES

    private AbstractCatalog abstractCatalog;
    
    // CONSTRUCTORS
    
    public OAIUnionCatalogFunction(Properties properties)
        throws Throwable {
        abstractCatalog = AbstractCatalog.factory(properties);
    }

    public void setIdentify(Identify identify) { abstractCatalog.setIdentify(identify); }
    
    public int process(Iterator records, String metadataPrefix, Crosswalk crosswalk)
	throws Exception {
	int count = 0;
	while (records.hasNext()) {
	    Record record = (Record)records.next();
	    if (record.isDeleted())
		abstractCatalog.delete(record.getIdentifier());
	    else
		abstractCatalog.write(record, metadataPrefix, crosswalk);
	    count++;
	}
	return count;
    }

    public void close() { abstractCatalog.close(); }
}
