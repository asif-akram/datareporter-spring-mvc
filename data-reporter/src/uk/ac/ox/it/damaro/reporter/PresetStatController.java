package uk.ac.ox.it.damaro.reporter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PresetStatController {
	
	@RequestMapping(value="/snapshotReport", method=RequestMethod.POST)
	public ModelAndView snapshotReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("snapshotReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/universityReport", method=RequestMethod.POST)
	public ModelAndView universityReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("universityReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/departmentReport", method=RequestMethod.POST)
	public ModelAndView departmentReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("departmentReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/divisionReport", method=RequestMethod.POST)
	public ModelAndView divisionReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("divisionReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/personalReport", method=RequestMethod.POST)
	public ModelAndView personalReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("personalReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/researchGroupReport", method=RequestMethod.POST)
	public ModelAndView researchGroupReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("researchGroupReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/fundingCouncilReport", method=RequestMethod.POST)
	public ModelAndView fundingCouncilReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("fundingCouncilReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/individualRecordReport", method=RequestMethod.POST)
	public ModelAndView individualRecordReport(@RequestParam(value="fromDate") String fromDate) {
		
		System.out.println(fromDate);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("individualRecordReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/retentionAssessmentReport", method=RequestMethod.POST)
	public ModelAndView retentionAssessmentReport(@RequestParam(value="numberOfYears") String numberOfYears) {
		
		System.out.println(numberOfYears);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("retentionAssessmentReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/lackManFieldsReport", method=RequestMethod.POST)
	public ModelAndView lackManFieldsReport() {		
		
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("lackManFieldsReport");
		return modelAndView;
		
	}
	
	@RequestMapping(value="/missingDOIReport", method=RequestMethod.POST)
	public ModelAndView missingDOIReport() {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "");				
		modelAndView.setViewName("missingDOIReport");
		return modelAndView;
		
	}

}
