package org.lessons.java.crud.controller;

import java.util.List;

import org.lessons.java.crud.model.Category;
import org.lessons.java.crud.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/categorie")
public class CategoryController {

    @Autowired
    private CategoryService service;
    
    // Mostra tutte le categorie
    @GetMapping()
    public String index(Model model) {
        List<Category> categories = service.findAll();
        model.addAttribute("categories", categories);
        return "/categorie/index";
    }

    // Visualizza i dettagli di una singola categoria
    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category", service.findById(id));
        return "/categorie/show";
    }

    // Mostra il form per aggiungere una nuova categoria
    @GetMapping("/create")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "/categorie/create";
    }

    // Salva una nuova categoria
    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("category") Category formCategory, 
                        BindingResult bindingResult, 
                        Model model,
                        RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "/categorie/create";
        }
        service.save(formCategory, attributes);
        return "redirect:/categorie";
    }

    // Mostra il form per modificare una categoria esistente
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category", service.findById(id));
        return "/categorie/edit";
    }

    // Aggiorna una categoria esistente
    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("category") Category updateFormCategory, 
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "/categorie/edit";
        }
        service.update(updateFormCategory, attributes);
        return "redirect:/categorie";
    }
    
    // Elimina una categoria
    @PostMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        service.deleteById(id, attributes);
        return "redirect:/categorie";
    }
}
