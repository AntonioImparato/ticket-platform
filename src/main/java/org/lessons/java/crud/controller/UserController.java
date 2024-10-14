package org.lessons.java.crud.controller;

import java.util.List;
import java.util.Optional;

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

    // Dashboard principale
    @GetMapping("/")
    public String dashboard(Model model, Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }
        return "page/dashboard";
    }

    // Modifica un ticket associato all'utente (operatore)
    @GetMapping("/operator/editUser/{id}")
    public String editUser(@PathVariable("id") Integer id,
                           Authentication auth,
                           RedirectAttributes attributes,
                           Model model) {
        Ticket ticket = serviceTicket.findById(id);
        if (ticket.getUser().getUsername().equals(auth.getName())) {
            model.addAttribute("ticket", ticket);
            return "/tickets/editUser";
        } else {
            attributes.addFlashAttribute("error", "Errore non sei autorizzato");
        }
        return "redirect:/tickets/operator";
    }

    // Aggiorna lo stato di un ticket (operatore)
    @PostMapping("/operator/editUser/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             @RequestParam("stato") String stato,
                             @RequestParam("userId") Integer userId,
                             Authentication auth,
                             RedirectAttributes attributes) {
        Ticket ticket = serviceTicket.findById(id);
        User user = serviceUser.findByUsername(auth.getName()).get();

        if (!user.getStatus()) {
            attributes.addFlashAttribute("error", "Non puoi modificare lo stato dei ticket");
            return "redirect:/tickets/operator";
        }

        if (ticket != null) {
            ticket.setStato(stato);  // Aggiorna solo lo stato del ticket
            serviceTicket.update(ticket, attributes);  // Salva le modifiche
        } else {
            attributes.addFlashAttribute("error", "Ticket non trovato.");
        }

        return "redirect:/tickets/operator";
    }

    // Mostra lo stato dell'utente
    @GetMapping("/operator/statusEdit/{id}")
    public String UserStatus(@PathVariable("id") Integer id,
                             RedirectAttributes attributes,
                             Model model,
                             Authentication auth) {

        User user = serviceUser.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            attributes.addFlashAttribute("error", "Utente non trovato.");
        }

        if (user.getUsername().equals(auth.getName())) {
            return "user/editStatus";
        } else {
            attributes.addFlashAttribute("error", "Non sei autorizzato.");
            return "redirect:/tickets/operator";
        }
    }

    // Aggiorna lo stato dell'utente (operatore)
    @PostMapping("/operator/statusEdit/{id}")
    public String editUserStatus(@PathVariable("id") Integer id,
                                 @RequestParam("status") Boolean status,
                                 RedirectAttributes attributes) {
        User user = serviceUser.findById(id);
        List<Ticket> tickets = serviceTicket.findByUsername(user.getUsername());

        // Verifica se ci sono ticket in corso o da fare
        for (Ticket ticket : tickets) {
            if (ticket.getStato().equals("Da fare") || ticket.getStato().equals("In corso")) {
                attributes.addFlashAttribute("error", "Hai ancora dei ticket da finire.");
                return "redirect:/tickets/operator";
            }
        }

        if (user != null) {
            user.setStatus(status);  // Aggiorna lo stato dell'utente
            serviceUser.update(user, attributes);  // Salva l'aggiornamento
        }

        return "redirect:/tickets/operator";
    }
}
