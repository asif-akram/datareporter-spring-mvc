package uk.ac.ox.it.damaro.piwik.http.client;

import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

public class PiwikClient {

	private BufferedImage image = null;;
	private URIBuilder builder;

	private HttpClient httpclient = new DefaultHttpClient();

	private String statsType = "null";

	public PiwikClient() {
		builder = new URIBuilder();
		builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk")
				.setPath("/index.php").setParameter("idSite", "2")
				.setParameter("token_auth", "6c56e6d07447248e7993706c6aa3dd54");

	}

	public/* StreamedContent */BufferedImage getGraphForDialog(String statsType) {

		System.out.println("getGraphForDialog: " + statsType);

		this.statsType = statsType;

		if (statsType.equalsIgnoreCase("pageUrls")) {
			return getPageUrls();
		} else if (statsType.equalsIgnoreCase("country")) {
			return getCountry();
		} else if (statsType.equalsIgnoreCase("continent")) {
			return getContinent();
		} else if (statsType.equalsIgnoreCase("outlinks")) {
			return getOutlinks();
		} else if (statsType.equalsIgnoreCase("downloads")) {
			return getDownloads();
		} else if (statsType.equalsIgnoreCase("exitPageUrls")) {
			return getExitPageUrls();
		} else if (statsType.equalsIgnoreCase("entryPageUrls")) {
			return getEntryPageUrls();
		} else if (statsType.equalsIgnoreCase("pageTitles")) {
			return getPageTitles();
		} else if (statsType.equalsIgnoreCase("keywords")) {
			return this.getKeywords();
		} else if (statsType.equalsIgnoreCase("numberOfDistinctKeywords")) {
			return this.getNumberOfDistinctKeywords();
		} else if (statsType.equalsIgnoreCase("websites")) {
			return this.getWebsites();
		} else if (statsType.equalsIgnoreCase("numberOfDistinctWebsites")) {
			return this.getNumberOfDistinctWebsites();
		} else if (statsType.equalsIgnoreCase("numberOfDistinctWebsitesUrls")) {
			return this.getNumberOfDistinctWebsitesUrls();
		} else if (statsType.equalsIgnoreCase("urlsFromWebsiteId")) {
			return this.getUrlsFromWebsiteId();
		} else if (statsType.equalsIgnoreCase("campaigns")) {
			return this.getCampaigns();
		} else if (statsType.equalsIgnoreCase("searchEngines")) {
			return this.getSearchEngines();
		} else if (statsType.equalsIgnoreCase("searchEnginesFromKeywordId")) {
			return this.getSearchEnginesFromKeywordId();
		} else if (statsType.equalsIgnoreCase("refererType")) {
			return this.getRefererType();
		}

		return getPageUrls();
	}

	public/* StreamedContent */BufferedImage getPageUrls() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Actions")
				.setParameter("apiAction", "getPageUrls")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getCountry() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "UserCountry")
				.setParameter("apiAction", "getCountry")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getContinent() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "UserCountry")
				.setParameter("apiAction", "getContinent")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getOutlinks() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Actions")
				.setParameter("apiAction", "getOutlinks")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");
		// setParameter("token_auth", "6c56e6d07447248e7993706c6aa3dd54");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getDownloads() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Actions")
				.setParameter("apiAction", "getDownloads")
				.setParameter("idSite", "2").setParameter("period", "range")
				.setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getExitPageUrls() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Actions")
				.setParameter("apiAction", "getExitPageUrls")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getEntryPageUrls() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Actions")
				.setParameter("apiAction", "getEntryPageUrls")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getPageTitles() {
		// builder = new URIBuilder();
		// builder.setScheme("http").setHost("orastats.bodleian.ox.ac.uk").setPath("/index.php").
		builder.setParameter("module", "API")
				.
				// setParameter("method", "Referers.getKeywords").
				setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Actions")
				.setParameter("apiAction", "getPageTitles")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getKeywords() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getKeywords")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getNumberOfDistinctKeywords() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getNumberOfDistinctKeywords")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getWebsites() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getWebsites")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getNumberOfDistinctWebsites() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getNumberOfDistinctWebsites")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getNumberOfDistinctWebsitesUrls() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getNumberOfDistinctWebsitesUrls")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getUrlsFromWebsiteId() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getUrlsFromWebsiteId")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getCampaigns() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getCampaigns")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getSearchEngines() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getSearchEngines")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getSearchEnginesFromKeywordId() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getSearchEnginesFromKeywordId")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	public/* StreamedContent */BufferedImage getRefererType() {
		builder.setParameter("module", "API")
				.setParameter("method", "ImageGraph.get")
				.setParameter("apiModule", "Referers")
				.setParameter("apiAction", "getRefererType")
				.
				// setParameter("idSite", "2").
				setParameter("period", "range").setParameter("format", "xml")
				.setParameter("filter_limit", "20")
				.setParameter("date", "2011-01-01,today");

		return getStreamedContent(builder);
	}

	private/* StreamedContent */BufferedImage getStreamedContent(
			URIBuilder builder) {

		URI uri;
		HttpGet httpget;
		HttpResponse responseHttp;

		try {
			uri = builder.build();

			httpget = new HttpGet(uri);
			System.out.println(httpget.getURI());

			responseHttp = httpclient.execute(httpget);
			image = ImageIO.read(responseHttp.getEntity().getContent());

			// ImageIO.write(image, "png",new File("/opt/out.png"));

			// File chartFile = new File("/opt/out.png");
			// dbImage = new DefaultStreamedContent(new
			// FileInputStream(chartFile), "image/png");
			// return new
			// DefaultStreamedContent(responseHttp.getEntity().getContent(),
			// "image/png");

			return image;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
