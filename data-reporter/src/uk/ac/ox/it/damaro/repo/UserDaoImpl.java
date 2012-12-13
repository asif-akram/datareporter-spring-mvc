package uk.ac.ox.it.damaro.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ox.it.damaro.domain.User;

@Repository("userDAO")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserDaoImpl implements UserDao{
	
	private EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	

	// To Save the article detail
	@Transactional
	public void saveUser(User user) {
		System.out.println("User should be saved here");
		//article.setAddedDate(new Date());
		em.persist(user);
	}
	
	public List<User> listUsers(){
		return null;
	}

}
