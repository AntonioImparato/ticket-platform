package org.lessons.java.crud.controller;
import java.util.List;

import org.lessons.java.crud.model.Note;
import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.model.User;
import org.lessons.java.crud.service.NoteService;
import org.lessons.java.crud.service.TicketService;
import org.lessons.java.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService serviceUser;

    //admin visualizza tutte le note associate a un ticket 
    @GetMapping("/admin/tickets/{ticketId}")
    public String listNotes(@PathVariable("ticketId") Integer ticketId, Model model) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets/admin"; 
        }
        List<Note> notes = noteService.findByTicket(ticket);
        model.addAttribute("notes", notes);
        model.addAttribute("ticket", ticket);
        return "notes/index"; 
    }

    //index operator note
    @GetMapping("operator/tickets/{ticketId}")
    public String showNotesByTicket(@PathVariable Integer ticketId, Model model,
    								Authentication auth,
    								RedirectAttributes attributes) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets/operator"; 
        }
        
        String username = auth.getName();
        if (!(ticket.getUser().getUsername().equals(username))) {
        	 attributes.addFlashAttribute("error", "Non sei autorizzato.");
            return "redirect:/tickets/operator"; 
        }


        List<Note> notes = noteService.findByTicket(ticket); 
        model.addAttribute("notes", notes);
        model.addAttribute("ticket", ticket);

        return "notes/show"; 
    }
    
  //create get
    @GetMapping("/create/{ticketId}")
    public String createNote(@Valid @PathVariable Integer ticketId, Model model,
    						Authentication auth,
    						RedirectAttributes attributes) {
    	
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets/operator"; 
        }
        User user = serviceUser.findByUsername(auth.getName()).get();
        String username = user.getUsername();
        if (serviceUser.checkIsUserRole(user.getUsername()) && !(ticket.getUser().getUsername().equals(username))) {
        		attributes.addFlashAttribute("error", "Non sei autorizzato.");
        		return "redirect:/tickets/operator"; 
        }
        
        Note note = new Note(user, ticket);
       
        model.addAttribute("ticket", ticket);
        model.addAttribute("note", note);
        model.addAttribute("userName",username);
        model.addAttribute("userId", user.getId());

        return "notes/create"; 
    }
    
  //create post
    @PostMapping("/create/{ticketId}")
    public String createNote(@PathVariable Integer ticketId, @Valid @ModelAttribute Note note, BindingResult bindingResult , 
    		RedirectAttributes attributes,
    		Authentication auth,
    		 Model model) {
    	 if (bindingResult.hasErrors()) {
    		 model.addAttribute("userName", serviceUser.findByUsername(auth.getName()).get().getUsername());
             model.addAttribute("user", noteService.findAll());
             return "/notes/create";
         }
    	noteService.save(note, attributes);
    	
    	User user = serviceUser.findByUsername(auth.getName()).get();
        if (serviceUser.checkIsUserRole(user.getUsername())) {
        	 return "redirect:/notes/operator/tickets/{ticketId}"; 
        }
        else
    	 return "redirect:/notes/admin/tickets/{ticketId}";
    }

  
//    @PostMapping("/tickets/{ticketId}/notes/delete/{noteId}")
//    public String deleteNote(@PathVariable("ticketId") Integer ticketId, 
//                             @PathVariable("noteId") Integer noteId, 
//                             RedirectAttributes redirectAttributes) {
//        noteService.deleteById(noteId);
//        redirectAttributes.addFlashAttribute("message", "Nota eliminata con successo!");
//        return "redirect:/notes/tickets/" + ticketId + "/notes"; // Cambiato l'URL per riflettere la lista delle note
//    }
}