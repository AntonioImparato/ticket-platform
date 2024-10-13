package org.lessons.java.crud.service;

import java.util.List;
import java.util.Optional;

import org.lessons.java.crud.model.Note;
import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.model.User;
import org.lessons.java.crud.repository.NoteRepository;
import org.lessons.java.crud.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private TicketRepository ticketRepository;

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public List<Note> findByTicket(Ticket ticket) {
        return noteRepository.findByTicket(ticket);
    }
 

    public List<Note> findByTicketId(Integer ticketId) {
        return noteRepository.findByTicketId(ticketId); // Recupera le note associate a un ticket specifico
    }

    public Optional<Note> findById(Integer id) {
        return noteRepository.findById(id);
    }

    public RedirectAttributes save(Note note, RedirectAttributes attributes) {
    	noteRepository.save(note);
    	return attributes.addFlashAttribute("successMessage", "Nota aggiunto con successo");
    	
       
    }

    public void deleteById(Integer id) {
        noteRepository.deleteById(id);
    }
    
    public User getOwnerOfTicket(Integer ticketId) {
        return ticketRepository.findUserByTicketId(ticketId);
    }
    
}