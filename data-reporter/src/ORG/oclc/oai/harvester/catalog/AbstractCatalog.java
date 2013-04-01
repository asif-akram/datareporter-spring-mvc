
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
*The Original Code is AbstractCatalog.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import ORG.oclc.oai.harvester.crosswalk.Crosswalk;
import ORG.oclc.oai.harvester.crosswalk.Crosswalks;
import ORG.oclc.oai.harvester.verb.Identify;
import ORG.oclc.oai.harvester.verb.Record;
// import ORG.oclc.oai.harvester.verb.CannotDisseminateFormatException;

/**
 * AbstractCatalog is the generic interface between OAICat and any arbitrary
 * database. Implement this interface to have OAICat work with your database.
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 */
public abstract class AbstractCatalog {
    protected Identify identify;

    public void setIdentify(Identify identify) { this.identify = identify; }
    protected Identify getIdentify() { return identify; }
    
    /**
     * Factory method for creating an AbstractCatalog instance. The properties
     * object must contain the following entries:
     * <ul>
     *   <li><b>AbstractCatalog.className</b> property which points to a class
     *       that implements the AbstractCatalog interface. Note that this class
     *       must have a constructor that accepts a properties object as a
     *       parameter.</li>
     *   <li><b>Crosswalks.&lt;supported formats&gt;</b> properties which
     *       satisfy the constructor for the Crosswalks class</li>
     * </ul>
     *
     * @param properties Properties object containing entries necessary to
     * initialize the class
     * to be created.
     * @return on object instantiating the AbstractCatalog interface.
     * @exception Throwable some sort of problem occurred.
     */
    public static AbstractCatalog factory(Properties properties) throws Throwable {
	AbstractCatalog oaiCatalog = null;
	String oaiCatalogClassName =
	    properties.getProperty("AbstractCatalog.oaiCatalogClassName");
        if (oaiCatalogClassName == null) {
            throw new ClassNotFoundException(
                  "AbstractCatalog.oaiCatalogClassName is missing from properties file");
        }
        Class oaiCatalogClass = Class.forName(oaiCatalogClassName);
        Constructor oaiCatalogConstructor =
            oaiCatalogClass.getConstructor(new Class[] {Properties.class});
        try {
            oaiCatalog =
                (AbstractCatalog)oaiCatalogConstructor.newInstance(new Object[]
                    {properties});
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
	return oaiCatalog;
    }

    public abstract void delete(String oaiIdentifier)
	throws IOException;

    public abstract void write(Record record, String metadataPrefix, Crosswalk crosswalk)
	throws IOException;

    /**
     * close the repository
     */
    public abstract void close();
}
