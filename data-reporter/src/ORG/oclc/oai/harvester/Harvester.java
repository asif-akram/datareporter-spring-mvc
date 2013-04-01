
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
*The Original Code is Harvester.java.
*The Initial Developer of the Original Code is Jeff Young.
*Portions created by ______________________ are
*Copyright (C) _____ _______________________. All Rights Reserved.
*Contributor(s):______________________________________.
*/

package ORG.oclc.oai.harvester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import org.xml.sax.SAXException;
import ORG.oclc.oai.harvester.verb.Identify;
import ORG.oclc.oai.harvester.verb.ListMetadataFormats;
import ORG.oclc.oai.harvester.verb.ListRecords;
import ORG.oclc.oai.harvester.verb.GetRecord;
import ORG.oclc.oai.harvester.crosswalk.Crosswalk;

/**
 * OAI Harvester
 */
public class Harvester {
    private static final boolean debug = false;
    
    // CLASS VARIABLES

    private static final String USAGE =
	"java Harvester [-p propertiesfile] [-u URL [-r resumptionToken -m metadataPrefix] " +
	"[-i identifierListFileName -m metadataPrefix]] " +
	"[-f lastHarvestDateOverride] [-t untilDateOverride]\n" +
        "(The default propertiesfile is 'harvester.properties')";
    
    // INSTANCE VARIABLES

    private URL baseURL;
    private String setSpec;
    private String lastHarvestDate;
    private String untilDate;
    private OAIHarvesterFunction oaiHarvesterFunction;
    private int numRecsProcessed = 0;
    private Identify identify;
    private ListMetadataFormats listMetadataFormats;
    
    // CONSTRUCTORS

