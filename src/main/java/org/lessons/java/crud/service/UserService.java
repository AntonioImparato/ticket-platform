	package org.lessons.java.crud.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.lessons.java.crud.model.Role;
import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.model.User;
import org.lessons.java.crud.repository.TicketRepository;
import org.lessons.java.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class UserService {
	
	 @Autowired
	    private UserRepository userRepository;
	 
	  @Autowired
	    private TicketRepository ticketRepository;
	
	public List<User> findAll() { 
    	return userRepository.findAll(); 
    
		}

	public List<User> findByStatusFalse() {
	
		return userRepository.findByStatusTrue();
		}
	
	public List<Ticket> findByUserId(Integer userId) {
	    return ticketRepository.findByUserId(userId);
		}
	public User findById(Integer userId) {
	    return  userRepository.findById(userId).get();
		}
	
	public Optional<User> findByUsername(String username){
		return userRepository.findByUsername(username);
		}
	
	public List<User> findByStatusTrueAndRoleName(@Param("roleName") String roleName){
		return userRepository.findByStatusTrueAndRoleName(roleName);
		}
	
	 public void updateStatus(User user, Boolean status) {
	        user.setStatus(status);  
	        userRepository.save(user); 
	    }

	    public void save(User user) {
	        userRepository.save(user);
	    }
	    public RedirectAttributes update(User formUser, RedirectAttributes attributes) {
			userRepository.save(formUser);
			return attributes.addFlashAttribute("successMessage"," ' " + formUser.getNome() + " ' " + " Modificato con successo"); 
		}
	    
	    public boolean checkIsUserRole(String username) {
	    	Set<Role> roles = findByUsername(username).get().getRoles();
	    	for(Role role : roles) {
	    		if(role.getName().equals("USER"))
	    			return true;
	    	}
	    	return false;
	    }
	    
}
