
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
*The Original Code is OAIUtil.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.util;

import java.net.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.Document;
import org.xml.sax.*;

public class OAIUtil {
    public static String xmlEncode(String s) {
        StringBuffer sb = new StringBuffer();

        for (int i=0; i<s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
    
    // HTTP FORM ACTION METHODS
    
//     public static HttpURLConnection get(URL baseURL, String query)
// 	throws MalformedURLException, IOException, SAXException {
//         URL u = new URL(baseURL.toString() + "?" + query);
//         HttpURLConnection connection = (HttpURLConnection)u.openConnection();
//         connection.setUseCaches(false);
//         return connection;
//     }

//     public static HttpURLConnection post(URL baseURL, String query)
// 	throws SAXException, IOException {
// 	HttpURLConnection http = (HttpURLConnection)baseURL.openConnection();
// 	http.setDoInput(true);
// 	http.setDoOutput(true);
// 	http.setUseCaches(false);
// 	http.setRequestProperty("Content-Type",
// 				"application/x-www-form-urlencoded");
// 	OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream(),
// 							"ASCII");
// 	out.write(query);
// 	out.flush();
// 	out.close();
//         return http;
//     }
    
    public static String getLCCN(String positionalLCCN) {
	StringBuffer sb = new StringBuffer();
	if (Character.isDigit(positionalLCCN.charAt(2))) {
	    sb.append(positionalLCCN.substring(0, 2).trim());
	    sb.append(positionalLCCN.substring(2, 6));
	    sb.append("-");
	    int i = Integer.parseInt(positionalLCCN.substring(6).trim());
	    sb.append(Integer.toString(i));
	} else {
	    sb.append(positionalLCCN.substring(0, 3).trim());
	    sb.append(positionalLCCN.substring(3, 5));
	    sb.append("-");
	    int i = Integer.parseInt(positionalLCCN.substring(5).trim());
	    sb.append(Integer.toString(i));
	}
	return sb.toString();
    }
}
