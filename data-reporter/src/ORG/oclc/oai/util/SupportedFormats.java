
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
*The Original Code is SupportedFormats.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.util;

import java.util.*;

/**
 * SupportedFormats contains information about the OAI formats this
 * application supports
 *
 * @author Jeffrey A. Young
 */
public class SupportedFormats {
    private Map supportedFormatsMap = new HashMap();

    /**
     * Constructor for SupportedFormat. Take the list of formats from
     * the specified Properties object.
     *
     * @param properties a properties object containing SupportedFormats entries
     */
    public SupportedFormats(Properties properties) {
        String propertyPrefix = "SupportedFormats.";
        Enumeration enum1 = properties.propertyNames();
        while (enum1.hasMoreElements()) {
            String propertyName = (String)enum1.nextElement();
            if (propertyName.startsWith(propertyPrefix)) {
                String schemaLabel = propertyName.substring(propertyPrefix.length());
                String schemaLocation = (String)properties.get(propertyName);
                supportedFormatsMap.put(schemaLocation, schemaLabel); // reverse the order since that's the most typical use
            }
        }
        if (supportedFormatsMap.size() == 0) {
            System.err.println("SupportedFormats entries are missing from properties file");
        }
    }

    /**
     * Get the metadataPrefix associated with the specified namespace/schema
     *
     * @param namespaceURI the namespaceURI portion of the format specifier
     * @param schemaURL the schemaURL portion of the format specifier
     * @return a String containing the metadataPrefix value associated with this pair
     */
    public String getMetadataPrefix(String namespaceURI, String schemaURL) {
        return (String)supportedFormatsMap.get(namespaceURI + " " + schemaURL);
    }

    /**
     * Get the schemaURL associated with the specified metadataPrefix
     *
     * @param metadataPrefix the prefix desired
     * @return a String containing the schemaURL associated with the metadataPrefix
     */
    public String getSchemaURL(String metadataPrefix) {
        String schemaLocation = getSchemaLocation(metadataPrefix);
        StringTokenizer tokenizer = new StringTokenizer(schemaLocation);
        tokenizer.nextToken();
        return tokenizer.nextToken();
    }
    
    /**
     * Get the namespaceURI associated with the specified metadataPrefix
     *
     * @param metadataPrefix the prefix desired
     * @return a String containing the namespaceURI associated with the metadataPrefix
     */
    public String getNamespaceURI(String metadataPrefix) {
        String schemaLocation = getSchemaLocation(metadataPrefix);
        StringTokenizer tokenizer = new StringTokenizer(schemaLocation);
        return tokenizer.nextToken();
    }
    
    /**
     * Get the namespaceURI/schemaURL associated with the specified metadataPrefix
     *
     * @param metadataPrefix the prefix desired
     * @return a String containing the namespaceURI/schemaURL associated with the metadataPrefix
     */
    public String getSchemaLocation(String metadataPrefix) {
        Iterator iterator = supportedFormatsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            String value = (String)entry.getValue();
            if (value.equals(metadataPrefix)) {
                return (String)entry.getKey();
            }
        }
        return null;
    }

    /**
     * Does the specified metadataPrefix appears in the list of supportedFormats?
     *
     * @param metadataPrefix the prefix desired
     * @return true if prefix is supported, false otherwise.
     */
    public boolean containsValue(String metadataPrefix) {
        return supportedFormatsMap.containsValue(metadataPrefix);
    }

    /**
     * Get an iterator containing Map.Entry's for the supported formats.
     *
     * @return an Iterator containing Map.Entry's for each supported format.
     */
    public Iterator iterator() {
        return supportedFormatsMap.entrySet().iterator();
    }
}
