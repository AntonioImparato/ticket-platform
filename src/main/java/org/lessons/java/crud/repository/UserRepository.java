package org.lessons.java.crud.repository;

import java.util.List;
import java.util.Optional;

import org.lessons.java.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Integer>{

	List<User> findByNome(String nome);
	
	public Optional<User> findByUsername(String username);
	
	List<User> findByNomeContains(String nome);
	
	List<User> findByStatusTrue();
	
	List<User> findAll();
	
	@Query(" SELECT u FROM User u JOIN u.roles r WHERE u.status = True and r.name = :roleName")
	List<User> findByStatusTrueAndRoleName(@Param("roleName") String roleName);
	
}
