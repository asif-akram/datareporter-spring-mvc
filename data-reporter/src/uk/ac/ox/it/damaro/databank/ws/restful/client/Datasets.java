/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ox.it.damaro.databank.ws.restful.client;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

import javax.xml.namespace.NamespaceContext;
import org.apache.commons.lang.StringUtils;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * 
 * @author Asif Akram
 */
public class Datasets {

	/*
	 * /{silo name}/datasets text/html
	 */
	public void getAllDataSetsAsHTML(WebResource webResource, String datasets) {
		String response = webResource.path(datasets)
				.accept(MediaType.TEXT_HTML_TYPE).get(String.class);
		System.out.println(response);
	}

	/*
	 * /{silo name}/datasets JSON
	 */
	public void getAllDataSetsAsJSON(WebResource webResource, String datasets) {
		String response = webResource.path(datasets)
				.accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}

	/*
	 * /{silo name}/datasets -> id = {id to create}, embargoed = {true|false},
	 * embargoed_until = {ISO8601 date}, title={(Optional)}
	 */
	public void createDataSet(WebResource webResource, String datasets,
			String datasetIDValue, String embargoedValue,
			String embargoed_untilValue, String title_OptionalValue) {
		if (datasetIDValue == null) {
			return;
		}
		// ClientResponse response = new ClientResponse();
		String datasetIDKey = "id";
		String embargoedKey = "embargoed";
		String embargoed_untilKey = "embargoed_until";
		String title_OptionalKey = "title";

		Form newForm = new Form();
		newForm.add(datasetIDKey, datasetIDValue);

		if (embargoedValue != null)
			newForm.add(embargoedKey, embargoedValue);

		if (embargoed_untilValue != null)
			newForm.add(embargoed_untilKey, embargoed_untilValue);

		if (title_OptionalValue != null)
			newForm.add(title_OptionalKey, title_OptionalValue);

		ClientResponse response = webResource.path(datasets)
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.post(ClientResponse.class, newForm);

		System.out.println(response.getStatus());
	}

	/*
	 * /{silo name}/datasets/{id}
	 */
	public void createDataSet(WebResource webResource, String datasets,
			String datasetIDValue) {
		String response = webResource.path(datasets).path(datasetIDValue)
				.accept(MediaType.APPLICATION_JSON).post(String.class);
		System.out.println(response);
	}

	/*
	 * /{silo name}/datasets/{id}
	 */
	public void getDataSetAsHTML(WebResource webResource, String datasets,
			String datasetIDValue) {
		String response = webResource.path(datasets).path(datasetIDValue)
				.accept(MediaType.TEXT_HTML_TYPE).get(String.class);
		System.out.println(response);
	}

