package uk.ac.ox.it.damaro.reporter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.ox.it.damaro.domain.CustomStats;
import uk.ac.ox.it.damaro.piwik.http.client.PiwikClient;

@Controller
public class PiwikController {
	
	@RequestMapping(value = "/{statsName}/imageTest", method = RequestMethod.GET)
	public @ResponseBody void getImage(@PathVariable("statsName") String statsName, HttpServletResponse response)
	{
		
		System.out.println(statsName);
		statsName = statsTypeConversion(statsName);
		
	    BufferedImage image = new PiwikClient().getGraphForDialog(statsName);

	    response.setContentType("image/png");
	    OutputStream out;
	    try
	    {
	        out = response.getOutputStream();
	        ImageIO.write(image, "png", out);
	        out.close();
	    }
	    catch (IOException ex)
	    {
	        //Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	
	@RequestMapping("/statsList")
	public ModelAndView supportedStats(){
		List<String> supportedStatsList = supportesStatsList();
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("supportedStatsList", supportedStatsList);
		modelAndView.setViewName("browseStats");
		
		return modelAndView;
	}
	
	@RequestMapping("/statsListTest")
	public ModelAndView supportedStatsTest(){
		List<String> supportedStatsList = supportesStatsList();
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("supportedStatsList", supportedStatsList);
		modelAndView.setViewName("presetReport");
		
		return modelAndView;
	}
	
	@RequestMapping("/presetReport")
	public ModelAndView presetReport(){
		
		System.out.println("@RequestMapping(/presetReport)");
		
		List<String> supportedStatsList = presetReportsList();
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("supportedStatsList", supportedStatsList);
		//modelAndView.addObject("command", new CustomStats());
		modelAndView.setViewName("presetReport");
		
		return modelAndView;
	}
	
	
	@RequestMapping("/search")
	public ModelAndView searchMetaData(){
		List<String> supportedStatsList = presetReportsList();
		
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.addObject("supportedStatsList", supportedStatsList);
		//modelAndView.addObject("command", new CustomStats());
		modelAndView.setViewName("search");
		
		return modelAndView;
	}
	
	@RequestMapping("/customStats")
	public ModelAndView customStats(){
		ModelAndView modelAndView = new ModelAndView();
		
		modelAndView.setViewName("customStats");
		//return new ModelAndView("customStats", "command", new CustomStats());
		return new ModelAndView("customStats", "command", new CustomStats());
	}
	
	private List<String> supportesStatsList(){
		String[] supportedStasArray = {"pageUrls", "country", "continent", "outlinks", "downloads", "exitPageUrls", "entryPageUrls", "pageTitles"};
		
		List<String> supportedStatsList = Arrays.asList(supportedStasArray);
		
		return supportedStatsList;
	}
	
	private List<String> presetReportsList(){
		String[] supportedStasArray = {"DataFinder_Snapshot", "University_Snapshot", "Personal_Publication_Report", "REF_Reports", "Departmental_Report", "exitPageUrls", "entryPageUrls", "pageTitles"};
		
		List<String> supportedStatsList = Arrays.asList(supportedStasArray);
		
		return supportedStatsList;
	}
	
	private String statsTypeConversion(String statsName){
		
		if(statsName.equalsIgnoreCase("DataFinder_Snapshot") || statsName.equalsIgnoreCase("University_Snapshot") ||
				statsName.equalsIgnoreCase("Personal_Publication_Report") || statsName.equalsIgnoreCase("REF_Reports")){
			return "outlinks";
		}
		
		return "statsName";
	}
	
	protected Map referenceData() throws Exception {
		Map referenceData = new HashMap();
		Map<String,String> customReport = new LinkedHashMap<String,String>();
		customReport.put("author", "By Author");
		customReport.put("university", "By University");
		customReport.put("embargo", "Embargo Content");
		customReport.put("notAccessed", "Not Accessed");
		//referenceData.put("countryList", country);
		return referenceData;
	}
	

}
