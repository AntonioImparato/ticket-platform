package org.lessons.java.crud.service;

import java.util.List;

import org.lessons.java.crud.model.Category;
import org.lessons.java.crud.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	   public List<Category> findAll() { 
	    	return categoryRepository.findAll(); 
	    
	    }
	    
	   public Category findById(Integer id) { 
	    	return categoryRepository.findById(id).get(); 
	    
	    }
	   
	   public RedirectAttributes save(Category formCategory, RedirectAttributes attributes) {
		   categoryRepository.save(formCategory);
			return  attributes.addFlashAttribute("successMessage", formCategory.getNome() + " aggiunto con successo"); 
		}

	   public RedirectAttributes update(Category formCategory, RedirectAttributes attributes) {
		   categoryRepository.save(formCategory);
			return  attributes.addFlashAttribute("successMessage", formCategory.getNome() + " aggiornato con successo"); 
		}

	   public RedirectAttributes deleteById(Integer id, RedirectAttributes attributes) { 
		   categoryRepository.deleteById(id);  
		     return attributes.addFlashAttribute("deleteMessage", " categoria eliminata" );
		    
	   }

}
