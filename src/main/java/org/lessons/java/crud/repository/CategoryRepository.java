package org.lessons.java.crud.repository;

import java.util.List;

import org.lessons.java.crud.model.Category;
import org.lessons.java.crud.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category, Integer>{


	List<Category> findAll();

	Category findByNome(String category);

	
	
}
