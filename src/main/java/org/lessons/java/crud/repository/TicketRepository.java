package org.lessons.java.crud.repository;

import java.util.List;

import org.lessons.java.crud.model.Ticket;
import org.lessons.java.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface TicketRepository extends JpaRepository<Ticket, Integer>{

	List<Ticket> findByStato(String stato);
	
	List<Ticket> findByUser(User user);
	
	@Query("SELECT t FROM Ticket t WHERE t.user.username = :username")
	List<Ticket> findByUsername(@Param("username") String username);
	
	List<Ticket> findByTitoloContains(String titolo);
	
	List<Ticket> findAll();
	
	@Query("SELECT t FROM Ticket t WHERE t.user.id = :userId")
	List<Ticket> findByUserId(@Param("userId") Integer userId);
	//Optional<Ticket> findById(Integer id);

    @Query("SELECT t.user FROM Ticket t WHERE t.id = :ticketId")
    User findUserByTicketId(@Param("ticketId") Integer ticketId);
}
