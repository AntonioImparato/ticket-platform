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
    
    // index
    @GetMapping()
    public String index(Model model, Integer id) {
        List<Category> categories;
            categories = service.findAll();
        model.addAttribute("categories", categories);
        return "/categorie/index";
    }

    // show
    @GetMapping("/show/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category", service.findById(id));
        return "/categorie/show";
    }

    // createGet
    @GetMapping("/create")
    public String add(Model model) {
        model.addAttribute("category", new Category());
        return "/categorie/create";
    }

    // createPost
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

    // edit
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category", service.findById(id));
        return "/categorie/edit";
    }

    // Update
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
    
  //delite
  		@PostMapping("delete/{id}")
  			public String delite(@PathVariable("id") Integer id,
  					RedirectAttributes attributes){
  				service.deleteById(id, attributes);
  				return"redirect:/categorie";
  			}
}
