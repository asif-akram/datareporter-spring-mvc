
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
*The Original Code is OAIHarvesterFunction.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester;

import java.util.Iterator;
import java.util.Properties;
import ORG.oclc.oai.harvester.crosswalk.Crosswalk;
import ORG.oclc.oai.harvester.crosswalk.Crosswalks;
import ORG.oclc.oai.harvester.verb.Identify;
import ORG.oclc.oai.harvester.verb.ListMetadataFormats;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public abstract class OAIHarvesterFunction {
    private Crosswalks crosswalks;
    
    // INSTANCE VARIABLES

    // Note: Implementers must define a constructor that takes a Properties object
    
    public abstract int process(Iterator records, String metadataPrefix, Crosswalk crosswalk)
	throws Exception;
    public abstract void setIdentify(Identify identify);
    public void setListMetadataFormats(ListMetadataFormats listMetadataFormats) {}
    public Crosswalks getCrosswalks() { return crosswalks; }
    public abstract void close();

    public static OAIHarvesterFunction factory(Properties properties)
        throws Throwable {
	OAIHarvesterFunction oaiHarvesterFunction = null;
	String className = properties.getProperty("OAIHarvesterFunction.className");
        if (className == null) {
            throw new ClassNotFoundException("OAIHarvesterFunction.className is missing from properties file");
        }
        
        Class oaiHarvesterFunctionClass = Class.forName(className);
        Constructor oaiHarvesterFunctionConstructor =
            oaiHarvesterFunctionClass.getConstructor(new Class[] {Properties.class});
        try {
            oaiHarvesterFunction =
                (OAIHarvesterFunction)oaiHarvesterFunctionConstructor.newInstance(new Object[]
                    {properties});
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
	oaiHarvesterFunction.crosswalks = new Crosswalks(properties);
	return oaiHarvesterFunction;
    }
}
