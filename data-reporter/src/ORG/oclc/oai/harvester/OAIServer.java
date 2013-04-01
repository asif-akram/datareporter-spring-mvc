
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
*The Original Code is OAIServer.java______________________________.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester;

import java.io.*;

public class OAIServer implements Serializable {
    private String baseURL;
    private String lastHarvestDate;
    private String setSpec;

    public OAIServer(String baseURL, String lastHarvestDate, String setSpec) {
        if (!baseURL.startsWith("http://"))
            this.baseURL = "http://" + baseURL;
        else
            this.baseURL = baseURL;
	this.lastHarvestDate = lastHarvestDate;
	this.setSpec = setSpec;
    }

    public String getBaseURL() { return baseURL; }
    public String getLastHarvestDate() { return lastHarvestDate; }
    public String getSetSpec() { return setSpec; }
    public void setLastHarvestDate(String lastHarvestDate) {
	this.lastHarvestDate = lastHarvestDate;
    }
}