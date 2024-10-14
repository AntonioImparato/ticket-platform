package org.lessons.java.crud.service;

import java.util.List;

import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class TicketService {
	
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> findAll() { 
    	return ticketRepository.findAll(); 
    
    }
    
    public Ticket findById(Integer id) { 
    	return ticketRepository.findById(id).get(); 
    
    }

    public List<Ticket> findByTitoloContainsInDb(String titolo){
		return ticketRepository.findByTitoloContains(titolo);
	}

	public List<Ticket> findAllOrderByTitolo() {
		return ticketRepository.findAll();
	}
	
	public RedirectAttributes save(Ticket formTicket, RedirectAttributes attributes) {
		ticketRepository.save(formTicket);
		return attributes.addFlashAttribute("successMessage", " ' " + formTicket.getTitolo()+ " ' " + " Aggiunto con successo"); 
	}
	
	public RedirectAttributes update(Ticket formTicket, RedirectAttributes attributes) {
		ticketRepository.save(formTicket);
		return attributes.addFlashAttribute("successMessage"," ' " + formTicket.getTitolo() + " ' " + " Modificato con successo"); 
	}
    
	 public RedirectAttributes deleteById(Integer id, RedirectAttributes attributes) { 
		 ticketRepository.deleteById(id);  
	     return attributes.addFlashAttribute("error", " Offerta eliminata" );
	    
	    }
	 
	 public List<Ticket> findByUserId(Integer userId) {
		    return ticketRepository.findByUserId(userId);
		}
	 
	 public List<Ticket> findByUsername(String username){
		 	return ticketRepository.findByUsername(username);
	 }

	public List<Ticket> findByStato(String stato) {
		return ticketRepository.findByStato(stato);
	}
}