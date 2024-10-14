package org.lessons.java.crud.repository;

import java.util.List;

import org.lessons.java.crud.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findAll();

	Category findByNome(String category);

}
