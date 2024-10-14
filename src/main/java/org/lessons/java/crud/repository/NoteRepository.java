package org.lessons.java.crud.repository;

import java.util.List;

import org.lessons.java.crud.model.Note;
import org.lessons.java.crud.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface NoteRepository extends JpaRepository<Note, Integer> {
	
    List<Note> findByTicket(Ticket ticket);
    
    List<Note> findByTicketId(Integer ticketId);
    
}