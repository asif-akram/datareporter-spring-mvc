package uk.ac.ox.it.damaro.reporter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import uk.ac.ox.it.damaro.service.DataBankClientService;

@Controller
public class TilesController {
	
	@Autowired
	DataBankClientService  dataBankClientService;
	
	@RequestMapping("/welcome")
	public ModelAndView welcomePage() {		
		
		//model.addAttribute("articles", listService.getLists());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "Spring 3 MVC Hello World");				
		modelAndView.setViewName("welcome");
		return modelAndView;
	}
	
	@RequestMapping("/browseSilos")
	public ModelAndView browseSilos() {
		
		List<String> silosNameList = dataBankClientService.getSilos();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("silosNameList", silosNameList);
		modelAndView.setViewName("browseSilos");
		return modelAndView;
	}
	
	@RequestMapping(value="/{siloName}/browseDataset", method=RequestMethod.GET)
	public ModelAndView browseDatasetsForSilo(@PathVariable("siloName") String siloName) {
		
		
		List<String> dataPackagesNameList = dataBankClientService.getDatasets(siloName);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("siloName", siloName);
		modelAndView.addObject("dataPackagesNameList", dataPackagesNameList);
		modelAndView.setViewName("browseDatasets");
		return modelAndView;
	}
	
	@RequestMapping(value="/{siloName}/{datasetName}/browseItems", method=RequestMethod.GET)
	public ModelAndView browseItemsForDataset(@PathVariable("siloName") String siloName, @PathVariable("datasetName") String datasetName) {
		
		
		List<String> itemsNameList = dataBankClientService.getItems(siloName, datasetName);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("siloName", siloName);
		modelAndView.addObject("datasetName", datasetName);
		modelAndView.addObject("itemsNameList", itemsNameList);
		modelAndView.setViewName("browseItems");
		return modelAndView;
	}
}
