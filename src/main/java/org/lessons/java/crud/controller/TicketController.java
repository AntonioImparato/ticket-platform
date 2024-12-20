package org.lessons.java.crud.controller;

import java.util.List;
import java.util.Optional;

import org.lessons.java.crud.model.Category;
import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.model.User;
import org.lessons.java.crud.service.CategoryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private CategoryService serviceCategory;
    
    @Autowired
    private UserService serviceUser;

    @Autowired
    private TicketService serviceTicket;

    // Mostra la dashboard per l'admin con la lista dei ticket
    @GetMapping("/admin")
    public String dashboardAdmin(Model model, String titolo) {
        List<Ticket> tickets;
        tickets = serviceTicket.findAllOrderByTitolo();
        if (titolo != null && !titolo.isEmpty()) {
            tickets = serviceTicket.findByTitoloContainsInDb(titolo);
        }

        model.addAttribute("ticket", tickets);
        return "tickets/indexAdmin";
    }

    // Mostra i ticket assegnati all'utente corrente (operatore)
    @GetMapping("/operator")
    public String getTicketsByUserString(Authentication auth, Model model) {
        List<Ticket> tickets = serviceTicket.findByUsername(auth.getName());
        User user = serviceUser.findByUsername(auth.getName()).get();
        String stato;

        if (user.getStatus()) {
            stato = "Attivo";
        } else {
            stato = "Non attivo";
        }

        model.addAttribute("user", user);
        model.addAttribute("tickets", tickets);
        model.addAttribute("stato", stato);
        return "tickets/indexUser";
    }

    // Mostra i dettagli di un ticket (admin)
    @GetMapping("/admin/show/{id}")
    public String showAdmin(@PathVariable("id") Integer id, Model model, Authentication auth) {
        List<Category> category = serviceCategory.findAll();

        model.addAttribute("user", serviceUser.findAll());
        model.addAttribute("ticket", serviceTicket.findById(id));
        model.addAttribute("username", auth.getName());
        model.addAttribute("categories", category);

        return "tickets/show";
    }

    // Mostra i dettagli di un ticket per l'operatore autenticato
    @GetMapping("/show/{id}")
    public String showUser(@PathVariable("id") Integer id, Model model, Authentication auth, RedirectAttributes attributes) {
        Ticket ticket = serviceTicket.findById(id);
        if (ticket == null) {
            return "redirect:/tickets/operator";
        }

        Optional<User> user = serviceUser.findByUsername(auth.getName());
        String username = user.get().getUsername();

        // Verifica se l'utente ha i permessi per vedere il ticket
        if (serviceUser.checkIsUserRole(username) && !(ticket.getUser().getUsername().equals(username))) {
            attributes.addFlashAttribute("error", "Non sei autorizzato.");
            return "redirect:/tickets/operator";
        }

        model.addAttribute("categories", serviceCategory.findAll());
        model.addAttribute("user", user.get());
        model.addAttribute("ticket", serviceTicket.findById(id));
        model.addAttribute("username", auth.getName());

        return "/tickets/show";
    }

    // Form per creare un nuovo ticket (admin)
    @GetMapping("/create")
    public String add(Model model) {
        List<User> users = serviceUser.findByStatusTrueAndRoleName("USER");
        model.addAttribute("categories", serviceCategory.findAll());
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("users", users);

        return "/tickets/create";
    }

    // Salva il nuovo ticket creato (admin)
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model,
                        RedirectAttributes attributes, Authentication auth) {
        if (bindingResult.hasErrors()) {
            List<User> users = serviceUser.findByStatusTrueAndRoleName("USER");
            model.addAttribute("users", users);
            model.addAttribute("categories", serviceCategory.findAll());
            attributes.addFlashAttribute("error", "Devi aggiungere i campi correttamente");
            return "/tickets/create";
        }
        serviceTicket.save(formTicket, attributes);

        return "redirect:/tickets/admin";
    }

    // Form per modificare un ticket esistente (admin)
    @GetMapping("/admin/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("categories", serviceCategory.findAll());
        model.addAttribute("ticket", serviceTicket.findById(id));
        return "/tickets/edit";
    }

    // Aggiorna un ticket esistente (admin)
    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("ticket") Ticket updateFormTicket, BindingResult bindingResult,
                         Model model, RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "/tickets/edit";
        }

        // Verifica se l'utente associato al ticket è attivo
        if (!serviceUser.findById(updateFormTicket.getUser().getId()).getStatus()) {
            attributes.addFlashAttribute("error", "Utente non attivo");
            return "redirect:/tickets/admin";
        }

        serviceTicket.update(updateFormTicket, attributes);
        return "redirect:/tickets/admin";
    }

    // Elimina un ticket (admin)
    @PostMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        serviceTicket.deleteById(id, attributes);
        return "redirect:/tickets/admin";
    }

    // Modifica un ticket (utente operatore)
    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) {
        Ticket ticket = serviceTicket.findById(id);
        model.addAttribute("ticket", ticket);
        return "/tickets/editUser";
    }

    // Aggiorna il ticket (utente operatore)
    @PostMapping("/editUser/{id}")
    public String updateUser(@PathVariable("id") Integer id, @RequestParam("stato") String stato,
                             @RequestParam("userId") Integer userId, RedirectAttributes attributes) {

        User user = serviceUser.findById(id);
        if (!user.getStatus()) {
            attributes.addFlashAttribute("error", "Non puoi modificare lo stato dei ticket");
            return "redirect:/tickets/operator";
        }

        Ticket ticket = serviceTicket.findById(id);
        if (ticket != null) {
            ticket.setStato(stato);
            serviceTicket.update(ticket, attributes);
        } else {
            attributes.addFlashAttribute("error", "Ticket non trovato.");
        }

        return "redirect:/tickets/operator";
    }
}
