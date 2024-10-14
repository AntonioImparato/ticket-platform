
package org.lessons.java.crud.conroller.api;

import java.util.List;

import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.service.CategoryService;
import org.lessons.java.crud.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping
public class TicketRestController {
	
	@Autowired
	private TicketService serviceTicket;
	
	@Autowired
	private CategoryService serviceCategory;

	@GetMapping("/api/ticketAll")
	public List<Ticket> ticketAll(){
		return serviceTicket.findAll();
	}
	
	@GetMapping("/api/ticketByCategory")
	public List<Ticket> ticketByCategory(@RequestParam(name = "category", required=true) String category){
		return serviceCategory.findByNome(category).getTicket();
	}
	
	@GetMapping("/api/ticketByStato")
	public List<Ticket> ticketByStato(@RequestParam(name = "stato", required=true) String stato){
		return serviceTicket.findByStato(stato);
	}
}

