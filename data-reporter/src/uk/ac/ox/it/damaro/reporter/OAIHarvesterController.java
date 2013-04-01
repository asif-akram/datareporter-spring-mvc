package uk.ac.ox.it.damaro.reporter;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import ORG.oclc.oai.harvester.io.OAIReader;
import ORG.oclc.oai.harvester.verb.OAIError;
import ORG.oclc.oai.harvester.verb.Record;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import uk.ac.ox.it.damaro.domain.RegistryQuery;

@Controller
public class OAIHarvesterController {

	@RequestMapping(value = "/registryQueryForm", method = RequestMethod.GET)
	public String registryQueryForm(Model model) {
		model.addAttribute("newRegistryQuery", new RegistryQuery());

		return "registryQueryForm";
	}

	@RequestMapping(value = "/registryQuery", method = RequestMethod.POST)
	public String registryQuery(
			@Valid @ModelAttribute("newRegistryQuery") RegistryQuery newRegistryQuery,
			BindingResult result, Model model) {

		System.out.println(newRegistryQuery.getRegistryBaseURL());

		String recordResults = getMetadataRecords(newRegistryQuery.getRegistryBaseURL(),
				newRegistryQuery.getFrom(), newRegistryQuery.getUntil(), newRegistryQuery.getNumberOfRecords());
		
		model.addAttribute("recordResults", recordResults);
		
		return "registryQuery";

	}

	public String prettyFormat(String input, int indent) {
		try {
			Source xmlInput = new StreamSource(new StringReader(input));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			// This statement works with JDK 6
			transformerFactory.setAttribute("indent-number", indent);

			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Throwable e) {
			// You'll come here if you are using JDK 1.5
			// you are getting an the following exeption
			// java.lang.IllegalArgumentException: Not supported: indent-number
			// Use this code (Set the output property in transformer.
			try {
				Source xmlInput = new StreamSource(new StringReader(input));
				StringWriter stringWriter = new StringWriter();
				StreamResult xmlOutput = new StreamResult(stringWriter);
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(
						"{http://xml.apache.org/xslt}indent-amount",
						String.valueOf(indent));
				transformer.transform(xmlInput, xmlOutput);
				return xmlOutput.getWriter().toString();
			} catch (Throwable t) {
				return input;
			}
		}
	}

	public String getMetadataRecords(String urlString, String from, String until, int numberOfRecords) {
		String recordsResult = "";
		int counter = 1;
		try {
			Record record;
			URL url = new URL(urlString);
			OAIReader oaiReader = new OAIReader(url, from, until,
					(String) null, "oai_dc");

			while ((record = oaiReader.readNext()) != null) {
				recordsResult = recordsResult + " <p> Record number= " + counter++ + "<br />";
				recordsResult = recordsResult + "Record.identifier="
						+ record.getIdentifier() + "\n";
				recordsResult = recordsResult + "Record.datestamp=" + record.getDatestamp() + "<br />";
				Iterator setSpecs = record.getSetSpecs();
				if (setSpecs != null) {
					while (setSpecs.hasNext()) {
						recordsResult = recordsResult + "Record.setSpec=" + setSpecs.next() + "<br />";
					}
				}
				if (record.isDeleted()) {
					recordsResult = recordsResult + "Record is deleted" + "<br />";
				}
				recordsResult = recordsResult + "Record.metadata="
						+ prettyFormat(record.getMetadata(), 3) + "<br />";
				Iterator abouts = record.getAbouts();
				if (abouts != null) {
					while (abouts.hasNext()) {
						recordsResult = recordsResult + "Record.about=" + abouts.next() + " </p> <br /> <br />";
					}
				}
				System.out.println();
				if(counter == numberOfRecords)
					break;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAIError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return recordsResult;
	}

}
