package uk.ac.ox.it.damaro.reporter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.ox.it.damaro.domain.User;
import uk.ac.ox.it.damaro.repo.UserDao;
 
@Controller
public class UsersController {
	
	private UserDao userDao;

	@Autowired
	public UsersController(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@PersistenceContext
    private EntityManager entityManager;
 
    @RequestMapping("/users")
    public String users(Model model) {
    	
    	model.addAttribute("users", entityManager.createQuery("select u from User u").getResultList());
    	
        return "users";
    }
    
    @RequestMapping(value = "/create-user", method = RequestMethod.GET)
    public String createUser(Model model) {
        return "create-user";
    }
    
    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public String createUser(Model model, String name) {
 
    	User user = new User();
        user.setName(name); 
        userDao.saveUser(user);
        
        System.out.println("createUser() called and user should be persisted");
        
        return "redirect:/users.html";
    }
}