	/*
	 * /{silo name}/datasets/{id}
	 */
	public void getDataSetAsJSON(WebResource webResource, String datasets,
			String datasetIDValue) {
		String response = webResource.path(datasets).path(datasetIDValue)
				.accept(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
				.get(String.class);
		System.out.println(response);
	}

	/*
	 * /{silo name}/datasets/{id}
	 */
	public String[] getDataSetAsXML(WebResource webResource, String datasets,
			String datasetIDValue) {
		String response = webResource.path(datasets).path(datasetIDValue)
				.accept(MediaType.TEXT_XML).get(String.class);
		System.out.println(response);

		return parseXML(response);
	}

	/*
	 * /{silo name}/datasets/{id} Embargo-controlChange embargo information on
	 * dataset id
	 * 
	 * Parameters-> embargo_change = true, embargoed = {true|false},
	 * embargoed_until = {ISO8601 date} Returns:JSON-encoded statement of new
	 * embargo information with a 200 on success.
	 */
	public void updateDataSet(WebResource webResource, String datasets,
			String datasetIDValue, String embargoedValue,
			String embargoed_untilValue, String title_OptionalValue) {
		if (datasetIDValue == null) {
			return;
		}
		// ClientResponse response = new ClientResponse();
		String embargo_changeKey = "embargo_change";
		String embargoedKey = "embargoed";
		String embargoed_untilKey = "embargoed_until";
		String title_OptionalKey = "title";

		Form newForm = new Form();
		newForm.add(embargo_changeKey, "true");

		if (embargoedValue != null)
			newForm.add(embargoedKey, embargoedValue);

		if (embargoed_untilValue != null)
			newForm.add(embargoed_untilKey, embargoed_untilValue);

		if (title_OptionalValue != null)
			newForm.add(title_OptionalKey, title_OptionalValue);

		ClientResponse response = webResource.path(datasets)
				.path(datasetIDValue)
				.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
				.post(ClientResponse.class, newForm);

		System.out.println(response.getStatus());
	}

	/*
	 * /{silo name}/datasets/{id} File-upload:Upload file to root directory
	 * 
	 * Parameters-> file = Multipart-encoded (HTML) file upload, filename =
	 * {Optional filename for upload} Returns:Accept: text/html - 302 redirect
	 * to top-level dataset view on success
	 */
	public void dataSetFileUpload(WebResource webResource, String datasets,
			String datasetIDValue, String fileURL) {
		webResource = webResource.path(datasets).path(datasetIDValue);
		doFileAttachmentTest(webResource, fileURL);
	}

	/*
	 * /{silo name}/datasets/{id} DELETE Deletes the dataset id - 200 on
	 * success, 404 if id doesn’t exist
	 */
	public void deleteDataset(WebResource webResource, String datasets,
			String datasetIDValue) {
		ClientResponse response = webResource.path(datasets)
				.path(datasetIDValue).delete(ClientResponse.class);
		System.out.println(response.getStatus());
	}

	/*
	 * /{silo name}/datasets/{id}/{subpath}
	 */
	public void getDataSetWithSubPathAsHTML(WebResource webResource,
			String datasets, String datasetIDValue, String subPathValue) {
		webResource = webResource.path(datasets).path(datasetIDValue)
				.path(subPathValue);
		ClientResponse response = webResource.accept(MediaType.TEXT_HTML_TYPE)
				.get(ClientResponse.class);
		System.out.println(response.getStatus());
	}

	/*
	 * /{silo name}/datasets/{id}/{subpath}
	 */
	public void getDataSetWithSubPathAsJSON(WebResource webResource,
			String datasets, String datasetIDValue, String subPathValue) {
		webResource = webResource.path(datasets).path(datasetIDValue)
				.path(subPathValue);
		ClientResponse response = webResource.accept(MediaType.TEXT_PLAIN,
				MediaType.APPLICATION_JSON).get(ClientResponse.class);
		System.out.println(response.getStatus());
	}

	/*
	 * /{silo name}/datasets/{id}/{subpath}
	 */
	public void createDataSetWithSubPath(WebResource webResource,
			String datasets, String datasetIDValue, String subPathValue) {
		webResource = webResource.path(datasets).path(datasetIDValue)
				.path(subPathValue);
		webResource.put();
	}

	/*
	 * /{silo name}/datasets/{id}/{subpath} DELETE TODO - recursive delete.
	 * Returns 400 if the directory has subdirectories but this is not intended
	 * to be a long-term API choice.
	 */
	public void deleteDataSetWithSubPath(WebResource webResource,
			String datasets, String datasetIDValue, String subPathValue) {
		webResource = webResource.path(datasets).path(datasetIDValue)
				.path(subPathValue);
		webResource.delete();
	}

	/*
	 * /{silo name}/datasets/{id}/{subpath} POST Upload a file within the
	 * subpath Parameters -> file = Multipart-encoded (HTML) file upload,
	 * filename = {Optional filename for upload}
	 * 
	 * Not working properly for me ...
	 */
	public void dataSetFileUploadWithSubPath(WebResource webResource,
			String datasets, String datasetIDValue, String subPathValue,
			String fileURL) {
		webResource = webResource.path(datasets).path(datasetIDValue)
				.path(subPathValue);
		// webResource.put();
		// webResource.post();
		doFileAttachmentTest(webResource, fileURL);
	}

	private void doFileAttachmentTest(WebResource resource, String fileURL) {
		try {

			File file = new File(fileURL);
			if (!file.exists()) {
				return;
			}

			MediaType imageType = new MediaType("application", "zip");

			FormDataMultiPart part = new FormDataMultiPart();
			final String fileParamName = "file";

			final FormDataBodyPart fileAttachmentPart = addFileAttachment(
					fileParamName, file, imageType);
			fileAttachmentPart.setParent(part);
			part.bodyPart(fileAttachmentPart);

			ClientResponse response = resource.type(
					MediaType.MULTIPART_FORM_DATA_TYPE).post(
					ClientResponse.class, part);
			System.out.println(response.getStatus());

			response.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			// fail(ex.getMessage());
		}
	}

	private FormDataBodyPart addFileAttachment(final String fileParamName,
			final File file, final MediaType mediaType) {
		FormDataBodyPart bodyPart = new FormDataBodyPart(fileParamName, file,
				mediaType);
		bodyPart.getHeaders().putSingle("Content-Type", mediaType.toString());
		bodyPart.getHeaders().putSingle("Content-Disposition",
				getContentDisposition(fileParamName, file));
		return bodyPart;
	}

	private String getContentDisposition(String paramName, File file) {
		if (StringUtils.isBlank(paramName)) {
			throw new IllegalArgumentException();
		}
		StringBuilder builder = new StringBuilder("form-data; name=");
		builder.append(paramName);
		if (file != null) {
			builder.append("; filename=").append(file.getName());
			if (file.exists()) {
				builder.append("; size=").append(file.length());
			}
		}
		return builder.toString();
	}

	private String[] parseXML(String response) {
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			DocumentBuilderFactory xmlFact = DocumentBuilderFactory
					.newInstance();
			xmlFact.setNamespaceAware(true);
			DocumentBuilder builder = xmlFact.newDocumentBuilder();
			Document doc = builder.parse(new java.io.ByteArrayInputStream(
					response.getBytes()));

			NamespaceContext ctx = new NamespaceContext() {

				public String getNamespaceURI(String prefix) {
					String uri;
					if (prefix.equals("ore")) {
						uri = "http://www.openarchives.org/ore/terms/";
					} else if (prefix.equals("dcterms")) {
						uri = "http://purl.org/dc/terms/";
					} else if (prefix.equals("oxds")) {
						uri = "http://vocab.ox.ac.uk/dataset/schema#";
					} else if (prefix.equals("rdf")) {
						uri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
					} else {
						uri = null;
					}
					return uri;
				}

				// Dummy implementation - not used!

				public Iterator getPrefixes(String val) {
					return null;
				}

				// Dummy implemenation - not used!
				public String getPrefix(String uri) {
					return null;
				}
			};

			String xpathStr = "//rdf:RDF/oxds:Grouping/ore:aggregates/@rdf:resource";
			XPathFactory xpathFact = XPathFactory.newInstance();
			XPath xpath = xpathFact.newXPath();
			xpath.setNamespaceContext(ctx);
			XPathExpression expr = xpath.compile(xpathStr);

			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				String tempString = nodes.item(i).getNodeValue();
				int lastIndex = tempString.lastIndexOf("/");
				String newString = tempString.substring(lastIndex + 1);

				arrayList.add(newString);
				// System.out.println(nodes.item(i).getNodeValue() + "   " +
				// newString);
			}

		} catch (XPathExpressionException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		}

		String[] filesDirArray = new String[arrayList.size()];
		filesDirArray = arrayList.toArray(filesDirArray);

		return filesDirArray;
	}
}
