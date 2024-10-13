package org.lessons.java.crud.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.lessons.java.crud.model.Role;
import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.model.User;
import org.lessons.java.crud.service.TicketService;
import org.lessons.java.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/")
public class UserController {
	
	@Autowired
	 private UserService serviceUser;
	
	@Autowired
	private TicketService serviceTicket;

	// index
    @GetMapping("/")
    public String dashboard(Model model , Authentication auth) {
    	if(auth == null) {
    		return "redirect:/login";
    	}
       
        return "page/dashboard";
    }
    
    @GetMapping("/operator/editUser/{id}")
    	public String editUser(@PathVariable("id") Integer id,
    							Authentication auth, 
    							 RedirectAttributes attributes,
    							 Model model) {
		    Ticket ticket = serviceTicket.findById(id);
		    Optional<User> user = serviceUser.findByUsername(auth.getName());
		    if(ticket.getUser().getUsername().equals(auth.getName())) {
		    	model.addAttribute("ticket", ticket);
			    return "/tickets/editUser";
		    }
		    else {
		    	attributes.addFlashAttribute("deleteMessage", " Errore non sei autorizzato  " );
		    }
		    return "redirect:/tickets/operator";
		}

	@PostMapping("/operator/editUser/{id}")
	public String updateUser(@PathVariable("id") Integer id, 
	                     @RequestParam("stato") String stato,
	                   @RequestParam("userId") Integer userId,
	                   Authentication auth,
	                     RedirectAttributes attributes) {
	    Ticket ticket = serviceTicket.findById(id);
	    
	    
	    User user = serviceUser.findByUsername(auth.getName()).get();
  		if(!user.getStatus())
  		{
  			attributes.addFlashAttribute("deleteMessage", "non puoi modificare lo stato dei ticket");
  			return "redirect:/tickets/operator";
  		}
  		
	    if (ticket != null) {
	        ticket.setStato(stato); // Aggiorna solo lo stato
	        serviceTicket.update(ticket, attributes); // Salva il ticket aggiornato
	    } else {
	        attributes.addFlashAttribute("deleteMessage", "Ticket non trovato.");
	    }

	    return "redirect:/tickets/operator";
	}
		
		//get
		@GetMapping("/operator/statusEdit/{id}")
		public String UserStatus(@PathVariable("id") Integer id, 
                RedirectAttributes attributes, Model model,
                Authentication auth){
			
			User user = serviceUser.findById(id);
			if (user != null) {
				model.addAttribute("user", user);
			} 
			
			else {
			   attributes.addFlashAttribute("deleteMessage", "Ticket non trovato.");
			}
			
			if(user.getUsername().equals(auth.getName())){
				return"user/editStatus";
			}
			else {
				attributes.addFlashAttribute("deleteMessage", "Non sei autorizzato.");
				return"redirect:/tickets/operator";
			}
		}
		
		// Update
				@PostMapping("/operator/statusEdit/{id}")
				public String editUserStatus(@PathVariable("id") Integer id,
											@RequestParam("status") Boolean status,
				                     RedirectAttributes attributes) {
					User user = serviceUser.findById(id);
				    List<Ticket> tickets = serviceTicket.findByUsername(user.getUsername());
				    
				    for(Ticket ticket : tickets) 
				    {
				    	if(ticket.getStato().equals("Da fare")  || ticket.getStato().equals("In corso") )
				    	{
				    		 attributes.addFlashAttribute("deleteMessage", "Hai ancora dei ticket da finire.");
				    		 return "redirect:/tickets/operator";
				    	}
				    }
					
				    if (user != null) {
				    	user.setStatus(status); // Aggiorna solo lo stato
				        serviceUser.update(user, attributes); // Salva l'aggiornato
				    } 

				    return "redirect:/tickets/operator";
				}
}