    /**
     * Construct a Harvester object to perform an incremental harvest
     */
    public Harvester(OAIHarvesterFunction oaiHarvesterFunction,
                     OAIServer oaiServer,
		     String lastHarvestDateOverride,
                     String untilDateOverride)
	throws MalformedURLException {
        this.oaiHarvesterFunction = oaiHarvesterFunction;
	setSpec = oaiServer.getSetSpec();
	baseURL = new URL(oaiServer.getBaseURL());
	if (!baseURL.getProtocol().toLowerCase().startsWith("http")) {
	    throw new
		IllegalArgumentException("Harvester only works for http URLs");
	}
        try {
	    // Identify the server
  	    identify = new Identify(baseURL);
            if (debug) {
                System.out.println("Harvester.Harvester: identify=" + identify.getResponseDate());
            }
            listMetadataFormats = new ListMetadataFormats(baseURL);
	    oaiHarvesterFunction.setIdentify(identify);
            oaiHarvesterFunction.setListMetadataFormats(listMetadataFormats);
        } catch (FileNotFoundException e) {
            System.err.print("Server not found: ");
            System.err.println(baseURL);
            return;
        } catch (ConnectException e) {
            System.err.print(e.getMessage());
            System.err.print(": ");
            System.err.println(baseURL);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
	if (lastHarvestDateOverride != null) {
	    lastHarvestDate = lastHarvestDateOverride;
	} else if ((lastHarvestDate = oaiServer.getLastHarvestDate()) == null) {
	    lastHarvestDate = identify.getEarliestDatestamp();
	}
	lastHarvestDate = fixGranularity(lastHarvestDate, identify.getGranularity());
	untilDate = untilDateOverride;
        if (debug) {
            System.err.println("Harvester.Harvester: untilDateOverride='"
                               + untilDateOverride + "'");
        }
	
	// Determine the responseDate date
	String responseDate = identify.getResponseDate();
	
	// Determine the 'until' date
	if (untilDate == null) {
	    untilDate = fixGranularity(responseDate, identify.getGranularity());
            if (debug) {
                System.err.println("Harvseter.Harvester: responseDate=" + responseDate);
                System.err.println("Harvester.Harvester: granularity="
                                   + identify.getGranularity());
                System.err.println("Harvester.Harvester: untilDate='" + untilDate + "'");
            }
	}
    }

    public String getBaseURL() { return baseURL.toString(); }
    public String getSetSpec() { return setSpec; }
    public String getUntilDate() { return untilDate; }
    public int getNumRecsProcessed() { return numRecsProcessed; }

    private String fixGranularity(String utcDate, String supportedGranularity) {
	StringBuffer sb = new StringBuffer(utcDate);
	int supportedLen = supportedGranularity.length();

	if (utcDate.length() > supportedLen) {
	    switch (supportedLen) {
	    case 10:
		sb.setLength(supportedLen);
		break;
	    case 17:
		sb.setLength(supportedLen-1);
		sb.append("Z");
		break;
	    case 22:
		sb.setLength(supportedLen-1);
		sb.append(".0Z");
		break;
	    case 20:
	    default:
		break;
	    }
	}
	return sb.toString();
    }

    public boolean run(String resumptionTokenOverride, String metadataPrefix) {
	boolean success = true; // assume all will go well
	
	if (resumptionTokenOverride == null) return run();
	else try {
	    Crosswalk crosswalk
		= oaiHarvesterFunction.getCrosswalks().getCrosswalk(metadataPrefix);
	    ListRecords listRecords;

	    try {
		listRecords = new ListRecords(baseURL, resumptionTokenOverride);
		if (debug) {
		    System.out.println("Harvester.run: listRecords=" + listRecords);
		}
	    } catch (SAXException e) {
		// assume this is because the metadataPrefix
		// isn't supported by this server
		listRecords = null;
		success = false;
	    }
	    if (debug)
		System.out.println("looping through record elements");
	    int retriesLeft = 10;
	    while (listRecords != null) {
		ArrayList errors = listRecords.getErrors();
                if (debug) System.err.println("Harvester.run: errors=" + errors);
		if (errors != null) {
		    Iterator iterator = errors.iterator();
		    while (iterator.hasNext()) System.out.println(iterator.next());
		}
		Iterator records = listRecords.iterator();
		numRecsProcessed += oaiHarvesterFunction.process(records, metadataPrefix,
								 crosswalk);
		// loop if resumptionToken is present
		String resumptionToken = listRecords.getResumptionToken();
		if (resumptionToken != null) {
		    System.out.println(resumptionToken);
		    listRecords = null;
		    while (listRecords == null) {
			try {
			    listRecords = new ListRecords(baseURL,
							  resumptionToken);
			} catch (FileNotFoundException e) {
			    // Server returned a ResponseCode >= 400
			    // This should never happen here because
			    // in this situation it's probably because
			    // the metadataPrefix isn't supported, in
			    // which case, it would have been caught
			    // the first time listRecords() was called.
			    if (debug) {
				System.out.println("STUPID JAVA EXCEPTION");
			    }
			    e.printStackTrace();
			    System.err.print("Abandoning:\nbaseURL:");
			    System.err.println(baseURL.toString());
			    System.err.print("lastHarvestDate:");
			    System.err.println(lastHarvestDate);
			    System.err.print("untilDate:");
			    System.err.println(untilDate);
			    System.err.print("setSpec:");
			    System.err.println(setSpec);
			    System.err.print("metadataPrefix:");
			    System.err.println(metadataPrefix);
			    System.err.print("resumptionToken:");
			    System.err.println(resumptionToken);
			    success = false;
			    break;
			} catch (ConnectException e) {
			    e.printStackTrace();
			    System.err.print("retries left: ");
			    System.err.println(retriesLeft);
			    if (retriesLeft == 0) {
				System.err.print("Abandoning:\nbaseURL:");
				System.err.println(baseURL.toString());
				System.err.print("lastHarvestDate:");
				System.err.println(lastHarvestDate);
				System.err.print("untilDate:");
				System.err.println(untilDate);
				System.err.print("setSpec:");
				System.err.println(setSpec);
				System.err.print("metadataPrefix:");
				System.err.println(metadataPrefix);
				System.err.print("resumptionToken:");
				System.err.println(resumptionToken);
				success = false;
				break;
			    } else {
				System.err.println("sleep for 10 seconds...");
				Thread.currentThread().sleep(10000);
				retriesLeft--;
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			    System.err.print("Abandoning:\nbaseURL:");
			    System.err.println(baseURL.toString());
			    System.err.print("lastHarvestDate:");
			    System.err.println(lastHarvestDate);
			    System.err.print("untilDate:");
			    System.err.println(untilDate);
			    System.err.print("setSpec:");
			    System.err.println(setSpec);
			    System.err.print("metadataPrefix:");
			    System.err.println(metadataPrefix);
			    System.err.print("resumptionToken:");
			    System.err.println(resumptionToken);
			    success = false;
			    break;
			}
		    }
		} else {
		    listRecords = null;
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    success = false;
        }
	return success;
    }
    // Perform the harvest
    
    public boolean run() {
	boolean success = true; // assume all will go well
	
	try {
            Iterator crosswalks = oaiHarvesterFunction.getCrosswalks().iterator();
            while (crosswalks.hasNext()) {
                Map.Entry entry = (Map.Entry)crosswalks.next();
                String metadataPrefix = (String)entry.getKey();
		Crosswalk crosswalk = (Crosswalk)entry.getValue();
                if (debug)
                    System.out.println("harvesting metadataPrefix=" + metadataPrefix);
                ListRecords listRecords;
                try {
                    listRecords = new ListRecords(baseURL, lastHarvestDate,
						  untilDate, setSpec, metadataPrefix);
		    if (debug) {
			System.out.println("Harvester.run: listRecords=" + listRecords);
		    }
                } catch (SAXException e) {
                    // assume this is because the metadataPrefix
                    // isn't supported by this server
                    listRecords = null;
                }
                if (debug)
                    System.out.println("looping through record elements");
		int retriesLeft = 10;
                while (listRecords != null) {
		    ArrayList errors = listRecords.getErrors();
                    if (debug) System.err.println("Harvester.run: errors=" + errors);
		    if (errors != null) {
			Iterator iterator = errors.iterator();
			while (iterator.hasNext()) System.out.println(iterator.next());
		    }
                    Iterator records = listRecords.iterator();
		    numRecsProcessed += oaiHarvesterFunction.process(records, metadataPrefix,
								     crosswalk);
                    // loop if resumptionToken is present
                    String resumptionToken = listRecords.getResumptionToken();
                    if (resumptionToken != null) {
			System.out.println(resumptionToken);
			listRecords = null;
			while (listRecords == null) {
			    try {
				listRecords = new ListRecords(baseURL,
							      resumptionToken);
			    } catch (FileNotFoundException e) {
				// Server returned a ResponseCode >= 400
				// This should never happen here because
				// in this situation it's probably because
				// the metadataPrefix isn't supported, in
				// which case, it would have been caught
				// the first time listRecords() was called.
				if (debug) {
				    System.out.println("STUPID JAVA EXCEPTION");
				}
				e.printStackTrace();
				System.err.print("Abandoning:\nbaseURL:");
				System.err.println(baseURL.toString());
				System.err.print("lastHarvestDate:");
				System.err.println(lastHarvestDate);
				System.err.print("untilDate:");
				System.err.println(untilDate);
				System.err.print("setSpec:");
				System.err.println(setSpec);
				System.err.print("metadataPrefix:");
				System.err.println(metadataPrefix);
				System.err.print("resumptionToken:");
				System.err.println(resumptionToken);
				success = false;
				break;
			    } catch (ConnectException e) {
				e.printStackTrace();
				System.err.print("retries left: ");
				System.err.println(retriesLeft);
				if (retriesLeft == 0) {
				    System.err.print("Abandoning:\nbaseURL:");
				    System.err.println(baseURL.toString());
				    System.err.print("lastHarvestDate:");
				    System.err.println(lastHarvestDate);
				    System.err.print("untilDate:");
				    System.err.println(untilDate);
				    System.err.print("setSpec:");
				    System.err.println(setSpec);
				    System.err.print("metadataPrefix:");
				    System.err.println(metadataPrefix);
				    System.err.print("resumptionToken:");
				    System.err.println(resumptionToken);
				    success = false;
				    break;
				} else {
				    System.err.println("sleep for 10 seconds...");
				    Thread.currentThread().sleep(10000);
				    retriesLeft--;
				}
			    } catch (Exception e) {
				e.printStackTrace();
				System.err.print("Abandoning:\nbaseURL:");
				System.err.println(baseURL.toString());
				System.err.print("lastHarvestDate:");
				System.err.println(lastHarvestDate);
				System.err.print("untilDate:");
				System.err.println(untilDate);
				System.err.print("setSpec:");
				System.err.println(setSpec);
				System.err.print("metadataPrefix:");
				System.err.println(metadataPrefix);
				System.err.print("resumptionToken:");
				System.err.println(resumptionToken);
				success = false;
				break;
			    }
			}
                    } else {
                        listRecords = null;
                    }
                }
            }
	} catch (Exception e) {
	    e.printStackTrace();
	    success = false;
        }
	return success;
    }

    public boolean run(Iterator identifierOverride, String metadataPrefix) {
	boolean success = true; // assume all will go well
	
	try {
	    Crosswalk crosswalk
		= oaiHarvesterFunction.getCrosswalks().getCrosswalk(metadataPrefix);
	    GetRecord getRecord;

	    try {
		getRecord = new GetRecord(baseURL, (String)identifierOverride.next(),
					  metadataPrefix);
		if (debug) {
		    System.out.println("Harvester.run: getRecord=" + getRecord);
		}
	    } catch (SAXException e) {
		// assume this is because the metadataPrefix
		// isn't supported by this server
		getRecord = null;
	    }
	    if (debug)
		System.out.println("looping through records");
	    int retriesLeft = 10;
	    while (getRecord != null) {
		ArrayList errors = getRecord.getErrors();
                if (debug) System.err.println("Harvester.run: errors=" + errors);
		if (errors != null) {
		    Iterator iterator = errors.iterator();
		    while (iterator.hasNext()) System.out.println(iterator.next());
		}
		Iterator records = getRecord.iterator();
		numRecsProcessed += oaiHarvesterFunction.process(records, metadataPrefix,
								 crosswalk);
		if (identifierOverride.hasNext()) {
		    getRecord = null;
		    while (getRecord == null) {
			try {
			    getRecord = new GetRecord(baseURL, (String)identifierOverride.next(),
						      metadataPrefix);
			} catch (FileNotFoundException e) {
			    // Server returned a ResponseCode >= 400
			    // This should never happen here because
			    // in this situation it's probably because
			    // the metadataPrefix isn't supported, in
			    // which case, it would have been caught
			    // the first time getRecord() was called.
			    if (debug) {
				System.out.println("STUPID JAVA EXCEPTION");
			    }
			    e.printStackTrace();
			    System.err.print("Abandoning:\nbaseURL:");
			    System.err.println(baseURL.toString());
			    System.err.print("lastHarvestDate:");
			    System.err.println(lastHarvestDate);
			    System.err.print("untilDate:");
			    System.err.println(untilDate);
			    System.err.print("setSpec:");
			    System.err.println(setSpec);
			    System.err.print("metadataPrefix:");
			    System.err.println(metadataPrefix);
			    success = false;
			    break;
			} catch (ConnectException e) {
			    e.printStackTrace();
			    System.err.print("retries left: ");
			    System.err.println(retriesLeft);
			    if (retriesLeft == 0) {
				System.err.print("Abandoning:\nbaseURL:");
				System.err.println(baseURL.toString());
				System.err.print("lastHarvestDate:");
				System.err.println(lastHarvestDate);
				System.err.print("untilDate:");
				System.err.println(untilDate);
				System.err.print("setSpec:");
				System.err.println(setSpec);
				System.err.print("metadataPrefix:");
				System.err.println(metadataPrefix);
				success = false;
				break;
			    } else {
				System.err.println("sleep for 10 seconds...");
				Thread.currentThread().sleep(10000);
				retriesLeft--;
			    }
			} catch (Exception e) {
			    e.printStackTrace();
			    System.err.print("Abandoning:\nbaseURL:");
			    System.err.println(baseURL.toString());
			    System.err.print("lastHarvestDate:");
			    System.err.println(lastHarvestDate);
			    System.err.print("untilDate:");
			    System.err.println(untilDate);
			    System.err.print("setSpec:");
			    System.err.println(setSpec);
			    System.err.print("metadataPrefix:");
			    System.err.println(metadataPrefix);
			    success = false;
			    break;
			}
		    }
		} else {
		    getRecord = null;
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    success = false;
        }
	return success;
    }
    // Perform the harvest
    
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append(baseURL);
	sb.append(" will be harvested from ");
	sb.append(lastHarvestDate);
	sb.append(" up to ");
	sb.append(untilDate);
	sb.append("\n\n");
	sb.append(identify.toString());
	sb.append("\n\n");
	sb.append(listMetadataFormats.toString());
	sb.append("\n");
	return sb.toString();
    }


    /**
     * Perform an incremental harvest against a set of OAI servers
     * that are being tracked in the configured OAIServerSet.
     */
    public static final void main(String[] args) {
	String baseURL = null;
	String setSpec = null;
	String propertiesFileName = "harvester.properties";
	String lastHarvestDateOverride = null;
	String untilDateOverride = null;
	String resumptionTokenOverride = null;
	String metadataPrefixOverride = null;
	ArrayList identifierOverride = null;

	for (int i=0; i<args.length; ++i) {
	    try {
		if (args[i].equals("-p")) {
		    propertiesFileName = args[++i];
		} else if (args[i].equals("-u")) {
		    baseURL = args[++i];
		} else if (args[i].equals("-m")) {
		    metadataPrefixOverride = args[++i];
		} else if (args[i].equals("-s")) {
		    setSpec = args[++i];
		} else if (args[i].equals("-f")) {
		    lastHarvestDateOverride = args[++i];
		} else if (args[i].equals("-t")) {
		    untilDateOverride = args[++i];
		} else if (args[i].equals("-r")) {
		    resumptionTokenOverride = args[++i];
		} else if (args[i].equals("-i")) {
		    identifierOverride = new ArrayList();
		    try {
			FileReader fr = new FileReader(args[++i]);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
			    line = line.trim();
			    if (line.length() > 0) {
				identifierOverride.add(line);
			    }
			}
			br.close();
			fr.close();
		    } catch (Exception e) {
			e.printStackTrace();
			return;
		    }
		} else {
		    System.out.println(USAGE);
		    return;
		}
	    } catch (ArrayIndexOutOfBoundsException e) {
		System.out.println(USAGE);
		return;
	    }
	}

	if (resumptionTokenOverride != null && baseURL == null) {
	    System.out.println("The -r parameter can only be used in association with the -u parameter");
	    System.out.println(USAGE);
	    return;
	}

	if (identifierOverride != null && baseURL == null) {
	    System.out.println("The -i parameter can only be used in association with the -u parameter");
	    System.out.println(USAGE);
	    return;
	}

	if ((resumptionTokenOverride != null && metadataPrefixOverride == null)) {
	    System.out.println("The -m parameter must be used in conjunction with the -r parameter");
	    System.out.println(USAGE);
	    return;
	}

	if ((identifierOverride != null && metadataPrefixOverride == null)) {
	    System.out.println("The -m parameter must be used in conjunction with the -i parameter");
	    System.out.println(USAGE);
	    return;
	}

	// grab the required properties file
	Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(propertiesFileName));
        } catch (Exception e) {
            System.err.println("Failed to load properties: " + e.getMessage());
            return;
        }

        // create a function object
        // load the server collection
        OAIHarvesterFunction oaiHarvesterFunction;
	OAIServerSet oaiServerSet;
        try {
            oaiHarvesterFunction = OAIHarvesterFunction.factory(properties);
            oaiServerSet = OAIServerSet.factory(properties, baseURL, setSpec);
        } catch (ClassNotFoundException e) {
	    e.printStackTrace();
            System.err.println(e.getMessage());
            return;
        } catch (FileNotFoundException e) {
	    e.printStackTrace();
            System.err.println(e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
	    e.printStackTrace();
            System.err.println(e.getMessage());
            return;
        } catch (Throwable e) {
	    e.printStackTrace();
            e.printStackTrace();
            return;
        }
        
	// harvest the Set of OAI servers in a loop
	Iterator oaiServerSetIterator = oaiServerSet.iterator();
        if (oaiServerSetIterator == null) {
            System.err.println("Failed to retrieve OAIServerSet iterator");
            return;
        }
	
	while (oaiServerSetIterator.hasNext()) {
	    OAIServer oaiServer = (OAIServer)oaiServerSetIterator.next();
	    try {
		System.out.println("Start: " + new Date());
		Harvester harvester = new Harvester(oaiHarvesterFunction,
						    oaiServer,
						    lastHarvestDateOverride,
						    untilDateOverride);
		System.out.println(harvester);
		if (identifierOverride != null) {
		    if (harvester.run(identifierOverride.iterator(), metadataPrefixOverride)) {
			oaiServer.setLastHarvestDate(harvester.getUntilDate());
		    } else {
			System.out.println("Harvest failed.");
		    }
		} else {
		    if (harvester.run(resumptionTokenOverride, metadataPrefixOverride)) {
			oaiServer.setLastHarvestDate(harvester.getUntilDate());
		    } else {
			System.out.println("Harvest failed.");
		    }
		}
                System.out.print(harvester.getNumRecsProcessed());
                System.out.print(" records processed from ");
                System.out.print(harvester.getBaseURL());
		System.out.print(" (setSpec='");
		System.out.print(harvester.getSetSpec());
		System.out.println("')");
		System.out.println("  End: " + new Date());
	    } catch (MalformedURLException e) {
		System.out.println(e);
		System.out.print("Harvest failed for baseURL: '");
		System.out.print(oaiServer.getBaseURL());
		System.out.println("'");
	    }
	}
	// close
        try {
            if (baseURL == null) { // don't store it if it was specified from the command line
                oaiServerSet.store();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	oaiHarvesterFunction.close();
    }
}
