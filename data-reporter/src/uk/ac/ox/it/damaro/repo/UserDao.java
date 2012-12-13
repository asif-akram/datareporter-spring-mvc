package uk.ac.ox.it.damaro.repo;

import java.util.List;

import uk.ac.ox.it.damaro.domain.User;

public interface UserDao {
	
	// To Save the article detail
		public void saveUser(User user);

		// To get list of all articles
		public List<User> listUsers();
	
	


}
