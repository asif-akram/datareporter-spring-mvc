
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
*The Original Code is Error.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester.verb;

// import java.io.*;
// import java.net.*;
// import java.util.*;
// import javax.servlet.*;
// import javax.servlet.http.*;
// import org.w3c.dom.*;
// import org.xml.sax.*;
// import ORG.oclc.oai.harvester.catalog.AbstractCatalog;
// import ORG.oclc.oai.harvester.crosswalk.Crosswalks;
// import ORG.oclc.oai.util.OAIUtil;

/**
 * Represents an OAI Error Verb response. This class is used on both
 * the client-side and on the server-side to represent a Error response
 *
 * @author Jeffrey A. Young, OCLC Online Computer Library Center
 * @deprecated use OAIError instead
 */
public class Error {
    private String code;
    private String message;

    public Error(String code, String message) {
	this.code = code;
	this.message = message;
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(code);
	sb.append(": ");
	sb.append(message);
	return sb.toString();
    }
}
