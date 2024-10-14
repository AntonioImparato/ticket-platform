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

    // Admin visualizza tutte le note associate a un ticket
    @GetMapping("/admin/tickets/{ticketId}")
    public String listNotes(@PathVariable("ticketId") Integer ticketId, Model model) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets/admin"; // Redirige se il ticket non esiste
        }
        List<Note> notes = noteService.findByTicket(ticket); // Recupera le note del ticket
        model.addAttribute("notes", notes);
        model.addAttribute("ticket", ticket);
        return "notes/index"; // Pagina per visualizzare le note
    }

    // Operatore visualizza le note associate a un ticket
    @GetMapping("operator/tickets/{ticketId}")
    public String showNotesByTicket(@PathVariable Integer ticketId, Model model,
                                    Authentication auth,
                                    RedirectAttributes attributes) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets/operator"; // Redirige se il ticket non esiste
        }

        String username = auth.getName();
        // Controllo se l'operatore è autorizzato a visualizzare il ticket
        if (!(ticket.getUser().getUsername().equals(username))) {
            attributes.addFlashAttribute("error", "Non sei autorizzato.");
            return "redirect:/tickets/operator";
        }

        List<Note> notes = noteService.findByTicket(ticket); // Recupera le note associate
        model.addAttribute("notes", notes);
        model.addAttribute("ticket", ticket);

        return "notes/show"; // Pagina per visualizzare le note
    }

    // Form per creare una nuova nota (operatore o admin)
    @GetMapping("/create/{ticketId}")
    public String createNote(@Valid @PathVariable Integer ticketId, Model model,
                             Authentication auth,
                             RedirectAttributes attributes) {
        Ticket ticket = ticketService.findById(ticketId);
        if (ticket == null) {
            return "redirect:/tickets/operator"; // Redirige se il ticket non esiste
        }

        User user = serviceUser.findByUsername(auth.getName()).get();
        String username = user.getUsername();
        
        // Controllo se l'operatore è autorizzato a creare note per questo ticket
        if (serviceUser.checkIsUserRole(user.getUsername()) && !(ticket.getUser().getUsername().equals(username))) {
            attributes.addFlashAttribute("error", "Non sei autorizzato.");
            return "redirect:/tickets/operator";
        }

        Note note = new Note(user, ticket); // Crea una nuova nota con il ticket associato
        model.addAttribute("ticket", ticket);
        model.addAttribute("note", note);
        model.addAttribute("userName", username);
        model.addAttribute("userId", user.getId());

        return "notes/create"; // Mostra la pagina di creazione della nota
    }

    // Salva la nuova nota creata
    @PostMapping("/create/{ticketId}")
    public String createNote(@PathVariable Integer ticketId, @Valid @ModelAttribute Note note, BindingResult bindingResult,
                             RedirectAttributes attributes, Authentication auth, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userName", serviceUser.findByUsername(auth.getName()).get().getUsername());
            return "/notes/create"; // Ritorna alla pagina di creazione in caso di errori
        }
        
        noteService.save(note, attributes); // Salva la nota

        User user = serviceUser.findByUsername(auth.getName()).get();
        // Redirige alla pagina corretta in base al ruolo dell'utente
        if (serviceUser.checkIsUserRole(user.getUsername())) {
            return "redirect:/notes/operator/tickets/{ticketId}";
        } else {
            return "redirect:/notes/admin/tickets/{ticketId}";
        }
    }
}
